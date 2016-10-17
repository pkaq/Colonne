package pkaq.colonne.httpunit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import javax.imageio.ImageReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Author: S.PKAQ
 * Datetime: 2016-09-20 19:01
 */
public class GetData {

    public GetData() throws IOException {
    }

    public static void main(String[] args) throws Exception {
        WebClient client = new WebClient(BrowserVersion.INTERNET_EXPLORER_11);
        //默认执行js，如果不执行js，则可能会登录失败，因为用户名密码框需要js来绘制。
        client.getOptions().setJavaScriptEnabled(true);
        client.getOptions().setCssEnabled(false);
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        client.getOptions().setThrowExceptionOnScriptError(false);

        String url = "http://www.scnldj.com";
        HtmlPage page = client.getPage(url);

        // 表单
        HtmlInput username = (HtmlInput) page.getElementById("username");
        username.type("郎敏");
        HtmlInput password = (HtmlInput) page.getElementById("password");
        password.type("123456");
        HtmlInput code = (HtmlInput) page.getElementById("code");
        password.type("00");
        HtmlInput dogInputid = (HtmlInput) page.getElementById("dogInputid");
        password.type("123456");
        HtmlInput usbdogid = (HtmlInput) page.getElementById("usbdogid");
        password.type("123456");
        HtmlInput dogdata = (HtmlInput) page.getElementById("dogdata");
        password.type("1");

        // 验证码
        HtmlImage valiCodeImg = (HtmlImage) page.getElementById("imgcode");
        ImageReader imageReader = valiCodeImg.getImageReader();
        BufferedImage bufferedImage = imageReader.read(0);

        JFrame f2 = new JFrame();
        JLabel l = new JLabel();
        l.setIcon(new ImageIcon(bufferedImage));
        f2.getContentPane().add(l);
        f2.setSize(100, 100);
        f2.setTitle("验证码");
        f2.setVisible(true);

        String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");
        f2.setVisible(false);

        // 提交
        HtmlButton submit = (HtmlButton) page.getElementById("loginBtn");
        HtmlPage nextPage = submit.click();
        System.out.println(nextPage.asXml());
    }
}
