# util-java-repeat
a small java library for repeating things


[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=wyldbill_util-java-repeat&metric=alert_status)](https://sonarcloud.io/dashboard?id=wyldbill_util-java-repeat)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=wyldbill_util-java-repeat&metric=coverage)](https://sonarcloud.io/dashboard?id=wyldbill_util-java-repeat)
![GitHub](https://img.shields.io/github/license/wyldbill/util-java-repeat)
![GitHub last commit](https://img.shields.io/github/last-commit/wyldbill/util-java-repeat)

## listOf
Use `listOf` to get a List fill with references to a single Object. This is
basically `Collections.nCopies()` but the List returned is mutable, and values < 0 
result in an empty List instead or throwing an `IllegalArgumentException`.


    // Create a List containing "FOO" five times.
    List<String> list = Repeat.listOf("FOO", 5);

If the supplied object is null, a List of nulls the appropriate size
is returned.

## streamOf
Use `streamOf` to get an infinite stream which continually supplies references to the 
supplied Object. This may not seem that useful since a reference to the actual
object could be used just as well, but it allows Stream sematics when operating
on an object multiple times.

    // Create a Stream repeating "FOO"
    Stream<String> s1 = Repeat.StreamOf("FOO");
    // Create a Stream of Suppliers<String> representing the times 
    // when the String is obtained from the Stream
    Stream<Supplier<String>> s2 = Repeat.streamOf(()-> Instant.now().toString()); 

    public static void invokeRange(int n, IntConsumer consumer) {
        invokeRange(0, n, consumer);
    }
    public static void invokeRange(int lower, int upper, IntConsumer consumer) {
        if (consumer == null) return;   //nothing to do
        IntStream.range(lower, upper).forEach(consumer);
    }
    public static void tossN(int n, Supplier<?> supplier) {
        if (supplier == null) return;
        streamOf(supplier).limit(Math.max(n, 0)).forEach(Supplier::get);
    }
    public static <T> List<T> getN(int n, Supplier<T> supplier) {
        if (supplier == null) return Collections.emptyList();
        return limitedSupplier(n, supplier).collect(Collectors.toList());
    }
    public static <T> void pipeN(int n, Supplier<? extends T> supplier, Consumer<T> consumer) {
        if (consumer == null) return;
        limitedSupplier(n, supplier).forEach(consumer);
    }
