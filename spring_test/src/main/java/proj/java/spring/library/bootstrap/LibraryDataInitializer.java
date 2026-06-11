package proj.java.spring.library.bootstrap;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import proj.java.spring.library.domain.Author;
import proj.java.spring.library.domain.Book;
import proj.java.spring.library.domain.BookCopy;
import proj.java.spring.library.domain.BookStatus;
import proj.java.spring.library.repository.BookRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Seeds the database with mock library data on startup when it is empty.
 * Defaults produce well over 10,000 rows (≈1,000 authors, ≈4,000 books and
 * ≈12,000 copies). Inserts are flushed in batches so Hibernate can use JDBC
 * batching.
 */
@Component
public class LibraryDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(LibraryDataInitializer.class);

    private static final String[] FIRST_NAMES = {
            "James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda",
            "William", "Elizabeth", "David", "Barbara", "Richard", "Susan", "Joseph", "Jessica",
            "Thomas", "Sarah", "Charles", "Karen", "Mei", "Wei", "Hiroshi", "Yuki", "Ahmed", "Fatima"
    };
    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
            "Rodriguez", "Martinez", "Hernandez", "Lopez", "Wilson", "Anderson", "Chen", "Wang",
            "Li", "Zhang", "Tanaka", "Sato", "Kumar", "Khan", "Nguyen", "Müller"
    };
    private static final String[] TITLE_ADJ = {
            "Hidden", "Silent", "Eternal", "Broken", "Golden", "Lost", "Distant", "Crimson",
            "Forgotten", "Shifting", "Quiet", "Burning", "Frozen", "Endless", "Secret", "Wandering"
    };
    private static final String[] TITLE_NOUN = {
            "Garden", "Empire", "River", "Shadow", "Mountain", "Promise", "Voyage", "Kingdom",
            "Machine", "Horizon", "Echo", "Labyrinth", "Compass", "Lantern", "Harvest", "Tide"
    };

    @PersistenceContext
    private EntityManager em;

    private final BookRepository bookRepository;

    @Value("${library.seed.enabled:true}")
    private boolean seedEnabled;

    @Value("${library.seed.authors:1000}")
    private int authorCount;

    @Value("${library.seed.books:4000}")
    private int bookCount;

    @Value("${library.seed.batch-size:100}")
    private int batchSize;

    public LibraryDataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!seedEnabled) {
            log.info("Library seeding disabled (library.seed.enabled=false).");
            return;
        }
        if (bookRepository.count() > 0) {
            log.info("Library data already present ({} books); skipping seeding.", bookRepository.count());
            return;
        }

        long start = System.currentTimeMillis();
        Random rnd = new Random(42);
        BookStatus[] statuses = BookStatus.values();

        log.info("Seeding library data: {} authors, {} books...", authorCount, bookCount);

        // 1) Authors
        List<Long> authorIds = new ArrayList<>(authorCount);
        for (int i = 0; i < authorCount; i++) {
            Author author = new Author(
                    FIRST_NAMES[rnd.nextInt(FIRST_NAMES.length)] + " " + LAST_NAMES[rnd.nextInt(LAST_NAMES.length)]);
            em.persist(author);
            authorIds.add(author.getId());
            if ((i + 1) % batchSize == 0) { // 控制 flush / clear，避免 persistence context 爆掉 如果直接用 repository.saveAll(全部 16000 筆)，這些 entity 會全部留在 persistence context（一級快取）裡。
                em.flush();
                em.clear();
            }
        }
        em.flush();
        em.clear();
        log.info("Inserted {} authors.", authorCount);

        // 2) Books (with 1-3 authors) + 3) Copies (2-4 per book), cascaded
        long copyCounter = 0;
        long copyTotal = 0;
        for (int i = 0; i < bookCount; i++) {
            Book book = new Book(
                    String.format("978%010d", i),
                    randomTitle(rnd),
                    "A " + TITLE_ADJ[rnd.nextInt(TITLE_ADJ.length)].toLowerCase() + " story about the human condition.",
                    LocalDate.of(1950 + rnd.nextInt(75), 1 + rnd.nextInt(12), 1 + rnd.nextInt(28)));

            int authorsPerBook = 1 + rnd.nextInt(3);
            for (int a = 0; a < authorsPerBook; a++) {
                Long authorId = authorIds.get(rnd.nextInt(authorIds.size()));
                // managed reference without loading the row
                book.getAuthors().add(em.getReference(Author.class, authorId)); // em.getReference() 掛關聯，不用多打 SELECT
            }

            int copiesPerBook = 2 + rnd.nextInt(3);
            for (int c = 0; c < copiesPerBook; c++) {
                String barcode = String.format("BC-%010d", ++copyCounter);
                book.addCopy(new BookCopy(barcode, statuses[rnd.nextInt(statuses.length)]));
                copyTotal++;
            }

            em.persist(book);
            if ((i + 1) % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }
        em.flush();
        em.clear();

        long total = (long) authorCount + bookCount + copyTotal;
        log.info("Seeding complete in {} ms: {} authors + {} books + {} copies = {} rows.",
                System.currentTimeMillis() - start, authorCount, bookCount, copyTotal, total);
    }

    private String randomTitle(Random rnd) {
        return "The " + TITLE_ADJ[rnd.nextInt(TITLE_ADJ.length)] + " " + TITLE_NOUN[rnd.nextInt(TITLE_NOUN.length)];
    }
}
