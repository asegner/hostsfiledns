package net.segner.util.hostsfiledns.inject;

import com.google.inject.MembersInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class Slf4jMembersInjector<T> implements MembersInjector<T> {
    private final Field field;
    private final Logger logger;

    Slf4jMembersInjector(Field field) {
        this.field = field;
        this.logger = LoggerFactory.getLogger(field.getDeclaringClass());
        field.setAccessible(true);
    }

    public void injectMembers(T t) {
        try {
            field.set(t, logger);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
