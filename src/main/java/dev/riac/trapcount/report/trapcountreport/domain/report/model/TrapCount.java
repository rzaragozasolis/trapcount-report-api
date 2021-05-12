package dev.riac.trapcount.report.trapcountreport.domain.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrapCount implements Comparable{

    private String trapId;
    private Integer trapCount;

    @Override
    public int compareTo(Object o) {

        TrapCount other = (TrapCount) o;

        if ((trapId != null &&
                other.getTrapId() != null) &&
            (trapCount != null &&
                    other.getTrapCount() != null)
        ) {
            return Comparator.comparing(TrapCount::getTrapId)
                    .thenComparing(TrapCount::getTrapCount)
                    .compare(this, other);
        } else if (trapId != null &&
                other.getTrapId() != null) {
            return Comparator.comparing(TrapCount::getTrapId)
                    .compare(this, other);
        } else if(trapCount != null &&
                other.getTrapCount() != null) {
            return Comparator.comparing(TrapCount::getTrapCount)
                    .compare(this, other);
        }

        return 0;


    }
}
