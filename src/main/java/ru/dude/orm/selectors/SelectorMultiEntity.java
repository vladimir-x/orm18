package ru.dude.orm.selectors;

import ru.dude.orm.model.JoinedField;

/**
 * Селектор для нескольких сущностей
 *
 * @author dude.
 */
public class SelectorMultiEntity extends SelectorSimple {

    protected SelectorMultiEntity(SelectorEntity... selectorsEntity) {
        super(null, null);
        if (selectorsEntity != null) {
            for (SelectorEntity se : selectorsEntity) {
                if (se != null) {
                    addSelectableJoinedFields(se.getSelectableFields());
                }
            }
        }
    }

}
