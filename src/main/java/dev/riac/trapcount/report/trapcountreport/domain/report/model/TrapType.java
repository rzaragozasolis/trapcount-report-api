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
public class TrapType implements Comparable{

    private String trapType;
    private String inspectType;

    @Override
    public int compareTo(Object o) {
        TrapType other = (TrapType) o;
        if((trapType != null &&
                other.trapType != null) &&
                (inspectType != null && other.inspectType != null)) {
            return Comparator.comparing(TrapType::getTrapType)
                    .thenComparing(TrapType::getInspectType)
                    .compare(this, other);
        } else if (trapType != null &&
                other.trapType != null) {
            return Comparator.comparing(TrapType::getTrapType)
                    .compare(this, other);
        }else if (inspectType != null && other.inspectType != null) {
            return Comparator.comparing(TrapType::getInspectType)
                    .compare(this, other);
        }
        return 0;
    }
}
