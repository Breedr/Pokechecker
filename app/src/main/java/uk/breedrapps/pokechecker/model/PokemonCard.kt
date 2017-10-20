package uk.breedrapps.pokechecker.model

/**
 * Awesome Pojo Generator
 */
data class PokemonCard(
        var id: String? = null,
        var name: String? = null,
        var nationalPokedexNumber: Int? = null,
        var types: List<String>? = null,
        var subtype: String? = null,
        var supertype: String? = null,
        var hp: String? = null,
        var number: String? = null,
        var artist: String? = null,
        var rarity: String? = null,
        var series: String? = null,
        var set: String? = null,
        var setCode: String? = null,
        var retreatCost: List<String>? = null,
        var text: List<String>? = null,
        var attacks: List<Attacks>? = null,
        var resistances: List<DamageModifier>? = null,
        var weaknesses: List<DamageModifier>? = null,
        var ancientTrait: Ability? = null,
        var evolvesFrom: String? = null,
        var ability: Ability? = null,
        var imageUrl: String? = null,
        var imageUrlHiRes: String? = null
) {

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

