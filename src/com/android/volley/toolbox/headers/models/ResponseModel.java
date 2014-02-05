package com.android.volley.toolbox.headers.models;

import java.util.HashMap;
import java.util.Map;

public class ResponseModel<T> implements IResponseModel<T>{
    
    private Map<String, String> mHeaders = new HashMap<String,String>();
    private T mData;

    @Override public Map<String, String> getHeaders() {
        return mHeaders;
    }

    @Override public T getData() {
        return mData;
    }
    
    public void setData(T response) {
        this.mData = response;
    }

    @Override public void addResponseHeader(String key, String value) {
        mHeaders.put(key, value);
    }
    
    @Override public void addResponseHeaders(Map<String, String> headers) {
        mHeaders.putAll(headers);
    }
}
