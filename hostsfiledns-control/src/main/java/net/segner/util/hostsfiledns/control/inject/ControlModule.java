package net.segner.util.hostsfiledns.control.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import net.segner.util.hostsfiledns.control.ControlApplicationMode;
import net.segner.util.hostsfiledns.control.ControlOptions;
import net.segner.util.hostsfiledns.control.setup.InstallerService;
import net.segner.util.hostsfiledns.control.setup.OsxInstallerService;
import net.segner.util.hostsfiledns.control.setup.PosixInstallerService;
import net.segner.util.hostsfiledns.control.setup.WindowsInstallerService;
import net.segner.util.hostsfiledns.inject.LibraryModule;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControlModule extends AbstractModule {
    private static final Logger logger = LoggerFactory.getLogger(ControlModule.class);

    private final String[] args;

    public ControlModule(String[] args) {
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
    private @NotNull ControlApplicationMode applicationMode() {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(new ControlOptions(), args);

            if (commandLine.hasOption(ControlOptions.OPTION_INSTALL)) {
                return ControlApplicationMode.INSTALL;
            } else {
                return ControlApplicationMode.HELP;
            }

        } catch (ParseException pe) {
            logger.error(pe.getMessage());
            return ControlApplicationMode.HELP;
        }
    }

    /**
     * @return OS-dependent installer service to manage application installs
     */
    @Provides
    private @NotNull InstallerService installerService(Provider<WindowsInstallerService> windowsInstallerServiceProvider, Provider<PosixInstallerService> posixInstallerServiceProvider, Provider<OsxInstallerService> osxInstallerServiceProvider) {
        if (SystemUtils.IS_OS_WINDOWS) {
            return windowsInstallerServiceProvider.get();
        } else if (SystemUtils.IS_OS_LINUX) {
            return posixInstallerServiceProvider.get();
        } else if (SystemUtils.IS_OS_MAC_OSX) {
            return osxInstallerServiceProvider.get();
        }

        // must not be a supported operating system, let's stop here
        String msg = "Unsupported operating system: " + SystemUtils.OS_NAME;
        logger.error(msg);
        throw new IllegalArgumentException(msg);
    }
}
