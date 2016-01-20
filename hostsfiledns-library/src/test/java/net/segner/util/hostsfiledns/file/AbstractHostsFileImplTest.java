package net.segner.util.hostsfiledns.file;

import net.segner.util.hostsfiledns.HostsFile;
import net.segner.util.hostsfiledns.HostsFileImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author aaronsegner
 */
public abstract class AbstractHostsFileImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHostsFileImplTest.class);

    private String resourcePath;
    protected HostsFile hostsFile;
    private Path tempFile;

    AbstractHostsFileImplTest(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Test
    public void testGetLocation() {
        Assert.assertEquals(tempFile.toAbsolutePath().toString(), hostsFile.getPath());
    }

    @Before
    public void setup() throws IOException {
        tempFile = Files.createTempFile("hostsfiledns", "hosts");
        LOGGER.info("Copied " + resourcePath + " to temp file: " + tempFile.toAbsolutePath().toString());

        Files.copy(getClass().getResourceAsStream(resourcePath), tempFile, StandardCopyOption.REPLACE_EXISTING);
        hostsFile = new HostsFileImpl(tempFile.toAbsolutePath().toString());
    }

    @After
    public void tearDown() throws IOException {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Removing temp file: " + tempFile.toAbsolutePath().toString());
            }
            Files.delete(tempFile);
        } finally {
            if (Files.exists(tempFile)) {
                LOGGER.warn("***FAILED TO REMOVE TEMP FILE***");
            } else {
                LOGGER.info("Temp file removed");
            }
        }
    }
}
