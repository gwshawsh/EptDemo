package com.chinasunsoft.ept.ad.network;


import com.chinasunsoft.ept.ad.bean.Result;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface RequestService {

    @POST("ss_getversion.do")
    Observable<Result> getVersion(@Query("bblx") String s);

}
