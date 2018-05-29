package com.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Cartesian {
    @SafeVarargs
	public final static <T> Stream<T> go(BinaryOperator<T> aggregator, Supplier<Stream<T>>... streams) {
        return Arrays.stream(streams)
                .reduce((s1, s2) -> () -> s1.get().flatMap(t1 -> s2.get().map(t2 -> aggregator.apply(t1, t2))))
                .orElse(Stream::empty).get();
    }
    
    public final static <T> Stream<T> go(BinaryOperator<T> aggregator, List<Supplier<Stream<T>>> list) {
        return list.stream()
                .reduce((s1, s2) -> () -> s1.get().flatMap(t1 -> s2.get().map(t2 -> aggregator.apply(t1, t2))))
                .orElse(Stream::empty).get();    	
    }

    public static void main(String[] args) {
		String strXYZ[][] = {{"abc", "lmn", "opq", "xyz"}, {"+", "-", "*", "/"},{"アイウエオ", "かきくけこ", "やゆよ"}};

		List<Supplier<Stream<String>>> l = new ArrayList<Supplier<Stream<String>>>();
		
		for(String[] strs : strXYZ) {
			l.add(() -> Stream.of(strs));
		}

		go((a, b) -> a + "," + b, l).forEach(System.out::println);
		
        go((a, b) -> a + "," + b,
            () -> Stream.of("1", "2", "3"),
            () -> Stream.of("A", "B")
        ).forEach(System.out::println);
    }
}