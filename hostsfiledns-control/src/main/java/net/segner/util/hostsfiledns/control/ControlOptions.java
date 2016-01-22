package net.segner.util.hostsfiledns.control;

import org.apache.commons.cli.Options;

public class ControlOptions extends Options {

    public static final String OPTION_INSTALL = "install";
    public static final String OPTION_HELP = "help";

    public ControlOptions() {
        addOption("i", OPTION_INSTALL, false, "Installs the HostsfileDNS service on this machine");
        addOption("?", OPTION_HELP, false, "Displays this help message");
    }
}
