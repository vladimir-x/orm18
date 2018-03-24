package ru.dude.orm.group;

import ru.dude.orm.model.JoinedField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dude.
 */
public class GroupBy {

    List<JoinedField> groupFields = new ArrayList<>();

    public GroupBy(JoinedField... joinedFields) {
        if (joinedFields != null && joinedFields.length > 0) {
            for (JoinedField jf : joinedFields) {
                if (jf != null) {
                    groupFields.add(jf);
                }
            }

        }
    }

    public String buildSql() {
        StringBuilder sb = new StringBuilder();
        for (JoinedField jf : groupFields) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(jf.buildSql());

        }
        sb.append("\n");
        return sb.toString();
    }
}
