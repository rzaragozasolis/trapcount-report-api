package dev.riac.trapcount.report.trapcountreport.domain.report.repository;

import dev.riac.trapcount.report.trapcountreport.domain.report.entity.TrapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TrapRepository extends JpaRepository<TrapEntity, Long> {

    @Query(nativeQuery = true, value = "{call csp_trapdata_get_all_by_ranch(:ranchkey, :startdate)}")
    Object[] getAllTrapDataByRanch(@Param("ranchkey") Long ranchkey, @Param("startdate") Date startdate);

}
