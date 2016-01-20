package net.segner.util.hostsfiledns;

import java.io.IOException;
import java.util.Map;

/**
 * @author aaronsegner
 */
public interface HostsFile {
    String DELIMITER_START="##**##**##**## HOSTSFILEDNS AUTO-GENERATED - DO NOT EDIT BETWEEN (BELOW) THESE LINES ##**##**##**##";
    String DELIMITER_STOP="##**##**##**## HOSTSFILEDNS AUTO-GENERATED - DO NOT EDIT BETWEEN (ABOVE) THESE LINES ##**##**##**##";

    /**
     * Removes a hostname from the host file
     */
    void mergeIpToHostMap(Map<String, String> ipToHostMap) throws IOException;

    /**
     * @return file system path to the host file
     */
    String getPath();
}
