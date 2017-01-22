package com.chinasunsoft.ept.ad.retrofit2;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/27.
 */
public interface Generator {
    public String generate(Method method, Map<String, String> extendFields, Object... args);
}
