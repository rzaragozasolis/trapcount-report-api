package dev.riac.trapcount.report.trapcountreport.domain.report.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "member")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MermberEntity {

    @Id
    @Column(name = "memberkey")
    private Long id;

    @Column(name = "membername")
    private String name;

    @Column
    private String password;

    @Column(name = "memberemail")
    private String email;

    @Column(name = "displayname")
    private String display;

    @Column(name = "membertype")
    private Integer type;

    @Column(name = "rowguid")
    private UUID rwo;

    @Column(name = "calendarstartpreference")
    private Long dateStartPreference;

    @Column(name = "calendarendpreference")
    private Long dateEndPreference;

    @Column(name = "sortkeypreference")
    private String sortKeyPreference;

    @Column(name = "sortdirectionpreference")
    private Integer sortDirectionPreference;
}
