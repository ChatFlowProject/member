package chatflow.memberservice.exception.custom;

import chatflow.memberservice.exception.ErrorCode;

public class InternalServiceException extends ServiceException {

    public InternalServiceException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public InternalServiceException(String message) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, message);
    }

    public InternalServiceException(ErrorCode errorCode) {
        super(errorCode);
    }

}
