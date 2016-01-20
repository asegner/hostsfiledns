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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HostsFileImpl implements HostsFile {
    @InjectLogger
    private Logger logger;

    private final String location;
    private boolean isEnhanced = false;
    private final List<String> content = new ArrayList<>();
    private final List<String> managedContent = new ArrayList<>();

    @Inject
    public HostsFileImpl(@Assisted @NotNull String location) throws IOException {
        this.location = location;

        // check to see if we have already modified this file
        // even large hosts files are relatively small so load into memory for easier processing
        try (Stream<String> stream = Files.lines(Paths.get(location))) {
            stream.forEach(line -> {
                if (StringUtils.startsWith(line, DELIMITER_START)) {
                    isEnhanced = true;
                }
                content.add(StringUtils.trim(line));
            });
        }

        // break our managed section out of the host file
        if (isEnhanced) {
            int startIndex = content.indexOf(DELIMITER_START);
            int stopIndex = content.indexOf(DELIMITER_STOP);
            if (startIndex < 0 || stopIndex < 0) {
                throw new IllegalStateException("Corrupt HostsFileDNS block - aborting");
            }
            managedContent.addAll(content.subList(startIndex + 1, stopIndex));
            managedContent.forEach(line -> content.remove(startIndex));
            content.remove(startIndex);
            content.remove(startIndex);
        }
    }

    public @NotNull List<String> getManagedContent() {
        return managedContent;
    }

    @Override
    public void mergeIpToHostMap(@NotNull Map<String, String> ipToHostMap) throws IOException {
        // Separate the bad resolves
        List<String> notResolvedHosts = (ipToHostMap.entrySet().stream())
                .filter(e -> e.getValue() == null)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        notResolvedHosts.forEach(ipToHostMap::remove);

        // remove invalid hosts and update existing hosts referenced in file
        final List<String> middleman = getManagedContent().stream()
                .filter(line -> StringUtils.startsWith(line, "#") || isValidHostOnLine(Arrays.asList(StringUtils.split(line)), ipToHostMap.keySet(), notResolvedHosts))
                .map(line -> updateLine(Arrays.asList(StringUtils.split(line)), ipToHostMap))
                .collect(Collectors.toList());

        // determine and add new hosts
        middleman.stream()
                .filter(line -> StringUtils.isNotBlank(line) && !StringUtils.startsWith(line, "#"))
                .forEach(line -> cleanHostsFromMap(line, ipToHostMap));
        ipToHostMap.forEach((key, value) -> {
            logger.debug("Adding: " + key + " with " + value);
            middleman.add(value + "\t" + key);
        });

        // merge managed content back with hosts file
        for (int i = content.size() - 1; i > 0; i--) {
            if (StringUtils.isBlank(content.get(i))) {
                content.remove(i);
            }
        }
        content.add(StringUtils.EMPTY);
        content.add(StringUtils.EMPTY);
        content.add(DELIMITER_START);
        content.addAll(middleman);
        content.add(DELIMITER_STOP);
        content.add(StringUtils.EMPTY);
        FileUtils.writeLines(new File(location), content);
    }

    private void cleanHostsFromMap(@NotNull String line, @NotNull Map<String, String> ipToHostMap) {
        for (String host : StringUtils.split(line)) {
            ipToHostMap.remove(host);
        }
    }

    private @NotNull String updateLine(List<String> splitLine, Map<String, String> resolvedHosts) {
        for (int i = 1; i < splitLine.size(); i++) {
            String value = splitLine.get(i);
            if (resolvedHosts.containsKey(value)) {
                String newIp = resolvedHosts.get(value);
                logger.debug("Updating: " + value + " with " + newIp);
                splitLine.set(0, newIp);
                break;
            }
        }
        return StringUtils.join(splitLine.toArray(), "\t");
    }

    private boolean isValidHostOnLine(List<String> splitLine, Collection<String> resolvedHosts, Collection<String> notResolvedHosts) {
        for (int i = 1; i < splitLine.size(); i++) {
            String value = splitLine.get(i);
            if (resolvedHosts.contains(value) || notResolvedHosts.contains(value)) {
                return true;
            }
        }
        logger.debug("Removing line: " + StringUtils.join(splitLine.toArray(), " "));
        return false;
    }

    public @NotNull String getPath() {
        return location;
    }

}
