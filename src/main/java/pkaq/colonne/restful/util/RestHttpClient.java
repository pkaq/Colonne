package pkaq.colonne.restful.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 采用Httpclient工具包请求restful服务
 * author: S.PKAQ
 * Datetime: 2016-07-26 14:15
 * To change this template use File | Settings | File Templates.
 */
public class RestHttpClient {

    /**
     * 通过get方式请求restful服务获取数据
     * @param requestUrl
     * @return 请求结果
     * @throws IOException
     */
    public String get(String requestUrl) throws IOException {

       try(CloseableHttpClient httpClient = HttpClients.createDefault()){
           HttpGet httpGet = new HttpGet(requestUrl);

           httpGet.addHeader("content-type", "application/json");
           httpGet.addHeader("Accept", "application/json");

           CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
           String response = EntityUtils.toString(httpResponse.getEntity());

           httpResponse.close();
           return response;
       }
    }

    /**
     * 通过post方式获取返回值
     * @param requestUrl
     * @param jsonParam
     * @return 请求返回内容
     * @throws IOException
     */
    public String post(String requestUrl,String jsonParam) throws IOException {
       try(CloseableHttpClient httpClient = HttpClients.createDefault()){
           HttpPost httpPost = new HttpPost(requestUrl);

           httpPost.addHeader("content-type", "application/json");
           httpPost.addHeader("Accept", "application/json");

           StringEntity stringEntity = new StringEntity(jsonParam);
           httpPost.setEntity(stringEntity);

           CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
           String response = EntityUtils.toString(httpResponse.getEntity());

           httpResponse.close();

           return response;
       }
    }
}
