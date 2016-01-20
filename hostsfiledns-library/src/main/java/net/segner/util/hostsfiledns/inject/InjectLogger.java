package net.segner.util.hostsfiledns.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Documented
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectLogger {
}
