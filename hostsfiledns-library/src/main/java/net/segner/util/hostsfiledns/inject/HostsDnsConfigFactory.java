package net.segner.util.hostsfiledns.inject;

import net.segner.util.hostsfiledns.HostsDnsConfig;

public interface HostsDnsConfigFactory {
    HostsDnsConfig load(String location);
}
