//package proj.java.spring.data.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import proj.java.spring.data.dto.MemberPo;
//
//import java.util.Optional;
//
//public interface MemberRepository extends JpaRepository<MemberPo, Long>, JpaSpecificationExecutor<MemberPo> {
//    Optional<MemberPo> findById(Long uuid);
//    void deleteById(Long id);
//}
