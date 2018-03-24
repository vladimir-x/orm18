package ru.dude.orm.order;

import ru.dude.orm.model.JoinedField;

/**
 * Поле сортировки для поля сущности
 *
 * @author dude.
 */
public class OrderJoinedField implements Orderrable {

    /**
     * Направление сортировки
     */
    Dir dir;

    /**
     * Объект запроса: поле сущности
     */
    JoinedField joinedField;

    public OrderJoinedField(Dir dir, JoinedField joinedField) {
        this.dir = dir;
        this.joinedField = joinedField;
    }

    @Override
    public String buildSql() {
        return joinedField.buildSql() + " " + dir.toSql();
    }
}
