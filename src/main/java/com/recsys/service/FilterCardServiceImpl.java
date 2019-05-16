package com.recsys.service;

import com.recsys.model.FilteredCard;
import com.recsys.model.SubAssociatedCard;
import com.recsys.repository.AssociatedCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterCardServiceImpl implements FilterCardService {

    @Autowired
    AssociatedCardRepository cardRepository;

    @Override
    public List<String> retrieveFilteredCardNo(List<SubAssociatedCard> nearestCardList) {

        return cardRepository.getFilteredCards()
                .stream().map(FilteredCard::getCard_no)
                .collect(Collectors.toList());

    }

    /* TODO :
     * it has to be changed to call recsys-coway API with caching
     * then both API need to update cache for change filtered_cards database
     * */
    @Override
    public List<String> retrieveFilteredCardNo() {
        return cardRepository.getFilteredCards()
                .stream().map(FilteredCard::getCard_no)
                .collect(Collectors.toList());
    }


}
