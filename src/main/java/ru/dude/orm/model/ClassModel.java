package ru.dude.orm.model;

import java.util.*;
import ru.dude.orm.model.annotations.InheritanceType;

/**
 * Динамическая модель класса
 *
 * @author dude.
 */
public class ClassModel {

    /**
     * SQL имя таблицы, обковыченное, с указанием пространства имён
     */
    String tableName;

    /**
     * Модели полей текущей сущности, без предков
     */
    Set<FieldModel> fieldModels = new LinkedHashSet<>();

    /**
     * Модели полей текущего сущности, с предками InheritanceType.SINGLE_TABLE
     */
    Set<FieldModel> fieldAllInheritanceModels = new LinkedHashSet<>();

    /**
     * Мэп для быстрого поиска модели поля по его имени TODO: вероятно не
     * используется и можно убрать
     */
    Map<String, FieldModel> fieldModelsMap = new HashMap<>();

    /**
     * Модель поля @ID
     */
    FieldModel idFieldModel;

    /**
     * Класс сущности
     */
    Class<?> entityClass;

    /**
     * Тип наследования у текущей сущности
     */
    InheritanceType inheritanceType;

    /**
     * базовый класс сущности, без учёта модели наследования используется только
     * на этапе создания модели
     */
    private ClassModel parentClassModel;

    /**
     * базовый класс сущности с моделью наследования InheritanceType.JOINED
     */
    ClassModel parentJoinedClassModel;

    public ClassModel() {

    }

    /**
     * Добавить модель поля
     *
     * @param fieldModel
     */
    public void addFieldModel(FieldModel fieldModel) {
        fieldModelsMap.put(fieldModel.getName(), fieldModel);
        fieldModels.add(fieldModel);
    }

    /**
     * Получить модель поля по имени
     *
     * @param name
     * @return
     */
    public FieldModel getFieldByName(String name) {
        if (fieldModelsMap.containsKey(name)) {
            return fieldModelsMap.get(name);
        }
        return null;
    }

    public FieldModel getIdFieldModel() {
        return idFieldModel;
    }

    public void setIdFieldModel(FieldModel idFieldModel) {
        this.idFieldModel = idFieldModel;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Set<FieldModel> getFieldModels() {
        return fieldModels;
    }

    public void setFieldModels(Set<FieldModel> fieldModels) {
        this.fieldModels = fieldModels;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public InheritanceType getInheritanceType() {
        return inheritanceType;
    }

    public void setInheritanceType(InheritanceType inheritanceType) {
        this.inheritanceType = inheritanceType;
    }

    public ClassModel getParentClassModel() {
        return parentClassModel;
    }

    public void setParentClassModel(ClassModel parentClassModel) {
        this.parentClassModel = parentClassModel;
    }

    public Set<FieldModel> getFieldAllInheritanceModels() {
        return fieldAllInheritanceModels;
    }

    public void setFieldAllInheritanceModels(Set<FieldModel> fieldAllInheritanceModels) {
        this.fieldAllInheritanceModels = fieldAllInheritanceModels;
    }

    public ClassModel getParentJoinedClassModel() {
        return parentJoinedClassModel;
    }

    public void setParentJoinedClassModel(ClassModel parentJoinedClassModel) {
        this.parentJoinedClassModel = parentJoinedClassModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassModel that = (ClassModel) o;

        if (!tableName.equals(that.tableName)) {
            return false;
        }
        return entityClass.equals(that.entityClass);
    }

    @Override
    public int hashCode() {
        int result = tableName.hashCode();
        result = 31 * result + entityClass.hashCode();
        return result;
    }
}
