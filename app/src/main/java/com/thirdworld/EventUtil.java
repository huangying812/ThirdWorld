package com.thirdworld;


import org.greenrobot.eventbus.EventBus;

import szz.com.thirdworld.BuildConfig;

/**
 * 作者：Ying.Huang on 2017/3/29 18:29
 * Version v1.0
 * 描述：对EventBus的简单封装，没有附加任何其余的操作。
 */

public class EventUtil {

    public static void cancelEventDelivery(Object event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }

    public static void clearCaches() {
        EventBus.clearCaches();
    }

    public static <T> T getStickyEvent(Class<T> eventType) {
        return EventBus.getDefault().getStickyEvent(eventType);
    }

    public static String getString() {
        return EventBus.getDefault().toString();
    }

    public static boolean hasSubscriberForEvent(Class<?> eventClass) {
        return EventBus.getDefault().hasSubscriberForEvent(eventClass);
    }

    public static void init() {
        EventBus.builder()
                .throwSubscriberException(BuildConfig.DEBUG)
                .logNoSubscriberMessages(BuildConfig.DEBUG)
                .sendNoSubscriberEvent(BuildConfig.DEBUG)
                .installDefaultEventBus();
    }

    public static boolean isRegistered(Object subscriber) {
        return EventBus.getDefault().isRegistered(subscriber);
    }

    public static void post(Object event){
        EventBus.getDefault().post(event);
    }

    public static void postSticky(Object event) {
        EventBus.getDefault().postSticky(event);
    }

    public static void register(Object subscriber) {
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(subscriber)) {
            eventBus.register(subscriber);
        }
    }

    public static void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }

    public static <T> T removeStickyEvent(Class<T> eventType) {
        return EventBus.getDefault().removeStickyEvent(eventType);
    }

    public static boolean removeStickyEvent(Object event) {
        return EventBus.getDefault().removeStickyEvent(event);
    }

    public static void unregister(Object subscriber) {
        EventBus eventBus = EventBus.getDefault();
        if (eventBus.isRegistered(subscriber)) {
            eventBus.unregister(subscriber);
        }
    }
}
