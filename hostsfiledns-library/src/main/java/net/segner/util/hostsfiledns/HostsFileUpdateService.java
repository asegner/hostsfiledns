package net.segner.util.hostsfiledns;

import java.io.IOException;

public interface HostsFileUpdateService {
    void update() throws IOException;

    void defaultConfig() throws IOException;
}
