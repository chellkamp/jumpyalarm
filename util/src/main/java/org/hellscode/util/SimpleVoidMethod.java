package org.hellscode.util;

/**
 * Represents a single-argument method with no return type
 * @param <T> argument type
 */
public interface SimpleVoidMethod<T> {
    /**
     * Method being emulated
     * @param arg argument
     */
    void run(T arg);
}
