package ru.dude.orm.model;

import ru.dude.orm.model.annotations.Column;
import ru.dude.orm.model.annotations.StaticMetamodel;

import javax.annotation.Generated;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Для генератора
 *
 * Метамодель класса , при генерации статической модели
 *
 * @author dude.
 */
public class MetaEntity {

    /**
     * Маркер генератора
     */
    public static final String GENERATED_MARK = "@Generated(value = \"" + Orm18ModelGenerator.class.getName() + "\")";

    /**
     * Пакет класса
     */
    String packageName;

    /**
     * �?мя класса
     */
    String modelClassName;

    /**
     * Поля класса
     */
    List<MetaAttribute> attributes;

    MetaEntity(Element element) {
        packageName = element.getEnclosingElement().toString();
        modelClassName = element.getSimpleName().toString();

        attributes = new ArrayList<>();

        for (Element child : element.getEnclosedElements()) {
            if (child.getKind() == ElementKind.FIELD) {
                if (child.getAnnotation(Column.class) != null) {
                    attributes.add(new MetaAttribute(child, modelClassName));
                }
            }
        }
    }

    /**
     * генерация
     *
     * @return
     */
    public String generateCode() {

        Set<String> imports = new HashSet<>();

        StringBuilder importBulder = new StringBuilder();
        StringBuilder bodyBulder = new StringBuilder();

        imports.add(Generated.class.getName());
        imports.add(StaticMetamodel.class.getName());
        imports.add(SingularAttribute.class.getName());
        imports.add(packageName + "." + modelClassName);

        importBulder.append("import " + Generated.class.getName() + ";\n");
        importBulder.append("import " + StaticMetamodel.class.getName() + ";\n");
        importBulder.append("import " + SingularAttribute.class.getName() + ";\n");
        importBulder.append("import " + packageName + "." + modelClassName + ";\n");

        bodyBulder.append(GENERATED_MARK).append("\n");
        bodyBulder.append("@" + StaticMetamodel.class.getSimpleName() + "(" + modelClassName + ".class)\n");

        bodyBulder.append("public abstract class ");
        bodyBulder.append(getGeneratedClassName());
        bodyBulder.append("{\n");

        for (MetaAttribute attribute : attributes) {

            if (!imports.contains(attribute.getFieldType())) {
                imports.add(attribute.getFieldType());
                importBulder.append("import " + attribute.getFieldType() + ";\n");
            }

            bodyBulder.append("\tpublic static volatile SingularAttribute<" + modelClassName + "," + attribute.getFieldTypeName() + "> " + attribute.getFieldName() + ";\n");
        }

        bodyBulder.append("}");

        StringBuilder allBuilder = new StringBuilder();

        allBuilder.append("package " + packageName + ";\n\n");

        allBuilder.append(importBulder);
        allBuilder.append("\n");
        allBuilder.append(bodyBulder);

        return allBuilder.toString();
    }

    public String getGeneratedClassName() {
        return modelClassName + "_";
    }

    public String getGeneratedFilePath() {
        return packageName + "." + modelClassName + "_";
    }

    public String getPackageName() {
        return packageName;
    }

    private void addImport(String className, Set<String> imports) {
        if (!imports.contains(className)) {
            imports.add(className);
        }
    }

    public String getModelClassName() {
        return modelClassName;
    }
}
