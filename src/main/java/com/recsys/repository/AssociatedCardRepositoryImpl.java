package com.recsys.repository;

import com.recsys.model.Card;
import com.recsys.model.FilteredCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AssociatedCardRepositoryImpl extends QuerydslRepositorySupport implements AssociatedCardRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AssociatedCardRepositoryImpl(MongoOperations operations, MongoTemplate mongoTemplate) {
        super(operations);
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public Card getAssociatedCard(String cardNum) {
        Query q = Query.query(Criteria.where("_id").is(cardNum));
        return mongoTemplate.findOne(q, Card.class);
    }

    @Override
    public List<FilteredCard> getFilteredCards(List<String> ids) {
        Query q = new Query();
        q.addCriteria(Criteria.where("card_no").in(ids)).fields().include("card_no");
        return mongoTemplate.find(q, FilteredCard.class);
    }

    @Override
    @Cacheable(cacheNames = "filteredCards",
            cacheManager = "cacheManager")
    public List<FilteredCard> getFilteredCards() {
        return mongoTemplate.findAll(FilteredCard.class);
    }

    @Override
    public List<Card> getCardsByDate(String startDate, String endDate) {

        Query q = new Query();
        q.addCriteria(Criteria.where("start_date").gte(startDate)
                .and("end_date").lte(endDate));
        q.fields().include("nearest_card_no_list");

        return mongoTemplate.find(q, Card.class);
    }

    @Override
    public List<Card> getAll() {
        return mongoTemplate.findAll(Card.class, "item2item_live_lite");
    }
}
