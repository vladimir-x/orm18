package ru.dude.orm.join;

import ru.dude.orm.model.JoinedField;

/**
 * Генератор псевдонимов для таблиц и селекторов
 *
 * НЕ ПОТОКОБЕЗОПАСЕН
 *
 * @author dude.
 */
public class AliasGenerator {

    /**
     * Локальный счётчик
     */
    private int aliasCounter = 0;

    /**
     * Постоянный алиас для корневой таблицы. Не поддерживается из-за
     * архитектуры построения запроса.
     */
    @Deprecated
    public String generateRootAlias() {
        return "_root";
    }

    /**
     * Псевдоним для присоединяемой таблицы
     *
     * @param tableName обковыченное имя таблицы с пространством имён
     * @return псевдоним формата xxxxN
     */
    public String generateJoinAlias(String tableName) {
        tableName = tableName.replaceAll("\"", "").toLowerCase().trim();
        int divPos = tableName.lastIndexOf(".");
        if (divPos < 0) {
            return "_" + substr4(tableName) + nextCounter();
        } else {
            String namepart = tableName.substring(divPos + 1);
            return "_" + substr4(namepart) + nextCounter();
        }
    }

    /**
     * Псевдоним для поля в бд(или для селектора с этим полем)
     *
     * @param joinedField - модель поля
     * @return псевдоним формата xxxxN
     */
    public String generateSelectAlias(JoinedField joinedField) {

        String tableAlias = joinedField.getTableAlias();
        String fieldColumn = joinedField.getFieldModel().getColumn();

        return tableAlias + "_" + substr4(fieldColumn) + nextCounter();
    }

    /**
     * Псевдоним для функционального выражения
     *
     * @param functionName имя фонкционального выражения или его начало
     * @return
     */
    public String generateFunctionAlias(String functionName) {
        return substr4(functionName) + nextCounter();
    }

    /**
     * Выдать следующий идентификатор
     *
     * @return
     */
    private Integer nextCounter() {
        return aliasCounter++;
    }

    /**
     * Подстрока 4 символа, для имени псевдонима
     *
     * @param x
     * @return
     */
    private String substr4(String x) {
        x = x.trim();
        if (x.length() > 4) {
            return x.substring(0, 4).trim();
        } else {
            return x;
        }
    }
}
