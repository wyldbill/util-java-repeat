/*
 *     LTLTD - Repeat Java Utility - A small Java library for repeating things.
 *     Copyright (C) 2021  Littlethunder Limited/William Dixon
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
    void listOfTest() {
        assertEquals(5, listOf(FOO, 5).size()); //simple case
        assertEquals(5,listOf(FOO, 5).stream().filter(s -> s == FOO).count());
        assertTrue(listOf(FOO, 0).isEmpty());   //check 0 n parameter
        assertTrue(listOf(FOO, -1).isEmpty());  //check negative parameter
        assertEquals(4, listOf(null, 4).size());    //check making list of nulls
        assertEquals(4,listOf(null, 4).stream().filter(Objects::isNull).count());
    }

    @SuppressWarnings("StringEquality")
    @Test
    void streamOfTest() {
        Stream<String> s = streamOf(FOO);
        assertNotNull(s);
        assertSame(FOO, streamOf(FOO).findFirst().orElse(null));
        assertEquals(0, streamOf(FOO).limit(200).filter(f -> FOO != f).count());
        assertEquals(0, streamOf(null).limit(200).filter(Objects::nonNull).count());
        assertEquals(200, streamOf(null).limit(200).filter(Objects::isNull).count());
    }

    @Test
    void invokeRangeUpperLowerTest() {
        List<Integer> list = new ArrayList<>();
        invokeRange(0, 5, list::add);
        assertEquals(List.of(0, 1, 2, 3, 4), list);
        list.clear();
        invokeRange(0, 0, list::add);
        assertEquals(EMPTY_LIST, list);
        list.clear();
        invokeRange(2, 5, list::add);
        assertEquals(List.of(2, 3, 4), list);
        list.clear();
        invokeRange(-2, 5, list::add);
        assertEquals(List.of(-2, -1, 0, 1, 2, 3, 4), list);
        list.clear();
        invokeRange(-5, -2, list::add);
        assertEquals(List.of(-5, -4, -3), list);
        list.clear();
        invokeRange(3,2,list::add);
        assertEquals(EMPTY_LIST, list);
        invokeRange(0, 0, list::add);
        assertEquals(EMPTY_LIST, list);
        invokeRange(3, 3, list::add);
        assertEquals(EMPTY_LIST, list);
        invokeRange(1, 4, null);
    }

    @Test
    void invokeRangeTest() {
        List<Integer> list = new ArrayList<>();
        invokeRange(5, list::add);
        assertEquals(List.of(0, 1, 2, 3, 4), list);
        list.clear();
        invokeRange(0, list::add);
        assertEquals(EMPTY_LIST, list);
        invokeRange(-5, list::add);
        assertEquals(EMPTY_LIST, list);
        invokeRange(3, null);
        assertEquals(EMPTY_LIST, list);
    }

    @Test
    void tossNTest() {
        AtomicInteger i = new AtomicInteger();
        tossN(5, i::incrementAndGet);
        assertEquals(5, i.get());
        tossN(0, i::incrementAndGet);
        assertEquals(5, i.get());
        tossN(-1, i::incrementAndGet);
        assertEquals(5, i.get());
        tossN(-1, null);
        assertEquals(5, i.get());
    }

    @Test
    void getNTest() {
        AtomicInteger i = new AtomicInteger();
        assertEquals(List.of(1, 2, 3, 4, 5), getN(5, i::incrementAndGet));
        assertEquals(List.of(6), getN(1, i::incrementAndGet));
        assertEquals(EMPTY_LIST, getN(0, i::incrementAndGet));
        assertEquals(EMPTY_LIST, getN(-1, i::incrementAndGet));
        assertEquals(EMPTY_LIST, getN(4, null));
    }

    @Test
    void pipeNTest() {
        AtomicInteger i = new AtomicInteger();
        List<Integer> list = new ArrayList<>();
        Consumer<Integer> c = list::add;
        //first invocation
        List<Integer> resList = List.of(1, 2, 3, 4, 5);
        pipeN(5, i::incrementAndGet, c);
        assertEquals(resList, list);
        assertEquals(5,i.get());
        // next invocation
        resList = List.of(1, 2, 3, 4, 5,6);
        pipeN(1, i::incrementAndGet, c);
        assertEquals(resList, list);
        assertEquals(6,i.get());
        // no invocations
        pipeN(0, i::incrementAndGet, c);
        assertEquals(resList, list);
        assertEquals(6,i.get());
        // negative invocations
        pipeN(-1, i::incrementAndGet, c);
        assertEquals(resList, list);
        assertEquals(6,i.get());
        // null supplier
        pipeN(3, null, c);
        assertEquals(resList, list);
        assertEquals(6,i.get());
        // null consumer
        pipeN(3, i::incrementAndGet, null);
        assertEquals(resList, list);
        assertEquals(6,i.get());
    }
}