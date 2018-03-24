package ru.dude.orm.model.annotations;

/**
 * Наследование сущностей
 *
 * @author dude.
 */
public enum InheritanceType {
    SINGLE_TABLE, //всё в одной таблице
    JOINED,         //каждый класс в своей таблице
    //TABLE_PER_CLASS;  // использование не планируется
}
