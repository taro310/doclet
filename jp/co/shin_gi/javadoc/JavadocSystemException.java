package jp.co.shin_gi.javadoc;

public class JavadocSystemException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JavadocSystemException() {
    }

    public JavadocSystemException(String message) {
        super(message);
    }

    public JavadocSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public JavadocSystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public JavadocSystemException(Throwable cause) {
        super(cause);
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name:
 * jp.co.shin_gi.javadoc.JavadocSystemException Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */