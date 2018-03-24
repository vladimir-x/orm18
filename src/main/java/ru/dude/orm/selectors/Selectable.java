package ru.dude.orm.selectors;

import java.util.List;

/**
 * Базовй интерфейс описания селектора
 *
 * @author dude.
 */
public interface Selectable {

    /**
     * SQL для вставки в блок SELECT
     *
     * @return
     */
    String buildSql();

    /**
     * Вернуть название вибираемых селектором колонок, для их последующего
     * совмещения с данными
     *
     * @return
     */
    List<String> getFieldNames();
}
