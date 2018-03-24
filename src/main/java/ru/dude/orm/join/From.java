package ru.dude.orm.join;

import ru.dude.orm.OrmOperationException;
import ru.dude.orm.model.JoinedField;
import ru.dude.orm.clauses.ClauseBuilder;
import ru.dude.orm.model.SingularAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель корневой таблицы в запросе
 *
 * @author dude.
 *
 * @param <E> Тип Сущности для данной таблицы/модели
 */
public class From<E> extends Join<E> {

    /**
     * Набор присоединённых таблиц Суда попадают все джойны, кроме наследников
     */
    List<Join> joinList = new ArrayList<>();

    /**
     * Корневая модель
     *
     * @param entityClass класс сущности, корневой таблицы
     */
    public From(Class<E> entityClass) throws OrmOperationException {
        super(entityClass, new AliasGenerator());
        //  alias = aliasGenerator.generateRootAlias();
        joinType = JoinType.FROM;
    }

    /**
     * Присоединение сущности. Тип присоединения LEFT_JOIN !!! К возвращаемой
     * сущности необходимо добавлять условие присоедиения Join.setOn(Clause)
     * !!!! Использовать только для создания Join со сложным условием ON
     *
     * @param entityClass класс присоединяемой сущности
     * @param <X> Тип присоединяемой сущности
     * @return
     */
    public <X> Join join(Class<X> entityClass) throws OrmOperationException {
        Join<X> res = new Join<X>(entityClass, aliasGenerator);
        res.joinType = JoinType.LEFT_JOIN;
        res.on = null;
        this.joinList.add(res);
        return res;
    }

    /**
     * Присоединение сущности Тип присоединения LEFT_JOIN Условие присоединения:
     * from.fromField = join.get(joinedField)
     *
     * @param entityClass класс присоединяемой сущности
     * @param joinedField поле из присоединяемой сущности для условия
     * присоединения
     * @param fromField поле из корневой/предыдущей/соседней дней сущности зи
     * завпроса для условия присоединения
     * @param <X> Тип присоединяемой сущности
     * @return
     */
    public <X> Join<X> join(Class<X> entityClass, SingularAttribute joinedField, JoinedField fromField) throws OrmOperationException {
        Join<X> res = join(entityClass);
        res.on = ClauseBuilder.eq(res.get(joinedField), fromField);
        return res;
    }

    public String buildSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.buildSql());

        for (Join<?> join : joinList) {
            sb.append(join.buildSql());
        }
        return sb.toString();
    }

}
