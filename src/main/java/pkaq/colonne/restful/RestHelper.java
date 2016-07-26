package pkaq.colonne.restful;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
  * Author: S.PKAQ
  * Date: ${DATE}
  * Time: ${TIME}
  * To change this template use File | Settings | File Templates.
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

        try {
            URL targetUrl = new URL(requestUrl);

            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
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

            String backMsg = "";
            while ((backMsg = responseBuffer.readLine()) != null) {
                output.append(backMsg);
            }

            httpConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return String.valueOf(output);
    }
}
