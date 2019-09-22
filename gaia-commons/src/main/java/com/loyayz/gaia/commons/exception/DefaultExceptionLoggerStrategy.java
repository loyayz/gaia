package com.loyayz.gaia.commons.exception;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Slf4j
public class DefaultExceptionLoggerStrategy implements ExceptionLoggerStrategy {

    /**
     * 0 级异常：不写日志
     * 1 级异常：warn
     * 1 级以上：error
     */
    @Override
    public void write(String apiMethod, String apiUrl, Throwable exception, ExceptionDisposer disposer) {
        int exceptionLevel = disposer.level(exception);
        ExceptionResult result = disposer.getResult(exception);
        String logPre = this.logPre(apiMethod, apiUrl);

        if (exceptionLevel > 1) {
            this.writeInError(logPre, exception, result);
        } else if (exceptionLevel == 1) {
            this.writeInWarn(logPre, exception, result);
        }
    }

    private void writeInWarn(String logPre, Throwable exception, ExceptionResult result) {
        if (log.isWarnEnabled()) {
            String logMsg = this.logMsg(logPre, result);
            log.warn(logMsg, exception);
        }
    }

    private void writeInError(String logPre, Throwable exception, ExceptionResult result) {
        if (log.isErrorEnabled()) {
            String logMsg = this.logMsg(logPre, result);
            log.error(logMsg, exception);
        }
    }

    private String logPre(String apiMethod, String apiUrl) {
        List<String> items = new ArrayList<>();
        if (apiMethod != null && !"".equals(apiMethod)) {
            items.add(apiMethod);
        }
        if (apiUrl != null && !"".equals(apiUrl)) {
            items.add(apiUrl);
        }
        if (items.isEmpty()) {
            return "";
        }
        return "[" + Joiner.on("-").join(items) + "] ";
    }

    private String logMsg(String pre, ExceptionResult result) {
        return pre + result;
    }

}
