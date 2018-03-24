package ru.dude.orm.model;

/**
 * Общий класс модели поля
 *
 * @author dude.
 */
public class SingularAttributeImpl extends SingularAttribute {

    FieldModel fieldModel;

    public FieldModel getFieldModel() {
        return fieldModel;
    }

    public void setFieldModel(FieldModel fieldModel) {
        this.fieldModel = fieldModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SingularAttributeImpl that = (SingularAttributeImpl) o;

        return fieldModel != null ? fieldModel.equals(that.fieldModel) : that.fieldModel == null;
    }

    @Override
    public int hashCode() {
        return fieldModel != null ? fieldModel.hashCode() : 0;
    }
}
