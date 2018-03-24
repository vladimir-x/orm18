/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dude.orm;

import java.sql.ResultSet;

/**
 *
 * @author dude
 */
public interface DBConnection {

    void init() throws OrmOperationException;

    void execute(String sql) throws OrmOperationException;
    
    ResultSet executeQuery(String sql) throws OrmOperationException;
    
    void closeResultSet(ResultSet rs) throws OrmOperationException;
    
    void closeConnection() throws OrmOperationException;
}
