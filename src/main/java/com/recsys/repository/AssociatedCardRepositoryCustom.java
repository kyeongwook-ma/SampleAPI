package com.recsys.repository;

import com.recsys.model.Card;
import com.recsys.model.FilteredCard;

import java.util.List;

public interface AssociatedCardRepositoryCustom {
    Card getAssociatedCard(String cardNum);

    List<FilteredCard> getFilteredCards(List<String> ids);

    List<FilteredCard> getFilteredCards();

    List<Card> getCardsByDate(String startDate, String endDate);

    List<Card> getAll();
}
