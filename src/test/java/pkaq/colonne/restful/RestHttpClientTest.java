package pkaq.colonne.restful;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pkaq.colonne.restful.util.RestHttpClient;

import java.io.IOException;
import java.util.Date;

/**
 * author: S.PKAQ
 * Datetime: 2016-07-26 14:53
 * To change this template use File | Settings | File Templates.
 */
public class RestHttpClientTest {

    private RestHttpClient restHttpClient = new RestHttpClient();
    private long start = 0l;
    private long end = 0l;

    @BeforeClass
    public static void beforeClass(){
        System.err.println("RestfulHelper Test Begin");
    }

    @Before
    public void beforeTest(){
        System.out.println("Method Test Begin");
        start = new Date().getTime();
    }

    @Test
    public void testPost(){
        String url = "http://gc.ditu.aliyun.com/geocoding";
        String paramJson = "{a:苏州市}";
        try{
            String str = restHttpClient.post(url,paramJson);
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void afterTest(){
        end = new Date().getTime();

        System.out.println("Method Teet End , use " + (end-start));
    }
}
