package us.supercheng.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import us.supercheng.utils.APIResponse;

@RestController
@RequestMapping("redis_test")
public class RedisTestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("get")
    public APIResponse getValue(String key) {
        if (StringUtils.isBlank(key))
            return APIResponse.ok();

        return APIResponse.ok(this.redisTemplate.opsForValue().get(key));
    }

    @PostMapping("set")
    public APIResponse setValue(String key, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value))
            return APIResponse.errorMsg("Empty key and/or value");

        this.redisTemplate.opsForValue().set(key, value);
        return APIResponse.ok();
    }

    @DeleteMapping("del")
    public APIResponse delValue(String key) {
        if (StringUtils.isBlank(key))
            return APIResponse.ok();

        if (!this.redisTemplate.delete(key))
            return APIResponse.errorMsg("Could not delete key: " + key);
        return APIResponse.ok();
    }
}