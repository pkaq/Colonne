package pkaq.colonne.restful.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pkaq.colonne.restful.response.Response;

/**
 * Created with IntelliJ IDEA.
 * Author: S.PKAQ
 * Datetime: 2016-10-17 15:16
 */
@RestController
@RequestMapping("/service")
public class SyncCallback {

    @RequestMapping("/x-01-001")
    public Response success(){
        System.out.println("");
        return new Response("true","success");
    }

    @RequestMapping("/x-01-002")
    public Response failed(){
        return new Response("false","faild");
    }
}
