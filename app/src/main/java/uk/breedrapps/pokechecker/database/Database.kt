package uk.breedrapps.pokechecker.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import uk.breedrapps.pokechecker.model.PokemonCard
import uk.breedrapps.pokechecker.model.PokemonCardDao

/**
 * Created by edgeorge on 23/10/2017.
 */

@Database(entities = arrayOf(PokemonCard::class), version = 1, exportSchema = true)
abstract class CardDatabase : RoomDatabase() {
    abstract fun cardDao(): PokemonCardDao
}