package com.s2paa.Utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;


public class GsonRequest<T> extends Request<T> {

    private final static int SOCKET_TIMEOUT_MS = 20 * 1000;

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final String body;
    private final Response.Listener<T> listener;
    public static final String CONTENT_TYPE = "application/json";
    private Handler handler;

    String progressMessage = "Loading...";
    ProgressDialog dialog;
    Context context;

    public GsonRequest(Context context, int method, String url, Class<T> clazz, String body,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.body = body;
        this.listener = listener;
        this.context = context;
        this.handler = new Handler(context.getMainLooper());
        dialog = new ProgressDialog(context);


        setRetryPolicy(new DefaultRetryPolicy(
                SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    @Override
    public byte[] getBody() throws AuthFailureError {
        AppLogger.info("Sending body" + body);

        handler.post(new Runnable() {
            @Override
            public void run() {
                dialog.setMessage(progressMessage);
                dialog.show();
            }
        });
        return body.getBytes();
    }

    @Override
    protected void deliverResponse(T response) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            AppLogger.info("Request:->"+getUrl()+"\n Response:->"+json);
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public String getBodyContentType() {
        return CONTENT_TYPE;
    }
}
