package flow;

public enum BusinessError {

    PermissionDeny(1000, "permission deny"),
    UnLoginError(1001, "un login error"),
    ;

    private int code;
    private String errorInfo;

    BusinessError(int code, String errorInfo) {
        this.code = code;
        this.errorInfo = errorInfo;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public boolean eq(BusinessException error) {
        return this.code == error.getErrorCode() && this.errorInfo.equals(error.getErrorMessage());
    }
}
