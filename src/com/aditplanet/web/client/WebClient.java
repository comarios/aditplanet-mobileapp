package com.aditplanet.web.client;

import com.loopj.android.http.*;

public class WebClient {
  private static final String BASE_URL = "http://aditplanet.com/";
  private static final int CONNECTION_TIMEOUT = 1000;
  
  private static AsyncHttpClient client = new AsyncHttpClient();

  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	  client.setTimeout(CONNECTION_TIMEOUT);

      client.setMaxRetriesAndTimeout(1, CONNECTION_TIMEOUT);

      client.get(getAbsoluteUrl(url), params, responseHandler);
  }

  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
      client.post(getAbsoluteUrl(url), params, responseHandler);
  }

  private static String getAbsoluteUrl(String relativeUrl) {
      return BASE_URL + relativeUrl;
  }
}