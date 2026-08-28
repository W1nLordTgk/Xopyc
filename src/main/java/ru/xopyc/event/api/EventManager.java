package ru.xopyc.event.api;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.xopyc.event.api.listener.EventHandler;
import ru.xopyc.event.api.listener.Listener;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventManager {
    @Getter private static final EventManager instance = new EventManager();
    private final Map<Class<?>, List<EventListener>> listenerCache = new ConcurrentHashMap<>();
    private final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private final Logger LOGGER = LogManager.getLogger();

    private EventManager() {
    }

    /**
     * Добавляет объект в коллекцию слушателей
     * Проверяет количество аргуметов в методе и аннотацию @EventHandler
     *
     * @param listener - любой java - Class
     */
    public void register(Listener listener) {
        Method[] methods = listener.getClass().getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                try {
                    method.setAccessible(true);
                    Class<?>[] params = method.getParameterTypes();
                    if (params.length == 1 && Event.class.isAssignableFrom(params[0])) {
                        MethodHandle methodHandle = LOOKUP.unreflect(method);
                        EventListener eventListener = new EventListener(listener, methodHandle);

                        listenerCache.computeIfAbsent(params[0], k -> new CopyOnWriteArrayList<>()).add(eventListener);
                    }
                } catch (IllegalAccessException e) {
                    LOGGER.warn(e.getMessage());
                }
            }
        }
    }

    /**
     * Удаляет объект из коллекции слушателей
     */
    public void unregister(Listener listener) {
        listenerCache.values().forEach(list ->
                // Сравниваем ссылки в памяти, а не объекты
                list.removeIf(eventListener -> eventListener.listener() == listener)
        );
    }

    /**
     * Вызывает событие и
     *
     * @param event - вызываемое событие
     */
    public void call(Event event) {

        // Слушатели для каждого класса
        List<EventListener> cachedListeners = listenerCache.get(event.getClass());

        if (cachedListeners != null) {
            for (EventListener eventListener : cachedListeners) {
                try {
                    eventListener.call(event);
                } catch (Throwable e) {
                    LOGGER.error("Error while calling event {}", event.getClass().getSimpleName(), e);
                }
                // Если отменено, дальше не вызывается
                if (event instanceof Cancellable && ((Cancellable) event).isCancelled()) {
                    return;
                }

            }
        }
    }

    private record EventListener(Listener listener, MethodHandle methodHandle) {
        public void call(Event event) throws Throwable {
            methodHandle.invoke(listener, event);
        }
    }

}

