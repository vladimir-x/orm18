package ru.dude.orm.selectors;

import ru.dude.orm.Builder;
import ru.dude.orm.model.JoinedField;
import ru.dude.orm.join.AliasGenerator;
import ru.dude.orm.join.Join;

import java.util.ArrayList;
import java.util.List;

/**
 * Набор селекторов
 *
 * @author dude.
 */
public class SelectBuilder {

    /**
     * Генератор псевдонимов
     */
    AliasGenerator aliasGenerator;

    /**
     * Объект создаётся в Builder
     */
    private SelectBuilder() {

    }

    /**
     * Создать объект для билдера
     *
     * @param builder
     * @return
     */
    public static SelectBuilder createForBuilder(Builder builder) {
        SelectBuilder sb = new SelectBuilder();
        sb.aliasGenerator = builder.GetAliasGenerator();
        return sb;
    }

    /**
     * Простой селектор, для набора полей
     *
     * @param joinedFields поля из запроса
     * @return
     */
    public SelectorSimple Simple(JoinedField... joinedFields) {
        List<SelectableField> sfs = new ArrayList<>();
        for (JoinedField jf : joinedFields) {
            sfs.add(new SelectableJoinedField(jf, aliasGenerator));
        }

        return new SelectorSimple(sfs);
    }

    /**
     * Сложный селектор для полей и агрегатных функций
     *
     * @param selectableFields
     * @return
     */
    public SelectorSimple Complex(SelectableField... selectableFields) {
        if (selectableFields != null && selectableFields.length > 0) {
            ArrayList<SelectableField> sfs = new ArrayList<SelectableField>();
            for (SelectableField sf : selectableFields) {
                if (sf != null) {
                    sfs.add(sf);
                }
            }

            if (sfs.size() > 0) {
                return new SelectorSimple(sfs);
            }
        }
        return null;
    }

    /**
     * Обёртка для создания выбираемого поля
     *
     * @param joinedField
     * @param <E>
     * @return
     */
    public <E> SelectableJoinedField<E> MakeSelectableJF(JoinedField<E> joinedField) {
        return new SelectableJoinedField<E>(joinedField, aliasGenerator);
    }

    /**
     * Селектор для сущности
     *
     * @param join объект таблицы
     * @param <E> тип возвращаемой сущности
     * @return
     */
    public <E> SelectorEntity<E> entity(Join<E> join) {
        return new SelectorEntity(aliasGenerator, join);
    }

    /**
     * Селектор для нескольких сущностей
     *
     * @param selectorsEntity селекторы сущностей
     * @return
     */
    public SelectorMultiEntity MultiEntity(SelectorEntity... selectorsEntity) {
        return new SelectorMultiEntity(selectorsEntity);
    }

    /**
     * Функция AVG
     *
     * @param joinedField объект запроса поле
     * @return
     */
    public SelectorFunction Avg(JoinedField joinedField) {
        return new SelectorFunction<Double>(aliasGenerator, "avg(%s)", joinedField, Double.class);
    }

    /**
     * Функция COUNT(*)
     *
     * @return
     */
    public SelectorFunction Count() {
        return new SelectorFunction<Long>(aliasGenerator, "count(*)", new Object[]{}, Long.class);
    }

    /**
     * Функция COUNT
     *
     * @param joinedField
     * @return Количество полей joinedField!=null
     */
    public SelectorFunction Count(JoinedField joinedField) {
        return new SelectorFunction<Long>(aliasGenerator, "count(%s)", joinedField, Long.class);
    }

    /**
     * Функция MAX
     *
     * @param joinedField поле
     * @param javaClass класс для преобразования в java. �?спользуется только
     * при селекте
     * @return
     */
    public <T> SelectorFunction Max(JoinedField joinedField, Class<T> javaClass) {
        return new SelectorFunction<T>(aliasGenerator, "max(%s)", joinedField, javaClass);
    }

    /**
     * Функция MIN
     *
     * @param joinedField поле
     * @param javaClass класс для преобразования в java. �?спользуется только
     * при селекте
     * @return
     */
    public <T> SelectorFunction Min(JoinedField joinedField, Class<T> javaClass) {
        return new SelectorFunction<T>(aliasGenerator, "min(%s)", joinedField, javaClass);
    }

    /**
     * Функция SUM
     *
     * @param joinedField поле
     * @return
     */
    public SelectorFunction Sum(JoinedField joinedField) {
        return new SelectorFunction<Double>(aliasGenerator, "min(%s)", joinedField, Double.class);
    }

    /**
     * Функция AGE
     *
     * @param joinedField
     * @return
     */
    public SelectorFunction Age(JoinedField joinedField) {
        return new SelectorFunction<Double>(aliasGenerator, "age(%s)", joinedField, Double.class);
    }

    /**
     * Функция количество дней между "Сейчас" и joinedField
     *
     * @param joinedField объект запроса поле
     * @return
     */
    public SelectorFunction DayBetweenNow(JoinedField joinedField) {
        return new SelectorFunction(aliasGenerator, "EXTRACT(DAY FROM (now()- %s))", joinedField, String.class);
    }

    /**
     * Функция количество дней между fixedDate и joinedField
     *
     * @param joinedField объект запроса поле
     * @param fixedDate константа в формате "YYYY-MM-DD"
     * @return
     */
    public SelectorFunction DayBetweenFixed(JoinedField joinedField, String fixedDate) {
        return new SelectorFunction(aliasGenerator, "EXTRACT(DAY FROM (%s - %s))", new Object[]{fixedDate, joinedField.buildSql()}, String.class);
    }

    /**
     * Объединение строк
     *
     * @param joinedFields
     * @return
     */
    public SelectorFunction Concatenate(JoinedField... joinedFields) {
        StringBuilder sb = new StringBuilder();
        List<String> operands = new ArrayList<>();
        if (joinedFields != null) {
            for (JoinedField joinedField : joinedFields) {
                if (joinedField != null) {
                    if (sb.length() > 0) {
                        sb.append("||");
                    }
                    sb.append("%s");
                    operands.add(joinedField.buildSql());
                }
            }
            return new SelectorFunction(aliasGenerator, sb.toString(), operands.toArray(), String.class);
        }
        return null;
    }

    /**
     * Перевод к нижнему регистру
     *
     * @param selectorFunction
     * @return
     */
    public SelectorFunction ToLower(SelectorFunction selectorFunction) {
        return new SelectorFunction(aliasGenerator, "lower(%s)", new Object[]{selectorFunction.buildClauseSql()}, String.class);
    }

}
