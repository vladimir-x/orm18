package ru.dude.orm.model.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация колонки
 *
 * author dude.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    String name() default "";
}
