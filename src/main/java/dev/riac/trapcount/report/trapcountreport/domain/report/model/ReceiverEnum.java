package dev.riac.trapcount.report.trapcountreport.domain.report.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReceiverEnum {

    GROWER("grower"), CONSULTANT("consultant"), BOTH("both");

    @Getter
    private String value;
}
