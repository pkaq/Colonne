package xingge;

import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;
import com.tencent.xinge.TimeInterval;
import org.json.JSONObject;

import java.util.Map;
import java.util.HashMap;

/**
 * @author : cpc
 * @description : 腾讯信鸽服务端测试
 * @date : 2015/12/3
 */
public class Xingge {

    //QQ申请应用中应用配置中
    public static String secretKey = "4ecdf92000b5a8de10ee59fddb4108f7";
    //QQ申请获得的安卓安装包安装后获得
    public static String token = "3be07c5903200c164457badba7346b05d285e54c";
    //QQ申请应用中应用配置中accessId
    public static long accessId = 2100168968;

    public static void main(String[] args) {
        XingeApp xinge = new XingeApp(accessId, secretKey);
        Message message1 = new Message();
        message1.setType(Message.TYPE_NOTIFICATION);
        Style style = new Style(1);
        style = new Style(3,1,0,1,0);
        ClickAction action = new ClickAction();
        action.setActionType(ClickAction.TYPE_URL);
        action.setUrl("http://xg.qq.com");
        Map<String, Object> custom = new HashMap<String, Object>();
        custom.put("key1", "value1");
        custom.put("key2", 2);
        message1.setTitle("测试标题");
        message1.setContent("内容测试");
        message1.setStyle(style);
        message1.setAction(action);
        message1.setCustom(custom);
        TimeInterval acceptTime1 = new TimeInterval(0,0,23,59);
        message1.addAcceptTime(acceptTime1);
        JSONObject ret = xinge.pushAllDevice(0, message1);
        System.out.println(ret);
    }
}
