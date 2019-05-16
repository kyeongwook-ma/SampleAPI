package com.recsys.service;

import io.lettuce.core.RedisCommandTimeoutException;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RedisService<T> {

    void putData(String id, T t);

    void delete(List<String> ids);

    T getData(String id) throws RedisCommandTimeoutException;

    Mono<Void> fillRedis();

    Mono<Void> fillRedis(String startDate, String endDate);

    Mono<Void> clearRedis();
}
