package ru.dude.orm.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Маппер для нескольких сущностей
 *
 * @author dude.
 */
public class EntityMultiMapper implements Mappable {

    /**
     * Мапперы для каждоый сущнсоти
     */
    List<EntityMapper> entityMapperList = new ArrayList<>();

    /**
     * Результат
     */
    Object[] result;

    /**
     * Маппер для нескольких сущностей
     *
     * @param entityMappers мапперы для сущностей
     */
    public EntityMultiMapper(EntityMapper... entityMappers) {
        if (entityMappers != null) {
            for (EntityMapper em : entityMappers) {
                if (em != null) {
                    entityMapperList.add(em);
                }
            }
        }
    }

    @Override
    public void init() {

        result = new Object[entityMapperList.size()];
        for (EntityMapper entityMapper : entityMapperList) {
            entityMapper.init();
        }

    }

    @Override
    public void mapLine(ResultSet rs) throws SQLException {
        for (EntityMapper entityMapper : entityMapperList) {
            entityMapper.mapLine(rs);
        }
    }

    @Override
    public void complete() {

        for (int i = 0; i < entityMapperList.size(); ++i) {
            EntityMapper entityMapper = entityMapperList.get(i);
            entityMapper.complete();
            result[i] = entityMapper.getResult();
        }
    }

    /**
     * Результат
     *
     * @return
     */
    public Object[] getResult() {
        return result;
    }
}
