/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dude.orm.sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.dude.orm.DBConnection;
import ru.dude.orm.Log;
import ru.dude.orm.OrmOperationException;

/**
 *
 * @author dude
 */
public class PsqlConnection implements DBConnection {

    String host;
    String port;
    String dbname;
    String login;
    String password;

    Connection c;

    public PsqlConnection(String host, String port, String dbname, String login, String password) {
        this.host = host;
        this.port = port;
        this.dbname = dbname;
        this.login = login;
        this.password = password;
    }
    
    

    @Override
    public void init() {
        c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + dbname + "", login, password);
        } catch (Exception ex) {
            Log.error(ex);
        }
    }

    @Override
    public void execute(String sql) throws OrmOperationException {
        if (c != null) {
            Statement statement = null;
            try {
                statement = c.createStatement();
                statement.execute(sql);
            } catch (SQLException ex) {
                throw new OrmOperationException(ex);
            } finally {

                try {
                    statement.close();
                } catch (SQLException ex) {
                    throw new OrmOperationException(ex);
                }
            }
        } else {
            throw new OrmOperationException("DBConnection not init!");
        }
    }

    @Override
    public ResultSet executeQuery(String sql) throws OrmOperationException {
        if (c != null) {
            Statement statement = null;
            try {
                statement = c.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                return rs;
            } catch (SQLException ex) {

                try {
                    statement.close();
                } catch (SQLException ex1) {
                    throw new OrmOperationException(ex1);
                } finally {
                    throw new OrmOperationException(ex);
                }
            }
        } else {
            throw new OrmOperationException("DBConnection not init!");
        }
    }

    @Override
    public void closeResultSet(ResultSet rs) throws OrmOperationException {
        try {
            if (rs != null && !rs.isClosed()) {
                
                 Statement statement = rs.getStatement();
                if (statement != null && !statement.isClosed()) {
                    statement.close();
                }
                if (!rs.isClosed()) {
                    rs.close();
                }

               
            }
        } catch (SQLException ex) {
            throw new OrmOperationException(ex);
        }
    }

    @Override
    public void closeConnection() throws OrmOperationException {

        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException ex) {
            throw new OrmOperationException(ex);
        }
    }

}
