package com.recsys.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.recsys.exception.InvalidIncludePropertiesException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SubAssociatedCard extends BaseModel<SubAssociatedCard> implements Serializable {

    @JsonProperty("card_no")
    @Field("card_no")
    public String card_no;

    @JsonProperty("sim_value")
    @Field("sim_value")
    public double sim_value;

    @JsonProperty("channel_no")
    @Field("channel_no")
    public String channel_no;

    @JsonProperty("community_no")
    @Field("community_no")
    public String community_no;

    @JsonProperty("cafe_no")
    @Field("cafe_no")
    public String cafe_no;

    @JsonProperty("game_no")
    @Field("game_no")
    public String game_no;

    @JsonProperty("game_id")
    @Field("game_id")
    public String game_id;

    @JsonProperty("type")
    @Field("type")
    public String type;

    @Override
    public boolean equals(Object obj) {
        SubAssociatedCard card = (SubAssociatedCard) obj;
        return this.getCard_no().equals(card.getCard_no());
    }

    @Override
    public SubAssociatedCard extractAttributes(Set<String> attrNames) throws IllegalAccessException, InvalidIncludePropertiesException {
        SubAssociatedCard extractedAttrCard = new SubAssociatedCard();

        for (String name : attrNames) {
            java.lang.reflect.Field field = ReflectionUtils.findField(SubAssociatedCard.class, name);

            if (field == null) {
                throw new InvalidIncludePropertiesException("'" + name + "'" + " is not included property in item");
            }

            /* TODO temporary code */
            if ("type".equals(name)) {
                extractedAttrCard.setType("ASSOCIATED");
            } else {
                ReflectionUtils.setField(field, extractedAttrCard, field.get(this));
            }
        }

        return extractedAttrCard;
    }

    @Override
    public SubAssociatedCard instantiateNewModel() {
        return new SubAssociatedCard();
    }

}
