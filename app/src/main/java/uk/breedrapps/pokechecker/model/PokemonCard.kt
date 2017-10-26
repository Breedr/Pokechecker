package uk.breedrapps.pokechecker.model

import android.arch.persistence.room.*
import io.reactivex.Flowable
import java.io.Serializable

/**
 * Awesome Pojo Generator
 */
@Entity(tableName = "user_cards")
data class PokemonCard(
        @PrimaryKey
        var id: String? = null,
        var name: String? = null,
        var nationalPokedexNumber: Int? = null,
        @Ignore var types: List<String>? = null,
        var subtype: String? = null,
        var supertype: String? = null,
        var hp: String? = null,
        var number: String? = null,
        var artist: String? = null,
        var rarity: String? = null,
        var series: String? = null,
        var set: String? = null,
        var setCode: String? = null,
        @Ignore var retreatCost: List<String>? = null,
        @Ignore var text: List<String>? = null,
        @Ignore var attacks: List<Attacks>? = null,
        @Ignore var resistances: List<DamageModifier>? = null,
        @Ignore var weaknesses: List<DamageModifier>? = null,
        @Ignore var ancientTrait: Ability? = null,
        var evolvesFrom: String? = null,
        @Ignore var ability: Ability? = null,
        var imageUrl: String? = null,
        var imageUrlHiRes: String? = null
) : Serializable {

    // TODO fix this
    fun adjustedId(): Int {

        try {
            return Integer.parseInt(number)
        } catch (e: NumberFormatException) {

        }

        var id = 1000
        try {
            id += Integer.parseInt(number?.replace("[^\\d.]".toRegex(), ""))
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return id
    }
}

data class Ability (
        val name: String?,
        val text: String?,
        val type: String?
)

data class DamageModifier(
        var type: String? = null,
        var value: String? = null
)

data class Attacks(
        var damage: String? = null,
        val cost: List<String>? = null,
        var name: String? = null,
        var text: String? = null,
        var convertedEnergyCost: Int = 0
)

@Dao
interface PokemonCardDao {

    @Query("SELECT * FROM user_cards")
    fun getAllCards(): Flowable<List<PokemonCard>>

    @Query("SELECT * FROM user_cards WHERE id = :cardId LIMIT 1")
    fun getCardWithId(id: String): Flowable<PokemonCard>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(card: PokemonCard)

    @Delete
    fun delete(card: PokemonCard)
}

