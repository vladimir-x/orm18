package ru.dude.orm.model;

import ru.dude.orm.model.util.PrimitiveConvert;

import javax.lang.model.element.Element;

/**
 * Для генератора
 *
 * Модель поля класса, для генерации статической модели
 *
 *
 * @author dude.
 */
public class MetaAttribute {

    /**
     * Тип класса, в котором содержится описываемое поле
     */
    String parentType;

    /**
     * �?мя поля
     */
    String fieldName;

    /**
     * Тип поля
     */
    String fieldType;

    /**
     * Часть типа: только имя класса
     */
    String fieldTypeName;

    /**
     * Часть типа: только пакет
     */
    String fieldTypePackage;

    MetaAttribute(Element element, String parentType) {
        this.parentType = parentType;

        fieldName = element.getSimpleName().toString();
        fieldType = element.asType().toString();

        if (PrimitiveConvert.isPrimitive(fieldType)) {
            fieldType = PrimitiveConvert.getClassName(fieldType);
        }

        int firstDiamond = fieldType.indexOf("<");
        if (firstDiamond > 0) {
            fieldType = fieldType.substring(0, firstDiamond);
        }

        int lastDot = fieldType.lastIndexOf(".");

        fieldTypeName = fieldType.substring(lastDot + 1);
        try {
            fieldTypePackage = fieldType.substring(0, lastDot);
        } catch (Exception ex) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>" + fieldName + " , " + fieldType);
            ex.printStackTrace();
        }

    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getFieldTypeName() {
        return fieldTypeName;
    }

    public String getFieldTypePackage() {
        return fieldTypePackage;
    }
}
