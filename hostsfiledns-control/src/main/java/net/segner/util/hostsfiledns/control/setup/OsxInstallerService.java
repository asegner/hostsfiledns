package net.segner.util.hostsfiledns.control.setup;

import net.segner.util.hostsfiledns.inject.InjectLogger;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class OsxInstallerService extends PosixInstallerService {
    public static final String HOSTSFILEDNS_PLIST = "net.segner.util.hostsfiledns.plist";
    public static final String STARTUPBIN_PATH = "/Library/LaunchAgents";

    @InjectLogger
    private Logger logger;

    @Override
    protected void createStartJob() throws IOException {
        logger.info("=--=-- Creating monitor service --=--=");

        // copy plist into place
        URL plist = getClass().getResource(HOSTSFILEDNS_PLIST);
        File dest = new File(STARTUPBIN_PATH + File.separator + HOSTSFILEDNS_PLIST);
        FileUtils.copyURLToFile(plist, dest);

        // setup correct permissions
        Files.setOwner(dest.toPath(), getUser("root"));
        Files.setPosixFilePermissions(dest.toPath(), PERMS_READ);

        // use launchd to register plist
        int loadResult = execute("launchctl load " + dest.getAbsolutePath(), 0);
        if (loadResult != 0) {
            String msg = "Unable to install monitor service";
            logger.error(msg);
            throw new RuntimeException(msg);
        }
    }
}
