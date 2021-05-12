package dev.riac.trapcount.report.trapcountreport.domain.report.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "ranch")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RanchEntity implements Comparable{

    @Id
    @Column(name = "ranchkey")
    private Long ranchKey;

    @Column(name = "trapcountpersonkey")
    private Long trapCountPersonKey;

    @Column(name = "fieldconsultantmemberkey")
    private Long fieldConsultantMemberKey;

    @Column(name = "sprayrecordsfarmkey")
    private Long sprayrecordsfarmkey;

    @Column(name = "ranchname")
    private String ranchName;

    @Column(name = "routesequence")
    private Integer routeSequence;

    @Column(name = "weekday")
    private Integer weekDay;

    @Column(name = "countstartdate")
    private String countStartDate;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "latitude")
    private Long latitude;

    @Column(name = "longitude")
    private Long longitude;

    @Column(name = "growername")
    private String growername;

    @Column(name = "growerphone")
    private String growerphone;

    @Column(name = "groweremail")
    private String groweremail;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "allowtraprelocation")
    private Boolean allowtraprelocation;

    @Column(name = "lastcounted")
    private String lastcounted;

    @Column(name = "lastmodified")
    private String lastmodified;

    @Column(name = "reportyear")
    private String reportyear;

    @Column(name = "reportstartmmdd")
    private String reportstartmmdd;

    @Column(name = "reportendmmdd")
    private String reportendmmdd;

    @Column(name = "rowguid")
    private UUID rowguid;

    @Column(name = "routekey")
    private Integer routekey;

    @Column(name = "mapname")
    private String mapname;

    @Override
    public int compareTo(Object o) {
        return this.ranchKey.compareTo(((RanchEntity)o).getRanchKey());
    }
}
