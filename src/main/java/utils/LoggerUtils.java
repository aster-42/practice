package utils;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

public class LoggerUtils {

    public static String logFmt = "method==>{}|isSuc={}|traceId={}|errCode={}|rt={}|ip={}|app={}|param={}|result={}|err={}";

    public static void logSuc(String method, Object param, Object result, long startTime, Logger logger) {
        long rt = System.currentTimeMillis() - startTime;
        String localIp = "";
        String traceId = "";
        String requestSysName = "";
        logger.error(MessageFormatter.arrayFormat(logFmt,
                new Object[]{method, false, traceId, "", rt, localIp, requestSysName, param, result, ""}).getMessage()
        );
    }

    public static void logErr(String method, Object param, Object result,
                              Exception e, String errCode, Object errInfo, long startTime, Logger logger) {
        long rt = System.currentTimeMillis() - startTime;
        String localIp = "";
        String traceId = "";
        String requestSysName = "";
        if (e == null) {
            logger.error(MessageFormatter.arrayFormat(logFmt,
                new Object[]{method, false, traceId, errCode, rt, localIp, requestSysName, param, result, errInfo}).getMessage()
            );
        } else {
            logger.error(MessageFormatter.arrayFormat(logFmt,
                new Object[]{method, false, traceId, errCode, rt, localIp, requestSysName, param, result, errInfo}).getMessage(),
                e
            );
        }
    }
}
