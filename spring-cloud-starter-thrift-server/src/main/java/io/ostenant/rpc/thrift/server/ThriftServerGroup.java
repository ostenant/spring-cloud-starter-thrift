package io.ostenant.rpc.thrift.server;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.thrift.server.TServer;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class ThriftServerGroup {

    private Queue<TServer> servers = new LinkedBlockingDeque<>();

    public ThriftServerGroup(TServer... servers) {
        if (Objects.isNull(servers) || servers.length == 0) {
            return;
        }

        this.servers.addAll(Arrays.asList(servers));
    }

    public ThriftServerGroup(List<TServer> servers) {
        if (CollectionUtils.isEmpty(servers)) {
            return;
        }

        this.servers.clear();
        this.servers.addAll(servers);
    }

    public Queue<TServer> getServers() {
        return servers;
    }

    public void setServers(Queue<TServer> servers) {
        this.servers = servers;
    }
}
