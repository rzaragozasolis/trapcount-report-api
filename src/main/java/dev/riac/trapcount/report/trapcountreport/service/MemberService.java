package dev.riac.trapcount.report.trapcountreport.service;

import dev.riac.trapcount.report.trapcountreport.domain.report.entity.MermberEntity;

public interface MemberService {

    Iterable<MermberEntity> getAllMembers();
    String getEmailAddressConsultant(Long memberKey);
}
