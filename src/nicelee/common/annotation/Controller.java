package nicelee.common.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface Controller {
	
	String path() default "";
	
	String note();// default "";
	
}
