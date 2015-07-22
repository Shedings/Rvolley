package com.volley.procotol;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.volley.AuthFailureError;
import com.volley.NetworkResponse;
import com.volley.Request;
import com.volley.RequestQueue;
import com.volley.Response;
import com.volley.VolleyError;
import com.volley.Request.Method;
import com.volley.toolbox.HttpHeaderParser;
import com.volley.toolbox.MultipartContent;
import com.volley.toolbox.MultipartRequest;
import com.volley.toolbox.StringRequest;

/***
 * Http Request Libs  Shedings;
 */
public class HttpTools {
	protected final String TYPE_UTF8_CHARSET = "charset=UTF-8"; 
	protected final String DEFULAT_CHARSET = "UTF-8";
	protected final String CODE = "code";
	protected final String DATA = "data";
	protected final String MSG = "msg";
	
	protected RequestQueue sRequestQueue; 
	private static HttpTools sHttpTools;
	
	//采用  -- 单例调用方式;
	public static HttpTools getHttpTools(RequestQueue paramRequestQueue){
		if(null == sHttpTools)
			sHttpTools = new HttpTools(paramRequestQueue);
		return sHttpTools;
	}
	
	private HttpTools(RequestQueue paramRequestQueue) {
		super();
		sRequestQueue = paramRequestQueue;
	}
	
	//添加一个Http请求;
	public void addHttp(Request<?> sRequest,Map<String, String> head, String tag,boolean cache) throws AuthFailureError {
		sRequest.getHeaders().putAll(head);
		sRequest.setShouldCache(cache);
		sRequest.setTag(tag);
		sRequestQueue.add(sRequest);
	}

	//开始发送请求;
	public void startHttp(){
		sRequestQueue.start();
	}
	
	//结束Http请求;
	public void stopHttp(){
		sRequestQueue.stop();
	}
	
	//取消指定的Http请求;
	public void callHttp(String tag){
		sRequestQueue.cancelAll(tag);
	}

	public Request<?> getHttp(int method, String url, HttpListener listener,final Map<String, String> requestParam){
		return getHttp(method,url,listener,requestParam,null,null);
	}

	public Request<?> getHttp(int method, String url, HttpListener listener,final Map<String, String> requestParam,final Map<String, String[]> requestParams){
		return getHttp(method,url,listener,requestParam,requestParams,null);
	}

	public Request<?> getHttpAdditional(int method, String url, HttpListener listener,final Map<String, String[]> requestParam){
		return getHttp(method,url,listener,null,requestParam,null);
	}

	public Request<?> getHttpFile(int method, String url, HttpListener listener,final Map<String, String> requestParam,final Map<String,MultipartContent> paramsFiles){
		return getHttp(method,url,listener,requestParam,null,paramsFiles);
	}

	//加载Http请求;
	public Request<?> getHttp(int method, String url, HttpListener listener,final Map<String, String> requestParam,final Map<String, String[]> requestParams,final Map<String,MultipartContent> paramsFile){
		HttpListenerIm sHttpListenerIm = new HttpListenerIm(listener);
		if(method == Method.GET && null != requestParam && !requestParam.isEmpty()){//处理Get过来的请求;
			Iterator<Map.Entry<String, String>> iterator = requestParam.entrySet().iterator();
			url = String.format(Locale.getDefault(),"%s%s", url,"?");
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next(); 
				if(iterator.hasNext())
					url += String.format(Locale.getDefault(),"%s=%s&", entry.getKey(),entry.getValue());
				else
					url += String.format(Locale.getDefault(),"%s=%s", entry.getKey(),entry.getValue());
			}
		}
		if(null != paramsFile && paramsFile.size() > 0){
			return new MultipartRequest(method, url, sHttpListenerIm,sHttpListenerIm){

				@Override
				protected Map<String, String[]> getAdditionalParams() throws AuthFailureError {
					return requestParams;
				}

				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					return requestParam;
				}

				@Override
				protected Map<String, MultipartContent> getParamsFile() {
					return paramsFile;
				}
			};
		}
		return new StringRequest(method, url, sHttpListenerIm,sHttpListenerIm){//只有Post的时候才会调用以下方法;

			@Override
			protected Map<String, String[]> getAdditionalParams() throws AuthFailureError {
				return requestParams;
			}

			protected Map<String,String> getParams() throws AuthFailureError {
				return requestParam;
			}

			@Override
			public Response<String> parseNetworkResponse(NetworkResponse response) {
				String parsed = null;
				try {  
	                String type = response.headers.get(HTTP.CONTENT_TYPE);  
	                if (type == null) {  
	                    type = TYPE_UTF8_CHARSET;  
	                    response.headers.put(HTTP.CONTENT_TYPE, type);  
	                } else if (!type.contains(DEFULAT_CHARSET)) {  
	                    type += ";" + TYPE_UTF8_CHARSET;  
	                    response.headers.put(HTTP.CONTENT_TYPE, type);  
	                }
	                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
	            } catch (Exception e) {  
	            	parsed = new String(response.data);
	            } 
				return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
			}
		};
	}

	//http请求封装;
	public class HttpListenerIm implements Response.Listener<String>,Response.ErrorListener{
		private HttpListener sHttpListener;
		
		public HttpListenerIm(HttpListener sHttpListener) {
			super();
			this.sHttpListener = sHttpListener;
		}

		@Override
		public void onErrorResponse(VolleyError error) {
			long statusCode = 400000;
			if(null != error.getNetworkResponse())
				statusCode = error.getNetworkResponse().statusCode;
			this.sHttpListener.onErrorResponse(new ResponseProtocol(Long.valueOf(statusCode), error.getMessage(), Log.getStackTraceString(error),""));
		}
		
		@Override
		public void onResponse(String response) {
			String code = "-1",data = "",msg = "";
			JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(response);
				if(!jsonObject.isNull(CODE))
					code = jsonObject.getString(CODE);
				if(!jsonObject.isNull(DATA))
					data = jsonObject.getString(DATA);
				if(!jsonObject.isNull(MSG))
					msg = jsonObject.getString(MSG);
			} catch (JSONException e) {
			}
			this.sHttpListener.onSuccessResponse(new ResponseProtocol(Long.valueOf(code),data, msg, response));
		}
	}

   //提供Http请求回调;
   public interface HttpListener {
		public void onSuccessResponse(ResponseProtocol sProtocol);
		public void onErrorResponse(ResponseProtocol sProtocol);
	} 
}