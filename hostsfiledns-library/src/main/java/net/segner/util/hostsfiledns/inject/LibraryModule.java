package net.segner.util.hostsfiledns.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import net.segner.util.hostsfiledns.HostsDnsConfig;
import net.segner.util.hostsfiledns.HostsDnsConfigImpl;
import net.segner.util.hostsfiledns.HostsFile;
import net.segner.util.hostsfiledns.HostsFileImpl;
import net.segner.util.hostsfiledns.HostsFileUpdateService;
import net.segner.util.hostsfiledns.HostsFileUpdateServiceImpl;
import net.segner.util.hostsfiledns.Resolver;
import net.segner.util.hostsfiledns.ResolverImpl;
import org.apache.commons.lang.SystemUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class LibraryModule extends AbstractModule {
    public static final String LOCATION_HOST_WINDOWS = "\\system32\\driver\\etc\\hosts";
    public static final String LOCATION_HOST_NIX = "/etc/hosts";
    public static final String LOCATION_CONFIG_WINDOWS = "\\hostsfiledns.config";
    public static final String LOCATION_CONFIG_NIX = "/etc/hostsfiledns";

    @Override
    protected void configure() {
        // setup logging
        bindListener(Matchers.any(), new Slf4jTypeListener());

        // setup prototype bean factories
        install(new FactoryModuleBuilder()
                .implement(HostsFile.class, HostsFileImpl.class)
                .build(HostsFileFactory.class));
        install(new FactoryModuleBuilder()
                .implement(HostsDnsConfig.class, HostsDnsConfigImpl.class)
                .build(HostsDnsConfigFactory.class));

        // setup singletons
        bind(Resolver.class).to(ResolverImpl.class);
        bind(HostsFileUpdateService.class).to(HostsFileUpdateServiceImpl.class);
    }

    @Provides
    public @NotNull HostsFile hostsFile(HostsFileFactory hostsFileFactory) {
        if (SystemUtils.IS_OS_WINDOWS) {
            return hostsFileFactory.load(System.getenv("windir") + LOCATION_HOST_WINDOWS);
        } else {
            return hostsFileFactory.load(LOCATION_HOST_NIX);
        }
    }

    @Provides
    public @NotNull HostsDnsConfig hostsDnsConfig(HostsDnsConfigFactory hostsDnsConfigFactory) {
        if (SystemUtils.IS_OS_WINDOWS) {
            return hostsDnsConfigFactory.load(System.getenv("ProgramFiles") + File.separator + "hostsfiledns" + File.separator + LOCATION_CONFIG_WINDOWS);
        } else {
            return hostsDnsConfigFactory.load(LOCATION_CONFIG_NIX);
        }
    }

}
