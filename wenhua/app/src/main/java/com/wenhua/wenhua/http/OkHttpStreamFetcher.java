package com.wenhua.wenhua.http;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by castiel on 2016/3/24.
 */
public class OkHttpStreamFetcher implements DataFetcher<InputStream> {
    private final OkHttpClient client;
    private final GlideUrl url;
    private InputStream stream;
    private ResponseBody responseBody;

    public OkHttpStreamFetcher(OkHttpClient client, GlideUrl url) {
        this.client = client;
        this.url = url;
    }

    public InputStream loadData(Priority priority) throws Exception {
        Request.Builder requestBuilder = (new Request.Builder()).url(this.url.toStringUrl());
        Iterator request = this.url.getHeaders().entrySet().iterator();

        while(request.hasNext()) {
            Map.Entry response = (Map.Entry)request.next();
            String contentLength = (String)response.getKey();
            requestBuilder.addHeader(contentLength, (String)response.getValue());
        }

        Request request1 = requestBuilder.build();
        Response response1 = this.client.newCall(request1).execute();
        this.responseBody = response1.body();
        if(!response1.isSuccessful()) {
            throw new IOException("Request failed with code: " + response1.code());
        } else {
            long contentLength1 = this.responseBody.contentLength();
            this.stream = ContentLengthInputStream.obtain(this.responseBody.byteStream(), contentLength1);
            return this.stream;
        }
    }

    public void cleanup() {
        if(this.stream != null) {
            try {
                this.stream.close();
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }

        if(this.responseBody != null) {

            try {
                this.responseBody.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public String getId() {
        return this.url.getCacheKey();
    }

    public void cancel() {
    }
}
