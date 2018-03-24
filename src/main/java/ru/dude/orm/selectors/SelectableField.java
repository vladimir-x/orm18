package ru.dude.orm.selectors;

/**
 * Абстракция для поля из селектора
 *
 * @author dude.
 */
public interface SelectableField {

    /**
     * SQL для вставки в блок SELECT
     *
     * @return
     */
    String buildSql();

    /**
     * Вернуть название вибираемой селектором колоноки
     *
     * @return
     */
    String getAlias();

}
