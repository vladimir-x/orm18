package ru.dude.orm.model.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация наследования сущности
 *
 * author dude.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Inheritance {

    InheritanceType strategy() default InheritanceType.SINGLE_TABLE;
}
