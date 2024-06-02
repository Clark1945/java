package proj.java.spring.data.service;

import org.springframework.stereotype.Service;
import proj.java.spring.data.dto.MemberPo;
import proj.java.spring.data.dto.MemberDTO;
import proj.java.spring.data.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberDTO create(MemberDTO memberDto) {
        return toDto(memberRepository.save(toPo(memberDto)));
    }

    private MemberDTO toDto(MemberPo savedMemberPO) {
        return MemberDTO
                .builder()
                .uuid(savedMemberPO.getUuid())
                .name(savedMemberPO.getFirstName() + "," + savedMemberPO.getLastName())
                .age(savedMemberPO.getAge())
                .height(savedMemberPO.getHeight())
                .build();
    }

    private MemberPo toPo(MemberDTO memberDto) {
        return MemberPo
                .builder()
                .uuid(memberDto.getUuid())
                .firstName(memberDto.getName().split(",")[0].trim())
                .lastName(memberDto.getName().split(",")[1].trim())
                .age(memberDto.getAge())
                .height(memberDto.getHeight())
                .build();
    }


    public MemberDTO updateByUuid(Long id, Float height) {
        MemberPo memberPo = memberRepository.findByUuid(id).orElseThrow(RuntimeException::new);
        memberPo.setHeight(height);

        return toDto(memberRepository.save(memberPo));
    }

//    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteByUuid(Long uuid) {
        memberRepository.deleteByUuid(uuid);
    }

    public MemberDTO findByUuid(Long uuid) {
        return memberRepository
                .findByUuid(uuid)
                .map(this::toDto)
                .orElseThrow(RuntimeException::new);
    }
}
