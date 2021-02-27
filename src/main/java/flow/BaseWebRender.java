package flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseWebRender<Param extends WebParam, Data extends ServerData> {
    private static final String loginUrl = "";
    private static final String permissionDenyPage = "";

    private static final Logger logger = LoggerFactory.getLogger(BaseWebRender.class);

    public void invoke(Param param, Data data) {
        long startTime = System.currentTimeMillis();
        Exception e = null;
        try {
            param.check();
            initParam(param);
            initServerData(param, data);
            render(param, data);
        } catch (IllegalArgumentException illegal) {
            e = illegal;
            WebErrorInfo errorInfo = webErrorInfo();
            errorInfo.setErrorTitle("参数异常");
            errorInfo.setErrorDetail(illegal.getMessage());
        } catch (BusinessException business) {
            e = business;
            WebErrorInfo errorInfo = webErrorInfo();
            if (BusinessError.UnLoginError.eq(business)) {
                errorInfo.redirect(loginUrl);
            }
            else if (BusinessError.PermissionDeny.eq(business)) {
                errorInfo.redirect(permissionDenyPage);
            }
            else {
                errorInfo.setErrorTitle("业务异常,错误编码:" + business.getErrorCode());
                errorInfo.setErrorDetail(business.getErrorMessage());
            }
        } catch (Exception exception) {
            e = exception;
            logger.error("system error", exception);
            WebErrorInfo errorInfo = webErrorInfo();
            errorInfo.setErrorTitle("系统异常");
            errorInfo.setErrorDetail(exception.getMessage());
        } finally {
            ThreadLocal<?> threadLocal = threadLocal();
            threadLocal.remove();
            if (e != null) {
                failedInvoke(param, data, e);
            }
        }
    }

    public abstract void initParam(Param param) throws BusinessException;
    public abstract void initServerData(Param param, Data data) throws BusinessException;
    public abstract void render(Param param, Data data) throws BusinessException;
    public abstract void failedInvoke(Param param, Data data, Exception e);

    /**
     * 是否需要灰度
     * */
    public boolean pageAbTest() {
        return false;
    }

    public abstract WebErrorInfo webErrorInfo();
    public abstract ThreadLocal<?> threadLocal();
}