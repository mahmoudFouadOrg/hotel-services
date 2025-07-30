package com.mfouad.gateway.filters;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.protobuf.Descriptors.Descriptor;

public class ProtobufUtil {
	
	private static final Map<String, Descriptor> descriptorCache = new ConcurrentHashMap<>();

    public static Descriptor getDescriptor(String className) throws Exception {
        return descriptorCache.computeIfAbsent(className, clz -> {
            try {
                Class<?> protoClass = Class.forName(clz);
                Method getDescriptor = protoClass.getMethod("getDescriptor");
                return (Descriptor) getDescriptor.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException("Failed to get descriptor for " + clz, e);
            }
        });
    }

}
