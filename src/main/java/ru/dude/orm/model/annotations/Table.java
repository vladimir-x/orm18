package ru.dude.orm.model.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация таблицы
 *
 * @author dude.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    String name() default "";
}
