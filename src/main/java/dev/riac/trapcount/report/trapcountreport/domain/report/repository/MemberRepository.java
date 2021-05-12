package dev.riac.trapcount.report.trapcountreport.domain.report.repository;


import dev.riac.trapcount.report.trapcountreport.domain.report.entity.MermberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<MermberEntity, Long> {

    @Query(value = "select t.memberemail from member t WHERE t.memberkey = ?1", nativeQuery = true)
    String getMemberEmailFromMemberKey(Long memberKey);
}
