package ru.dude.orm.join;

import ru.dude.orm.OrmModelStorage;
import ru.dude.orm.OrmOperationException;
import ru.dude.orm.model.JoinedField;
import ru.dude.orm.clauses.Clause;
import ru.dude.orm.clauses.ClauseBuilder;
import ru.dude.orm.model.ClassModel;
import ru.dude.orm.model.FieldModel;
import ru.dude.orm.model.SingularAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Объект запроса: Таблицы
 *
 * @author dude.
 *
 * @param <E> Тип Сущности для данной таблицы/модели
 */
public class Join<E> {

    /**
     * Генеротор псевдонимов
     */
    protected AliasGenerator aliasGenerator;

    /**
     * Модель класса сущности
     */
    ClassModel classModel;

    /**
     * Псевдоним для данного Join в запросе
     */
    String alias;

    /**
     * Тип Join
     */
    JoinType joinType;

    /**
     * Условие присоединения
     */
    Clause on;

    /**
     * Присоединение базовой таблицы для данной модели сущьности Сюда попадают
     * наследованные сущности по стратегии InheritanceType.JOINED
     */
    Join baseJoin;

    /**
     * Создание модели таблицы для запроса
     *
     * @param entityClass класс сущности
     * @param aliasGenerator генератор псевдонимов
     */
    public Join(Class<E> entityClass, AliasGenerator aliasGenerator) throws OrmOperationException {
        this.aliasGenerator = aliasGenerator;
        this.classModel = OrmModelStorage.getInstance().getClassModel(entityClass);
        this.alias = aliasGenerator.generateJoinAlias(classModel.getTableName());

        ClassModel baseJoinClassModel = classModel.getParentJoinedClassModel();
        if (baseJoinClassModel != null) {
            this.baseJoin = new Join(baseJoinClassModel.getEntityClass(), aliasGenerator);
            this.baseJoin.joinType = JoinType.INNER_JOIN;
            this.baseJoin.setOn(ClauseBuilder.byID(this, baseJoin));
        }

    }

    /**
     * Модель поля для запроса, по предкомпилированной статической модели
     * сущности
     *
     * @param attribute статичное поле из статичной модели вида
     * EntityClass_.xxxxxxxx
     * @param <X> тип сущности
     * @param <T> тип объекта поля
     * @return
     */
    public <X, T> JoinedField<T> get(SingularAttribute<X, T> attribute) {
        return new JoinedField(this, attribute.getFieldModel());
    }

    /**
     * Модель поля ID для данной модели сущности для запроса
     *
     * @return
     */
    public JoinedField getIDFiled() {
        return new JoinedField(this, classModel.getIdFieldModel());
    }

    public String buildSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(joinType.getSqlName() + " " + classModel.getTableName() + " as " + alias);
        if (on != null) {
            sb.append(" on " + on.buildSql());
        }
        sb.append("\n");
        if (baseJoin != null) {
            sb.append("\t").append(baseJoin.buildSql());
        }
        return sb.toString();
    }

    /**
     * Все поля из текущего класса и из предков
     *
     * @return
     */
    public List<JoinedField> getAllJoinedFields(boolean isColumn, boolean isMF) {
        Set<FieldModel> allFieldModels = classModel.getFieldAllInheritanceModels();
        List<JoinedField> res = new ArrayList<>();
        for (FieldModel fieldModel : allFieldModels) {
            if ((isColumn && fieldModel.isColumn()) || (isMF && fieldModel.isColumn())) {
                res.add(new JoinedField(this, fieldModel));
            }
        }

        if (baseJoin != null) {
            res.addAll(baseJoin.getAllJoinedFields(isColumn, isMF));
        }
        return res;
    }

    //*****************
    public String getAlias() {
        return alias;
    }

    /**
     * Установить условие присоединения
     *
     * @param on
     */
    public void setOn(Clause on) {
        this.on = on;
    }

    public ClassModel getClassModel() {
        return classModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Join join = (Join) o;

        if (!classModel.equals(join.classModel)) {
            return false;
        }
        return alias.equals(join.alias);
    }

    @Override
    public int hashCode() {
        int result = classModel.hashCode();
        result = 31 * result + alias.hashCode();
        return result;
    }
}
