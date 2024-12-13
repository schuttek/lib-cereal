# Cereal

## Overview

lib-cereal is a pure Java implementation of Cereal, a binary object serialization mechanism. It also acts as the reference implementation.

Cereal is language neutral (it should be possible to implement this toolkit in any programming language).

Much like JSON or Proto Buffers, Cereal is useful for the exchange of typed objects (or collection of objects). It is considerably more compact and faster, yet lacks the flexibility of more advanced serialization systems. It's main advantage is that it can be used for streaming objects (ie, you can read objects before the last object has been written).


### Pros

- Cereal creates very compact files by writing the binary forms of values (ie, a 32 bit float will use 4 bytes).
- Cerealized objects are stored without headers or any markers, thereby avoiding any overhead.
- Cerealizing and decerealizing objects is VERY fast, since there is no real parsing or data transformation going on. Some benchmarks have been able to surpass 3GB/s in cerealization and decerealization...
- The lack of headers means files can be read and written as endless streams of objects. (See below for details)
- Some objects can be cerealized to a fixed number of bytes, allowing a reader to seek through a data file to find the Nth object in the file.

### Cons

- The lack of headers means a file containing Cereal data cannot tell you how to read it.
- If the format of a Cerealized object changes, any previous data files containing Cerealized objects will be unreadable.
- Data files are not human readable.
- Cerealization is done by an explicit bit of code implemented in the class of the Cerealizable object itself. Unlike many other serialization tools, Cereal is not automatic.

## GitHub

The main repository for the ongoing development of lib-cereal is here:

https://github.com/Wezr/lib-cereal

Please use the Issues page to ask questions, report bugs, etc.

## Importing in your project

lib-cereal is not yet published on any central repository. It is however available on bintray's public download servers.

#### Maven

```gradle
	repositories {
		maven {
			url  "https://dl.bintray.com/wezr-ci/com.wezr"
		}
	}
	dependencies {
		compile 'com.wezr:lib-cereal:0.4.21'
	}

```

# Quick and Simple Guide



## Two things to keep in mind when implementing Cerealizable 
 
 - The order in which you write fields must be strictly the same as the order in which you read fields.
 - Make sure you read everything you may have written (seems obvious, but it can get tricky when fields are optional or null).



Let's create a new Model Object for a Tree:

```java
	public class Tree {
        private float height;
        private int leafCount;
        private String speciesName;
    }
```    
 A class needs to implement the Cerealizable interface, which requires the implementation of to methods: one to cerealize the Object, the other to deceralize it.

```java


public class Tree {
    private float height;
    private int leafCount;
    private String speciesName;

    @Override
    public void cerealizeTo(final ByteArray ba) {

    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {

    }

}
```
 
 
 To write fields, use the add() methods on ByteArray. To read them, use the get*Type*() methods.
 
```java
     @Override
     public void cerealizeTo(final ByteArray ba) {
         ba.add(height);
         ba.add(leafCount);
         ba.add(speciesName);
     }
 
     @Override
     public void uncerealizeFrom(final ByteArray ba) {
         height = ba.getFloat();
         leafCount = ba.getInt();
         speciesName = ba.getString();
     }
```


A Cerealizable object must always have a public empty constructor. So before we add a parametrized constructor, we must also declare an empty constructor explicitly;



```java
    public Tree() {}

    public Tree(final float height, final int leafCount, final String speciesName) {
        this.height = height;
        this.leafCount = leafCount;
        this.speciesName = speciesName;
    }
```




To cerealize the Object: 

```java
        final Tree birch = new Tree(23.68f, 2692, "Birch");
        // cerealize:
        ByteArray ba = new ByteArray();
        birch.cerealizeTo(ba);
        final byte[] buffer1 = ba.getAllBytes();
        // same thing, less typing:
        final byte[] buffer2 = ByteArray.cerealize(birch).getAllBytes();
```
        
To uncerealize the object: 

```java
        ByteArray readBa = new ByteArray(buffer1);
        final Tree uncerealizedBirch1 = new Tree();
        uncerealizedBirch1.uncerealizeFrom(readBa);

        // same thing in short hand notation.
        final Tree uncerealizedBirch2 = ByteArray.wrap(buffer2).uncerealize(Tree.class);

        assert(uncerealizedBirch1.equals(birch));
```




# Advanced Guides



## Cerealizing collections

There is no predefined convention to write or read cerealized collections, it's really up to you, and how you want to implement the Cerealizable methods.

There are only two rules: 

 - The order in which you write fields must be strictly the same as the order in which you read fields.
 - Make sure you read everything you may have written.
 
The easiest way to cerealize a collection is to write the number of items in the collection, then write each item in the collection. On the uncerealization side, you read the number of objects in the collection, then write that number of objects. 

For example: 

```java
    public class Point {
        private float[] coordinates;

        public void cerealizeTo(final ByteArray ba) {
            ba.add(coordinates.length);
            for (float c : coordinates) {
                ba.add(c);
            }
        }
 
        public void uncerealizeFrom(final ByteArray ba) {
            int count = ba.getInt();
            coordinates = new float[count];
            for (int i=0; i<count; i++) {
                coordinates[i] = ba.getFloat();
            }
        }
    }
```

For Maps, the trick is to keep keys and values next to each other: 

```java
    public class Register implements Cerealizable {
        private Map<Integer, String> userNamesById;

        public void cerealizeTo(final ByteArray ba) {
            ba.add(userNames.size());
            userNamesById.forEach( (k,v) -> {
                ba.add(k);
                ba.add(v);
            }
        }
 
        public void uncerealizeFrom(final ByteArray ba) {
            int count = ba.getInt();
            userNamesById = new HashMap<>();
            for (int i=0; i<count; i++) {
                userNamesById.put(ba.getInt(), ba.getString());
            }
        }
    }
```


## Cerealizing complex objects

### Object fields

To cerealize a field that is also a Cerealizable, use that class's cerealization methods to append to or read from the ByteArray... 
```java
public class Person implements Cerealizable {
    private Address address;
    private String name;
      
    public void cerealizeTo(final ByteArray ba) {
        address.cerealizeTo(ba);
        ba.add(name);
    }
        
    public void uncerealizeFrom(final ByteArray ba) {
        address = ba.uncerealize(Address.class);
        name = ba.getString();
    }
}
```

### Inheritance

Use the super class's Cerealizable implementation to simplify your subclass's implementation: 


```java
public class GeoPoint implements Cerealizable {
    private float lat;
    private float lng;
      
    public void cerealizeTo(final ByteArray ba) {
        ba.add(lat);
        ba.add(lng);
    }
        
    public void uncerealizeFrom(final ByteArray ba) {
         lat = ba.getFloat();
         lng = ba.getFloat();
    }     
}
    
public class DataGeoPoint extends GeoPoint {
    private float dataValue;

    public void cerealizeTo(final ByteArray ba) {
        super.cerealizeTo(ba);
        ba.add(dataValue);
    }
        
    public void uncerealizeFrom(final ByteArray ba) {
        super.uncerealizeFrom(ba);
        dataValue = ba.getValue();
    }     
}
```


## Dealing with null values and optional fields

### Null Strings

Strings are handled a little differently than other objects. They are the only object that can be null when passed to the add() and retrieved with getString(). This is because internally, Strings are treated as a collection of characters, and so the first 4 bytes are actually the length of the string. When that length is -1, the String is null. 


### Null Objects

The trick is to add a boolean value that saves whether a field is null: 

```java
public class Person implements Cerializable {
    private long id;
    private Address address;
    
    public void cerealizeTo(final ByteArray ba) {
        ba.add(id);
        ba.add(address == null);
        if (address != null) {
            address.cerealizeTo(ba);
        }
    }
        
    public void uncerealizeFrom(final ByteArray ba) {
        id = ba.getLong();
        boolean hasAddress = ba.getBoolean();
        if (hasAddress) {
            address = ba.uncerealize(Address.class);
        } else {
            address = null;
        }
    }     
}

```

### Null Numbers

A few handy methods exist to add possibly null Numbers (like the Integer, Double, Float, etc classes). 

```java
public class DataPoint implements Cerealizable {
    private Float temperature;
    private Float pressure;
    
    public void cerealizeTo(final ByteArray ba) {
        ba.add(temperature == null);
        ba.addIfNotNull(temperature);
        ba.add(pressure == null);
        ba.addIfNotNull(pressure);
    }
        
    public void uncerealizeFrom(final ByteArray ba) {
        temperature = ba.getBoolean() ? null : ba.getFloat();
        pressure = ba.getBoolean() ? null : ba.getFloat();
    }
}
```



### BitMap and the nullMap 

When you have many optional fields, or fields that can be null, It may be more efficient to make a bitmap which stores which fields are null. During decerealization, this nullmap determines which fields can be skipped. 

See https://github.com/Wezr/lib-cereal/blob/master/src/test/java/com/wezr/lib/cereal/Forecast.java for a complete example. 


## Using Cereal I/O Streams

To write a collection of Cerealizable objects to a file (or any OutputStream), you can use CerealOutputStream: 

```java
class Application {
    public static void main(String[] args) {
        List<GeoPoint> points = getPoints();
        try (CerealOutputStream cos = new CerealOutputStream(new FileOutputStream("data.cereal"))) {
            for (GeoPoint point : points) {
                cos.write(point);
            }
        }
    }
}    
```

To read the same file: 
```java
class Application {
    public static void main(String[] args) {
        List<GeoPoint> points = new LinkedList<>();
        try (CerealInputStream cis = new CerealInputStream(new FileInputStream("data.cereal"))) {
            Optional<GeoPoint> point;
            do {
                point = cis.read(GeoPoint.class);
                point.ifPresent(points::add);
            } while(point.isPresent());
        }
    }
}
```

Okay, that last example has a little too much weird syntax... Here's the same with more basic syntax: 
```java
class Application {
    public static void main(String[] args) {
        List<GeoPoint> points = new LinkedList<>();
        try (CerealInputStream cis = new CerealInputStream(new FileInputStream("data.cereal"))) {
            while (true) {
                final Optional<GeoPoint> readOpt = cis.read(GeoPoint.class);
                if (readOpt.isPresent()) {
                    points.add(readOpt.get());
                } else {
                    break;
                }
            }
        }
    }
}
```


## Using Cerealizer

Sometimes, you can't implement (or don't want) a public empty constructor (because you want or need immutability or the object need a reference to an object available at runtime).

For that, you can use a `Cerealizer` which is an object able to serealize and unserialize an object with a specific type. 

Using a `Cerealizer` doen't require anymore to have a public constructor with no argument for the target type. You can use a `Cerealizer` instead of `Cerealizable` everywhere in this library (directly or by using the classe `CereralizableCerealizer`)

Example:
```java
public final class User {
    private final String username;
    private final int age;
    private final UserRepository repository; // This field can not be serialized and is available at runtime
    
    public User (String repository, String username, String age) {
        this.repository =repository;
        this.username = username;
        this.age = age;
    }
}

public final class UserCerealizer implements Cerealizer<User> {
    void cerealizeTo(ByteArray ba, User obj) {
        ba.add( obj.getUsername() );
        ba.add( obj.getAge() );
    }
    
    User uncerealizeFrom(ByteArray ba) {
        UserRepository repository = getService(UserRepository.class);
        return new User(repository, ba.getString(), ba.getInt());
    }
}
``` 


## Using CerealFileSorter

With Cereal you can store absolutely massive collections of objects in a single file. Which can be a problem when you want to sort the items in said file, since loading them all into memory would probably exceed your available memory. 

CerealFileSorter implements a sorting algorithm that runs in a fixed memory space. It does need disk space for temporary files, and that disk space must be large enough to contain the entire file that needs to be sorted. 

It works by quicksorting small sections of the input file in memory, and writing each chunk to a temporary file. Then, it performs a merge sort of all the presorted temporary files to produce the sorted stream of objects. 

For examples on how to use CerealFileSorter, have a look at: https://github.com/Wezr/lib-cereal/blob/master/src/test/java/com/wezr/lib/cereal/SorterTest.java


## Compressing large arrays

Cereal is often used for handling gigantic amounts of fairly monotonus data. 

Let's say you want to store a timeseries of a temperature measurement on a fixed grid. So your data looks something like, where temperature's indices are the x and y coordinates of the grid.

```java
class DataGrid {
    private long timestamp;
    private float[][] temperature;
}
```

If you cerealize the floats as a simple collection, it would looke like this: 

```java
class DataGrid {
    private long timestamp;
    private float[][] temperature;
    
    public void cerealizeTo(final ByteArray ba) {
        ba.add(timestamp);
        ba.add(temperature.length);
        ba.add(temperature[0].length);
        for (int x=0; x<temperature.length; x++) {
            for (int y=0; y<temperature[0].length; y++) {
                ba.add(temperature[x][y]);
            }
        }
    }
        
    public void uncerealizeFrom(final ByteArray ba) {
        timestamp = ba.getLong();
        temperature = new float[ba.getInt()][ba.getInt()];
        for (int x=0; x<temperature.length; x++) {
            for (int y=0; y<temperature[0].length; y++) {
                temperature[x][y] = ba.getFloat();
            }
        }
    }
}
```

However, this float array is probably relatively monotonic, meaning that the variation between the highest and the lowest temperature is probably very small. 

This means that compression will probably work very well on this array of floats. The point is that each Cerealizable object is allowed to define it's own method of cerealization, so you can integrate compression directly into a Cerealizable implementation!

```java
class DataGrid {
    private long timestamp;
    private float[][] temperature;
    
    public void cerealizeTo(final ByteArray ba) {
        ba.add(timestamp);
        ByteArray uncompressedBuffer = new ByteArray();
        
        uncompressedBuffer.add(temperature.length);
        uncompressedBuffer.add(temperature[0].length);
        for (int x=0; x<temperature.length; x++) {
            for (int y=0; y<temperature[0].length; y++) {
                uncompressedBuffer.add(temperature[x][y]);
            }
        }
        
        byte[] compressed = gzipCompress(uncompressedBuffer.getAllBytes());
        ba.addByteArray(compressed);
    }
        
    public void uncerealizeFrom(final ByteArray ba) {
        timestamp = ba.getLong();
        
        byte[] compressed = ba.getByteArray();
        
        byte[] uncompressed = gzipUncompress(compressed);
        ByteArray uncompressedBuffer = ByteArray.wrap(uncompressed);
        temperature = new float[uncompressedBuffer.getInt()][uncompressedBuffer.getInt()];
        for (int x=0; x<temperature.length; x++) {
            for (int y=0; y<temperature[0].length; y++) {
                temperature[x][y] = uncompressedBuffer.getFloat();
            }
        }
    }
}
```

There's a neat little extension to Snappy, which is called bit shuffling, which further enhances the compression of this kind of data. 

In my experiments, the method below can provide around 85% compression, while the method above rarely exceeds 50% compression.

```java
import org.xerial.snappy.BitShuffle;
import org.xerial.snappy.Snappy;

class DataGrid {
    private long timestamp;
    private float[][] temperature;
    
    public void cerealizeTo(final ByteArray ba) {
        ba.add(timestamp);
        ba.add(temperature.length);
        ba.add(temperature[0].length);
        
        float[] oneDimensionalArray = convertTo1D(temperature);
        ba.addByteArray(Snappy.compress(BitShuffle.shuffle(oneDimensionalArray)));
    }
        
    public void uncerealizeFrom(final ByteArray ba) {
        timestamp = ba.getLong();
        int x = ba.getInt();
        int y = ba.getInt();
        float[] oneDimensionalArray = BitShuffle.unshuffleFloatArray(Snappy.uncompress(ba.getByteArray()));
        temperature = convertTo2D(oneDimensionalArray, x, y);
    }
}
```




## Binary Search in a sorted file of fixed length objects

Let's say you have an object which once cerealized, always uses the same number of bytes. This happens when all the fields of an object are of either constant length (ie, a float always uses 4 bytes), or the collections it contains are of fixed length. 

When these objects are written to a file, the bytes for a new object will start every N bytes, where N is the constant number of bytes a Cerealized object uses. 

This means that you can open a file, skip ahead to the i'th object by skipping i*N bytes. 

And this is where it gets cool, because that means that if the objects in a file are sorted, you can search through that file with a simple binary search, allowing you to find one object in a potentially *massive* file by only using log(n) reads. 

This hasn't actually been implemented yet, it's on my TODO list, and will be added to lib-cereal once it is functional.
