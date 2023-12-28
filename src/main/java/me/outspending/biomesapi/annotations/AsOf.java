package me.outspending.biomesapi.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to indicate the version or date since the annotated element has been present or modified.
 * It is typically used for API documentation to inform users about the lifecycle of the API elements.
 *
 * Usage:
 * <pre>
 * {@code
 * @AsOf("1.0")
 * public void someApiMethod() {
 *     //...
 * }
 * }
 * </pre>
 *
 * In the above example, the {@code someApiMethod} method has been present or modified since version 1.0.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AsOf {

    /**
     * Returns the version or date since the annotated element has been present or modified.
     *
     * @return A string representing the version or date.
     */
    String value();

}
