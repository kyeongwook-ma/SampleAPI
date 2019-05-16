package com.recsys.service;

import com.recsys.model.SubAssociatedCard;

import java.util.List;

public interface FilterCardService {
    List<String> retrieveFilteredCardNo(List<SubAssociatedCard> nearest_card_no_list);

    List<String> retrieveFilteredCardNo();
}
