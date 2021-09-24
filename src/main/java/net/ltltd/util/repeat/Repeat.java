/*
 *     LTLTD - Repeat Java Utility - A small Java utility repeat things.
 *     Copyright (C) 2021  Littlethunder Limited/William Dixon
 *     code@ltltd.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/gpl-3.0.txt
 */

package net.ltltd.util.repeat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A set of Utilities which repeat or sequentially execute things
 */
public final class Repeat {

    /**
     * Private constructor to prevent instantiation
     */
    private Repeat() {
    }

    /**
     * Obtain a list of specified legth which contains references to a supplied object
     *
     * @param t   the Object to fill the list
     * @param n   the length of the returned list
     * @param <T> the type of the Object repeated
     * @return a list of length n filled with references to t
     */
    public static <T> List<T> listOf(T t, int n) {
        return (n > -1) ?
            new ArrayList<>(Collections.nCopies(n, t)) : Collections.emptyList();
    }

    /**
     * Create an infinite Stream of references to a supplied Object
     *
     * @param t   the Object to stream
     * @param <T> the type of the Object to Stream
     * @return a Stream which continually provides references to t
     */
    public static <T> Stream<T> streamOf(T t) {
        return Stream.generate(() -> t);
    }

    /**
     * Call an IntConsumer with a range of Integers, starting at 0.
     *
     * @param n        upper bound, exclusive. IN the case, the number of invocations as well
     * @param consumer the IntConsumer invoked with each value in the range
     */
    public static void invokeRange(int n, IntConsumer consumer) {
        //TODO Add error handling
        invokeRange(0, n, consumer);
    }

    /**
     * Call an IntConsumer with a range of Integers, starting at lower and ending exclusively at upper.
     *
     * @param lower    lower bound inclusive
     * @param upper    upper bound, exclusive.
     * @param consumer consumer the IntConsumer invoked with each value in the range
     */
    public static void invokeRange(int lower, int upper, IntConsumer consumer) {
        //TODO Add error handling
        IntStream.range(lower, upper).forEach(consumer);
    }

    /**
     * invoke a Supplier n times, discarding the results
     *
     * @param n        the number of invocations
     * @param supplier the Supplier to obtain values to discard
     */
    public static void tossN(int n, Supplier<?> supplier) {
        //TODO Add error handling
        streamOf(supplier).limit(Math.max(n, 0)).forEach(Supplier::get);
    }

    /**
     * Invoke a Supplier n times, returning the result of the invocation in a List.
     *
     * @param n        the times to invoke the Supplier and the length of the returned List
     * @param supplier the Supplier from which to obtain values
     * @param <T>      the type of the things retrieved from the Supplier and returned in the List
     * @return a list of resulting in order of invocation results
     */
    public static <T> List<T> getN(int n, Supplier<T> supplier) {
        //TODO Add error handling
        return limitedSupplier(n, supplier).collect(Collectors.toList());
    }

    /**
     * Invoke a Supplier n times invoking the supplied Consumer with the result.
     *
     * @param n        the times to invoke the supplier and subsequently the Consumer
     * @param supplier the Supplier from which to obtain values
     * @param <T>      the type of the things retrieved from the Supplier submitted to the Consumer
     */
    public static <T> void pipeN(int n, Supplier<T> supplier, Consumer<T> consumer) {
        //TODO Add error handling
        limitedSupplier(n, supplier).forEach(consumer);
    }

    /**
     * Invoke a Supplier n times, returning the results as a Stream. An internal utility method.
     * If an invocation is
     *
     * @param n        the times to invoke the Supplier
     * @param supplier the Supplier from which to obtain values
     * @param <T>      the type of the things retrieved from the Supplier and returned in the Stream
     * @return a Stream of values in order of invocation results
     */
    private static <T> Stream<T> limitedSupplier(int n, Supplier<T> supplier) {
        return streamOf(supplier).limit(Math.max(n, 0)).map(i -> supplier.get());
    }
}
