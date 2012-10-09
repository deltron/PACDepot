package com.pacdepot.database;

/**
 * An exception that indicates some operation with the back-end
 * data source failed.
 */
public class DataException extends Exception {
    private Throwable rootCause;

    /**
     * Wrap a DataException around another throwable.
     */
    public DataException(Throwable rootCause) {
        super(rootCause.getMessage());
        this.rootCause = rootCause;
    }

    /**
     * Construct an exception with the specified detail message.
     */
    public DataException(String message) {
        super(message);
    }

    /**
     * @return a reference to the root exception or null.
     */
    public Throwable getRootCause() {
        return this.rootCause;
    }
}
