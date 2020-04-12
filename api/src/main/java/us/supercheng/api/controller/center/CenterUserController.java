package us.supercheng.api.controller.center;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.supercheng.utils.APIResponse;

@RestController
@RequestMapping("userInfo")

public class CenterUserController {

    @PostMapping("update")
    public Object update(@RequestParam String userId) {
        if (StringUtils.isBlank(userId))
            return APIResponse.errorMsg("Missing required field userId");

        return APIResponse.errorMsg("Im not there yet");
    }
}
