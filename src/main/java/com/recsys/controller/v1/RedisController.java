package com.recsys.controller.v1;

import com.recsys.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@ConditionalOnExpression("${control.redis:false}")
@RequestMapping(value = "/redis")
public class RedisController {

    private final RedisService redisService;

    @Autowired
    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @RequestMapping(value = "/clear")
    public Mono<ResponseEntity> clearRedis() {
        return redisService.clearRedis()
                .map(aVoid -> ResponseEntity.ok().build());
    }

    @RequestMapping(value = "/fill")
    public Mono<ResponseEntity> fillRedis(
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate
    ) {

        if (startDate == null || endDate == null) {
            redisService.fillRedis().subscribe();
            return Mono.just(ResponseEntity.ok().build());
        } else {
            redisService.fillRedis(startDate, endDate).subscribe();
            return Mono.just(ResponseEntity.ok().build());
        }

    }

}
