package net.segner.util.hostsfiledns.monitor.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import net.segner.util.hostsfiledns.inject.LibraryModule;
import net.segner.util.hostsfiledns.monitor.MonitorApplicationMode;
import net.segner.util.hostsfiledns.monitor.MonitorOptions;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorModule extends AbstractModule {
    private static final Logger logger = LoggerFactory.getLogger(MonitorModule.class);

    private CommandLine commandLine;
    private final String[] args;

    public MonitorModule(String[] args) {
        this.args = args;
    }

    @Override
    protected void configure() {
        install(new LibraryModule());
    }

    /**
     * Parses command line arguments and sets the application mode
     *
     * @return ApplicationMode enum representing runtime mode
     */
    @Provides
    private @NotNull MonitorApplicationMode applicationMode() {
        try {
            parseCommandLine();
            if (commandLine.hasOption(MonitorOptions.OPTION_UPDATE)) {
                return MonitorApplicationMode.UPDATE;
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug(ExceptionUtils.getStackTrace(e));
            } else {
                logger.error(e.getMessage());
            }
            return MonitorApplicationMode.HELP;
        }
        return MonitorApplicationMode.HELP;
    }

    @Provides
    private @NotNull boolean isQuiet() {
        try {
            parseCommandLine();
            return commandLine.hasOption(MonitorOptions.OPTION_QUIET);
        } catch (Exception e) {
            // exceptions in command line parsing will be handled by the application mode block and will cause HELP application mode
        }
        return false;
    }

    private synchronized void parseCommandLine() throws ParseException {
        if (commandLine == null) {
            CommandLineParser parser = new DefaultParser();
            commandLine = parser.parse(new MonitorOptions(), args);
        }
    }

}
