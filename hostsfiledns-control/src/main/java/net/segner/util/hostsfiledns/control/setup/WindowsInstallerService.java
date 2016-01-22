package net.segner.util.hostsfiledns.control.setup;

import net.segner.util.hostsfiledns.inject.InjectLogger;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class WindowsInstallerService extends GenericInstallerService {
    public static final String BIN_PATH = System.getenv("ProgramFiles") + File.separator + "hostsfiledns";
    private static final String BIN_NAME = "hostsfiledns.bat";
    private static final String BIN_MONITOR_CMD = "@ECHO OFF\r\njava -jar \"" + BIN_PATH + File.separator + "hostsfiledns-monitor.jar\" \"%*\"\r\n ";
    private static final String SCHTASKS_CMD = "schtasks /CREATE /F /TN hostsfileDNS /XML ";
    public static final String SCHTASKS_XML = "hostsfileDNS.xml";

    @InjectLogger
    private Logger logger;

    @Override
    protected void doOsInstall() throws IOException {
        copyApplicationFiles();
    }

    @Override
    protected void createStartJob() throws IOException {
        logger.info("=--=-- Creating monitor service --=--=");

        // use Schtasks to register service
        File xml = new File(BIN_PATH + File.separator + SCHTASKS_XML);
        execute(SCHTASKS_CMD + "\"" + xml.getAbsolutePath() + "\"", 0);
    }

    private void copyApplicationFiles() throws IOException {
        logger.info("=--=-- Copying application files --=--=");
        createFolder(BIN_PATH);

        // copy the startup task configuration
        copyResource(getClass().getPackage().getName().replace(".", "/") + "/" + SCHTASKS_XML, BIN_PATH);

        // create the bootstrap script
        String bscriptName = BIN_PATH + File.separator + BIN_NAME;
        logger.debug("  Creating bootstrap script: " + bscriptName);
        File bscriptFile = new File(bscriptName);
        bscriptFile.setWritable(true);
        writeBinFile(bscriptFile, BIN_MONITOR_CMD);
        copyJarsToPath(BIN_PATH);
    }

    private void writeBinFile(@NotNull File file, @NotNull String binContents) throws IOException {
        FileUtils.writeStringToFile(file, binContents);
        if (!file.setExecutable(true)) throw new IOException("Unable to set permissions");
        if (!file.setReadable(true)) throw new IOException("Unable to set permissions");
        if (!file.setWritable(false)) throw new IOException("Unable to set permissions");
    }
}
