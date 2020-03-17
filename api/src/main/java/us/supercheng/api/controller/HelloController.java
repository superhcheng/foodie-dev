package us.supercheng.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class HelloController {

    @RequestMapping("/")
    public Object hello() {
        return "This is just a test";
    }

}