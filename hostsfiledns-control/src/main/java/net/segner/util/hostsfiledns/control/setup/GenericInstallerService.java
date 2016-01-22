package net.segner.util.hostsfiledns.control.setup;

import com.google.inject.Inject;
import net.segner.util.hostsfiledns.HostsFileUpdateService;
import net.segner.util.hostsfiledns.inject.InjectLogger;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystemException;

public abstract class GenericInstallerService implements InstallerService {
    @InjectLogger
    private Logger logger;
    @Inject
    protected HostsFileUpdateService hostsFileUpdateService;

    protected abstract void doOsInstall() throws IOException;

    protected abstract void createStartJob() throws IOException;

    @Override
    public final void install() throws IOException {
        logger.info("HostsDNS Service Install");
        logger.info("Operating System: " + SystemUtils.OS_NAME);

        createDefaultConfiguration();
        doOsInstall();
        createStartJob();
        updateHostsFile();

        logger.info("=--=-- Install Complete --=--=");
    }

    protected void updateHostsFile() throws IOException {
        logger.info("=--=-- Updating Hosts File --=--=");
        hostsFileUpdateService.update();
    }

    protected void createDefaultConfiguration() throws IOException {
        hostsFileUpdateService.defaultConfig();
    }

    protected void createFolder(@NotNull String folderPath) throws FileSystemException {
        logger.debug("  Creating directory: " + folderPath);
        File lib = new File(folderPath);
        lib.mkdirs();
        if (!lib.exists()) {
            throw new FileSystemException("Unable to create path");
        }
    }

    protected void copyJarsToPath(@NotNull String libraryPath) throws IOException {
        for (String jar : jarApplicationResources) {
            copyResource(jar, libraryPath);
        }
    }

    protected int execute(@NotNull String command, int successExitValue) throws IOException {
        CommandLine cmdLine = CommandLine.parse(command);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(successExitValue);
        ExecuteWatchdog watchdog = new ExecuteWatchdog(10000);
        executor.setWatchdog(watchdog);
        return executor.execute(cmdLine);
    }

    protected void copyResource(@NotNull String resource, @NotNull String dest) throws IOException {
        URL url = ClassLoader.getSystemClassLoader().getResource(resource);
        String destResource = dest + File.separator + FilenameUtils.getName(url.getFile());
        logger.debug("  Copying '" + resource + "' to '" + destResource + "'");
        File destFile = new File(destResource);
        FileUtils.copyURLToFile(url, destFile);
        if (!destFile.exists()) {
            throw new FileSystemException("Copy failed");
        }
    }
}
