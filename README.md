# Rvolley

��дVolley�⣬1��post�������һ��key ���valueֵ�ύ

              2��post�������֧��Headͷ��Ϣ�ύ

              3������ļ��ϴ�����֧�� �� �������������ϴ�֧��

# 

#souce code sample

package first.com.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.volley.AuthFailureError;
import com.volley.Request;
import com.volley.procotol.HttpTools;
import com.volley.procotol.ResponseProtocol;
import com.volley.toolbox.MultipartContent;
import com.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends Activity {
    public static HttpTools sHttpTools = null;

    public static  String apiHeadUrl = "";//����ͷurl;
    public static final String loginUrl = "";//����ķ�����Ӧ������,����Ϊ��¼;
    public static final String setAvatarUrl = "";//����ͷ��url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sHttpTools = HttpTools.getHttpTools(Volley.newRequestQueue(getApplicationContext()));
        login("18257106739", "123456", "8d893ab659e94b3d359eed8806e7f949", 0, new HttpTools.HttpListener() {
            @Override
            public void onSuccessResponse(ResponseProtocol sProtocol) {//ResponseProtocol  �����Ϊ�Լ���װ��Http������ ��������Լ��� HttpTools�����Լ�����ͷ�װ;
                System.out.println(sProtocol.getComplete());//��ӡHttp����Ľ��;
            }

            @Override
            public void onErrorResponse(ResponseProtocol sProtocol) {
                System.out.println(sProtocol.getMsg());//��ӡHttp�����쳣����;
            }
        });
    }

    /**
     * �û���¼�ӿ�,�����¼;
     *
     * @param passport
     * @param password
     * @return
     */
    public static void login(String passport,String password,String deviceToken,Integer platform,HttpTools.HttpListener listener){
        Map<String, String> map = new HashMap<String, String>();//�������������post����;
        Map<String, String[]> mapParams = new HashMap<String, String[]>();//�������������post����.һ��key���value;
        String url = String.format(Locale.getDefault(),"%s%s", apiHeadUrl,loginUrl);//�������������url;
        map.put("passport", passport);//��Ҫ�ύ��post����,����ֻ��ʾ��;
        map.put("password", password);
        Request<?> sRequest = sHttpTools.getHttp(Request.Method.POST, url, listener, map, mapParams);//�ύ��ʽ��װ����  HttpTools.getHttp��������;
        addHttp(sRequest, loginUrl, false);
    }

    /***
     //	 * �ϴ��ļ�ʾ��;
     //	 */
	public static void uploadHead(String headFilePath,HttpTools.HttpListener listener)
	{
		final Map<String,MultipartContent> multipartContentMap = new Hashtable<String,MultipartContent>();
		final String url = String.format(Locale.getDefault(),"%s%s", apiHeadUrl,setAvatarUrl);
		File sFile = new File(headFilePath);
		multipartContentMap.put("images",new MultipartContent("file",sFile.getName(),sFile));
		Request<?> sRequest = sHttpTools.getHttpFile(Request.Method.POST, url, listener, null, multipartContentMap);
		addHttp(sRequest, setAvatarUrl, false);
	}

    /***
     * �ϴ�ͷ���ļ���������ʾ��;
     */
    public static void uploadHead(Bitmap bitmap,HttpTools.HttpListener listener)
    {
        final Map<String,MultipartContent> multipartContentMap = new Hashtable<String,MultipartContent>();
        final String url = String.format(Locale.getDefault(), "%s%s", apiHeadUrl, setAvatarUrl);
        String fileName = "your FileName";
        if(bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Drawable drawable = new BitmapDrawable(bitmap);
            Bitmap.CompressFormat compressFormat;
            if(drawable.getOpacity() == PixelFormat.OPAQUE) //����ͼƬ���ͱ���ͼƬ;
            {
                compressFormat = Bitmap.CompressFormat.JPEG;
                fileName+=".jpg";
            }else{
                compressFormat = Bitmap.CompressFormat.PNG;	//��pngͼƬ;
                fileName+=".png";
            }
            bitmap.compress(compressFormat, 90, byteArrayOutputStream);
            multipartContentMap.put("images",new MultipartContent("file",fileName,byteArrayOutputStream.toByteArray()));
        }
        Request<?> sRequest = sHttpTools.getHttpFile(Request.Method.POST, url, listener, null, multipartContentMap);
        addHttp(sRequest, setAvatarUrl, false);
    }

    //���һ��Http����;
    public static void addHttp(Request<?> sRequest,String tag,boolean cache){
        Map<String, String> loginHeadMap = new HashMap<String, String>();//����������Head ͷ��Ϣ;
        try {
            sHttpTools.addHttp(sRequest, new HashMap<String, String>(), tag, cache);
        } catch (AuthFailureError authFailureError) {
            System.out.println(Log.getStackTraceString(authFailureError));
        }
        sHttpTools.startHttp();
    }
}

###
