package com.ves.main.util;

import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;

public class ObjectUtil {
	public static <T> T clone(T object) {
		Kryo kryo = new Kryo();
		try {
			kryo.setAsmEnabled(true);
			kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
			return kryo.copy(object);
		} finally {
			kryo = null;
		}
	}
}
