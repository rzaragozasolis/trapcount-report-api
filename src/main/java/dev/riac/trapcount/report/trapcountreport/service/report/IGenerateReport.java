package dev.riac.trapcount.report.trapcountreport.service.report;

import dev.riac.trapcount.report.trapcountreport.domain.report.entity.MapEntity;
import dev.riac.trapcount.report.trapcountreport.domain.report.entity.RanchEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IGenerateReport<T> {

    ByteArrayInputStream generateReport(Map<RanchEntity,List<T>> dataReport, String  fileName, Optional<MapEntity> maybeImageMap, boolean flagRanchReport) throws IOException;
}
