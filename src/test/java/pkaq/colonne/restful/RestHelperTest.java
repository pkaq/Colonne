package pkaq.colonne.restful;

import org.junit.*;
import pkaq.colonne.constant.RequestMethod;
import pkaq.colonne.restful.util.RestHelper;

import java.util.Date;

/**
 * author: S。PKAQ
 * Datetime: 2016-07-26 10:38
 */
public class RestHelperTest {

    private RestHelper restHelper = new RestHelper();

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
    public void testGet(){
        String url = "http://gc.ditu.aliyun.com/geocoding";
        String paramJson = "{a:泰安市}";
        String str =  restHelper.get(url, RequestMethod.GET,paramJson);
        System.out.println(str);
    }

    @After
    public void afterTest(){
        end = new Date().getTime();

        System.out.println("Method Teet End , use " + (end-start));
    }
}
