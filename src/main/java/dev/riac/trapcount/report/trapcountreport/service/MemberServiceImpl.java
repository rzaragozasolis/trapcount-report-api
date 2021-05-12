package dev.riac.trapcount.report.trapcountreport.service;

import dev.riac.trapcount.report.trapcountreport.domain.report.entity.MermberEntity;
import dev.riac.trapcount.report.trapcountreport.domain.report.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Iterable<MermberEntity> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public String getEmailAddressConsultant(Long memberKey) {

        return memberRepository.getMemberEmailFromMemberKey(memberKey);
    }
}
