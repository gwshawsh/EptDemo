package com.chinasunsoft.ept.ad.retrofit2;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by benny on 4/30/16.
 */
public interface MapGenerator {
    Map<String, String> generate(Method method, Map<String, String> extendFields, Object... args);
}
