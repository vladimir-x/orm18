package ru.dude.orm.mappers;

import ru.dude.orm.selectors.SelectableField;
import ru.dude.orm.selectors.SelectableJoinedField;
import ru.dude.orm.selectors.SelectorEntity;
import ru.dude.orm.model.DBTypeConvert;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Маппер в сущность
 *
 * @author dude.
 *
 * @param <E> - Тип сущности
 */
public class EntityMapper<E> implements Mappable {

    /**
     * Преобразователь типов
     */
    DBTypeConvert convert = new DBTypeConvert();

    /**
     * Селектор
     */
    SelectorEntity selector;

    /**
     * Класс сущности
     */
    Class<E> entityClass;

    /**
     * Выборка для списка результатов
     */
    List<E> result;

    /**
     * Для одного результата
     */
    E resultInstance;

    /**
     * Маппер для селектора
     *
     * @param selector
     */
    public EntityMapper(SelectorEntity<E> selector) {
        this.selector = selector;
        this.entityClass = (Class<E>) selector.getJoin().getClassModel().getEntityClass();
    }

    @Override
    public void init() {
        result = new ArrayList<>();
    }

    @Override
    public void mapLine(ResultSet rs) throws SQLException {

        try {

            E inst = resultInstance != null ? resultInstance : createNewEntity();

            for (SelectableField sf : selector.getSelectableFields()) {

                if (sf instanceof SelectableJoinedField) {

                    SelectableJoinedField sjf = (SelectableJoinedField) sf;

                    Field entityField = sjf.getJoinedField().getFieldModel().getField();
                    Object dbValue = rs.getObject(sjf.getSelectAlias());

                    Object entityValue = convert.ToEntityValue(entityField, dbValue);
                    entityField.set(inst, entityValue);
                } else {
                    //пока никак не обрабатываю
                }

            }
            result.add(inst);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void complete() {

    }

    /**
     * Создание нового экземляра сущнсоти
     *
     * @return
     * @throws Exception
     */
    private E createNewEntity() throws Exception {
        return entityClass.newInstance();
    }

    /**
     * Результат
     *
     * @return
     */
    public List<E> getResult() {
        return result;
    }

    /**
     * Единичный результат
     *
     * @return
     */
    public E getOneResult() {
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    /**
     * Результат для вкладываемой сущности
     *
     * @return
     */
    public E getResultInstance() {
        return resultInstance;
    }

    /**
     * Установить сущность для вставки значений. В этом режиме новые объект не
     * создаётся
     *
     * @param resultInstance
     */
    public void setResultInstance(E resultInstance) {
        this.resultInstance = resultInstance;
    }
}
