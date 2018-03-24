package ru.dude.orm.mappers;

import ru.dude.orm.selectors.SelectableJoinedField;
import ru.dude.orm.OrmOperationException;
import ru.dude.orm.model.DBTypeConvert;
import ru.dude.orm.model.JoinedField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ru.dude.orm.selectors.SelectorFunction;
import ru.dude.orm.selectors.SelectorSimple;

/**
 * Мэппер кортежей. Для выборок нескольких полей из большого запроса.
 *
 * @author dude.
 */
public class TupleMapper implements Mappable {

    /**
     * Тип Кортеж
     */
    public class Tuple {

        /**
         * Выборка из БД : псеводин - значение
         */
        Map<String, Object> line;

        public Tuple(Map<String, Object> line) {
            this.line = line;
        }

        /**
         * Значение из БД
         *
         * @param joinedField модель поля запроса
         * @param <T> тип возвращаемого значения
         * @return
         */
        public <T> T get(JoinedField<T> joinedField) {
            String key = joinedField.buildSql();
            return line.containsKey(key) ? (T) line.get(key) : null;
        }

        /**
         * Значение из БД
         *
         * @param joinedField поле из селектора
         * @param <T> тип возвращаемого значения
         * @return
         */
        public <T> T get(SelectableJoinedField<T> joinedField) {
            String key = joinedField.buildSql();
            return line.containsKey(key) ? (T) line.get(key) : null;
        }

        /**
         * Значение из БД
         *
         * @param sf селектор функции
         * @param <T> тип возвращаемого значения
         * @return
         */
        public <T> T get(SelectorFunction<T> sf) throws OrmOperationException {
            String key = sf.getAlias();
            if (line.containsKey(key)) {
                Object dbValue = line.get(key);
                return (T) new DBTypeConvert().ToEntityValue(sf.getJavaType(), dbValue);
            }
            return null;
        }

        /**
         * Проверка на существование
         *
         * @param joinedField
         * @return
         */
        public boolean isExist(JoinedField joinedField) {
            String key = joinedField.buildSql();
            return line.containsKey(key);
        }
    }

    /**
     * Мэппер для внутренней работы
     */
    HashMapMapper hashMapMapper;

    /**
     * Результат
     */
    List<Tuple> result;

    /**
     * Мэппер кортежей
     *
     * @param selector селектор
     */
    public TupleMapper(SelectorSimple selector) {
        hashMapMapper = new HashMapMapper(selector, HashMapMapper.Type.FIELD_JONED_IDENT);
    }

    @Override
    public void init() {
        result = new ArrayList<>();
        hashMapMapper.init();
    }

    @Override
    public void mapLine(ResultSet rs) throws SQLException {
        hashMapMapper.mapLine(rs);
    }

    @Override
    public void complete() {
        hashMapMapper.complete();

        for (Map<String, Object> hashRes : hashMapMapper.getResult()) {
            result.add(new Tuple(hashRes));
        }
    }

    /**
     * Результат
     *
     * @return
     */
    public List<Tuple> getResult() {
        return result;
    }

}
