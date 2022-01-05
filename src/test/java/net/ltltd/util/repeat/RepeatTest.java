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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Collections.EMPTY_LIST;
import static net.ltltd.util.repeat.Repeat.*;
import static org.junit.jupiter.api.Assertions.*;

class RepeatTest {

    private static final String FOO = "foo";

    /**
     * Test Repeat listOf function
     */
    @SuppressWarnings("StringEquality")
    @Test
    void testListOf() {
        assertEquals(5, listOf(FOO, 5).size(), "listOf: returns list with n size");
        assertEquals(5, listOf(FOO, 5).stream().filter(s -> s == FOO).count(), "listOf:returns list containes only t");
        assertTrue(listOf(FOO, 0).isEmpty(),"listOf: returns empty list with 0 size");
        assertTrue(listOf(FOO, -1).isEmpty(),"listOf: returns empty list with size < 0");
        assertEquals(4, listOf(null, 4).size(), "listOf: returns correct  size list for t of null");
        assertEquals(4, listOf(null, 4).stream().filter(Objects::isNull).count(),
                "listOf: returns list of all null for t of null");
    }

    @SuppressWarnings("StringEquality")
    @Test
    void testStreamOf() {
        Stream<String> s = streamOf(FOO);
        assertNotNull(s,"streamOf: Stream returned is not null");
        assertSame(FOO, streamOf(FOO).findFirst().orElse(null),"streamOf: Stream contains correct references");
        assertEquals(0, streamOf(FOO).limit(200).filter(f -> FOO != f).count(),"streamOf: Stream contains only t");
        assertEquals(0, streamOf(null).limit(200).filter(Objects::nonNull).count(),"streamOf: null t returns Stream of null");
        assertEquals(200, streamOf(null).limit(200).filter(Objects::isNull).count(),"streamOf: null t returns Stream of null");
    }

    @Test
    void testInvokeRangeUpperLower() {
        List<Integer> list = new ArrayList<>();
        invokeRange(0, 5, list::add);
        assertEquals(List.of(0, 1, 2, 3, 4), list, "testInvokeRangeUpperLower: Consumer invoked with ints 0..4");
        list.clear();
        invokeRange(0, 0, list::add);
        assertEquals(EMPTY_LIST, list,"testInvokeRangeUpperLower: Consumer invoked with ints bounds equal = 0");
        list.clear();
        invokeRange(2, 5, list::add);
        assertEquals(List.of(2, 3, 4), list,"testInvokeRangeUpperLower: Consumer invoked with lower bound !=0");
        list.clear();
        invokeRange(-2, 5, list::add);
        assertEquals(List.of(-2, -1, 0, 1, 2, 3, 4), list,"testInvokeRangeUpperLower: Consumer invoked with lower bound < 0");
        list.clear();
        invokeRange(-5, -2, list::add);
        assertEquals(List.of(-5, -4, -3), list,"testInvokeRangeUpperLower: Consumer invoked with upper and lower bound < 0");
        list.clear();
        invokeRange(3, 2, list::add);
        assertEquals(EMPTY_LIST, list,"testInvokeRangeUpperLower: returns empty when upper < lower bound");
         invokeRange(3, 3, list::add);
        assertEquals(EMPTY_LIST, list,"testInvokeRangeUpperLower: Consumer not invoked when bounds are equal");
        invokeRange(1, 4, null);
        assertEquals(EMPTY_LIST, list,"testInvokeRangeUpperLower: null consumer fails gracefully");

    }

    @Test
    void testInvokeRange() {
        List<Integer> list = new ArrayList<>();
        invokeRange(5, list::add);
        assertEquals(List.of(0, 1, 2, 3, 4), list, "testInvokeRange: Consumer invoked with ints 0..4");
        list.clear();
        invokeRange(0, list::add);
        assertEquals(EMPTY_LIST, list, "testInvokeRange: Consumer not invoked for n = 0");
        invokeRange(-5, list::add);
        assertEquals(EMPTY_LIST, list,"testInvokeRange: Consumer not invoked for n < 0");
        invokeRange(3, null);
        assertEquals(EMPTY_LIST, list, "testInvokeRange: null Consumer fails gracefully");
    }

    @Test
    void testTossN() {
        AtomicInteger i = new AtomicInteger();
        tossN(5, i::incrementAndGet);
        assertEquals(5, i.get(),"testTossN: ensure supplier invoked n times");
        tossN(0, i::incrementAndGet);
        assertEquals(5, i.get(),"testTossN: ensure supplier not invoked for n=0");
        tossN(-1, i::incrementAndGet);
        assertEquals(5, i.get(),"testTossN: ensure supplier not invoked for n<0");
        tossN(-1, null);
        assertEquals(5, i.get(),"testTossN: ensure supplier = null fails gracefully");
    }

    @Test
    void testGetN() {
        AtomicInteger i = new AtomicInteger();
        assertEquals(List.of(1, 2, 3, 4, 5), getN(5, i::incrementAndGet),
                "testGetN: return of n supplier invocations");
        assertEquals(List.of(6), getN(1, i::incrementAndGet),"testGetN: return of additional supplier invocations");
        assertEquals(EMPTY_LIST, getN(0, i::incrementAndGet),"testGetN: empty return for n = 0");
        assertEquals(EMPTY_LIST, getN(-1, i::incrementAndGet),"testGetN: empty return for n < 0");
        assertEquals(EMPTY_LIST, getN(4, null),"testGetN: empty return null supplier");
    }

    @Test
    void testPipeN() {
        AtomicInteger i = new AtomicInteger();
        List<Integer> list = new ArrayList<>();
        Consumer<Integer> c = list::add;
        //first invocation
        List<Integer> resList = List.of(1, 2, 3, 4, 5);
        pipeN(5, i::incrementAndGet, c);
        assertEquals(resList, list, "testPipeN: Values piped from supplier received correctly");
        assertEquals(5, i.get(), "testPipeN: Ensure supplier invoked n times");
        // next invocation
        resList = List.of(1, 2, 3, 4, 5, 6);
        pipeN(1, i::incrementAndGet, c);
        assertEquals(resList, list,"testPipeN: Values piped from supplier received correctly");
        assertEquals(6, i.get(),"testPipeN: Ensure supplier invoked n times");
        // no invocations
        pipeN(0, i::incrementAndGet, c);
        assertEquals(resList, list,"testPipeN: Ensure supplier invoked 0 times produces nothing");
        assertEquals(6, i.get(),"testPipeN: Ensure supplier invoked 0 times consumes nothing");
        // negative invocations
        pipeN(-1, i::incrementAndGet, c);
        assertEquals(resList, list,"testPipeN: Ensure supplier invoked -1 times produces nothing");
        assertEquals(6, i.get(),"testPipeN: Ensure supplier invoked -1 times consums nothing");
        // null supplier
        pipeN(3, null, c);
        assertEquals(resList, list,"testPipeN: Ensure null supplier param produces nothing");
        assertEquals(6, i.get(),"testPipeN: Ensure null supplier param consumes nothing");
        // null consumer
        pipeN(3, i::incrementAndGet, null);
        assertEquals(resList, list,"testPipeN: Ensure null consumer param produces nothing");
        assertEquals(6, i.get(),"testPipeN: Ensure null consumer param consumes nothing");
    }
}