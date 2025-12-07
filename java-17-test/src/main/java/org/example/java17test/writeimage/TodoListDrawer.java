package org.example.java17test.writeimage;

import com.google.api.services.tasks.model.Task;
import org.example.java17test.googletask.GoogleTasksService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.List;

@Service
public class TodoListDrawer {

    private static final Font titleFont = new Font("Serif", Font.BOLD, 32);
    private static final Font textFont = new Font("Serif", Font.PLAIN, 28);
    private static final int TEMPLATE_WIDTH_POSITION = 120;
    private static final int TEMPLATE_HEIGHT_POSITION = 280;
    private static final int TEMPLATE_MAX_WIDTH = 600;

    private final GoogleTasksService googleTasksService;

    public TodoListDrawer(GoogleTasksService googleTasksService) {
        this.googleTasksService = googleTasksService;
    }

    public void drawImage() throws Exception {
        // 1. 載入圖片
        BufferedImage image = ImageIO.read(TodoListDrawer.class.getClassLoader().getResourceAsStream("todolist" + "1" + ".jpg"));

        // 2. 建立 Graphics2D
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 3. 待辦內容（會自動換行）
        List<Task> googleTasks = googleTasksService.getGoogleTasks(); // 使用注入的服務實例

        // 4. 寫入文字
        drawWrappedText(g, googleTasks, TEMPLATE_HEIGHT_POSITION); // X,Y,maxWidth
        g.dispose();

        // 5. 輸出圖片
        ImageIO.write(image, "png", new File("output.png"));
        System.out.println("已輸出：output.png");
    }

    // ⬇⬇⬇ 自動換行的工具方法
    private void drawWrappedText(Graphics2D g, List<Task> taskList, int y) { // 改為非靜態

        g.setColor(Color.BLACK);

        for (Task task: taskList) {
            // 寫title
            y = drawWithFont("✍️" + task.getTitle(),TodoListDrawer.titleFont,g,y);
            y = drawWithFont(task.getNotes(),TodoListDrawer.textFont,g,y);
            y = createTaskSpacing(y);
        }
    }

    private int drawWithFont(String text, Font font, Graphics2D g, int y) { // 改為非靜態
        AttributedString attributedString = new AttributedString(text);

        attributedString.addAttribute(TextAttribute.FONT, font);
        g.setFont(font);
        AttributedCharacterIterator iterator = attributedString.getIterator();
        LineBreakMeasurer measurer = new LineBreakMeasurer(iterator, g.getFontRenderContext());
        while (measurer.getPosition() < iterator.getEndIndex()) {
            TextLayout layout = measurer.nextLayout(TodoListDrawer.TEMPLATE_MAX_WIDTH);
            y += layout.getAscent();
            layout.draw(g, TodoListDrawer.TEMPLATE_WIDTH_POSITION, y);
            y += layout.getDescent() + layout.getLeading();
        }
        return y;
    }

    private int createTaskSpacing(int y) { // 改為非靜態
        y += (int) (TodoListDrawer.textFont.getSize() * 1f); // 每行之間的額外間距
        return y;
    }
}

