package com.chinasunsoft.ept.ad.retrofit2.adapters.fastjson;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Type type;

    public FastJsonResponseBodyConverter(Type type) {
        this.type = type;
    }

    /*
    * 转换方法
    */
    @Override
    public T convert(ResponseBody value) throws IOException {
        /*InputStream is = value.byteStream();
        is = new GZIPInputStream(new BufferedInputStream(is));

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line = reader.readLine();
        while (line!=null){
            builder.append(line);
            line = reader.readLine();
        }*/
        BufferedSource bufferedSource = Okio.buffer(value.source());
      /*  String tempStr = bufferedSource.toString();
        LogU.e("tempStr:   ",tempStr);*/



        return JSON.parseObject(bufferedSource.readUtf8(), type);

    }
}
