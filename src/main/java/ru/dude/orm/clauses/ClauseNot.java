package ru.dude.orm.clauses;

/**
 * Предикат NOT
 *
 * @author dude.
 */
public class ClauseNot implements Clause {

    /**
     * Вложенный предикат
     */
    private Clause subClause;

    /**
     * Предикат NOT
     *
     * @param subClause
     */
    public ClauseNot(Clause subClause) {
        this.subClause = subClause;
    }

    @Override
    public String buildSql() {
        return Operand.NOT.getSqlOperand() + subClause.buildSql();
    }
}
