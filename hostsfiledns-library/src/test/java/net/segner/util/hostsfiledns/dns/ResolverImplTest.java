package net.segner.util.hostsfiledns.dns;

import net.segner.util.hostsfiledns.Resolver;
import net.segner.util.hostsfiledns.ResolverImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import java.net.UnknownHostException;
import java.util.Set;

import static org.hamcrest.Matchers.greaterThan;

/**
 * @author aaronsegner
 */
public class ResolverImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResolverImplTest.class);
    public Resolver resolver;

    @Before
    public void before() throws NamingException {
        resolver = new ResolverImpl(); //TODO test with a guice test runner and then make constructor private(jukito?)
    }

    @Test
    public void resolveIp4() throws UnknownHostException, NamingException {
        String testHost = "drive.google.com";
        LOGGER.info("Resolving " + testHost);
        Set<String> ips = resolver.resolveIp4(testHost);

        Assert.assertNotNull(ips);
        Assert.assertThat("At least one ip resolved", ips.size(), greaterThan(0));

        ips.forEach(ip -> LOGGER.info(ip));

    }

    @Test
    public void resolveIp_emptyNotNull() throws UnknownHostException, NamingException {
        Set<String> ips = resolver.resolveIp4("");
        Assert.assertNotNull(ips);
    }
}
