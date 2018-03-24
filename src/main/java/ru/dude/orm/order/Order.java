package ru.dude.orm.order;

import ru.dude.orm.model.JoinedField;
import ru.dude.orm.selectors.SelectableField;

import java.util.ArrayList;
import java.util.List;

/**
 * Объект запроса: Сортировка
 *
 * @author dude.
 */
public class Order {

    /**
     * Список колонок для сортировки
     */
    List<Orderrable> orders = new ArrayList<>();

    public Order() {

    }

    /**
     * Добавление из селектора
     *
     * @param dir
     * @param selectableFields
     * @return
     */
    public Order add(Dir dir, SelectableField... selectableFields) {
        if (selectableFields != null) {
            for (SelectableField sf : selectableFields) {
                if (sf != null) {
                    orders.add(new OrderSelector(dir, sf));
                }
            }
        }
        return this;

    }

    /**
     * Добавление из табличных колонок
     *
     * @param dir
     * @param joinedFields
     * @return
     */
    public Order add(Dir dir, JoinedField... joinedFields) {
        if (joinedFields != null) {
            for (JoinedField f : joinedFields) {
                if (f != null) {
                    orders.add(new OrderJoinedField(dir, f));
                }
            }
        }
        return this;
    }

    public String buildSql() {

        StringBuilder sb = new StringBuilder();
        for (Orderrable order : orders) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(order.buildSql());

        }
        sb.append("\n");
        return sb.toString();
    }

}
