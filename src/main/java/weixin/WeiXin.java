package weixin;

import com.google.gson.*;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.*;

/**
 * @author : cpc
 * @description : 微信开发测试
 * @date : 2015/12/7
 */
public class WeiXin {
    //微信获取access_token地址
    private static String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    //微信AppID
    public static String AppID = "wx054dbdd6b33ec19c";
    //微信AppSecret
    public static String AppSecret = "d4624c36b6795d1d99dcf0547af5443d";

    public static void main(String[] args) {
        //获取access_token
        //微信文档参考地址http://mp.weixin.qq.com/wiki/2/88b2bf1265a707c031e51f26ca5e6512.html
        try {
            //创建okHttpClient对象
            OkHttpClient httpClient = new OkHttpClient();
            //创建一个获取获取access_token的Request
            Request request = new Request.Builder()
                    .url(url+"&appid="+AppID+"&secret="+AppSecret)
                    .get()
                    .build();
            Response response = httpClient.newCall(request).execute();
            String returnStr = response.body().string();
            //调用方法类转化为map,方便调用数据
            Map<String, Object> temp = WeiXin.toMap(WeiXin.parseJson(returnStr));
            System.out.println("公众号的全局唯一票据access_token值为:"+temp.get("access_token"));

            //获取微信服务器IP地址
            //创建一个获取获取access_token的Request
//            OkHttpClient httpClientnew = new OkHttpClient();
//            Request requestip = new Request.Builder()
//                    .url("https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=pwVOYu5AWdAv7eGPKIgX2KZbo7h8vCOfToQmUFLwwkDMLhqZziwTSxRjNd0K6juKHYWZZMNANyb6SR7sQ25YVDE7mqQJd7oJZoKIUNBS9kgOVReAJAZGC")
//                    .get()
//                    .build();
//            Response responseip = httpClientnew.newCall(requestip).execute();
//            System.out.println("获取微信服务器IP地址列表为:"+responseip.body().string());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取JsonObject
     * @param json
     * @return
     */
    public static JsonObject parseJson(String json){
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        return jsonObj;
    }

    /**
     * 将JSONObjec对象转换成Map-List集合
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(JsonObject json){
        Map<String, Object> map = new HashMap<String, Object>();
        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
        for (Iterator<Map.Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ){
            Map.Entry<String, JsonElement> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof JsonArray)
                map.put((String) key, toList((JsonArray) value));
            else if(value instanceof JsonObject)
                map.put((String) key, toMap((JsonObject) value));
            else
                map.put((String) key, value);
        }
        return map;
    }

    /**
     * 将JSONArray对象转换成List集合
     * @param json
     * @return
     */
    public static List<Object> toList(JsonArray json){
        List<Object> list = new ArrayList<Object>();
        for (int i=0; i<json.size(); i++){
            Object value = json.get(i);
            if(value instanceof JsonArray){
                list.add(toList((JsonArray) value));
            }
            else if(value instanceof JsonObject){
                list.add(toMap((JsonObject) value));
            }
            else{
                list.add(value);
            }
        }
        return list;
    }
}
