package com.recsys.service;

import com.recsys.model.Card;
import com.recsys.model.SubAssociatedCard;
import com.recsys.repository.AssociatedCardRepository;
import io.lettuce.core.RedisCommandTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;
import java.util.stream.Collectors;

@Service("redisService")
public class RedisServiceImpl implements RedisService<List<SubAssociatedCard>> {

    private final AssociatedCardRepository associatedCardRepository;

    private final RedisTemplate<String, List<SubAssociatedCard>> redisTemplate;

    private Logger logger = LogManager.getLogger("stbLogger");

    private HashOperations<String, String, List<SubAssociatedCard>> hashOperations;

    @Autowired
    public RedisServiceImpl(AssociatedCardRepository associatedCardRepository,
                            RedisTemplate<String, List<SubAssociatedCard>> redisTemplate) {
        this.associatedCardRepository = associatedCardRepository;
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    @Async
    public void putData(String id, List<SubAssociatedCard> card) {
        hashOperations.put("cards", id, card);
    }

    @Override
    public void delete(List<String> cardNos) {
        hashOperations.delete("cards", cardNos.toArray());
    }

    @Override
    public List<SubAssociatedCard> getData(String id) throws RedisCommandTimeoutException {
        return hashOperations.get("cards", id);
    }

    @Override
    public Mono<Void> fillRedis() {
        logger.info("mongo fetch start");
        List<Card> associatedCards = associatedCardRepository.getAll();
        logger.info("mongo fetch done");

        loadData2Redis(associatedCards);
        return Mono.empty();
    }


    private void loadData2Redis(List<Card> associatedCards) {

        logger.info("redis loading start");

        Flux.fromIterable(associatedCards)
                .subscribeOn(Schedulers.elastic())
                .buffer(1000 * 4)
                .doOnComplete(() -> logger.info("redis loading done"))
                .subscribe(cards -> {

                    Map<String, List<SubAssociatedCard>> maps =
                            cards
                                    .parallelStream()
                                    .collect(Collectors.toMap(Card::get_id, Card::getNearest_card_no_list));

                    logger.info("redis insert start");

                    hashOperations.putAll("cards", maps);

                    logger.info("redis insert finished");
                });

    }

    @Override
    public Mono<Void> fillRedis(String startDate, String endDate) {
        logger.info("1. mongo fetch start");

        Mono.defer(() -> Mono.just(associatedCardRepository.getCardsByDate(startDate, endDate)))
                .subscribeOn(Schedulers.elastic())
                .subscribe(cards -> {

                    logger.info("1. mongo fetch done");

                    loadData2Redis(cards);

                    logger.info("2. get current key start");

                    Set<String> currentKeys = hashOperations.keys("cards");

                    logger.info("2. get current key done");

                    logger.info("3. remove key start");

                    List<String> willBeUpdated = cards
                            .stream()
                            .map(Card::get_id)
                            .collect(Collectors.toList());

                    currentKeys.removeAll(willBeUpdated);

                    logger.info("3. remove key done");

                    logger.info("4. delete key start");

                    delete(new ArrayList<>(currentKeys));

                    logger.info("4. delete key done");
                });

        return Mono.empty();
    }

    @Override
    public Mono<Void> clearRedis() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
                .getConnection().serverCommands().flushAll();
        return Mono.empty();
    }
}
