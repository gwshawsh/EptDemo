package com.chinasunsoft.ept.ad.retrofit2.factory.interceptor;

import com.chinasunsoft.ept.ad.event.Download;
import com.chinasunsoft.ept.ad.event.Event;
import com.chinasunsoft.ept.ad.event.EventType;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Administrator on 2017/1/18.
 */

public class DownloadProgressInterceptor implements Interceptor {

    public class DownloadProgressResponseBody extends ResponseBody {

        private ResponseBody responseBody;
        private BufferedSource bufferedSource;

        public DownloadProgressResponseBody(ResponseBody responseBody) {
            this.responseBody = responseBody;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    Event.post(EventType.DOWNLOAD_PROGRESS, new Download(totalBytesRead, responseBody.contentLength(), bytesRead == -1));
                    return bytesRead;
                }
            };

        }
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new DownloadProgressResponseBody(originalResponse.body()))
                .build();
    }
}
