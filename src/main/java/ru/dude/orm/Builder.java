package ru.dude.orm;

import ru.dude.orm.group.GroupBy;
import ru.dude.orm.selectors.SelectBuilder;
import ru.dude.orm.selectors.Selectable;
import ru.dude.orm.clauses.Clause;
import ru.dude.orm.join.AliasGenerator;
import ru.dude.orm.join.From;
import ru.dude.orm.mappers.Mappable;
import ru.dude.orm.order.LimitOffset;
import ru.dude.orm.order.Order;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Построитель запросов
 *
 * @author dude.
 */
public class Builder {

    /**
     * Генератор псевдонимов для селекторов
     */
    AliasGenerator aliasGenerator = new AliasGenerator();

    /**
     * Постоитель селекторов
     */
    SelectBuilder selectBuilder = SelectBuilder.createForBuilder(this);

    /**
     * Селектор запроса
     */
    Selectable selector;
    /**
     * Объекты таблиц
     */
    From rootFrom;
    /**
     * Объект с предикатами
     */
    Clause whereClause;
    /**
     * Группировка
     */
    GroupBy groupBy;
    /**
     * Предикаты к группировке
     */
    Clause havingClause;
    /**
     * Объект сортировки
     */
    Order order;
    /**
     * Объект ограничения выборки
     */
    LimitOffset limitOffset;

    /**
     * Маппер для обработки выбранных данных
     */
    Mappable mapper;

    public Builder() {

    }

    public Selectable select(Selectable selector) {
        this.selector = selector;
        return selector;
    }

    public void from(From from) {
        this.rootFrom = from;

    }

    public Clause where(Clause clause) {
        this.whereClause = clause;
        return whereClause;
    }

    public GroupBy groupBy(GroupBy groupBy) {
        this.groupBy = groupBy;
        return groupBy;
    }

    public Clause having(Clause clause) {
        this.havingClause = clause;
        return havingClause;
    }

    public Order orderBy(Order order) {
        this.order = order;
        return order;
    }

    public void limitOffset(LimitOffset limitOffset) {
        this.limitOffset = limitOffset;
    }

    public void mapper(Mappable mapper) {
        this.mapper = mapper;
    }

    //********************************************************************************
    /**
     * Построить запрос
     *
     * @throws OrmOperationException
     */
    public void build() throws OrmOperationException {
        String sql = buildSelectSql();
        execute(sql);
    }

    /**
     * Выполнение нативного SQL, без объектной обработки
     *
     * @param sql текст запроса
     * @return
     */
    public ResultSet nativeSql(String sql) throws OrmOperationException {
        try {
            return EntityManager.getConnection().executeQuery(sql);
        } catch (Exception ex) {
            throw new OrmOperationException(ex);
        }
    }
    
    public void closeResultSet(ResultSet rs) throws OrmOperationException{
        EntityManager.getConnection().closeResultSet(rs);
    }

    /**
     * Построение запроса
     *
     * @return
     */
    private String buildSelectSql() {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ").append(selector != null ? selector.buildSql() : "*").append("\n");

        sb.append(rootFrom.buildSql());

        if (whereClause != null) {
            String whereSql = whereClause.buildSql();
            if (StringUtils.isNotBlank(whereSql)) {
                sb.append("WHERE ").append(whereSql).append("\n");
            }
        }

        if (groupBy != null) {
            String groupBySql = groupBy.buildSql();
            if (StringUtils.isNotBlank(groupBySql)) {
                sb.append("GROUP BY ").append(groupBySql);
            }

            if (havingClause != null) {
                String havingSql = havingClause.buildSql();
                if (StringUtils.isNotBlank(havingSql)) {
                    sb.append("HAVING ").append(havingSql).append("\n");
                }
            }
        }

        if (order != null) {
            String orderSql = order.buildSql();
            if (StringUtils.isNotBlank(orderSql)) {
                sb.append("ORDER BY ").append(orderSql);
            }
        }

        if (limitOffset != null) {
            sb.append(limitOffset.buildSql());
        }

        return sb.toString();
    }

    /**
     * Выполнение запроса
     *
     * @param sql
     */
    private void execute(String sql) throws OrmOperationException {
        try {
            try (ResultSet rs = EntityManager.getConnection().executeQuery(sql)) {
                mapper.init();
                while (rs.next()) {
                    mapper.mapLine(rs);
                }
                mapper.complete();
                EntityManager.getConnection().closeResultSet(rs);
            }
        } catch (Exception ex) {
            throw new OrmOperationException(ex);
        }
    }

    /**
     * Получить построитель селекторов для данного билдера
     *
     * @return
     */
    public SelectBuilder getSelectBuilder() {
        return selectBuilder;
    }

    /**
     * Получить объект корневой таблицы
     *
     * @return
     */
    public From getFrom() {
        return rootFrom;
    }

    /**
     * Получить генератор псевдонимов
     *
     * @return
     */
    public AliasGenerator GetAliasGenerator() {
        return aliasGenerator;
    }
}
