/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dude.orm;

/**
 *
 * @author dude
 */
public class Log {

    public static void info(String message) {
        System.out.println(message);
    }

    public static void warning(String message) {
        System.out.println(message);
    }

    public static void debug(String message) {
        System.out.println(message);
    }

    public static void error(String message) {
        System.out.println(message);
    }
    
    public static void debug(Exception ex) {
        ex.printStackTrace();
    }
    
    public static void error(Exception ex) {
        ex.printStackTrace();
    }
     
}
