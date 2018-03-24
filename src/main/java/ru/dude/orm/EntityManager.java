package ru.dude.orm;

import ru.dude.orm.mappers.TupleMapper;
import ru.dude.orm.order.Order;
import ru.dude.orm.selectors.SelectBuilder;
import ru.dude.orm.selectors.SelectorEntity;
import ru.dude.orm.clauses.Clause;
import ru.dude.orm.clauses.ClauseBuilder;
import ru.dude.orm.join.From;
import ru.dude.orm.join.Join;
import ru.dude.orm.mappers.EntityMapper;
import ru.dude.orm.model.ClassModel;
import ru.dude.orm.selectors.SelectorFunction;
import ru.dude.orm.selectors.SelectorSimple;

import java.util.*;

/**
 * Реализация методов работы с сущностями CREATE UPDATE DELETE, с вытеснением
 * платформенной зависимости в абстрактный класс и набор обёрток для выборок
 * сущностей READ
 *
 * @author dude.
 */
public class EntityManager {

    private static DBConnection connection =null;
    
    public static void init(DBConnection connection)throws OrmOperationException{
        EntityManager.connection = connection;
        EntityManager.connection.init();
        
        OrmModelStorage.getInstance().loadModels(EntityManager.class.getClassLoader());
        
    }

    public static DBConnection getConnection(){
        return EntityManager.connection;
    }
    
    //***********************************************************************************************
    /**
     * Фукнции загрузки сущности
     */
    //Возвращающие
    /**
     * Возвращающий, По ID
     *
     * @param entityClass - класс сущности
     * @param id - значение id
     * @param <E> - шаблон класса сущности
     * @return экземпляр сущности или null при его отсутсвии
     */
    public static <E> E loadByID(Class<E> entityClass, int id) throws OrmOperationException {
        From<E> from = new From<>(entityClass);
        return EntityManager.loadOneByFromClause(from, ClauseBuilder.byID(from, id));
    }

    /**
     * Возвращающий, По модели сущности и условию
     *
     * @param from - модель сущности
     * @param clause - условие для модели from
     * @param <E> - шаблон класса сущности
     * @return экземпляр сущности или null при его отсутсвии
     */
    public static <E> E loadOneByFromClause(From<E> from, Clause clause) throws OrmOperationException {
        List<E> res = loadByFromClause(from, clause);
        return res.size() > 0 ? res.get(0) : null;
    }

    /**
     * Возвращающий список, По модели сущности и условию
     *
     * @param from - модель сущности
     * @param clause - условие для модели from
     * @param <E> - шаблон класса сущности
     * @return Список экземпляров сущности. При отсутсвии результатов , список
     * пуст.
     */
    public static <E> List<E> loadByFromClause(From<E> from, Clause clause) throws OrmOperationException {
        return loadByFromClauseOrder(from, clause, null);
    }

    /**
     * Возвращающий список, По модели сущности, условию и с сортировкой
     *
     * @param from - модель сущности
     * @param clause - условие для модели from
     * @param order - сотрировка
     * @param <E> - шаблон класса сущности
     * @return Список экземпляров сущности. При отсутсвии результатов , список
     * пуст.
     */
    public static <E> List<E> loadByFromClauseOrder(From<E> from, Clause clause, Order order) throws OrmOperationException {
        Builder builder = new Builder();

        builder.from(from);
        builder.where(clause);
        builder.orderBy(order);

        return EntityManager.loadByBuilder(builder);
    }

    /**
     * Возвращающий список, По конструктору запросов
     *
     * @param builder - конструктор
     * @param <E> - шаблон класса сущности
     * @return экземпляр сущности или null при его отсутсвии
     */
    public static <E> List<E> loadByBuilder(Builder builder) throws OrmOperationException {
        return loadByBuilder(builder, builder.getFrom());
    }

    /**
     * Возвращающий список, По конструктору запросов, С указанием возвращаемой
     * сущности
     *
     * @param builder - конструктор
     * @param selectingJoin - модель возвращаемой сущности
     * @param <E> - шаблон класса сущности
     * @return Список экземпляров сущности для selectingJoin. При отсутсвии
     * результатов , список пуст.
     */
    public static <E> List<E> loadByBuilder(Builder builder, Join<E> selectingJoin) throws OrmOperationException {

        SelectorEntity<E> selectorEntity = builder.getSelectBuilder().entity(selectingJoin);

        EntityMapper<E> mapper = new EntityMapper<>(selectorEntity);

        builder.select(selectorEntity);
        builder.mapper(mapper);
        builder.build();

        return mapper.getResult();
    }

    //Устанавливающие (Сеттеры)
    /**
     * Устанавливающий , По ID
     *
     * @param entityClass - класс сущности
     * @param id - значение id
     * @param resInstance - экземпляр сущности для установки. Не изменяется если
     * значение не найдено
     * @param <E> - шаблон класса сущности
     */
    public static <E> void loadByID(Class<E> entityClass, int id, E resInstance) throws OrmOperationException {
        From<E> from = new From<>(entityClass);
        loadOneByFromClause(from, ClauseBuilder.byID(from, id), resInstance);
    }

    /**
     * Устанавливающий , По модели сущности и условию
     *
     * @param from - модель сущности
     * @param clause - условие для модели from
     * @param resInstance - экземпляр сущности для установки. Не изменяется если
     * значение не найдено
     * @param <E> - шаблон класса сущности
     */
    public static <E> void loadOneByFromClause(From<E> from, Clause clause, E resInstance) throws OrmOperationException {
        Builder builder = new Builder();

        builder.from(from);
        builder.where(clause);

        loadOneByBuilder(builder, resInstance);
    }

    /**
     * Устанавливающий , По конструктору запросов
     *
     * @param builder - конструктор
     * @param resInstance - экземпляр сущности для установки. Не изменяется если
     * значение не найдено
     * @param <E> - шаблон класса сущности
     */
    public static <E> void loadOneByBuilder(Builder builder, E resInstance) throws OrmOperationException {

        SelectorEntity<E> selectorEntity = builder.getSelectBuilder().entity(builder.getFrom());

        EntityMapper<E> mapper = new EntityMapper<>(selectorEntity);
        mapper.setResultInstance(resInstance);

        builder.select(selectorEntity);
        builder.mapper(mapper);
        builder.build();
    }

    //Количество строк
    /**
     * Возвращает количество строк в табице, с учётом условий where
     *
     * @param from
     * @param clause
     * @param <E>
     * @return
     * @throws OrmOperationException
     */
    public static <E> Long countByFromClause(From<E> from, Clause clause) throws OrmOperationException {

        Builder builder = new Builder();

        SelectBuilder sb = builder.getSelectBuilder();
        SelectorFunction<Long> countField = sb.Count();
        SelectorSimple selector = sb.Complex(countField);
        TupleMapper mapper = new TupleMapper(selector);

        builder.select(selector);
        builder.from(from);
        builder.where(clause);

        builder.mapper(mapper);
        builder.build();

        List<TupleMapper.Tuple> result = mapper.getResult();
        if (result.size() > 0) {
            return result.get(0).get(countField);
        }
        return null;
    }

    //***********************************************************************************************
    /**
     * Сохранение сущности
     *
     * @param entity - сущность для сохранения
     * @param platformSaver - платформозависимая реализация
     */
    public static void save(Object entity) throws OrmOperationException {
        ClassModel classModel = OrmModelStorage.getInstance().getClassModel(entity.getClass());
        EntitySaver entitySaver = new EntitySaver(getConnection());
        entitySaver.saveInner(entity, classModel);
    }

    //***********************************************************************************************
    /**
     * Удаление сущности
     *
     * @param entity - сущностб для удаления
     * @param platformSaver - платформозависимая реализация
     */
    public static void delete(Object entity) throws OrmOperationException {
        
        ClassModel classModel = OrmModelStorage.getInstance().getClassModel(entity.getClass());
        EntitySaver entitySaver = new EntitySaver(getConnection());
        entitySaver.deleteInner(entity, classModel);
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
