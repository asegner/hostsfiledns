package net.segner.util.hostsfiledns.control.setup;

import net.segner.util.hostsfiledns.inject.InjectLogger;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PosixInstallerService extends GenericInstallerService {
    protected static final String LIB_PATH = "/usr/local/lib/hostsfiledns";
    protected static final String BIN_PATH = "/usr/local/bin";
    protected static final String BIN_NAME = "hostsfiledns";
    protected static final String BIN_MONITOR_CMD = "#!/bin/sh\njava -jar /usr/local/lib/hostsfiledns/hostsfiledns-monitor.jar \"$@\" ";
    protected static final Set<PosixFilePermission> PERMS_READ = new HashSet<>(Arrays.asList(
            PosixFilePermission.OWNER_READ,
            PosixFilePermission.GROUP_READ,
            PosixFilePermission.OTHERS_READ));
    protected static final Set<PosixFilePermission> PERMS_READEXEC = new HashSet<>(Arrays.asList(
            PosixFilePermission.OWNER_READ,
            PosixFilePermission.OWNER_EXECUTE,
            PosixFilePermission.GROUP_READ,
            PosixFilePermission.GROUP_EXECUTE,
            PosixFilePermission.OTHERS_READ,
            PosixFilePermission.OTHERS_EXECUTE));

    @InjectLogger
    private Logger logger;

    @Override
    protected void doOsInstall() throws IOException {
        copyApplicationFiles();
    }

    private void copyApplicationFiles() throws IOException {
        logger.info("=--=-- Copying application files --=--=");
        createFolder(LIB_PATH);
        createFolder(BIN_PATH);

        String bscriptName = BIN_PATH + File.separator + BIN_NAME;
        logger.debug("  Creating bootstrap script: " + bscriptName);
        writeBinFile(new File(bscriptName), BIN_MONITOR_CMD);
        copyJarsToPath(LIB_PATH);
    }

    private void writeBinFile(File file, String binContents) throws IOException {
        FileUtils.writeStringToFile(file, binContents);
        Files.setPosixFilePermissions(file.toPath(), PERMS_READEXEC);
    }

    @Override
    protected void createStartJob() throws IOException {
        logger.info("=--=-- Creating monitor service --=--=");

        //TODO install init script in posix
    }

    protected @NotNull UserPrincipal getUser(@NotNull String username) throws IOException {
        return FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByName(username);
    }

    protected @NotNull GroupPrincipal getGroup(@NotNull String groupname) throws IOException {
        return FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByGroupName(groupname);
    }
}
