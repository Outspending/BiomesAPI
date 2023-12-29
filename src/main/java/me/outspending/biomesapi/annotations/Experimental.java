package me.outspending.biomesapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark elements that are experimental.
 * Experimental elements are those that are not yet stable and may change in the future.
 * This annotation can be applied to methods, types, constructors, modules, and packages.
 *
 * @version 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD,
        ElementType.TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.MODULE,
        ElementType.PACKAGE
})
@AsOf("0.0.1")
public @interface Experimental {
}