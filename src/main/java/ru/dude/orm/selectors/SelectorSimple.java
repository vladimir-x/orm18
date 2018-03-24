package ru.dude.orm.selectors;

import ru.dude.orm.model.JoinedField;
import ru.dude.orm.join.AliasGenerator;

import java.util.*;

/**
 * Простой селектор для группы полей
 *
 * @author dude.
 */
public class SelectorSimple implements Selectable {

    /**
     * Выбирайемая группа полей из запроса
     */
    List<SelectableField> selectableFields = new ArrayList<>();

    public SelectorSimple(List<SelectableField> selectableFields) {
        this.selectableFields = selectableFields;
    }

    protected SelectorSimple(AliasGenerator aliasGenerator, Collection<JoinedField> joinedFields) {
        addJoinedFields(aliasGenerator, joinedFields);
    }

    protected void addJoinedFields(AliasGenerator aliasGenerator, Collection<JoinedField> joinedFieldList) {
        if (joinedFieldList != null) {
            for (JoinedField joinedField : joinedFieldList) {
                if (joinedField != null) {
                    selectableFields.add(new SelectableJoinedField(joinedField, aliasGenerator));
                }
            }
        }
    }

    public void addSelectableJoinedFields(List<SelectableField> selectableFields) {
        this.selectableFields.addAll(selectableFields);
    }

    @Override
    public String buildSql() {

        StringBuilder sb = new StringBuilder();
        for (SelectableField sf : selectableFields) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(sf.buildSql());

        }
        return sb.toString();
    }

    @Override
    public List<String> getFieldNames() {
        List<String> res = new ArrayList<>();
        for (SelectableField sf : selectableFields) {
            res.add(sf.getAlias());
        }
        return res;
    }

    public List<SelectableField> getSelectableFields() {
        return selectableFields;
    }
}
