package org.hellscode.util;

/**
 * Represents a single-argument method with a non-void return type
 * @param <T> argument type
 * @param <R> return type
 */
public interface SimpleMethod<T,R> {
    /**
     * Method being emulated
     * @param arg argument
     * @return result of the method
     */
    R run(T arg);
}
