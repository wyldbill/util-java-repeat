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

## tossN
Use `tossN` to throw away n values from a Supplier. This allows you to skip n 
items from something like a Collection, Result or similar.
    
    //Pop 10 items off a Deque
    Repeat.tossN(10, deque::pop);

## getN
Use `getN` to retrieve a List of n items from a Supplier. A companion to tossN,
is actually cares about what's returned from the Supplier.
    
    //get the first 5 items from a Deque
    List<String> firstFive = Repeat.getN(5,deque::pop);

## pipeN
Use `pipeN` to retrieve n items from a Supplier and pass them to a consumer. 
Useful for sequentially batch processing items from a Supplier.
    
    //print the first 10 items from a Deque
    Repeat.pipeN(10,deque::pop, System.out::println);

## invokeRange
Use `streainvokeRangemOf` to invoice an IntConsumer with a range of ints
from 0 to n, exclusive or from n inclusive to m exclusive.

    //Print the ints from 0 to 10
    Repeat.invokeRange(11, System.out::print);

    //Print the ints from 5 to 10
    Repeat.invokeRange(5, 11, System.out::print);


