package proj.java.spring.data.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proj.java.spring.data.dto.MemberDTO;
import proj.java.spring.data.service.MemberService;

@RestController
@RequestMapping("/api/v1.0")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberDTO> create(@RequestBody MemberDTO member) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberService.create(member));
    }

    @GetMapping("/members/{uuid}")
    public ResponseEntity<MemberDTO> readByUuid(@PathVariable Long uuid) {
        return ResponseEntity.ok(memberService.findByUuid(uuid));
    }

    @PatchMapping("/members")
    public ResponseEntity<MemberDTO> update(@RequestBody MemberDTO member) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.updateByUuid(member.getUuid(), member.getHeight()));
    }

    @DeleteMapping("/members/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable Long uuid) {
        memberService.deleteByUuid(uuid);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}

