package ru.dude.orm.clauses;

import ru.dude.orm.OrmOperationException;
import ru.dude.orm.model.JoinedField;
import ru.dude.orm.model.DBTypeConvert;

/**
 * Предикат IN
 *
 * @author dude.
 */
public class ClauseIn implements Clause {

    /**
     * Поле в таблице БД
     */
    private JoinedField joinedField;

    /**
     * Список значений через ","
     */
    String valuesQuery;

    /**
     * Предикат IN
     *
     * @param joinedField - Поле в таблице БД
     * @param vals список значений, обрабатывается по правилам DBTypeConvert
     * @throws OrmOperationException
     */
    public ClauseIn(JoinedField joinedField, Object... vals) throws OrmOperationException {

        this.joinedField = joinedField;

        DBTypeConvert convert = new DBTypeConvert();

        StringBuilder sb = new StringBuilder();

        if (vals != null) {
            //выделение не нулевых условий
            for (Object val : vals) {
                String sqlVal = convert.ToSqlValue(val);
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(sqlVal);
            }
        }
        valuesQuery = sb.toString();
    }

    @Override
    public String buildSql() {
        return joinedField.buildSql() + " " + Operand.IN + valuesQuery;
    }
}
