package ru.dude.orm.model.util;

import ru.dude.orm.OrmModelStorage;
import ru.dude.orm.OrmOperationException;
import ru.dude.orm.model.ClassModel;
import ru.dude.orm.model.SingularAttribute;

/**
 * Утилиты для работы с моделями сущностей
 *
 * @author dude.
 */
public class EntityUtils {

    /**
     * Получить sql название таблицы
     *
     * @param entityClass
     * @return
     */
    public static String getTableName(Class<?> entityClass) throws OrmOperationException {
        ClassModel model = OrmModelStorage.getInstance().getClassModel(entityClass);
        return model != null ? model.getTableName() : null;
    }

    /**
     * Получить sql название колонки
     *
     * @param attr
     * @return
     */
    public static String getFieldColumn(SingularAttribute attr) {
        return attr.getFieldModel().getColumnNameQuoted();
    }
}
