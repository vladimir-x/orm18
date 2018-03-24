package ru.dude.orm.order;

import ru.dude.orm.selectors.Selectable;
import ru.dude.orm.selectors.SelectableField;

import java.util.List;

/**
 * Поле сортировки для селектора
 *
 * @author dude.
 */
public class OrderSelector implements Orderrable {

    /**
     * Направление сортировки
     */
    Dir dir;

    /**
     * Селектор
     */
    SelectableField selectableField;

    public OrderSelector(Dir dir, SelectableField selector) {
        this.dir = dir;
        this.selectableField = selector;
    }

    @Override
    public String buildSql() {

        return selectableField.getAlias() + " " + dir.toSql();
    }
}
