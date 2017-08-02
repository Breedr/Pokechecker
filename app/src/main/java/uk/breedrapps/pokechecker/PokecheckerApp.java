package uk.breedrapps.pokechecker;

import android.app.Application;

import com.facebook.stetho.Stetho;

import uk.breedrapps.pokechecker.backend.PokemonTcgApi;
import uk.breedrapps.pokechecker.backend.components.DaggerNetworkComponent;
import uk.breedrapps.pokechecker.backend.components.NetworkComponent;
import uk.breedrapps.pokechecker.backend.modules.NetworkingModule;
import uk.breedrapps.pokechecker.backend.modules.TcgApiModule;

/**
 * Created by edgeorge on 28/07/2017.
 */

public class PokecheckerApp extends Application {

    private NetworkComponent networkComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            // Initialise Stetho library - visit chrome://inspect/#devices in browser
            Stetho.initializeWithDefaults(this);
        }

        networkComponent = DaggerNetworkComponent.builder()
                .networkingModule(new NetworkingModule(PokemonTcgApi.HOST))
                .tcgApiModule(new TcgApiModule())
                .build();

    }

    public NetworkComponent getNetworkComponent() {
        return networkComponent;
    }
}