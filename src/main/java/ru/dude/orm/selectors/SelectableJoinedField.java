package ru.dude.orm.selectors;

import ru.dude.orm.model.JoinedField;
import ru.dude.orm.join.AliasGenerator;

/**
 * Селектор для модели поля
 *
 * @author dude.
 */
public class SelectableJoinedField<T> implements SelectableField {

    /**
     * Объект запроса: поле запроса
     */
    JoinedField joinedField;

    /**
     * Псевдоним для селектора
     */
    String selectAlias;

    public SelectableJoinedField(JoinedField joinedField, AliasGenerator aliasGenerator) {
        this.joinedField = joinedField;
        this.selectAlias = aliasGenerator.generateSelectAlias(joinedField);
    }

    public JoinedField getJoinedField() {
        return joinedField;
    }

    public String getSelectAlias() {
        return selectAlias;
    }

    public String buildSql() {
        return joinedField.buildSql() + " as " + selectAlias;
    }

    @Override
    public String getAlias() {
        return selectAlias;
    }

}
