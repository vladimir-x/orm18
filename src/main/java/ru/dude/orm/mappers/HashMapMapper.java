package ru.dude.orm.mappers;

import ru.dude.orm.selectors.SelectableField;
import ru.dude.orm.selectors.SelectableJoinedField;
import ru.dude.orm.selectors.SelectorSimple;
import ru.dude.orm.model.DBTypeConvert;
import ru.dude.orm.model.FieldModel;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Маппер в хэш-мэп Ключами могут являться: имена колонок, имена полей в
 * сущности, псевдоним поля в запросе
 *
 * @author dude.
 */
public class HashMapMapper implements Mappable {

    /**
     * Типы именования ключей
     */
    public enum Type {
        COLUMN_NAME, //по имени колонки
        ALIAS, //по псевдониму
        FIELD_NAME, //по имени поля сущности
        FIELD_JONED_IDENT, //по идентификации поля
    }

    /**
     * Преобразователь типов
     */
    DBTypeConvert convert = new DBTypeConvert();

    /**
     * Селектор
     */
    SelectorSimple selector;

    /**
     * Тип именования ключей
     */
    Type labeledType;

    /**
     * Выборка для списка результатов
     */
    List<Map<String, Object>> result;

    /**
     * Маппер с ключами по имени колонки в БД
     *
     * @param selector
     */
    public HashMapMapper(SelectorSimple selector) {
        this.selector = selector;
        this.labeledType = Type.COLUMN_NAME;
    }

    /**
     * Маппер с указанием типа ключей
     *
     * @param selector
     * @param labeledType
     */
    public HashMapMapper(SelectorSimple selector, Type labeledType) {
        this.selector = selector;
        this.labeledType = labeledType;
    }

    @Override
    public void init() {
        result = new ArrayList<>();
    }

    @Override
    public void mapLine(ResultSet rs) throws SQLException {

        try {

            Map<String, Object> line = new HashMap<>();

            for (SelectableField sf : selector.getSelectableFields()) {

                if (sf instanceof SelectableJoinedField) {

                    SelectableJoinedField sjf = (SelectableJoinedField) sf;

                    FieldModel fieldModel = sjf.getJoinedField().getFieldModel();

                    Field entityField = fieldModel.getField();
                    Object dbValue = rs.getObject(sjf.getSelectAlias());

                    Object entityValue = convert.ToEntityValue(entityField, dbValue);

                    String keyStr = null;
                    switch (labeledType) {
                        case FIELD_JONED_IDENT:
                        default:
                            keyStr = sjf.getJoinedField().buildSql();
                            break;
                        case ALIAS:
                            keyStr = sjf.getAlias();
                            break;
                        case FIELD_NAME:
                            keyStr = fieldModel.getName();
                            break;
                        case COLUMN_NAME:
                            keyStr = fieldModel.getColumn();
                            break;
                    }

                    line.put(keyStr, entityValue);
                } else {
                    Object dbValue = rs.getObject(sf.getAlias());
                    line.put(sf.getAlias(), dbValue);
                }

            }
            result.add(line);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void complete() {

    }

    /**
     * Результат
     *
     * @return
     */
    public List<Map<String, Object>> getResult() {
        return result;
    }
}
