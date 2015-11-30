package pingpp;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.exception.PingppException;
import com.pingplusplus.model.App;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : cpc
 * @description :
 * @date : 2015/11/30
 */
public class PingPPTest {
    /**
     * pingpp 管理平台对应的 API key
     */
    public static String apiKey = "sk_test_4mTSKGDarbz5S4urr5XnXbH0";
    /**
     * pingpp 管理平台对应的应用 ID
     */
    public static String appId = "app_arPGC4Su5KmH8qL8";

    public static void main(String[] args) {
        Pingpp.apiKey = apiKey;
        PingPPTest ce = new PingPPTest();
        System.out.println("---------创建 charge");
        Charge charge = ce.charge();
        System.out.println("---------查询 charge");
        ce.retrieve(charge.getId());
        System.out.println("---------查询 charge列表");
        ce.all();
    }

    /**
     * 创建 Charge
     *
     * 创建 Charge 用户需要组装一个 map 对象作为参数传递给 Charge.create();
     * map 里面参数的具体说明请参考：https://pingxx.com/document/api#api-c-new
     * @return
     */
    public Charge charge() {
        Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", 100);
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", "Your Subject");
        chargeMap.put("body", "Your Body");
        chargeMap.put("order_no", "123456789");
        chargeMap.put("channel", "alipay");
        chargeMap.put("client_ip", "127.0.0.1");
        Map<String, String> app = new HashMap<String, String>();
        app.put("id",appId);
        chargeMap.put("app", app);
        try {
            //发起交易请求
            charge = Charge.create(chargeMap);
            System.out.println(charge);
        } catch (PingppException e) {
            e.printStackTrace();
        }
        return charge;
    }

    /**
     * 查询 Charge
     *
     * 该接口根据 charge Id 查询对应的 charge 。
     * 参考文档：https://pingxx.com/document/api#api-c-inquiry
     *
     * 该接口可以传递一个 expand ， 返回的 charge 中的 app 会变成 app 对象。
     * 参考文档： https://pingxx.com/document/api#api-expanding
     * @param id
     */
    public void retrieve(String id) {
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            List<String> expande = new ArrayList<String>();
            expande.add("app");
            param.put("expand", expande);
            //Charge charge = Charge.retrieve(id);
            //Expand app
            Charge charge = Charge.retrieve(id, param);
            if (charge.getApp() instanceof App) {
                //App app = (App) charge.getApp();
                // System.out.println("App Object ,appId = " + app.getId());
            } else {
                // System.out.println("String ,appId = " + charge.getApp());
            }

            System.out.println(charge);

        } catch (PingppException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分页查询Charge
     *
     * 该接口为批量查询接口，默认一次查询10条。
     * 用户可以通过添加 limit 参数自行设置查询数目，最多一次不能超过 100 条。
     *
     * 该接口同样可以使用 expand 参数。
     * @return
     */
    public ChargeCollection all() {
        ChargeCollection chargeCollection = null;
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("limit", 3);

        //增加此处设施，刻意获取app expande
        //        List<String> expande = new ArrayList<String>();
        //        expande.add("app");
        //        chargeParams.put("expand", expande);

        try {
            chargeCollection = Charge.all(chargeParams);
            System.out.println(chargeCollection);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeCollection;
    }
}
