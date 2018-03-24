package ru.dude.orm.model;

/**
 * Аннотация типа, конвертируемого в sqls author dude.
 */
public interface IPrimitive {

    /**
     * Вернуть значение, пригодное для подстановки в SQL
     *
     * @return
     */
    String toSqlValue();

    /**
     * Заполнить значение данными из БД
     *
     * @return
     */
    Object makeByDB(Object pDbValue);
}
