package net.segner.util.hostsfiledns;

import javax.naming.NamingException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;

/**
 * @author aaronsegner
 */
public interface Resolver {
    Set<String> resolveIp4(String hostname) throws UnknownHostException, NamingException;
}
