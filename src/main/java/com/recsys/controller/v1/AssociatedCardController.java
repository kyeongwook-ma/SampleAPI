
package com.recsys.controller.v1;

import com.recsys.Constants;
import com.recsys.controller.ItemsResponse;
import com.recsys.controller.QueryParser;
import com.recsys.controller.v1_1.response.BaseResponse;
import com.recsys.controller.v1_1.response.ResponseCreatorV1_1;
import com.recsys.exception.InvalidQueryException;
import com.recsys.model.SubAssociatedCard;
import com.recsys.service.AssociatedCardService;
import com.recsys.service.DynamicQuery;
import com.recsys.service.PageCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(AssociatedCardController.RESOURCES.ROOT)
public class AssociatedCardController {

    private final AssociatedCardService associatedCardService;


    @Autowired
    public AssociatedCardController(AssociatedCardService associatedCardService) {
        this.associatedCardService = associatedCardService;
    }


    @GetMapping(
            value = RESOURCES.GET_RELATED_CARDS,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<ItemsResponse<SubAssociatedCard>>> getAssociatedCards(
            @PathVariable(value = "card_no") String cardNum,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "50") int size,
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "include_properties",
                    defaultValue = "card_no",
                    required = false) String includeProperties
    ) throws InvalidQueryException, NumberFormatException {

        List<DynamicQuery> queries = QueryParser.parse(q);
        Set<String> attrNames = new HashSet<>();

        /* parse 'include_properties' */
        if (includeProperties != null) {
            attrNames = new HashSet<>(Arrays.asList(includeProperties.split(" ")));
        }

        /* must include card number */
        attrNames.add("card_no");

        List<SubAssociatedCard> associatedCards = associatedCardService.getAssociatedCards(cardNum,
                queries, attrNames);

        return ResponseEntity.ok(ResponseCreatorV1_1.generateResponse(
                PageCreator.generatePage(associatedCards,
                        page, size),
                page, size));

    }

    public static class RESOURCES {
        public static final String ROOT = Constants.SupportedVersions.V1_0;
        public static final String GET_RELATED_CARDS = "/card/{card_no}";
    }


}

