package net.segner.util.hostsfiledns.control;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import net.segner.util.hostsfiledns.control.inject.ControlModule;
import net.segner.util.hostsfiledns.control.setup.InstallerService;
import net.segner.util.hostsfiledns.inject.InjectLogger;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;

import java.io.IOException;

@Singleton
public class ControlApplication {
    @InjectLogger
    private Logger logger;
    @Inject
    private ControlApplicationMode controlApplicationMode;
    @Inject
    private InstallerService installerService;

    public void start() {
        switch (controlApplicationMode) {
            case INSTALL:
                try {
                    installerService.install();
                } catch (IOException e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(ExceptionUtils.getStackTrace(e));
                    } else {
                        logger.error(e.getMessage());
                    }
                    printHelp();
                }
                break;

            case HELP:
                printHelp();
                break;
        }
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar hostsfiledns-control", new ControlOptions(), true);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ControlModule(args));
        ControlApplication application = injector.getInstance(ControlApplication.class);
        application.start();
    }
}
