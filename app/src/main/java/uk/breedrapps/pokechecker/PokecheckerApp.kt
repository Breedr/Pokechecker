package uk.breedrapps.pokechecker

import android.app.Application
import android.arch.persistence.room.Room
import com.facebook.stetho.Stetho
import uk.breedrapps.pokechecker.backend.PokemonTcgApi
import uk.breedrapps.pokechecker.backend.components.DaggerNetworkComponent
import uk.breedrapps.pokechecker.backend.components.NetworkComponent
import uk.breedrapps.pokechecker.backend.modules.NetworkingModule
import uk.breedrapps.pokechecker.backend.modules.TcgApiModule
import uk.breedrapps.pokechecker.database.CardDatabase

/**
 * Created by edgeorge on 28/07/2017.
 */

class PokecheckerApp : Application() {

    lateinit var networkComponent: NetworkComponent

    companion object {
        var database: CardDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            // Initialise Stetho library - visit chrome://inspect/#devices in browser
            Stetho.initializeWithDefaults(this)
        }

        database = Room.databaseBuilder(this, CardDatabase::class.java, "pokechecker").build()

        networkComponent = DaggerNetworkComponent.builder()
                .networkingModule(NetworkingModule(PokemonTcgApi.HOST))
                .tcgApiModule(TcgApiModule())
                .build()

    }
}