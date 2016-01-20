package net.segner.util.hostsfiledns;

import com.google.inject.Singleton;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Singleton
public class ResolverImpl implements Resolver {
    private static final Pattern pattern = Pattern.compile("(^127\\.0\\.0\\.1)|(^10\\.)|(^172\\.1[6-9]\\.)|(^172\\.2[0-9]\\.)|(^172\\.3[0-1]\\.)|(^1‌​92\\.168\\.)");
    private InitialDirContext idc;

    public ResolverImpl() throws NamingException {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
        env.put("com.sun.jndi.dns.timeout.initial", "9000");
        env.put("com.sun.jndi.dns.timeout.retries", "1");
        idc = new InitialDirContext(env);
    }

    public @NotNull Set<String> resolveIp4(String hostname) throws UnknownHostException, NamingException {
        Set<String> ips = new HashSet<>();
        if (StringUtils.isNotBlank(hostname)) {
            ips = resolveHostDns(hostname);
            ips = ips.stream()
                    .filter(ip -> {
                        Matcher matcher = pattern.matcher(ip);
                        return !matcher.find();
                    }).collect(Collectors.toSet());
        }
        return ips;
    }

    private @NotNull Set<String> resolveHostDns(String hostname) throws NamingException {
        Set<String> results = new HashSet<>();
        Attributes attrs = idc.getAttributes(hostname, new String[]{"A"});
        if (attrs.size() <= 0) return results;
        Attribute dnsResult = attrs.get("A");
        if (dnsResult.size() <= 0) return results;
        NamingEnumeration<?> dnsEnumeration = dnsResult.getAll();
        while (dnsEnumeration.hasMore()) {
            results.add(dnsEnumeration.next().toString());
        }
        return results;
    }
}
