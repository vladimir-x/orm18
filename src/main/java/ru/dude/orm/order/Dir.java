package ru.dude.orm.order;

/**
 * Направление сортировки
 *
 * @author dude.
 */
public enum Dir {
    ASC,
    DESC;

    public String toSql() {
        return name();
    }
}
