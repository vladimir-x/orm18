package ru.dude.orm.model;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель поля
 *
 * @author dudeauthor dude.
 */
public class FieldModel {

    /**
     * �?мя поля
     */
    public String name;

    /**
     * �?мя колонки в базе
     */
    public String column;

    /**
     * �?мя класса , владельца поля
     */
    public String entityClassName;

    /**
     * Флаг ID
     */
    public boolean isId;

    /**
     * Присоединяемая колонка Не используется
     */
    public Class<?> joinedColumn;

    /**
     * Поле класса
     */
    Field field;

    //****************************************************************************
    // поля для метаформы
    /**
     * Поля метаформы
     */
    List<String> mfFields = new ArrayList<>();

    //****************************************************************************
    public FieldModel(String name) {
        this.name = name;
    }

    public FieldModel(String name, String column, boolean isId) {
        this.name = name;
        this.column = column;
        this.isId = isId;
    }

    /**
     * Добавить поля метаформы
     *
     * @param names
     */
    public void addMfFieldNames(String... names) {
        for (String name : names) {
            if (name != null) {
                mfFields.add(name);
            }
        }

    }

    /**
     * обрамляет колонку в кавычки
     *
     * @return
     */
    public String getColumnNameQuoted() {
        if (!column.equals(column.toLowerCase())) {
            //есть в верхнем регистре
            return "\"" + column + "\"";
        }
        return column;
    }

    public boolean isColumn() {
        return StringUtils.isNotBlank(column);
    }

    public boolean isMF() {
        return mfFields.size() > 0;
    }

    public void setJoinedColumn(Class<?> joinedColumn) {
        this.joinedColumn = joinedColumn;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isId() {
        return isId;
    }

    public void setIsId(boolean id) {
        isId = id;
    }

    public Class<?> getJoinedColumn() {
        return joinedColumn;
    }

    public List<String> getMfFields() {
        return mfFields;
    }

    public void setMfFields(List<String> mfFields) {
        this.mfFields = mfFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FieldModel that = (FieldModel) o;

        if (!name.equals(that.name)) {
            return false;
        }
        return entityClassName.equals(that.entityClassName);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + entityClassName.hashCode();
        return result;
    }
}
