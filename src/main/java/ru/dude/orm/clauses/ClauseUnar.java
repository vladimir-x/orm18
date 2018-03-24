package ru.dude.orm.clauses;

import ru.dude.orm.model.JoinedField;

/**
 * Общий предикат для унарных операций
 *
 * @author dude.
 */
public class ClauseUnar implements Clause {

    /**
     * Операция
     */
    private Operand operand;

    /**
     * Поле в бд
     */
    private JoinedField joinedField;

    /**
     * Общий предикат для унарных операций
     *
     * @param joinedField Поле в бд
     * @param operand Операция
     */
    public ClauseUnar(JoinedField joinedField, Operand operand) {
        this.operand = operand;
        this.joinedField = joinedField;
    }

    @Override
    public String buildSql() {
        return joinedField.buildSql() + operand.getSqlOperand();
    }
}
