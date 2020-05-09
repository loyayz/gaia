package com.loyayz.gaia.exception.helper;

import com.loyayz.gaia.exception.ExceptionResult;
import lombok.Data;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class ExceptionLoggerParam {

    private Throwable exception;
    private String requestMethod;
    private String requestUrl;
    private String logLevel;
    private ExceptionResult result;

    public String getLogMessage() {
        return this.getRequestMessage() + this.result;
    }

    private String getRequestMessage() {
        boolean hasMethod = this.requestMethod != null && !"".equals(this.requestMethod);
        boolean hasUrl = this.requestUrl != null && !"".equals(this.requestUrl);
        if (hasMethod && hasUrl) {
            return "[" + this.requestMethod + "-" + this.requestUrl + "] ";
        } else if (hasMethod) {
            return "[" + this.requestMethod + "] ";
        } else if (hasUrl) {
            return "[" + this.requestUrl + "] ";
        } else {
            return "";
        }
    }

}
