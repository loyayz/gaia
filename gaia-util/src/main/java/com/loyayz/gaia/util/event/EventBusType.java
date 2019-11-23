package com.loyayz.gaia.util.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface EventBusType {

    /**
     * 事件分组名
     */
    String eventGroupName();

    /**
     * 是否异步事件
     */
    default boolean async() {
        return true;
    }

    default EventBus newEventBus() {
        String name = this.eventGroupName();
        if (this.async()) {
            int poolSize = Runtime.getRuntime().availableProcessors() * 2;
            return new AsyncEventBus(name, new ThreadPoolExecutor(
                    poolSize, Integer.MAX_VALUE,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    new ThreadFactoryBuilder().setNameFormat(name).build()));
        } else {
            return new EventBus(name);
        }
    }

}
