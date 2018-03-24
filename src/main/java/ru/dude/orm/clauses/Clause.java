package ru.dude.orm.clauses;

/**
 * �?нетрфейс предиката (условия)
 *
 * @author dude.
 */
public interface Clause {

    /**
     * Готовый к выполнению sql предикат, подставляемый в WHERE
     *
     * @return
     */
    String buildSql();
}
