package net.segner.util.hostsfiledns.control.setup;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author aaronsegner
 */
public interface InstallerService {
    List<String> jarApplicationResources = Arrays.asList("hostsfiledns-monitor.jar");

    void install() throws IOException;
}
