package uk.breedrapps.pokechecker.backend

import uk.breedrapps.pokechecker.model.PokemonCard
import uk.breedrapps.pokechecker.model.PokemonSet

/**
 * Created by edgeorge on 28/07/2017.
 */

data class SetResponse(
        val sets : List<PokemonSet>
)

data class CardResponse(
        val cards : List<PokemonCard>
)