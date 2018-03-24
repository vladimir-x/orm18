package ru.dude.orm.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Интерфейс для мэпперов
 *
 * @author dude.
 */
public interface Mappable {

    /**
     * Начало обработки выборок
     */
    void init();

    /**
     * Обработать одну выборку
     */
    void mapLine(ResultSet rs) throws SQLException;

    /**
     * Завершение обработки выборок
     */
    void complete();
}
