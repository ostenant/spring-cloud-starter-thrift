package io.ostenant.rpc.thrift.client.discovery;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThriftConsulServerListUpdater implements ServerListUpdater {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftConsulServerListUpdater.class);

    private final AtomicBoolean isActive = new AtomicBoolean(false);
    private final long initialDelayMs;
    private final long refreshIntervalMs;
    private volatile ScheduledFuture<?> scheduledFuture;

    public ThriftConsulServerListUpdater() {
        this(30000);
    }

    public ThriftConsulServerListUpdater(long refreshIntervalMs) {
        this(0, refreshIntervalMs);
    }

    public ThriftConsulServerListUpdater(long initialDelayMs, long refreshIntervalMs) {
        this.initialDelayMs = initialDelayMs;
        this.refreshIntervalMs = refreshIntervalMs;
    }

    private static class LazyHolder {
        private static final int CORE_THREAD = 2;
        private static Thread shutdownThread;

        static ScheduledThreadPoolExecutor serverListRefreshExecutor;

        static {
            ThreadFactory factory = new ThreadFactoryBuilder()
                    .setNameFormat("ThriftConsulServerListUpdater-%d")
                    .setDaemon(true)
                    .build();

            serverListRefreshExecutor = new ScheduledThreadPoolExecutor(CORE_THREAD, factory);

            shutdownThread = new Thread(() -> {
                LOGGER.info("Shutting down the Executor Pool for ThriftConsulServerListUpdater");
                shutdownExecutorPool();
            });

            Runtime.getRuntime().addShutdownHook(shutdownThread);
        }

        private static void shutdownExecutorPool() {
            if (serverListRefreshExecutor != null) {
                serverListRefreshExecutor.shutdown();

                if (shutdownThread != null) {
                    try {
                        Runtime.getRuntime().removeShutdownHook(shutdownThread);
                    } catch (IllegalStateException e) {
                        LOGGER.error("Failed to shutdown the Executor Pool for ThriftConsulServerListUpdater", e);
                    }
                }

            }
        }
    }

    private static ScheduledThreadPoolExecutor getRefreshExecutor() {
        return LazyHolder.serverListRefreshExecutor;
    }

    @Override
    public synchronized void start(UpdateAction updateAction) {
        if (isActive.compareAndSet(false, true)) {
            Runnable scheduledRunnable = () -> {
                if (!isActive.get()) {
                    if (scheduledFuture != null) {
                        scheduledFuture.cancel(true);
                    }
                    return;
                }

                try {
                    updateAction.doUpdate();
                } catch (Exception e) {
                    LOGGER.warn("Failed one do update action", e);
                }

            };

            scheduledFuture = getRefreshExecutor().scheduleWithFixedDelay(
                    scheduledRunnable,
                    initialDelayMs,
                    refreshIntervalMs,
                    TimeUnit.MILLISECONDS
            );
        } else {
            LOGGER.info("Already active, no other operation");
        }
    }

    @Override
    public void stop() {
        if (isActive.compareAndSet(true, false)) {
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
        } else {
            LOGGER.info("Not active, no other operation");
        }
    }
}
