package ru.dude.orm;

import com.google.common.reflect.ClassPath;
import ru.dude.orm.model.ClassModel;
import ru.dude.orm.model.FieldModel;
import ru.dude.orm.model.SingularAttribute;
import ru.dude.orm.model.SingularAttributeImpl;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ru.dude.orm.model.annotations.Column;
import ru.dude.orm.model.annotations.Entity;
import ru.dude.orm.model.annotations.Id;
import ru.dude.orm.model.annotations.Inheritance;
import ru.dude.orm.model.annotations.InheritanceType;
import ru.dude.orm.model.annotations.JoinColumn;
import ru.dude.orm.model.annotations.StaticMetamodel;
import ru.dude.orm.model.annotations.Table;

/**
 * Класс загрузки статичных моделей и составления моделей сущностей при запуске
 * сервера
 *
 * @author dude.
 */
public class OrmModelStorage {

    /**
     * Объект синглтона
     */
    private static final OrmModelStorage singletone = new OrmModelStorage();

    /**
     * Набор загруженных jar для предотвращения постоянной пеергрузки модели
     */
    private Set<String> loadedUrls = new HashSet<>();
    /**
     * Набор загруженных моделей для предотвращения постоянной пеергрузки модели
     */
    @Deprecated
    private Set<String> loadedMetaModels = new HashSet<>();
    /**
     * Карта имя_класса_сущности - модель сущности
     */
    private Map<Class, ClassModel> entityModels = new HashMap<>();
    /**
     * Карта Поле_статичноа_модель - модель_поля_сущсноти
     */
    private Map<SingularAttribute, FieldModel> fieldModels = new HashMap<>();

    private OrmModelStorage() {

    }

    /**
     * Синглтон
     *
     * @return
     */
    public static OrmModelStorage getInstance() {
        return singletone;
    }

    /**
     * Загрузка всех метамоделей
     *
     * @param loader
     */
    synchronized public void loadModels(ClassLoader loader) {
        try {

            ClassPath classPath = ClassPath.from(loader);

            Log.debug("Loading orm static model");
            for (ClassPath.ClassInfo classInfo : classPath.getAllClasses()) {
                String pn = classInfo.getPackageName();
                if (classInfo.getName().endsWith("_")) {
                    Class<?> loadClass = classInfo.load();
                    if (loadClass.isAnnotationPresent(StaticMetamodel.class)) {
                        loadStaticModel(loadClass);
                    }
                }
            }

        } catch (Exception ex) {
            Log.debug(ex);
        }

    }

    /**
     * Загрузка статической модели и линковка
     *
     * @param loadClass
     * @throws Exception
     */
    private void loadStaticModel(Class<?> loadClass) throws OrmOperationException {

        try {

            Class<?> entityClass = loadClass.getAnnotation(StaticMetamodel.class).value();

            ClassModel model = loadModel(entityClass);

            for (Field metaField : loadClass.getDeclaredFields()) {
                // новый объект - идентификатор поля
                SingularAttributeImpl sa = new SingularAttributeImpl();
                // устанавливается в статический класс модели
                metaField.set(metaField, sa);

                // запоминается для модели поля
                FieldModel fm = model.getFieldByName(metaField.getName());
                if (fm != null) {
                    fieldModels.put(sa, fm);
                }

                //ссылки на модель добавляются в сингуляр атрибут, для быстрого извлечения
                sa.setFieldModel(fm);
            }

        } catch (Exception ex) {
            throw new OrmOperationException(ex);
        }
    }

    /**
     * Загрузка модели класса
     *
     * @param entityClass
     */
    private ClassModel loadModel(Class<?> entityClass) throws OrmOperationException {
        if (isLoad(entityClass)) {
            return getClassModelInner(entityClass);
        }

        ClassModel model = new ClassModel();

        if (entityClass.isAnnotationPresent(Table.class)) {
            model.setTableName(entityClass.getAnnotation(Table.class).name());
        }

        if (entityClass.isAnnotationPresent(Inheritance.class)) {
            model.setInheritanceType(entityClass.getAnnotation(Inheritance.class).strategy());
        } else {
            model.setInheritanceType(InheritanceType.SINGLE_TABLE);
        }

        model.setEntityClass(entityClass);

        for (Field field : entityClass.getDeclaredFields()) {
            FieldModel fieldModel = loadField(field, entityClass);
            if (fieldModel != null) {

                model.setIdFieldModel(fieldModel.isId() ? fieldModel : model.getIdFieldModel());

                model.addFieldModel(fieldModel);
            }
        }

        model.setParentClassModel(loadParentModel(entityClass));

        model.setParentJoinedClassModel(fillJoinInheritanceInfo(model));

        putClassModel(entityClass, model);
        return model;

    }

    /**
     * Загрузка предков
     *
     * @param entityClass
     * @return
     */
    private ClassModel loadParentModel(Class<?> entityClass) throws OrmOperationException {
        Class<?> parentClass = entityClass.getSuperclass();
        if (parentClass.isAnnotationPresent(Entity.class)) {
            return loadModel(parentClass);

        } else if (!parentClass.equals(Object.class)) {
            //ищем предков дальше
            return loadParentModel(parentClass);
        }
        return null;
    }

    /**
     * Загрузка модели одного поля
     *
     * @param field
     * @param entityClass
     * @return
     */
    private FieldModel loadField(Field field, Class<?> entityClass) {
        Column colunmAnnotate = field.getAnnotation(Column.class);

        if (colunmAnnotate == null) {
            // поля без аннотаций исключаем
            return null;
        }

        field.setAccessible(true);

        FieldModel fieldModel = new FieldModel(field.getName());

        fieldModel.setEntityClassName(entityClass.getName());
        fieldModel.setField(field);

        fieldModel.setIsId(field.isAnnotationPresent(Id.class));

        if (colunmAnnotate != null) {

            fieldModel.setColumn(colunmAnnotate.name());
            if (field.isAnnotationPresent(JoinColumn.class)) {
                Class<?> joinedClass = field.getDeclaringClass();
                fieldModel.setJoinedColumn(joinedClass);
            }
        }

        return fieldModel;
    }

    /**
     * Заполнение полей из предков, из приджойненого предка
     *
     * @param model
     * @return
     */
    private ClassModel fillJoinInheritanceInfo(ClassModel model) {

        Set<FieldModel> all = model.getFieldAllInheritanceModels();
        all.addAll(model.getFieldModels());

        ClassModel parentClassModel = model.getParentClassModel();
        if (parentClassModel != null) {
            if (parentClassModel.getInheritanceType() == InheritanceType.SINGLE_TABLE) {
                ClassModel res = fillJoinInheritanceInfo(parentClassModel);
                all.addAll(parentClassModel.getFieldAllInheritanceModels());

                if (model.getTableName() == null) {
                    model.setTableName(parentClassModel.getTableName());
                }
                if (model.getIdFieldModel() == null) {
                    model.setIdFieldModel(parentClassModel.getIdFieldModel());
                }

                return res;
            } else if (parentClassModel.getInheritanceType() == InheritanceType.JOINED) {
                return parentClassModel;
            }
        }
        return null;
    }

    /**
     * Проверка, загружена ли сущность
     *
     * @param entityClass
     * @return
     */
    public boolean isLoad(Class<?> entityClass) {
        return entityModels.containsKey(entityClass);
    }

    /**
     * Получить модель класса
     *
     * @param entityClass
     * @return
     */
    private ClassModel getClassModelInner(Class<?> entityClass) {
        if (entityModels.containsKey(entityClass)) {
            return entityModels.get(entityClass);
        } else {
            return null;
        }
    }

    public ClassModel getClassModel(Class<?> entityClass) throws OrmOperationException {
        return loadModel(entityClass);
    }

    /**
     * Получить модель поля
     *
     * @param sa
     * @return
     */
    public FieldModel getFieldModel(SingularAttribute sa) {
        if (fieldModels.containsKey(sa)) {
            return fieldModels.get(sa);
        } else {
            return null;
        }
    }

    /**
     * положить загруженнвую модель в карту моделей
     *
     * @param entityClass
     * @param classModel
     */
    public void putClassModel(Class<?> entityClass, ClassModel classModel) {
        entityModels.put(entityClass, classModel);
    }

    /**
     * Проверяет загружены ли уже классы из этого загрузчика
     *
     * @param loader
     * @return - true.
     */
    private boolean isLoad(URLClassLoader loader) {
        boolean res = true;
        for (URL url : loader.getURLs()) {
            if (!loadedUrls.contains(url.getFile())) {
                res = false;
            }
        }
        return res;
    }

    /**
     * Добавить пути из загрузчика в список обработанных
     */
    private void addLoadedUrls(URLClassLoader loader) {
        for (URL url : loader.getURLs()) {
            loadedUrls.add(url.getFile());
        }
    }

}
