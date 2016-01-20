package net.segner.util.hostsfiledns.monitor;

import org.apache.commons.cli.Options;

public class MonitorOptions extends Options {
    public static final String OPTION_QUIET = "quiet";
    public static final String OPTION_UPDATE = "update";
    public static final String OPTION_HELP = "help";

    public MonitorOptions() {
        addOption("q", OPTION_QUIET, false, "Quiet mode. Suppress informational messages and only display errors and critical warnings");
        addOption("u", OPTION_UPDATE, false, "Update DNS for hosts in hosts file.");
        addOption("?", OPTION_HELP, false, "Displays this help message");
    }
}
