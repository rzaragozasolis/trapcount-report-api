package dev.riac.trapcount.report.trapcountreport.domain.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RanchEntityResponse {

    private Long ranchkey;
    private String ranchnamel;
}
