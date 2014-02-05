package com.android.volley.toolbox.headers;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Cache.Entry;
import com.android.volley.toolbox.headers.models.ResponseModel;

/**
 * Base class for all network requests.
 * 
 * @param <T>
 *            The type of parsed response this request expects.
 */
public class ResponseWithHeaders<T> extends Response<T> {

    protected ResponseWithHeaders(T result, Entry cacheEntry) {
        super(result, cacheEntry);
    }

    protected ResponseWithHeaders(VolleyError error) {
        super(error);
    }

    /** Returns a successful response containing the parsed result. */
    public static <T> ResponseWithHeaders<T> success(T result, Cache.Entry cacheEntry) {
        return new ResponseWithHeaders<T>(result, cacheEntry);
    }

    /**
     * Returns a failed response containing the given error code and an optional localized message displayed to the
     * user.
     */
    public static <T> ResponseWithHeaders<T> error(VolleyError error) {
        return new ResponseWithHeaders<T>(error);
    }

    /** Callback interface for delivering parsed responses. */
    public interface Listener<T> {
        /** Called when a response is received. */
        public void onResponse(ResponseModel<T> response);
    }

}
