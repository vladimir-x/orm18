package ru.dude.orm.clauses;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Общий предикат с множеством операндов
 *
 * @author dude.
 */
public class ClauseMany implements Clause {

    /**
     * Операция
     */
    private Operand operand;

    /**
     * Набор вложенных предикатов
     */
    private List<Clause> clauses;

    /**
     * Общий предикат с множеством операндов
     *
     * @param operand Операция
     * @param clauses Набор вложенных предикатов, null игнорируются
     */
    public ClauseMany(Operand operand, Clause... clauses) {
        this.operand = operand;

        this.clauses = new ArrayList<>();
        if (clauses != null) {
            //выделение не нулевых условий
            for (Clause c : clauses) {
                if (c != null) {
                    this.clauses.add(c);
                }
            }
        }
    }

    @Override
    public String buildSql() {
        if (clauses.size() == 0) {
            return null;
        }
        if (clauses.size() == 1) {
            return clauses.get(0).buildSql();
        }

        List<String> notNullSqls = new ArrayList<>();
        for (Clause c : clauses) {
            String currSql = c.buildSql();
            if (StringUtils.isNotBlank(currSql)) {
                notNullSqls.add(currSql);
            }
        }

        if (notNullSqls.size() == 0) {
            return null;
        }
        if (notNullSqls.size() == 1) {
            return notNullSqls.get(0);
        }

        StringBuilder sb = new StringBuilder();
        for (String notNullSql : notNullSqls) {
            if (sb.length() > 0) {
                sb.append(operand.getSqlOperand());
            }
            sb.append("(").append(notNullSql).append(")");
        }

        return sb.toString();
    }
}
