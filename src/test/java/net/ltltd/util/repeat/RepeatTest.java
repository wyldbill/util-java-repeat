package net.ltltd.util.repeat;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
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
    @Test
    void listOfTest() {
        List<String> list = listOf(FOO, 5);
        assertEquals(5, list.size()); //simple case
        for (String s : list) {
            assertSame(s, FOO); //check instance is the same
        }
        list = listOf(FOO, 0);  //check 0 n parameter
        assertTrue(list.isEmpty());
        list = listOf(FOO, -1); ///check negative parameter
        assertTrue(list.isEmpty());
        list = listOf(null, 4); //check making list of nulls
        assertEquals(4, list.size());
        for (String s : list) {
            assertNull(s);
        }
    }

    @SuppressWarnings("StringEquality")
    @Test
    void streamOfTest() {
        Stream<String> s = streamOf(FOO);
        assertNotNull(s);
        assertSame(FOO, streamOf(FOO).findFirst().orElse(null));
        assertEquals(0, streamOf(FOO).limit(200).filter(f -> FOO != f).count());
        assertEquals(0, streamOf(null).limit(200).filter(f -> f != null).count());
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
        invokeRange(5, 0, list::add);
        assertEquals(EMPTY_LIST, list);
    }

    @Test
    void testInvokeRangeTest() {
        List<Integer> list = new ArrayList<>();
        invokeRange(5, list::add);
        assertEquals(List.of(0, 1, 2, 3, 4), list);
        list.clear();
        invokeRange(0, list::add);
        assertEquals(EMPTY_LIST, list);
        list.clear();
        invokeRange(5, list::add);
        assertEquals(List.of(0, 1, 2, 3, 4), list);
        list.clear();
        invokeRange(-5, list::add);
        assertEquals(EMPTY_LIST, list);
        list.clear();
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
    }

    @Test
    void getNTest() {
        AtomicInteger i = new AtomicInteger();
        assertEquals(List.of(1, 2, 3, 4, 5), getN(5, i::incrementAndGet));
        assertEquals(List.of(6), getN(1, i::incrementAndGet));
        assertEquals(EMPTY_LIST, getN(0, i::incrementAndGet));
        assertEquals(EMPTY_LIST, getN(-1, i::incrementAndGet));
    }

    @Test
    void pipeNTest() {
        AtomicInteger i = new AtomicInteger();
        List<Integer> list = new ArrayList<>();
        Consumer<Integer> c = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                list.add(integer);
            }
        };
        pipeN(5, i::incrementAndGet, c);
        assertEquals(List.of(1, 2, 3, 4, 5), list);
        pipeN(1, i::incrementAndGet, c);
        assertEquals(List.of(1,2,3,4,5,6), list);
        pipeN(0, i::incrementAndGet, c);
        assertEquals(List.of(1,2,3,4,5,6), list);
        pipeN(-1, i::incrementAndGet, c);
        assertEquals(List.of(1,2,3,4,5,6), list);
    }
}