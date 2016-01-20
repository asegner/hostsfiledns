package net.segner.util.hostsfiledns;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.segner.util.hostsfiledns.inject.InjectLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class HostsFileUpdateServiceImpl implements HostsFileUpdateService {
    @InjectLogger
    private Logger logger;
    @Inject
    private HostsFile hostsFile;
    @Inject
    private HostsDnsConfig hostsDnsConfig;
    @Inject
    private Resolver resolver;

    @Override
    public void update() throws IOException {
        // get hosts to monitor from the config
        Set<String> hostSet = hostsDnsConfig.load();

        // pass the mapping to the host file for persisting
        hostsFile.mergeIpToHostMap(mapHostsToIps(hostSet));
    }

    @Override
    public void defaultConfig() throws IOException {
        hostsDnsConfig.save();
    }

    // resolve ip for host names, for bad resolves, add null
    private @NotNull Map<String, String> mapHostsToIps(Set<String> hostSet) {
        Map<String, String> ipMapping = new HashMap<>();
        hostSet.forEach(host -> {
            try {
                Set<String> ips = resolver.resolveIp4(host);
                if (!ips.isEmpty()) {
                    ipMapping.put(host, ips.iterator().next()); // only one ip wins, host file ignores multiple ips for one host, only supports multiple host names for one ip
                } else {
                    ipMapping.put(host, null);
                }
            } catch (Exception e) {
                logger.debug(e.getMessage());
                ipMapping.put(host, null);
            }
        });
        return ipMapping;
    }

}
