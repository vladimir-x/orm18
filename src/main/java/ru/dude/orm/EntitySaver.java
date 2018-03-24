package ru.dude.orm;

import ru.dude.orm.model.ClassModel;
import ru.dude.orm.model.DBTypeConvert;
import ru.dude.orm.model.FieldModel;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Операции над сущностями CREATE UPDATE DELETE READ - вынесено в общие запросы
 *
 * @author dude.
 */
public class EntitySaver {
    
    DBConnection connection;

    public EntitySaver(DBConnection connection) {
        this.connection = connection;
    }
        
    /**
     * Иерархическое удаление с учётом наследования
     *
     * @param entity
     * @param model
     * @throws Exception
     */
    protected void deleteInner(Object entity, ClassModel model) throws OrmOperationException {
        ClassModel parentJoinded = model.getParentJoinedClassModel();

        if (parentJoinded != null) {
            //удаление базового объекта
            deleteInner(entity, parentJoinded);
        }

        //удаление текущего объекта
        MakeDeleteSql(entity, model);
    }

    /**
     * Иерархическое сохранение, с учётом наследования. Автоматически определяет
     * INSERT или UPDATE
     *
     * @param entity
     * @param model
     * @return
     * @throws Exception
     */
    protected Integer saveInner(Object entity, ClassModel model) throws OrmOperationException {
        try {
            ClassModel parentJoinded = model.getParentJoinedClassModel();

            Field idField = model.getIdFieldModel().getField();
            Integer currentId = (Integer) idField.get(entity);

            //сохранение базового объекта
            if (parentJoinded != null) {
                // base PPOBJECT
                Integer parentId = saveInner(entity, parentJoinded);
                idField.set(entity, parentId);
            }

            //сохранение текущего объекта
            if (currentId == null || currentId.equals(0)) {
                Insert(entity, model);
                currentId = (Integer) idField.get(entity);
            } else {
                Update(entity, model);
            }

            return currentId;
        } catch (Exception ex) {
            throw new OrmOperationException(ex);
        }
    }

    /**
     * Вставка одной сущности, с обновлением поля id
     *
     * @param entity
     * @param model
     * @throws Exception
     */
    private void Insert(Object entity, ClassModel model) throws Exception {
        String idColumn = model.getIdFieldModel().getColumnNameQuoted();
        String sql = MakeInsertSql(entity, model);
        Integer resid = 0;
        try (ResultSet rs = connection.executeQuery(sql)) {
            if (rs.next()) {
                resid = rs.getInt(idColumn);
            }
            rs.close();
        }
        model.getIdFieldModel().getField().set(entity, resid);
    }

    /**
     * Обновление значений в БД
     *
     * @param entity
     * @param model
     * @throws Exception
     */
    private void Update(Object entity, ClassModel model) throws Exception {
        String sql = MakeUpdateSql(entity, model);
        connection.execute(sql);
    }

    /**
     * Удаление из БД
     *
     * @param entity
     * @param model
     * @throws Exception
     */
    private void Delete(Object entity, ClassModel model) throws Exception {
        String sql = MakeDeleteSql(entity, model);
        connection.execute(sql);
    }

    /**
     * SQL запрос на вставку одной сущности в одну таблицу
     *
     * @param entity
     * @param model
     * @return
     * @throws Exception
     */
    private String MakeInsertSql(Object entity, ClassModel model) throws OrmOperationException {
        try {
            DBTypeConvert convert = new DBTypeConvert();

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ").append(model.getTableName()).append("(");
            boolean firstFlag = true;
            
            FieldModel idFieldModel = model.getIdFieldModel();
            Integer currIdValue = (Integer) idFieldModel.getField().get(entity);
            boolean addId = (currIdValue!=null && !currIdValue.equals(0));

            for (FieldModel fieldModel : model.getFieldAllInheritanceModels()) {
                if (fieldModel.isColumn()) {
                    if(!fieldModel.isId() || addId){
                        sb.append(firstFlag ? "" : ",");
                        firstFlag = false;
                        sb.append(fieldModel.getColumnNameQuoted());
                    }
                }
            }

            sb.append(")").append("\n VALUES (");
            firstFlag = true;
            for (FieldModel fieldModel : model.getFieldAllInheritanceModels()) {
                if (fieldModel.isColumn()) {
                    if(!fieldModel.isId() ||addId ){
                        sb.append(firstFlag ? "" : ",");
                        firstFlag = false;
                        String sqlValue = convert.ToSqlValue(fieldModel.getField().get(entity));
                        sb.append(sqlValue);
                    }
                }
            }

            String idColumn = model.getIdFieldModel().getColumnNameQuoted();
            sb.append(") RETURNING ").append(idColumn);

            return sb.toString();
        } catch (Exception ex) {
            throw new OrmOperationException(ex);
        }
    }

    /**
     * SQL запрос на обновление данных в таблице
     *
     * @param entity
     * @param model
     * @return
     * @throws Exception
     */
    private String MakeUpdateSql(Object entity, ClassModel model) throws OrmOperationException {
        try {
            DBTypeConvert convert = new DBTypeConvert();

            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE ").append(model.getTableName()).append(" SET ");
            boolean firstFlag = true;

            for (FieldModel fieldModel : model.getFieldAllInheritanceModels()) {
                if (!fieldModel.isId() && fieldModel.isColumn()) {
                    sb.append(firstFlag ? "" : ",");
                    firstFlag = false;

                    String sqlValue = convert.ToSqlValue(fieldModel.getField().get(entity));

                    sb.append(fieldModel.getColumnNameQuoted()).append("=").append(sqlValue);
                }
            }

            Integer currentId = (Integer) model.getIdFieldModel().getField().get(entity);
            String idColumn = model.getIdFieldModel().getColumnNameQuoted();
            sb.append("\n").append(" WHERE ").append(idColumn).append("=").append(currentId);

            return sb.toString();
        } catch (Exception ex) {
            throw new OrmOperationException(ex);
        }
    }

    /**
     * SQL запрос для удаления данных из таблицы по ID
     *
     * @param entity
     * @param model
     * @return
     * @throws Exception
     */
    private String MakeDeleteSql(Object entity, ClassModel model) throws OrmOperationException {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM ").append(model.getTableName()).append(" WHERE ");
            FieldModel idFieldModel = model.getIdFieldModel();
            Integer idValue = (Integer) idFieldModel.getField().get(entity);
            sb.append(idFieldModel.getColumnNameQuoted()).append(" = ").append(idValue);
            return sb.toString();
        } catch (Exception ex) {
            throw new OrmOperationException(ex);
        }
    }


}
