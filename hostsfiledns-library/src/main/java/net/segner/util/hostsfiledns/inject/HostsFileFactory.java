package net.segner.util.hostsfiledns.inject;

import net.segner.util.hostsfiledns.HostsFile;

/**
 * @author aaronsegner
 */
public interface HostsFileFactory {
    HostsFile load(String location);
}
