package io.ostenant.rpc.thrift.client.discovery;

import com.orbitz.consul.model.health.HealthCheck;
import com.orbitz.consul.model.health.Node;
import com.orbitz.consul.model.health.Service;
import com.orbitz.consul.model.health.ServiceHealth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ThriftConsulServerUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftConsulServerUtils.class);

    private static final String CHECK_STATUS_PASSING = "passing";

    private ThriftConsulServerUtils() {
    }

    public static String findHost(ServiceHealth serviceHealth) {
        Service service = serviceHealth.getService();
        Node node = serviceHealth.getNode();
        if (StringUtils.hasText(service.getAddress())) {
            return fixIPv6Address(service.getAddress());
        } else {
            return StringUtils.hasText(node.getAddress()) ? fixIPv6Address(node.getAddress()) : node.getNode();
        }
    }

    private static String fixIPv6Address(String address) {
        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            return inetAddress instanceof Inet6Address ? "[" + inetAddress.getHostName() + "]" : address;
        } catch (UnknownHostException var2) {
            LOGGER.error("Not InetAddress: " + address + " , resolved as is.");
            return address;
        }
    }

    public static List<String> getTags(ServiceHealth serviceHealth) {
        return serviceHealth.getService().getTags();
    }


    public static boolean isPassingCheck(ServiceHealth serviceHealth) {
        List<HealthCheck> healthChecks = serviceHealth.getChecks();
        for (HealthCheck healthCheck : healthChecks) {
            if (!CHECK_STATUS_PASSING.equals(healthCheck.getStatus())) {
                return false;
            }
        }
        return true;
    }


    public static Map<String, String> getMetadata(ServiceHealth serviceHealth) {
        return getMetadata(serviceHealth.getService().getTags());
    }

    private static Map<String, String> getMetadata(List<String> tags) {
        LinkedHashMap<String, String> metadata = new LinkedHashMap<>();
        if (tags != null) {

            for (String tag : tags) {
                String[] parts = StringUtils.delimitedListToStringArray(tag, "=");
                switch (parts.length) {
                    case 0:
                        break;
                    case 1:
                        metadata.put(parts[0], parts[0]);
                        break;
                    case 2:
                        metadata.put(parts[0], parts[1]);
                        break;
                    default:
                        String[] end = Arrays.copyOfRange(parts, 1, parts.length);
                        metadata.put(parts[0], StringUtils.arrayToDelimitedString(end, "="));
                }
            }
        }

        return metadata;
    }

}
