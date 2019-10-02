package com.loyayz.gaia.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Slf4j
public class DefaultExceptionLoggerStrategy implements ExceptionLoggerStrategy {

    /**
     * 1 级异常：warn
     * 1 级以上：error
     */
    @Override
    public void write(String apiMethod, String apiUrl, Throwable exception, ExceptionDisposer disposer) {
        int exceptionLevel = this.getExceptionLevel(disposer, exception);
        ExceptionResult result = disposer.getResult(exception);
        String logPre = this.logPre(apiMethod, apiUrl);

        if (exceptionLevel > 1) {
            this.writeInError(logPre, exception, result);
        } else if (exceptionLevel == 1) {
            this.writeInWarn(logPre, exception, result);
        }
    }

    /**
     * 异常等级
     * 当为默认等级 {@code ExceptionDisposer.DEFAULT_LEVEL } 时，则等级赋值为：
     * unchecked Exception:1, checked Exception:2, Error:3
     */
    private int getExceptionLevel(ExceptionDisposer disposer, Throwable exception) {
        int result = disposer.level(exception);
        if (result == ExceptionDisposer.DEFAULT_LEVEL) {
            if (exception instanceof RuntimeException) {
                result = 1;
            } else if (exception instanceof Exception) {
                result = 2;
            } else {
                result = 3;
            }
        }
        return result;
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
        } else if (items.size() == 1) {
            return "[" + items.get(0) + "] ";
        } else {
            return "[" + items.get(0) + "-" + items.get(1) + "] ";
        }
    }

    private String logMsg(String pre, ExceptionResult result) {
        return pre + result;
    }

}
