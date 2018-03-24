package ru.dude.orm.clauses;

import ru.dude.orm.model.JoinedField;

/**
 * Общий предикат с двумя переменными
 *
 * @author dude.
 */
public class ClauseBinar implements Clause {

    /**
     * Операция
     */
    private Operand operand;

    /**
     * Левый операнд (готовый sql)
     */
    private String aSqlValue;

    /**
     * Правый операнд (готовый sql)
     */
    private String bSqlValue;

    /**
     * По двум полям в БД
     *
     * @param aJoinedField -левое поле
     * @param operand - операция
     * @param bJoinedField - правое поле
     */
    public ClauseBinar(JoinedField aJoinedField, Operand operand, JoinedField bJoinedField) {
        aSqlValue = aJoinedField.buildSql();
        this.operand = operand;
        bSqlValue = bJoinedField.buildSql();
    }

    /**
     * По полю и значению или выражению
     *
     * @param aJoinedField - левое поле
     * @param operand - операция
     * @param bSqlValue - правое выражение sql
     */
    public ClauseBinar(JoinedField aJoinedField, Operand operand, String bSqlValue) {
        this.aSqlValue = aJoinedField.buildSql();
        this.operand = operand;
        this.bSqlValue = bSqlValue;
    }

    /**
     * По двум значениям или выражениям
     *
     * @param aSqlValue - левое выражение sql
     * @param operand - операция
     * @param bSqlValue - правое выражение sql
     */
    public ClauseBinar(String aSqlValue, Operand operand, String bSqlValue) {
        this.aSqlValue = aSqlValue;
        this.operand = operand;
        this.bSqlValue = bSqlValue;

    }

    @Override
    public String buildSql() {
        return aSqlValue + operand.getSqlOperand() + bSqlValue;
    }

}
