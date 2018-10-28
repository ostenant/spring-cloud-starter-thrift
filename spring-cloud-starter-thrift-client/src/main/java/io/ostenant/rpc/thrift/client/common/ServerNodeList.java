package io.ostenant.rpc.thrift.client.common;

import java.util.LinkedHashSet;
import java.util.Map;

public interface ServerNodeList<T extends ThriftServerNode> {

    Map<String, LinkedHashSet<T>> getInitialListOfThriftServers();

    Map<String, LinkedHashSet<T>> getUpdatedListOfThriftServers();
}
