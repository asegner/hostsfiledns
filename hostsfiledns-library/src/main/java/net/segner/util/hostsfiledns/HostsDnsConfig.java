package net.segner.util.hostsfiledns;

import java.io.IOException;
import java.util.Set;

public interface HostsDnsConfig {

    /**
     * persists hostsfiledns configuration
     */
    void save() throws IOException;

    /**
     * loads host names from the config file, which is very simply one host per line
     *
     */
    Set<String> load() throws IOException;
}
