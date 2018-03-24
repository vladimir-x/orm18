package ru.dude.orm.clauses;

/**
 * Операнды в sql
 *
 * author dude.
 */
public enum Operand {
    AND(" AND "),
    OR(" OR "),
    EQ(" = "),
    NOT_EQ(" != "),
    MORE(" > "),
    LESS(" < "),
    MORE_EQ(" >= "),
    LESS_EQ(" <= "),
    ILIKE(" ILIKE "),
    IN(" IN "),
    NOT(" NOT "),
    IS_TRUE(" IS TRUE "),
    IS_FALSE(" IS FALSE"),
    IS_NULL(" IS NULL "),
    IS_NOT_NULL(" IS NOT NULL "),
    OVERLAPS(" OVERLAPS "),;

    Operand(String pSqlOperand) {
        this.sqlOperand = pSqlOperand;
    }

    private String sqlOperand;

    public String getSqlOperand() {
        return sqlOperand;
    }
}
