package ru.dude.orm.selectors;

import ru.dude.orm.join.AliasGenerator;
import ru.dude.orm.model.JoinedField;

/**
 * Селектор для реализации функций (агрегатных и выражений с функциями БД)
 *
 * @author dude.
 */
public class SelectorFunction<R> implements SelectableField {

    /**
     * Маска с sql кодом и символами %s для вставки операндов
     */
    String formatMask;

    /**
     * Операнды
     */
    Object[] operands;

    /**
     * Псевдоним для селектора
     */
    String alias;

    /**
     * Тип возвращаемого значения в java
     */
    Class<?> javaType;

    public SelectorFunction(AliasGenerator aliasGenerator, String formatMask, Object[] operands, Class<?> javaType) {
        this.formatMask = formatMask;
        this.operands = operands;
        this.javaType = javaType;

        alias = aliasGenerator.generateFunctionAlias(formatMask);
    }

    public SelectorFunction(AliasGenerator aliasGenerator, String formatMask, JoinedField joinedField, Class<?> javaType) {
        this.formatMask = formatMask;
        this.operands = new Object[]{joinedField.buildSql()};
        this.javaType = javaType;

        alias = aliasGenerator.generateFunctionAlias(formatMask);
    }

    @Override
    public String buildSql() {
        return buildClauseSql() + " as " + alias;
    }

    public String buildClauseSql() {
        return String.format(formatMask, operands);
    }

    @Override
    public String getAlias() {
        return alias;
    }

    public Class<?> getJavaType() {
        return javaType;
    }
}
