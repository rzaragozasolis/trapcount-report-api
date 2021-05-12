package dev.riac.trapcount.report.trapcountreport.domain.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrapDataResponse {

    private Integer weekNumber;
    private Timestamp weekStart;
    private String trapId;
    private BigInteger trapKey;
    private String trapType;
    private Integer trapCount;
    private String insectType;
    private String blockName;
}
