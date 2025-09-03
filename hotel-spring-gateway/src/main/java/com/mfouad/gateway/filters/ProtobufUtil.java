package com.mfouad.gateway.filters;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.GenericDescriptor;

public class ProtobufUtil {
	
	private static final Map<String, GenericDescriptor> descriptorCache = new ConcurrentHashMap<>();

    public static GenericDescriptor getDescriptor(String className) throws Exception {
        return descriptorCache.computeIfAbsent(className, clz -> {
            try {
                Class<?> protoClass = Class.forName(className);
                Method getDescriptor = protoClass.getMethod("getDescriptor");
                return (GenericDescriptor) getDescriptor.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException("Failed to get descriptor for " + clz, e);
            }
        });
    }

}
