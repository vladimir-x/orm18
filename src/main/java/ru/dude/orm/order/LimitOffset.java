package ru.dude.orm.order;

/**
 * Лимит, оффсет
 *
 * @author dude.
 */
public class LimitOffset {

    Integer limit = null;
    Integer offset = null;

    public LimitOffset(Integer limit) {
        this.limit = limit;
    }

    public LimitOffset(Integer limit, Integer offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public String buildSql() {

        StringBuilder sb = new StringBuilder();
        if (limit != null) {
            sb.append("LIMIT ").append(limit);
        }
        if (offset != null) {
            sb.append(" OFFSET ").append(offset);
        }
        sb.append("\n");
        return sb.toString();
    }
}
