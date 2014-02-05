package com.android.volley.toolbox.headers.models;

import java.util.Map;

public interface IResponseModel<T> {
    public Map<String, String> getHeaders();
    public T getData();
    public void addResponseHeader(String key, String value);
    public void addResponseHeaders(Map<String, String> headers);
}