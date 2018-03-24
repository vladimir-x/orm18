package ru.dude.orm.selectors;

import ru.dude.orm.join.AliasGenerator;
import ru.dude.orm.join.Join;

/**
 * Селектор для сущности
 *
 * @author dude.
 */
public class SelectorEntity<E> extends SelectorSimple {

    /**
     * Объект таблицы сущности
     */
    Join join;

    public SelectorEntity(AliasGenerator aliasGenerator, Join join) {
        super(aliasGenerator, join.getAllJoinedFields(true, false));
        this.join = join;
    }

    public Join getJoin() {
        return join;
    }
}
