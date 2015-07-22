package com.volley.toolbox;

import com.volley.AuthFailureError;
import com.volley.NetworkResponse;
import com.volley.Request;
import com.volley.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import static com.volley.toolbox.MultipartContent.*;

/**
 * Created by Shedings on 2015/7/20.
 */
public class MultipartRequest extends Request<String> {
    private MultipartEntity mMultiPartEntity = new MultipartEntity();
//    private MultipartEntity mMultiPartEntity = new MultipartEntity("UTF-8");//这里可以设置Http上传文件和提交参数的编码,默认为UTF-8;

    private final Response.Listener<String> mListener;
    private Map<String, String> headMap = new HashMap<String, String>();

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public MultipartRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    /**
     * @return MultipartEntity
     */
    public MultipartEntity getMultiPartEntity() {
        return mMultiPartEntity;
    }

    @Override
    public String getBodyContentType() {
        return mMultiPartEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            Map<String,String> param = getParams();
            Map<String,MultipartContent> paramsFile = getParamsFile();
            if(null != param && param.size() > 0){
                for (Map.Entry<String,String> sParam : param.entrySet())
                    getMultiPartEntity().addStringPart(sParam.getKey(), sParam.getValue());
            }
            if(null != paramsFile && paramsFile.size() > 0){
                for (Map.Entry<String,MultipartContent> sBinary : paramsFile.entrySet()){
                    MultipartContent sMultipartContent = sBinary.getValue();
                    if(null == sMultipartContent)
                        throw new AuthFailureError("getBody MultipartContent NullException");
                    if(sMultipartContent.getType().equals(BINARYFILETYPE))
                        getMultiPartEntity().addBinaryPart(sMultipartContent.getFileType(),sMultipartContent.getBinary(),sMultipartContent.getFileName());
                    else if(sMultipartContent.getType().equals(DEFAUTLFILETYPE))
                        getMultiPartEntity().addFilePart(sMultipartContent.getFileType(),sMultipartContent.getFile());
                }
            }
            mMultiPartEntity.writeTo(bos);// 将mMultiPartEntity中的参数写入到bos中;
        } catch (IOException e) {
            throw new AuthFailureError("IOException writing to ByteArrayOutputStream",new IOException());
        }
        return bos.toByteArray();
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    protected Map<String,MultipartContent> getParamsFile(){
        return null;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headMap;
    }
}
