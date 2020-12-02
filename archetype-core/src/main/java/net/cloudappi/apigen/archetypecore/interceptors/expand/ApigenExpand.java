package net.cloudappi.apigen.archetypecore.interceptors.expand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApigenExpand {
    public String[] allowed() default {};
    public String[] excluded() default {};
    public int maxLevel() default -1;

}
