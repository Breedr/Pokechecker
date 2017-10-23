package uk.breedrapps.pokechecker

import android.app.Application
import com.facebook.stetho.Stetho
import uk.breedrapps.pokechecker.backend.PokemonTcgApi
import uk.breedrapps.pokechecker.backend.components.DaggerNetworkComponent
import uk.breedrapps.pokechecker.backend.components.NetworkComponent
import uk.breedrapps.pokechecker.backend.modules.NetworkingModule
import uk.breedrapps.pokechecker.backend.modules.TcgApiModule

/**
 * Created by edgeorge on 28/07/2017.
 */

class PokecheckerApp : Application() {

    lateinit var networkComponent: NetworkComponent


    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            // Initialise Stetho library - visit chrome://inspect/#devices in browser
            Stetho.initializeWithDefaults(this)
        }

        networkComponent = DaggerNetworkComponent.builder()
                .networkingModule(NetworkingModule(PokemonTcgApi.HOST))
                .tcgApiModule(TcgApiModule())
                .build()

    }
}