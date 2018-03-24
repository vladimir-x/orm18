/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dude.orm.sample;

import ru.dude.orm.DBConnection;
import ru.dude.orm.EntityManager;
import ru.dude.orm.OrmOperationException;
import ru.dude.orm.sample.entity.Car;
import ru.dude.orm.sample.entity.BodyType;
import ru.dude.orm.sample.entity.Driver;

/**
 *
 * @author dude
 */
public class OrmTest {

    public static void main(String[] args) throws OrmOperationException {
        DBConnection connection = new PsqlConnection("localhost","5432","dudebase","postgres","postgres");
        EntityManager.init(connection);        
        
        //Examples.testNativeSQL();
        //Examples.testSave();
        Examples.testLoaders();
    }
}
