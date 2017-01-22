package com.chinasunsoft.ept.ad.retrofit2.adapters.fastjson;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;

import retrofit2.Converter;


public class FastJsonStringConverter<T> implements Converter<T, String> {
    private final Type type;
    public FastJsonStringConverter(Type type){
        this.type = type;
    }
    @Override
    public String convert(T value) throws IOException {
        String s = JSON.toJSONString(value);
        //LogU.e("",s);
        return s;
    }

}