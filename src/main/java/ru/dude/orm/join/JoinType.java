package ru.dude.orm.join;

/**
 * Типы присоединений
 *
 * @author dude.
 */
public enum JoinType {
    FROM("FROM"),
    INNER_JOIN("INNER JOIN"),
    LEFT_JOIN("LEFT JOIN"),
    RIGHT_JOIN("RIGHT JOIN"),
    FULL_JOIN("FULL JOIN"),
    CROSS_JOIN("CROSS JOIN"),;

    String sqlName;

    JoinType(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getSqlName() {
        return sqlName;
    }
}
