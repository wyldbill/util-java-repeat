/*
 *     LTLTD - Repeat Java Utility - A small Java library for repeating things.
 *     Copyright (C) 2021 Littlethunder Limited/William Dixon
 *     code@ltltd.net
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
 * A small library for repeating or sequentially executing things
 */
public final class Repeat {

    /**
     * Private constructor to prevent instantiation
     */
    private Repeat() {
    }

    /**
     * Obtain a list of specified length which contains references to a supplied object<pre>{@code
     *     // Create a Stream repeating "FOO"
     *     Stream<String> s1 = Repeat.StreamOf("FOO");}</pre>
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
     * Create an infinite Stream of references to a supplied Object<pre>{@code
     *     // Create a Stream repeating "FOO"
     *     Stream<String> s1 = Repeat.StreamOf("FOO");
     *
     *     // Create a Stream of Suppliers<String> representing the times
     *     // when the String is obtained from the Stream
     *     Stream<Supplier<String>> s2 = Repeat.streamOf(()-> Instant.now().toString()); }</pre>
     *
     * @param t   the Object to stream
     * @param <T> the type of the Object to Stream
     * @return a Stream which continually provides references to t. t may be null.
     */
    public static <T> Stream<T> streamOf(T t) {
        return Stream.generate(() -> t);
    }

    /**
     * Call an IntConsumer with a range of Integers, starting at 0.<pre>{@code
     *     //Print the ints from 0 to 10
     *     Repeat.invokeRange(11, System.out::print);}</pre>
     * @param n        upper bound, exclusive. In this case, the number of invocations as well
     * @param consumer the IntConsumer invoked with each value in the range
     */
    public static void invokeRange(int n, IntConsumer consumer) {
        invokeRange(0, n, consumer);
    }

    /**
     * Call an IntConsumer with a range of Integers, starting at lower and ending exclusively at upper.
     * If upper &lt;= lower, consumer will not be invoked.<pre>{@code
     *     //Print the ints from 5 to 10
     *     Repeat.invokeRange(5, 11, System.out::print);}</pre>
     *
     * @param lower    lower bound, inclusive
     * @param upper    upper bound, exclusive.
     * @param consumer the IntConsumer invoked sequentially with each value in the range
     */
    public static void invokeRange(int lower, int upper, IntConsumer consumer) {
        if (consumer == null) {
            return;   //nothing to do
        }
        IntStream.range(lower, upper).forEach(consumer);
    }

    /**
     * Invoke a Supplier n times, discarding the results. If supplier is null, no operations are performed.<pre>{@code
     *     //Pop 10 items off a Deque
     *     Repeat.tossN(10, deque::pop);}</pre>
     *
     * @param n        the number of invocations
     * @param supplier the Supplier to obtain values to discard
     */
    public static void tossN(int n, Supplier<?> supplier) {
        if (supplier == null) {
            return; //nothing to do
        }
        streamOf(supplier).limit(Math.max(n, 0)).forEach(Supplier::get);
    }

    /**
     * Invoke a Supplier n times, returning the result of the invocations in a List. If Supplier is null
     * an empty List is returned.
     * <pre>{@code
     *     //get the first 5 items from a Deque
     *     List<String> firstFive = Repeat.getN(5,deque::pop);}</pre>
     *
     * @param n        the times to invoke the Supplier and the length of the returned List
     * @param supplier the Supplier from which to obtain values
     * @param <T>      the type of the things retrieved from the Supplier and returned in the List
     * @return a list of resulting in order of invocation results
     */
    public static <T> List<T> getN(int n, Supplier<T> supplier) {
        if (supplier == null) {
            return Collections.emptyList();
        }
        return limitedSupplier(n, supplier).collect(Collectors.toList());
    }

    /**
     * Invoke a Supplier n times invoking the supplied Consumer with each result. If supplier or consumer is null
     * no invocations of supplier or consumer occur.
     * <pre>{@code
     *     //print the first 10 items from a Deque
     *     Repeat.pipeN(10,deque::pop, System.out::println);}</pre>
     *
     * @param n        the times to invoke the supplier and subsequently the Consumer
     * @param supplier the Supplier from which to obtain values
     * @param consumer the Consumer to which to provide values
     * @param <T>      the type of the things retrieved from the Supplier submitted to the Consumer
     */
    public static <T> void pipeN(int n, Supplier<? extends T> supplier, Consumer<T> consumer) {
        if (consumer == null) {
            return; //nothing to do
        }
        limitedSupplier(n, supplier).forEach(consumer);
    }

    /**
     * Invoke a Supplier n times, returning the results as a Stream. An internal utility method.
     *
     * @param n        the times to invoke the Supplier. if <= 0 the Stream will be empty
     * @param supplier the Supplier from which to obtain values
     * @param <T>      the type of the things retrieved from the Supplier and returned in the Stream
     * @return a Stream of values in order of invocation results
     */
    private static <T> Stream<T> limitedSupplier(int n, Supplier<T> supplier) {
        if (supplier == null) {
            return Stream.of();
        }
        return streamOf(supplier).limit(Math.max(n, 0)).map(Supplier::get);
    }
}
