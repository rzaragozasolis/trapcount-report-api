package dev.riac.trapcount.report.trapcountreport.domain.report.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "maps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapEntity {

    @Id
    @Column(name = "trapmapid")
    private Long trapId;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "pageorientation")
    private String pageOrientation;

}
