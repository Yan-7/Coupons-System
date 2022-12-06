package exceptions;

public class CouponsException extends  Exception {
    public CouponsException() {
    }

    public CouponsException(String message) {
        super(message);
    }

    public CouponsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CouponsException(Throwable cause) {
        super(cause);
    }

    public CouponsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
