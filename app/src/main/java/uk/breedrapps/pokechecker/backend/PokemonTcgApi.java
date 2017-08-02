package uk.breedrapps.pokechecker.backend;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by edgeorge on 28/07/2017.
 */

public interface PokemonTcgApi {

    String HOST = "https://api.pokemontcg.io/v1/";

    @GET("sets")
    Observable<SetResponse> getSets();

    @GET("cards")
    Observable<CardResponse> getCardsForSet(@Query("setCode") String setCode, @Query("pageSize") int pageSize);
}
