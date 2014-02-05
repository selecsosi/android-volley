package com.android.volley.toolbox.headers;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.headers.ResponseWithHeaders.Listener;
import com.android.volley.toolbox.headers.models.ResponseModel;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Wrapper class to take a Request and instead of returning a instance of the generic T used to construct the class,
 * this should return a {@link ResponseModel} with access to the response data (parsed as the T type) as well as the
 * response headers from the request
 * 
 * @param <T>
 *            The type of parsed response this request expects.
 */
public class ResponseHeaderRequest<T> extends Request<T> {
    private static final String TAG = ResponseHeaderRequest.class.getSimpleName();

    Gson mGson = new Gson();

    Listener<T> mResponseListener;
    Map<String, String> mResponseHeaders;
    Type mResponseType;
    
    public ResponseHeaderRequest(int method, String url, ErrorListener errorListener) {
        this(method, url, null, errorListener);
    }

    /**
     * Creates a new request with the given method (one of the values from {@link Method}), URL, and error listener.
     * Note that the normal response listener is not provided here as delivery of responses is provided by subclasses,
     * who have a better idea of how to deliver an already-parsed response.
     */
    public ResponseHeaderRequest(int method, String url, Listener<T> responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        mResponseListener = responseListener;
    }

    /**
     * Default deliver of response, constructs a new ResponseModel<T>, adds the
     */
    @Override protected void deliverResponse(T response) {
        ResponseModel<T> r = new ResponseModel<T>();
        r.setData(response);
        if(mResponseHeaders != null) {
            r.addResponseHeaders(mResponseHeaders);
        }
        deliverResponse(r);
    }

    /**
     * Subclasses must implement this to perform delivery of the parsed response to their listeners. The given response
     * is guaranteed to be non-null; responses that fail to parse are not delivered.
     * 
     * @param response
     *            The parsed response returned by {@link #parseNetworkResponse(NetworkResponse)}
     */
    protected void deliverResponse(ResponseModel<T> response) {
        mResponseListener.onResponse(response);
    }

    @SuppressWarnings("unchecked") @Override protected ResponseWithHeaders<T> parseNetworkResponse(
            NetworkResponse response) {
        //Store the response headers on the request, they will be used when serving the response
        setResponseHeaders(response.headers);
        String json = null;
        if (response != null && response.data != null) {
            try {
                json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } catch (UnsupportedEncodingException e) {
                return ResponseWithHeaders.error(new VolleyError(response));
            }
        }

        T responseObject = null;
        if (!TextUtils.isEmpty(json)) {
            try {
                responseObject = (T) mGson.fromJson(json, mResponseType);
            } catch (JsonSyntaxException e) {
                return ResponseWithHeaders.error(new VolleyError(response));
            }
        } else {
            return ResponseWithHeaders.error(new VolleyError(response));
        }
        return ResponseWithHeaders.success(responseObject, HttpHeaderParser.parseCacheHeaders(response));
    }

    public Type getResponseType() {
        return mResponseType;
    }

    public ResponseHeaderRequest<T> setResponseType(Type responseType) {
        mResponseType = responseType;
        return this;
    }

    public Listener<T> getResponseListener() {
        return mResponseListener;
    }

    public ResponseHeaderRequest<T> setResponseListener(Listener<T> responseListener) {
        mResponseListener = responseListener;
        return this;
    }
    
    public void setGson(Gson gson) {
        mGson = gson;
    }

    public Map<String, String> getResponseHeaders() {
        return mResponseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        mResponseHeaders = responseHeaders;
    }
}
