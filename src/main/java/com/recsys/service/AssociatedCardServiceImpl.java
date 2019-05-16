package com.recsys.service;

import com.recsys.exception.NotFoundResourceException;
import com.recsys.model.SubAssociatedCard;
import com.recsys.repository.AssociatedCardRepository;
import io.lettuce.core.RedisCommandTimeoutException;
import io.lettuce.core.RedisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AssociatedCardServiceImpl implements AssociatedCardService {

    private final AssociatedCardRepository associatedCardRepository;

    private final RedisService<List<SubAssociatedCard>> redisService;

    private final FilterCardService filterCardService;

    private FilterCreator<SubAssociatedCard> filterCreator = new FilterCreator<>();


    @Autowired
    public AssociatedCardServiceImpl(AssociatedCardRepository associatedCardRepository, RedisService<List<SubAssociatedCard>> redisService, FilterCardService filterCardService) {
        this.associatedCardRepository = associatedCardRepository;
        this.redisService = redisService;
        this.filterCardService = filterCardService;
    }

    @Override
    public List<SubAssociatedCard> getAssociatedCards(String cardNum,
                                                      List<DynamicQuery> queries, Set<String> attrNames) {

        /* retrieve filtered card data from coway */
        List<String> filteredCardNos = filterCardService.retrieveFilteredCardNo();

        return findTargetCard(cardNum)
                .stream()
                /* filtering card from coway */
                .filter(cardInnerKey -> !filteredCardNos.contains(cardInnerKey.getCard_no()))
                /* filtering card by dynamic query */
                .filter(filterCreator.generateFilters(SubAssociatedCard.class, queries))
                /* extract some attribute from 'include_properties' parameter */
                .map(subAssociatedCard -> {
                    try {
                        return subAssociatedCard.extractAttributes(attrNames);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return new SubAssociatedCard();
                })
                .collect(Collectors.toList());
    }

    private List<SubAssociatedCard> getCardFromRedis(String cardNum) throws RedisConnectionFailureException,
            RedisCommandTimeoutException {
        return redisService.getData(cardNum);
    }

    private List<SubAssociatedCard> findTargetCard(String cardNum) {

        try {
            List<SubAssociatedCard> fromRedis = getCardFromRedis(cardNum);

            /* assume that all datas on redis */
            if (fromRedis == null) {
                throw new NotFoundResourceException();
            }

            return fromRedis;

        } catch (RedisConnectionFailureException | RedisException e) {
            e.printStackTrace();
        }

        /* if fail to fetch data from redis return empty list */
        return new ArrayList<>();
    }


}
