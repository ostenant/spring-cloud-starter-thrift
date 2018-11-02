package io.ostenant.rpc.thrift.server;

import org.apache.thrift.server.TServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class ThriftServerBootstrap implements SmartLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftServerBootstrap.class);

    private ThriftServerGroup thriftServerGroup;

    public ThriftServerBootstrap(ThriftServerGroup thriftServerGroup) {
        this.thriftServerGroup = thriftServerGroup;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable runnable) {
        if (isRunning()) {
            LOGGER.info("Shutting down thrift servers");
            thriftServerGroup.getServers().forEach(server -> {
                server.setShouldStop(true);
                server.stop();
            });
            runnable.run();
        }
    }

    @Override
    public void start() {
        if (CollectionUtils.isEmpty(thriftServerGroup.getServers())) {
            return;
        }

        LOGGER.info("Starting thrift servers");

        AtomicInteger serverIndex = new AtomicInteger(0);
        thriftServerGroup.getServers().forEach(server -> {
            ThriftRunner runner = new ThriftRunner(server);
            new Thread(runner, "thrift-server-" + serverIndex.incrementAndGet()).start();
        });
    }

    @Override
    public void stop() {
        stop(null);
    }

    @Override
    public boolean isRunning() {
        return thriftServerGroup.getServers().stream().anyMatch(TServer::isServing);
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    private static class ThriftRunner implements Runnable {

        private static final Logger LOGGER = LoggerFactory.getLogger(ThriftRunner.class);

        private TServer server;

        public ThriftRunner(TServer server) {
            this.server = server;
        }

        @Override
        public void run() {
            if (server != null) {
                this.server.serve();
                LOGGER.info(server.isServing() ? "Thrift server started successfully" : "Thrift server failed to start");
            }
        }
    }
}
