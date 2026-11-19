package com.example.gobaltvisual.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GVLog {
    private static final Logger LOG = LoggerFactory.getLogger("GobaltVisual");
    private static final Deque<LogEntry> BUFFER = new ArrayDeque<>();
    private static final int MAX_LINES = 500;

    public enum Level { DEBUG, INFO, WARN, ERROR }

    public static class LogEntry {
        public final long time;
        public final Level level;
        public final String message;

        public LogEntry(Level level, String message) {
            this.time = System.currentTimeMillis();
            this.level = level;
            this.message = message;
        }
    }

    public static void info(String msg) {
        LOG.info(msg);
        push(Level.INFO, msg);
    }

    public static void warn(String msg) {
        LOG.warn(msg);
        push(Level.WARN, msg);
    }

    public static void error(String msg) {
        error(msg, null);
    }

    public static void error(String msg, Throwable t) {
        LOG.error(msg, t);
        String suffix = t != null ? " | " + t.getClass().getSimpleName() + ": " + t.getMessage() : "";
        push(Level.ERROR, msg + suffix);
    }

    public static void debug(String msg) {
        if (DeveloperMode.isEnabled()) {
            LOG.info("[DEBUG] " + msg);
            push(Level.DEBUG, msg);
        }
    }

    public static void debug(DeveloperMode.Feature feature, String msg) {
        if (DeveloperMode.isDebug(feature)) {
            LOG.info("[DEBUG/" + feature.name() + "] " + msg);
            push(Level.DEBUG, "[" + feature.name() + "] " + msg);
        }
    }

    private static void push(Level level, String msg) {
        synchronized (BUFFER) {
            BUFFER.addLast(new LogEntry(level, msg));
            while (BUFFER.size() > MAX_LINES) {
                BUFFER.removeFirst();
            }
        }
    }

    public static List<LogEntry> getLines() {
        synchronized (BUFFER) {
            return new ArrayList<>(BUFFER);
        }
    }

    public static void clear() {
        synchronized (BUFFER) {
            BUFFER.clear();
        }
    }
}