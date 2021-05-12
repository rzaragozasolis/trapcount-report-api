package dev.riac.trapcount.report.trapcountreport.service;

import dev.riac.trapcount.report.trapcountreport.domain.report.entity.MapEntity;

import java.util.Optional;

public interface MapService {

    Optional<MapEntity> getResourseImageMap(Long ranchKey);
}
