package ru.dude.orm.order;

/**
 * Интерфейс для объектов сортировки
 *
 * @author dude.
 */
public interface Orderrable {

    /**
     * SQL для вставки в блок ORDER BY
     *
     * @return
     */
    String buildSql();
}
