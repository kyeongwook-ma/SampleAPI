package com.recsys.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Document(collection = "filtered_cards")
public class FilteredCard {
    @Id
    @JsonProperty("card_no")
    @Field("card_no")
    public String card_no;

    @JsonProperty("filtered_date")
    @Field("filtered_date")
    public Date filtered_date;

    @JsonProperty("game_no")
    @Field("game_no")
    public String game_no;

    @JsonProperty("cafe_no")
    @Field("cafe_no")
    public String cafe_no;

    @JsonProperty("channel_no")
    @Field("channel_no")
    public String channel_no;

}
