package dev.riac.trapcount.report.trapcountreport.domain.report.repository;

import dev.riac.trapcount.report.trapcountreport.domain.report.entity.RanchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RanchRepository extends JpaRepository<RanchEntity, Long> {

    @Query(value = "select r from ranch r where r.growername = ?1 and r.fieldConsultantMemberKey = ?2 and r.active = true ")
    List<RanchEntity> getRanchesKeyByGrowerNameAndFieldConsultantMemberKey(String growerName, Long consultantKey);
}
