package net.segner.util.hostsfiledns.file;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HostsFileImplTest_Clean extends AbstractHostsFileImplTest {

    public HostsFileImplTest_Clean() {
        super("linux-host-clean");
    }

    @Test
    public void noResolutionResults() throws IOException {
        Map<String, String> noresolutions = new HashMap<>();
        noresolutions.put("google.com", null);
        noresolutions.put("yahoo.com", null);
        hostsFile.mergeIpToHostMap(noresolutions);

        //TODO add assertions and mock checks
    }
}
