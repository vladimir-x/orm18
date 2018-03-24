package ru.dude.orm.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Признак сгенерированного статического класса описания сущности.
 *
 * @author dude.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StaticMetamodel {

    /**
     * Class being modeled by the annotated class. Класс сущности с описаниями
     * полей.
     */
    Class<?> value();
}
