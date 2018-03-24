package ru.dude.orm.model;

import ru.dude.orm.OrmOperationException;

import javax.activation.UnsupportedDataTypeException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * Преобразователь типов
 *
 * @author dude.
 */
public class DBTypeConvert {

    private static final SimpleDateFormat PSQL_DATE_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     *
     * @param field Поле в БД
     * @param dbValue значение из БД
     * @return
     * @throws OrmOperationException
     */
    public Object ToEntityValue(Field field, Object dbValue) throws OrmOperationException {
        try {
            return ToEntityValue(field.getType(), dbValue);
        } catch (OrmOperationException ex) {
            throw new OrmOperationException("Field = '" + field.getName() + "' : " + ex.getMessage());
        }
    }

    /**
     * Возвращает данные для вставки в Сущность
     *
     * @param fieldType тип поля в сущности
     * @param dbValue значение из БД
     * @return
     * @throws OrmOperationException
     */
    public Object ToEntityValue(Class<?> fieldType, Object dbValue) throws OrmOperationException {

        if (dbValue == null) {
            return null;
        } else if (fieldType.equals(String.class)) {

            if (dbValue instanceof String) {
                return (String) dbValue;
            } else {
                return dbValue + "";
            }
        } else if (fieldType.equals(Integer.class) || fieldType == int.class) {
            if (dbValue instanceof Long) {
                return ((Long) dbValue).intValue();
            }
            return (Integer) dbValue;
        } else if (fieldType.equals(Long.class) || fieldType == long.class) {
            return (Long) dbValue;
        } else if (fieldType.equals(Float.class)) {
            return (Float) dbValue;
        } else if (fieldType.equals(Double.class)) {
            return (Double) dbValue;
        } else if (fieldType.equals(Date.class)) {
            return (Date) dbValue;
        } else if (fieldType.equals(Boolean.class)) {
            return (Boolean) dbValue;
        } else if (fieldType.equals(UUID.class)) {
            return (UUID) dbValue;
        } else if (fieldType.isEnum()) {
            return parseEnum((Class<Enum>) fieldType, (String) dbValue);
        } else if (fieldType.isAssignableFrom(IPrimitive.class)) {
            IPrimitive primImpl = primitiveCreate(fieldType);
            primImpl.makeByDB(dbValue);
            return primImpl;
        } else {
            throw new OrmOperationException("'type'" + fieldType + "' can't be parse! Implement IPrimitive !");
        }
    }

    private Enum parseEnum(Class<Enum> enumClass, String enumName) {

        for (Enum anEnum : enumClass.getEnumConstants()) {
            if (anEnum.name().equals(enumName)) {
                return anEnum;
            }
        }
        return null;
    }

    private IPrimitive primitiveCreate(Class<?> fieldType) throws OrmOperationException {
        try {
            return (IPrimitive) fieldType.getConstructor(new Class[]{}).newInstance(new Object[]{});
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new OrmOperationException(e);
        }
    }

    public String ToSqlValue(Object[] objects) throws OrmOperationException {
        return ToSqlValue(Arrays.asList(objects));
    }

    public String ToSqlValue(Collection collection) throws OrmOperationException {
        StringBuilder sb = new StringBuilder();
        for (Object o : collection) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(ToSqlValue(o));
        }
        return "(" + sb.toString() + ")";

    }

    /**
     * Возвращает данные, для вставки в SQL
     *
     * @param pRawVal
     * @return
     * @throws UnsupportedDataTypeException
     */
    public String ToSqlValue(Object pRawVal) throws OrmOperationException {

        if (pRawVal == null) {
            return "null";
        } else if (pRawVal instanceof String) {
            return normalize((String) pRawVal);
        } else if (pRawVal instanceof Integer) {
            return normalize((Integer) pRawVal);
        } else if (pRawVal.getClass() == int.class) {
            return normalize((Integer) pRawVal);
        } else if (pRawVal instanceof Long) {
            return normalize((Long) pRawVal);
        } else if (pRawVal.getClass() == long.class) {
            return normalize((Long) pRawVal);
        } else if (pRawVal instanceof Float) {
            return normalize((Float) pRawVal);
        } else if (pRawVal instanceof Double) {
            return normalize((Double) pRawVal);
        } else if (pRawVal instanceof Date) {
            return normalize((Date) pRawVal);
        } else if (pRawVal instanceof Boolean) {
            return normalize((Boolean) pRawVal);
        } else if (pRawVal instanceof UUID) {
            return normalize((UUID) pRawVal);
        } else if (pRawVal instanceof Enum) {
            return normalize(((Enum) pRawVal).name());
        } else if (pRawVal instanceof IPrimitive) {
            return ((IPrimitive) pRawVal).toSqlValue();
        } else if (pRawVal instanceof Collection) {
            return ToSqlValue((Collection) pRawVal);
        } else if (pRawVal.getClass().isArray()) {
            return ToSqlValue((Object[]) pRawVal);

        } else {
            throw new OrmOperationException("Type " + pRawVal.getClass() + " can't be normalize! Implement IPrimitive !");
        }
    }
    
    
    public String normalize(String javaValue){
        return javaValue !=null ? "'"+javaValue+"'" :"null";
    }
    
    public String normalize(Integer javaValue){
        return javaValue !=null ? javaValue.toString() :"null";
    }
    
    public String normalize(Long javaValue){
        return javaValue !=null ? javaValue.toString() :"null";
    }
    
    public String normalize(Float javaValue){
        return javaValue !=null ? javaValue.toString() :"null";
    }
    
    public String normalize(Double javaValue){
        return javaValue !=null ? javaValue.toString() :"null";
    }
    
    public String normalize(Date javaValue){
        return javaValue !=null ? "'"+PSQL_DATE_SDF.format(javaValue)+"'":"null";
    }
    
    public String normalize(Boolean javaValue){
        return javaValue !=null ? javaValue.toString() :"null";
    }
    
     public String normalize(UUID javaValue){
        return javaValue !=null ? "'"+javaValue+"'":"null";
    }
  
  
    
}
