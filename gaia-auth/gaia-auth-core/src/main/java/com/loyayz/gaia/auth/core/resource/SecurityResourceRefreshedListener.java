package com.loyayz.gaia.auth.core.resource;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface SecurityResourceRefreshedListener {

    /**
     * 资源成功刷新后回调
     */
    void onResourceRefreshed();

}
