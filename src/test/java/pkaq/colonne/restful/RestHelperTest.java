package pkaq.colonne.restful;

import org.junit.*;
import pkaq.colonne.constant.RequestMethod;

/**
 * Created with IntelliJ IDEA.
 * author: S。PKAQ
 * Datetime: 2016-07-26 10:38
 * To change this template use File | Settings | File Templates.
 */
public class RestHelperTest {

    private RestHelper restHelper = new RestHelper();

    @BeforeClass
    public static void beforeClass(){
        System.err.println("RestfulHelper Test Begin");
    }

    @Before
    public void beforeTest(){
        System.out.println("Method Test Begin");
    }

    @Test
    public void testGet(){
        String url = "http://gc.ditu.aliyun.com/geocoding";
        String paramJson = "{a:苏州市}";
        restHelper.get(url, RequestMethod.GET,paramJson);
    }

    @After
    public void afterTest(){
        System.out.println("Method Teet End ");
    }
    @AfterClass
    public static void afterClass(){
        System.err.println("RestfulHelper Test End");
    }
}
