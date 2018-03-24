package ru.dude.orm.model;

import ru.dude.orm.join.Join;

/**
 * Объект запроса : модель поля. Содержит информацию о модели поля сущности и
 * объекте запроса.
 *
 * @author dude.
 */
public class JoinedField<T> {

    /**
     * Объект запроса
     */
    Join joinTable;

    /**
     * Модель поля сущности
     */
    FieldModel fieldModel;

    public JoinedField(Join joinTable, FieldModel fieldModel) {
        this.joinTable = joinTable;
        this.fieldModel = fieldModel;
    }

    public String buildSql() {
        if (cashedSqlRes == null) {
            cashedSqlRes = joinTable.getAlias() + "." + fieldModel.getColumnNameQuoted();
        }
        return cashedSqlRes;
    }
    String cashedSqlRes = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JoinedField that = (JoinedField) o;

        if (!joinTable.equals(that.joinTable)) {
            return false;
        }
        return fieldModel.equals(that.fieldModel);
    }

    public String getTableAlias() {
        return joinTable.getAlias();
    }

    public FieldModel getFieldModel() {
        return fieldModel;
    }

    @Override
    public int hashCode() {
        int result = joinTable.hashCode();
        result = 31 * result + fieldModel.hashCode();
        return result;
    }
}
