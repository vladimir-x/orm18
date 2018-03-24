package ru.dude.orm.clauses;

import org.apache.commons.lang3.StringUtils;
import ru.dude.orm.OrmOperationException;
import ru.dude.orm.model.JoinedField;
import ru.dude.orm.join.Join;
import ru.dude.orm.model.DBTypeConvert;
import ru.dude.orm.selectors.SelectorFunction;

import java.util.Date;

/**
 * Набор готовых условий для вставки в запросы
 *
 * @author dude.
 */
public class ClauseBuilder {

    /**
     * Группа операндов, с логической связкой AND Значения null игнорируются
     * Если указан один не null предикат - возвращает его без AND Если все
     * предикаты null , возвращает null
     *
     * @param clauses предикаты
     * @return AND условие, одно условие, или null в завсисмости от входных
     * предикатов
     */
    public static Clause and(Clause... clauses) {
        return new ClauseMany(Operand.AND, clauses);
    }

    /**
     * Группа операндов, с логической связкой OR Значения null игнорируются Если
     * указан один не null предикат - возвращает его без OR Если все предикаты
     * null , возвращает null
     *
     * @param clauses предикаты
     * @return OR условие, одно условие, или null в завсисмости от входных
     * предикатов
     */
    public static Clause or(Clause... clauses) {
        return new ClauseMany(Operand.OR, clauses);
    }

    /**
     * Предикат a < b
     *
     * @param a поле в БД
     * @param b поле в БД
     * @return
     */
    public static Clause less(JoinedField a, JoinedField b) {
        return new ClauseBinar(a, Operand.LESS, b);
    }

    /**
     * Предикат a < b
     *
     * @param a поле в БД
     * @param b функциональное выражение
     * @return
     */
    public static Clause less(JoinedField a, SelectorFunction b) {
        return new ClauseBinar(a, Operand.LESS, b.buildClauseSql());
    }

    /**
     * Предикат a < entityValue entityValue преобразовывается к SQL по правилам
     * класса DBTypeConvert @param a поле в БД @param entityValue java объект -
     * значение @return
     *
     */
    public static Clause less(JoinedField a, Object entityValue) throws OrmOperationException {
        String sqlValue = new DBTypeConvert().ToSqlValue(entityValue);
        return new ClauseBinar(a, Operand.LESS, sqlValue);
    }

    /**
     * Предикат a < entityValue entityValue преобразовывается к SQL по правилам
     * класса DBTypeConvert @param a функциональное выражение @param entityValue
     * java объект - значение
     *
     * @
     * r
     * eturn
     */
    public static Clause less(SelectorFunction a, Object entityValue) throws OrmOperationException {
        String sqlValue = new DBTypeConvert().ToSqlValue(entityValue);
        return new ClauseBinar(a.buildClauseSql(), Operand.LESS, sqlValue);
    }

    /**
     * Предикат a <= b
     *
     * @param a поле в БД
     * @param b поле в БД
     * @return
     */
    public static Clause lessEq(JoinedField a, JoinedField b) {
        return new ClauseBinar(a, Operand.LESS_EQ, b);
    }

    /**
     * Предикат a <= b
     *
     * @param a поле в БД
     * @param b функциональное выражение
     * @return
     */
    public static Clause lessEq(JoinedField a, SelectorFunction b) {
        return new ClauseBinar(a, Operand.LESS_EQ, b.buildClauseSql());
    }

    /**
     * Предикат a <= entityValue entityValue преобразовывается к SQL по правилам
     * класса DBTypeConvert @param a поле в БД @param entityValue java объект -
     * значение @return
     *
     */
    public static Clause lessEq(JoinedField a, Object entityValue) throws OrmOperationException {
        String sqlValue = new DBTypeConvert().ToSqlValue(entityValue);
        return new ClauseBinar(a, Operand.LESS_EQ, sqlValue);
    }

    /**
     * Предикат a <= entityValue entityValue преобразовывается к SQL по правилам
     * класса DBTypeConvert @param a функциональное выражение @param entityValue
     * java объект - значение
     *
     * @
     * r
     * eturn
     */
    public static Clause lessEq(SelectorFunction a, Object entityValue) throws OrmOperationException {
        String sqlValue = new DBTypeConvert().ToSqlValue(entityValue);
        return new ClauseBinar(a.buildClauseSql(), Operand.LESS_EQ, sqlValue);
    }

    /**
     * Предикат a > b
     *
     * @param a поле в БД
     * @param b поле в БД
     * @return
     */
    public static Clause more(JoinedField a, JoinedField b) {
        return new ClauseBinar(a, Operand.MORE, b);
    }

    /**
     * Предикат a > b
     *
     * @param a поле в БД
     * @param b функциональное выражение
     * @return
     */
    public static Clause more(JoinedField a, SelectorFunction b) {
        return new ClauseBinar(a, Operand.MORE, b.buildClauseSql());
    }

    /**
     * Предикат a > entityValue entityValue преобразовывается к SQL по правилам
     * класса DBTypeConvert
     *
     * @param a функциональное выражение
     * @param entityValue java объект - значение
     * @return
     */
    public static Clause more(SelectorFunction a, Object entityValue) throws OrmOperationException {
        String sqlValue = new DBTypeConvert().ToSqlValue(entityValue);
        return new ClauseBinar(a.buildClauseSql(), Operand.MORE, sqlValue);
    }

    /**
     * Предикат a > b
     *
     * @param a поле в БД
     * @param entityValue java объект - значение entityValue преобразовывается к
     * SQL по правилам класса DBTypeConvert
     * @return
     */
    public static Clause more(JoinedField a, Object entityValue) throws OrmOperationException {
        String sqlValue = new DBTypeConvert().ToSqlValue(entityValue);
        return new ClauseBinar(a, Operand.MORE, sqlValue);
    }

    /**
     * Предикат a >= b
     *
     * @param a поле в БД
     * @param entityValue java объект - значение entityValue преобразовывается к
     * SQL по правилам класса DBTypeConvert
     * @return
     */
    public static Clause moreEq(SelectorFunction a, Object entityValue) throws OrmOperationException {
        String sqlValue = new DBTypeConvert().ToSqlValue(entityValue);
        return new ClauseBinar(a.buildClauseSql(), Operand.MORE_EQ, sqlValue);
    }

    /**
     * Предикат a >= b
     *
     * @param a поле в БД
     * @param b поле в БД
     * @return
     */
    public static Clause moreEq(JoinedField a, JoinedField b) {
        return new ClauseBinar(a, Operand.MORE_EQ, b);
    }

    /**
     * Предикат a >= b
     *
     * @param a поле в БД
     * @param b функциональное выражение
     * @return
     */
    public static Clause moreEq(JoinedField a, SelectorFunction b) {
        return new ClauseBinar(a, Operand.MORE_EQ, b.buildClauseSql());
    }

    /**
     * Предикат a >= b
     *
     * @param a поле в БД
     * @param entityValue java объект - значение entityValue преобразовывается к
     * SQL по правилам класса DBTypeConvert
     * @return
     */
    public static Clause moreEq(JoinedField a, Object entityValue) throws OrmOperationException {
        String sqlValue = new DBTypeConvert().ToSqlValue(entityValue);
        return new ClauseBinar(a, Operand.MORE_EQ, sqlValue);
    }

    /**
     * Предикат с указанием типа операции. Не рекомендуется использовать.
     *
     * Новые предикаты можно реализовывать и добавлять в класс ClauseBuilder по
     * мере необходимости
     *
     * @param a левое поле
     * @param op операция
     * @param b правое поле
     * @return
     */
    @Deprecated
    public static Clause raw(JoinedField a, Operand op, JoinedField b) {
        return new ClauseBinar(a, op, b);
    }

    /**
     * Предикат с указанием типа операции, с проверкой на not NULL Не
     * рекомендуется использовать.
     *
     * Новые предикаты можно реализовывать и добавлять в класс ClauseBuilder по
     * мере необходимости
     *
     * @param a левое поле в бд
     * @param op операция
     * @param entityValue java объект - значение
     * @return предикат, или null, если entityValue == null
     */
    @Deprecated
    public static Clause rawNotNull(JoinedField a, Operand op, Object entityValue) throws OrmOperationException {
        if (entityValue != null) {
            String sqlValue = new DBTypeConvert().ToSqlValue(entityValue);
            return new ClauseBinar(a, op, sqlValue);
        }
        return null;
    }

    /**
     * Предикат с указанием типа операции, с проверкой на not NULL Не
     * рекомендуется использовать.
     *
     * Новые предикаты можно реализовывать и добавлять в класс ClauseBuilder по
     * мере необходимости
     *
     * @param a функциональное выражение
     * @param op операция
     * @param entityValue java объект - значение
     * @return предикат, или null, если entityValue == null
     */
    @Deprecated
    public static Clause rawNotNull(SelectorFunction a, Operand op, Object entityValue) throws OrmOperationException {
        if (entityValue != null) {
            String sqlValue = new DBTypeConvert().ToSqlValue(entityValue);
            return new ClauseBinar(a.buildClauseSql(), op, sqlValue);
        }
        return null;
    }

    /**
     * Предикат с указанием типа операции, с проверкой на not NULL и на не
     * пустую строку Не рекомендуется использовать.
     *
     * Новые предикаты можно реализовывать и добавлять в класс ClauseBuilder по
     * мере необходимости
     *
     * @param a функциональное выражение
     * @param op операция
     * @param stringValue строкове значение
     * @return предикат, или null, если stringValue == null
     */
    @Deprecated
    public static Clause rawNotBlank(SelectorFunction a, Operand op, String stringValue) throws OrmOperationException {
        if (StringUtils.isNotBlank(stringValue)) {
            String sqlValue = new DBTypeConvert().ToSqlValue(stringValue);
            return new ClauseBinar(a.buildClauseSql(), op, sqlValue);
        }
        return null;
    }

    /**
     * Предикат a = b
     *
     * @param a поле в БД
     * @param b поле в БД
     * @return
     */
    public static Clause eq(JoinedField a, JoinedField b) {
        return new ClauseBinar(a, Operand.EQ, b);
    }

    /**
     * Предикат a = entityValue
     *
     * @param a поле в БД
     * @param entityValue java объект - значение entityValue преобразовывается к
     * SQL по правилам класса DBTypeConvert
     * @return
     */
    public static Clause eq(JoinedField a, Object entityValue) throws OrmOperationException {
        String sqlValue = new DBTypeConvert().ToSqlValue(entityValue);
        return new ClauseBinar(a, Operand.EQ, sqlValue);
    }

    /**
     * Предикат a = entityValue entityValue преобразовывается к SQL по правилам
     * класса DBTypeConvert
     *
     * @param a функциональное выражение
     * @param entityValue java объект - значение
     * @return
     */
    public static Clause eq(SelectorFunction a, Object entityValue) throws OrmOperationException {
        String sqlValue = new DBTypeConvert().ToSqlValue(entityValue);
        return new ClauseBinar(a.buildClauseSql(), Operand.EQ, sqlValue);
    }

    /**
     * Предикат a = b
     *
     * @param a поле в БД
     * @param b функциональное выражение
     * @return
     */
    public static Clause eq(JoinedField a, SelectorFunction b) {
        return new ClauseBinar(a, Operand.EQ, b.buildClauseSql());
    }

    /**
     * Предикат a = entityValue, с проверкой на не null
     *
     * @param a поле в БД
     * @param entityValue java объект - значение entityValue преобразовывается к
     * SQL по правилам класса DBTypeConvert
     * @return предикат или null если entityValue==null
     */
    public static Clause eqNotNull(JoinedField a, Object entityValue) throws OrmOperationException {
        if (entityValue != null) {
            return ClauseBuilder.eq(a, entityValue);
        }
        return null;
    }

    /**
     * Предикат a = stringValue, с проверкой на не null и на не пустую строку
     *
     * @param a поле в БД
     * @param stringValue строковое значение
     * @return предикат или null если (stringValue==null или пустая)
     */
    public static Clause eqNotBlank(JoinedField a, String stringValue) throws OrmOperationException {
        if (StringUtils.isNotBlank(stringValue)) {
            return ClauseBuilder.eq(a, stringValue);
        }
        return null;
    }

    /**
     * Предикат a ILIKE stringValue, с проверкой на не null и на не пустую
     * строку знаки "%" необходимо указывать вручную, слева и/или справа от
     * операнда stringValue
     *
     * @param a поле в БД
     * @param stringValue строковое sql значение
     * @return предикат или null если (stringValue==null или пустая)
     */
    public static Clause ilikeNotBlank(JoinedField a, String stringValue) throws OrmOperationException {
        String unquoted = stringValue.replace("%", "").trim();
        if (StringUtils.isNotBlank(unquoted) && !unquoted.equals("null")) {
            return new ClauseBinar(a, Operand.ILIKE, "'" + stringValue + "'");
        }
        return null;
    }

    /**
     * Предикат a ILIKE stringValue, с проверкой на не null и на не пустую
     * строку знаки "%" необходимо указывать вручную, слева и/или справа от
     * операнда stringValue
     *
     * @param a функциональное выражение
     * @param stringValue строковое sql значение
     * @return предикат или null если (stringValue==null или пустая)
     */
    public static Clause ilikeNotBlank(SelectorFunction a, String stringValue) throws OrmOperationException {
        if (StringUtils.isNotBlank(stringValue.replace("%", ""))) {
            return new ClauseBinar(a.buildClauseSql(), Operand.ILIKE, "'" + stringValue + "'");
        }
        return null;
    }

    /**
     * Предикат not
     *
     * @param clause условие
     * @return
     */
    public static Clause not(Clause clause) {
        return new ClauseNot(clause);
    }

    /**
     * Предикат IN
     *
     *
     * @param a поле в БД
     * @param vals группа значений, обрабатывается по правилам DBTypeConvert
     * @return
     */
    public static Clause in(JoinedField a, Object... vals) throws OrmOperationException {
        return new ClauseIn(a, vals);
    }

    /**
     * Предикат is_true
     *
     * @param a поле в бд
     * @return
     */
    public static Clause isTrue(JoinedField a) {
        return new ClauseUnar(a, Operand.IS_TRUE);
    }

    public static Clause isFalse(JoinedField a) {
        return new ClauseUnar(a, Operand.IS_FALSE);
    }

    public static Clause isNull(JoinedField a) {
        return new ClauseUnar(a, Operand.IS_NULL);
    }

    public static Clause isNotNull(JoinedField a) {
        return new ClauseUnar(a, Operand.IS_NOT_NULL);
    }

    /**
     * Предикат перекрытие дат
     *
     * @param st_a
     * @param en_a
     * @param st_b
     * @param en_b
     * @return
     */
    public static Clause overlaps(JoinedField st_a, JoinedField en_a, JoinedField st_b, JoinedField en_b) {
        String left = "(" + st_a.buildSql() + "," + en_a.buildSql() + ")";
        String right = "(" + st_b.buildSql() + "," + en_b.buildSql() + ")";
        return new ClauseBinar(left, Operand.OVERLAPS, right);
    }

    /**
     * Предикат перекрытие дат
     *
     * @param st_a
     * @param en_a
     * @param st_b
     * @param en_b
     * @return
     */
    public static Clause overlaps(JoinedField st_a, JoinedField en_a, Date st_b, Date en_b) throws OrmOperationException {
        DBTypeConvert convert = new DBTypeConvert();
        String left = "(" + st_a.buildSql() + "," + en_a.buildSql() + ")";
        String right = "(" + convert.ToSqlValue(st_b) + "," + convert.ToSqlValue(en_b) + ")";
        return new ClauseBinar(left, Operand.OVERLAPS, right);
    }

    /**
     * Предикат равенства по ID полю a.ID = b.ID ориетируется по аннотациям @ID
     *
     * @param a объект левой таблицы
     * @param b объект правой таблицы
     * @return
     */
    public static Clause byID(Join a, Join b) {
        return ClauseBuilder.eq(a.getIDFiled(), b.getIDFiled());
    }

    /**
     * Предикат равенства по ID полю a.ID = b ориетируется по аннотациям @ID
     *
     * @param a объект левой таблицы
     * @param b значение ID
     * @return
     */
    public static Clause byID(Join a, Integer b) throws OrmOperationException {
        return ClauseBuilder.eq(a.getIDFiled(), b);
    }
}
