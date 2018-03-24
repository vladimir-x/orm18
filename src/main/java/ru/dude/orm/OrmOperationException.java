package ru.dude.orm;

/**
 * Исключение при работе ORM
 */
public class OrmOperationException extends Exception {

    public OrmOperationException() {
        super();
    }

    public OrmOperationException(String pErr) {
        super(pErr);
    }

    public OrmOperationException(Throwable cause) {
        super(cause);
    }

    public OrmOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
