package it.codingjam.lifecyclebinder;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LifeCycleAware {
    boolean retained() default false;

    String name() default "";
}
