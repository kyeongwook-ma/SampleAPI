package com.recsys.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Document(collection = "item2item_live_lite")
public class Card extends BaseModel<Card> implements Serializable {
    @Id
    @JsonProperty("_id")
    @Field("_id")
    public String _id;

    @JsonProperty("card_no")
    @Field("card_no")
    public String card_no;

    @JsonProperty("sim_value")
    @Field("sim_value")
    public double sim_value;

    @JsonProperty("how_many_training_steps")
    @Field("how_many_training_steps")
    public long how_many_training_steps;

    @JsonProperty("embedding_size")
    @Field("embedding_size")
    public int embedding_size;

    @JsonProperty("weight_reg_date")
    @Field("weight_reg_date")
    public String weight_reg_date;

    @JsonProperty("channel_no")
    @Field("channel_no")
    public String channel_no;

    @JsonProperty("game_no")
    @Field("game_no")
    public String game_no;

    @JsonProperty("cafe_no")
    @Field("cafe_no")
    public String cafe_no;

    @JsonProperty("community_no")
    @Field("community_no")
    public String community_no;

    @JsonProperty("cafe_key")
    @Field("cafe_key")
    public String cafe_key;

    @JsonProperty("country_cd")
    @Field("country_cd")
    public String country_cd;

    @JsonProperty("client_lang_cd")
    @Field("client_lang_cd")
    public String client_lang_cd;

    @JsonProperty("site_lang_cd")
    @Field("site_lang_cd")
    public String site_lang_cd;

    @JsonProperty("result_reg_date")
    @Field("result_reg_date")
    public Date result_reg_date;

    @JsonProperty("source_version")
    @Field("source_version")
    public String source_version;

    @JsonProperty("annoy_ntrees")
    @Field("annoy_ntrees")
    public int annoy_ntrees;

    @JsonProperty("use_window_size")
    @Field("use_window_size")
    public int use_window_size;

    @JsonProperty("similar_algorithm")
    @Field("similar_algorithm")
    public String similar_algorithm;

    @JsonProperty("start_date")
    @Field("start_date")
    public String start_date;

    @JsonProperty("end_date")
    @Field("end_date")
    public String end_date;

    @JsonProperty("nearest_card_no_list")
    @Field("nearest_card_no_list")
    public List<SubAssociatedCard> nearest_card_no_list;

    @Override
    public Card instantiateNewModel() {
        return new Card();
    }

}


