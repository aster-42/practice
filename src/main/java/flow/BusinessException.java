package flow;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends Exception {
    private static final long serialVersionUID = 9040557989512775034L;
    private int errorCode;
    private String errorMessage;

    public BusinessException(BusinessError error) {
        this.errorCode = error.getCode();
        this.errorMessage = error.getErrorInfo();
    }

    public BusinessException(BusinessError error, String errorMessage) {
        this.errorCode = error.getCode();
        this.errorMessage = error.getErrorInfo() + ":" + errorMessage;
    }
}
