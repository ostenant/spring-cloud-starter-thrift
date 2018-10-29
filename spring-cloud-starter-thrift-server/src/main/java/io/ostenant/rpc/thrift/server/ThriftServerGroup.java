package io.ostenant.rpc.thrift.server;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.OperatorClient;
import com.orbitz.consul.SessionClient;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import com.orbitz.consul.model.session.Session;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.thrift.server.TServer;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

public class ThriftServerGroup {

    public static void main(String[] args) {
        Consul consul = Consul.builder().withUrl("").build();
        AgentClient agentClient = consul.agentClient();
        Registration.RegCheck check = ImmutableRegCheck.tcp("", 10L, 1000L);
        Registration registration = ImmutableRegistration.builder()
                .id("").name("").port(1000)
                .tags(new ArrayList<>())
                .check(check)
                .build();
        agentClient.register(registration);
    }

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
