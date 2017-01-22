package com.chinasunsoft.ept.ad.retrofit2.generator;

import com.chinasunsoft.ept.ad.retrofit2.Generator;

import java.lang.reflect.Method;
import java.util.Map;



public class EmptyGenerator implements Generator {
    @Override
    public String generate(Method method, Map<String, String> extendFields, Object... args) {
        return "";
    }
}
