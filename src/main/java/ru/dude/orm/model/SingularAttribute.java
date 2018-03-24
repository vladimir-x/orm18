package ru.dude.orm.model;

/**
 * Абстрактный класс, базовый для полей статическоймодели
 *
 * @author dude.
 */
public abstract class SingularAttribute<X, T> {

    /**
     * Получить созданную в памяти модель поля
     *
     * @return
     */
    public abstract FieldModel getFieldModel();
}
