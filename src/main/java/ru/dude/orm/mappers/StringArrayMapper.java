package ru.dude.orm.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Мэппер в массив строк
 *
 * @author dude.
 */
public class StringArrayMapper implements Mappable {

    /**
     * Результат
     */
    ArrayList<String[]> res;

    @Override
    public void init() {
        res = new ArrayList<>();
    }

    @Override
    public void mapLine(ResultSet rs) throws SQLException {

        int columnCount = rs.getMetaData().getColumnCount();
        String[] currLine = new String[columnCount];
        for (int i = 0; i < columnCount; ++i) {
            currLine[i] = rs.getString(i + 1);
        }
        res.add(currLine);
    }

    @Override
    public void complete() {

    }

    /**
     * Результат
     *
     * @return
     */
    public ArrayList<String[]> getRes() {
        return res;
    }
}
