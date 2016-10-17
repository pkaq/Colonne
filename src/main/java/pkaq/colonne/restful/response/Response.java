package pkaq.colonne.restful.response;

/**
 * Created with IntelliJ IDEA.
 * Author: S.PKAQ
 * Datetime: 2016-10-17 15:02
 */
public class Response {
    private String ErrorCode;
    private String Message;

    public Response(String errorCode, String message) {
        ErrorCode = errorCode;
        Message = message;
    }

    public String getErrorCode() {
        return ErrorCode;
    }
    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
