package dev.riac.trapcount.report.trapcountreport.domain.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Comparator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrapCountTemp implements Comparable{

    private String trapId;
    private BigInteger trapKey;
    private Integer trapCount;

    @Override
    public int compareTo(Object o) {

        TrapCountTemp other = (TrapCountTemp) o;

        return Comparator.comparing(TrapCountTemp::getTrapId)
                .thenComparing(TrapCountTemp::getTrapCount)
                .compare(this, other);
    }
}
