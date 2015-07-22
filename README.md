# Rvolley

重写Volley库，1：post请求：添加一个key 多个value值提交

              2：post请求：添加支持Head头信息提交

              3：添加文件上传功能支持 及 二进制流数据上传支持

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

    public static  String apiHeadUrl = "";//请求头url;
    public static final String loginUrl = "";//请求的方法对应的名称,这里为登录;
    public static final String setAvatarUrl = "";//设置头像url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sHttpTools = HttpTools.getHttpTools(Volley.newRequestQueue(getApplicationContext()));
        login("18257106739", "123456", "8d893ab659e94b3d359eed8806e7f949", 0, new HttpTools.HttpListener() {
            @Override
            public void onSuccessResponse(ResponseProtocol sProtocol) {//ResponseProtocol  这个类为自己封装的Http请求类 具体可以自己到 HttpTools里面自己定义和封装;
                System.out.println(sProtocol.getComplete());//打印Http请求的结果;
            }

            @Override
            public void onErrorResponse(ResponseProtocol sProtocol) {
                System.out.println(sProtocol.getMsg());//打印Http请求异常代码;
            }
        });
    }

    /**
     * 用户登录接口,无需登录;
     *
     * @param passport
     * @param password
     * @return
     */
    public static void login(String passport,String password,String deviceToken,Integer platform,HttpTools.HttpListener listener){
        Map<String, String> map = new HashMap<String, String>();//这里是你请求的post参数;
        Map<String, String[]> mapParams = new HashMap<String, String[]>();//这里是你请求的post参数.一个key多个value;
        String url = String.format(Locale.getDefault(),"%s%s", apiHeadUrl,loginUrl);//这里是你的请求url;
        map.put("passport", passport);//你要提交的post参数,这里只是示例;
        map.put("password", password);
        Request<?> sRequest = sHttpTools.getHttp(Request.Method.POST, url, listener, map, mapParams);//提交方式封装到了  HttpTools.getHttp方法里面;
        addHttp(sRequest, loginUrl, false);
    }

    /***
     //	 * 上传文件示例;
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
     * 上传头像文件二进制流示例;
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
            if(drawable.getOpacity() == PixelFormat.OPAQUE) //根据图片类型保存图片;
            {
                compressFormat = Bitmap.CompressFormat.JPEG;
                fileName+=".jpg";
            }else{
                compressFormat = Bitmap.CompressFormat.PNG;	//是png图片;
                fileName+=".png";
            }
            bitmap.compress(compressFormat, 90, byteArrayOutputStream);
            multipartContentMap.put("images",new MultipartContent("file",fileName,byteArrayOutputStream.toByteArray()));
        }
        Request<?> sRequest = sHttpTools.getHttpFile(Request.Method.POST, url, listener, null, multipartContentMap);
        addHttp(sRequest, setAvatarUrl, false);
    }

    //添加一个Http请求;
    public static void addHttp(Request<?> sRequest,String tag,boolean cache){
        Map<String, String> loginHeadMap = new HashMap<String, String>();//这里添加你的Head 头信息;
        try {
            sHttpTools.addHttp(sRequest, new HashMap<String, String>(), tag, cache);
        } catch (AuthFailureError authFailureError) {
            System.out.println(Log.getStackTraceString(authFailureError));
        }
        sHttpTools.startHttp();
    }
}

###
