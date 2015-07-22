package com.volley.toolbox;

import com.volley.AuthFailureError;
import com.volley.Request;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Shedings on 2015/7/20.
 */
public class MultipartStack implements HttpStack {

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        return null;
    }
}
