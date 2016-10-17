package pkaq.colonne.restful.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
  * 请求restful服务获取数据
  * Author: S.PKAQ
 */
public class RestHelper {

    /**
     * 请求restful服务，返回请求结果
     * @param requestUrl
     * @param requestMethod
     * @param jsonParam
     * @return  请求返回参数
     */
    public String get(String requestUrl,String requestMethod,String jsonParam){

        StringBuffer output = new StringBuffer();
        URL targetUrl = null;
        HttpURLConnection httpConnection = null;
        try {
            targetUrl = new URL(requestUrl);
            httpConnection = (HttpURLConnection) targetUrl.openConnection();

            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);

            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(2000);


            httpConnection.setRequestMethod(requestMethod);
            httpConnection.setRequestProperty("Content-Type", "application/json");

            OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(jsonParam.getBytes());
            outputStream.flush();

            if (httpConnection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
            }

            BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));

            String backMsg;
            while ((backMsg = responseBuffer.readLine()) != null) {
                output.append(backMsg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpConnection.disconnect();
        }

        return String.valueOf(output);
    }
}
