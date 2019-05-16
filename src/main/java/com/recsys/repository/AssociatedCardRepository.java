package com.recsys.repository;

import com.recsys.model.Card;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssociatedCardRepository extends MongoRepository<Card, String>,
        AssociatedCardRepositoryCustom {
}
