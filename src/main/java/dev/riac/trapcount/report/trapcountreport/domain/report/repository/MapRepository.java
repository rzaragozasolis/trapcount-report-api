package dev.riac.trapcount.report.trapcountreport.domain.report.repository;

import dev.riac.trapcount.report.trapcountreport.domain.report.entity.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MapRepository extends JpaRepository<MapEntity, Long> {

    @Query(value = "select m.trapmapid, m.filename, m.pageorientation from maps m where m.ranchkey = ?1 and [current] = 'true'", nativeQuery = true)
    MapEntity getFileNameMap(Long ranchKey);
}
