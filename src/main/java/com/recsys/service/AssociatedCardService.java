package com.recsys.service;

import com.recsys.model.SubAssociatedCard;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;

public interface AssociatedCardService {

    List<SubAssociatedCard> getAssociatedCards(String cardNum,
                                               @Nullable List<DynamicQuery> queries,
                                               Set<String> attrNames);

}
