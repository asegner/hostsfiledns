package net.segner.util.hostsfiledns.monitor;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import net.segner.util.hostsfiledns.HostsFileUpdateService;
import net.segner.util.hostsfiledns.inject.InjectLogger;
import net.segner.util.hostsfiledns.monitor.inject.MonitorModule;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;

import java.io.IOException;

@Singleton
public class MonitorApplication {
    @InjectLogger
    private Logger logger;
    @Inject
    private HostsFileUpdateService hostsFileUpdateService;
    @Inject
    private MonitorApplicationMode applicationMode;

    public void start() {
        switch (applicationMode) {
            case UPDATE:
                try {
                    logger.info("HostsFileDNS :: Checking DNS updates");
                    hostsFileUpdateService.update();
                    logger.info("HostsFileDNS :: Update Complete");
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
        formatter.printHelp("hostsfiledns", new MonitorOptions(), true);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MonitorModule(args));
        MonitorApplication application = injector.getInstance(MonitorApplication.class);
        application.start();
    }
}
