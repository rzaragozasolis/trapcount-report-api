package dev.riac.trapcount.report.trapcountreport.domain.report.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "trap")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrapEntity {

    @Id
    @Column(name = "trapkey")
    private Long trapkey;

    @Column(name = "ranchkey")
    private Long ranchkey;

    @Column(name = "traptypekey")
    private Long traptypekey;

    @Column(name = "insecttypekey")
    private Long insecttypekey;

    @Column(name = "lastmodifiedmemberkey")
    private Long lastmodifiedmemberkey;

    @Column(name = "trapid")
    private String trapid;

    @Column(name = "routesequence")
    private Integer routesequence;

    @Column(name = "latitude")
    private Long latitude;

    @Column(name = "longitude")
    private Long longitude;

    @Column(name = "installed")
    private String installed;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "lastcounted")
    private String lastcounted;

    @Column(name = "lastmodified")
    private String lastmodified;

    @Column(name = "rowguid")
    private UUID rowguid;

    @Column(name = "CherryLotID")
    private Integer cherryLotID;

    @Column(name = "blockkey")
    private Long blockkey;
}
