package com.loyayz.gaia.util.event;

import com.google.common.eventbus.EventBus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class EventBusUtils {
    private static Map<String, EventBus> eventBusMap = new ConcurrentHashMap<>();

    /**
     * 推送事件
     */
    public static void postEvent(EventBusType eventBusType, Object event) {
        getEventBus(eventBusType).post(event);
    }

    /**
     * 推送事件
     */
    public static void postEvent(String eventGroupName, Object event) {
        EventBus eventBus = getEventBus(eventGroupName);
        if (eventBus == null) {
            throw new IllegalArgumentException(eventGroupName + " event bus not found!");
        }
        eventBus.post(event);
    }

    /**
     * 添加事件监听
     */
    public static void addEventListener(EventBusType eventBusType, Object listener) {
        getEventBus(eventBusType).register(listener);
    }

    /**
     * 移除事件监听
     */
    public static void removeEventListener(EventBusType eventBusType, Object listener) {
        getEventBus(eventBusType).unregister(listener);
    }

    private synchronized static EventBus getEventBus(EventBusType eventBusType) {
        EventBus eventBus = eventBusMap.get(eventBusType.eventGroupName());
        if (eventBus == null) {
            eventBus = eventBusType.newEventBus();
            eventBusMap.put(eventBusType.eventGroupName(), eventBus);
        }
        return eventBus;
    }

    private synchronized static EventBus getEventBus(String eventGroupName) {
        return eventBusMap.get(eventGroupName);
    }

}
