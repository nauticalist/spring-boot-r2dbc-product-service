package io.seanapse.app.products.infrastructure.exception.model;

public class ErrorCodes {
    /**
     * Error code for Runtime Exception
     */
    public static final int ERR_RUNTIME = 1000;

    /**
     * Error code for Handler not found
     */
    public static final int ERR_HANDLER_NOT_FOUND = 1010;

    /**
     * Error code for Resource not found
     */
    public static final int ERR_RESOURCE_NOT_FOUND = 1020;

    /**
     * Error code for validation failed exceptions
     */
    public static final int ERR_REQUEST_PARAMS_BODY_VALIDATION_FAILED = 1030;

    /**
     * Error code for Constraint check exceptions
     */
    public static final int ERR_CONSTRAINT_CHECK_FAILED = 1040;

    /**
     * Error code for web client request exception
     */
    public static final int ERR_WEBCLIENT_REQUEST_FAILED = 1050;

    /**
     * Error code for access denied
     */
    public static final int ERR_ACCESS_DENIED = 1060;

    /**
     * Error code for unauthorized access denied
     */
    public static final int ERR_UNAUTHORIZED_ACCESS = 1060;
}
