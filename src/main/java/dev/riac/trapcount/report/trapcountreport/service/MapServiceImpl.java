package dev.riac.trapcount.report.trapcountreport.service;

import dev.riac.trapcount.report.trapcountreport.domain.report.entity.MapEntity;
import dev.riac.trapcount.report.trapcountreport.domain.report.repository.MapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService{

    @Value("${report.url.image.map}")
    private String pathImageMap;

    private final MapRepository mapRepository;

    @Override
    public Optional<MapEntity> getResourseImageMap(Long ranchKey) {

        return Optional.ofNullable(mapRepository.getFileNameMap(ranchKey))
                .map(m -> MapEntity.builder()
                        .trapId(m.getTrapId())
                        .pageOrientation(m.getPageOrientation())
                        .fileName(pathImageMap + m.getFileName().replaceAll(" ", "%20"))
                        .build());
    }
}
