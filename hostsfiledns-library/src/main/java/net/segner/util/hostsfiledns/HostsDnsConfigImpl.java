package net.segner.util.hostsfiledns;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import net.segner.util.hostsfiledns.inject.InjectLogger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;


public class HostsDnsConfigImpl implements HostsDnsConfig {
    private static final String DEFAULT_CONTENTS = "#\n# HOSTSFILEDNS CONFIG\n# Add hostnames, one per line, to have its DNS lookup managed in the host file and effectively bypass VPN DNS lookup\n#\ndrive.google.com\ndocs.google.com\n\n";

    @InjectLogger
    private Logger logger;

    private final String location;

    @Inject
    public HostsDnsConfigImpl(@Assisted String location) {
        this.location = location;
    }

    @Override
    public void save() throws IOException {
        File configFile = new File(location);
        if (!configFile.exists()) {
            logger.debug("  Creating default configuration");
            FileUtils.writeStringToFile(configFile, DEFAULT_CONTENTS);
        }
    }

    @Override
    public @NotNull Set<String> load() throws IOException {
        HashSet<String> result = new HashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get(location))) {
            stream.forEach(line -> {
                String lineStr = StringUtils.defaultString(line);
                if (!StringUtils.startsWith(lineStr, "#")) { //ignore comment lines
                    result.add(lineStr);
                }
            });
        }
        return result;
    }

}
