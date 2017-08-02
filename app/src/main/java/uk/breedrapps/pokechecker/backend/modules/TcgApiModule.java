package uk.breedrapps.pokechecker.backend.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import uk.breedrapps.pokechecker.backend.PokemonTcgApi;

@Module
public class TcgApiModule {
    @Provides
    @Singleton
    PokemonTcgApi provideTcgBackend(Retrofit retrofit){
        return retrofit.create(PokemonTcgApi.class);
    }
}
