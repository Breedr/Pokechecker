package uk.breedrapps.pokechecker.fragments

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_quick_card_overview.*
import uk.breedrapps.pokechecker.R
import uk.breedrapps.pokechecker.model.PokemonCard
import java.util.*

/**
 * Created by edgeorge on 03/08/2017.
 */
class QuickCardOverviewFragment : BaseFragment() {

    private var id : String? = null
    private var imageUrl : String? = null
    private var types : List<String>? = null

    companion object {
        fun newInstance(card: PokemonCard?) : QuickCardOverviewFragment {

            val bundle = Bundle()
            bundle.putString("image", card?.imageUrl)
            bundle.putStringArrayList("types", card?.types as ArrayList<String>?)
            bundle.putString("id", card?.id)

            val instance = QuickCardOverviewFragment()
            instance.arguments = bundle
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageUrl = arguments.getString("image")
        id = arguments.getString("id")
        types = arguments.getStringArrayList("types")

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quick_overview_background.background = generateBackground()
        Glide.with(this).load(imageUrl).crossFade().into(quick_overview_card)
    }

    private fun  generateBackground(): GradientDrawable? {
        val drawable = GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                intArrayOf(typeToColor().toInt(), Color.argb(230, 0, 0, 0))
        )
        drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        drawable.gradientRadius = 0f
        return drawable
    }

    private fun typeToColor(): Long {
        return when(types?.get(0)) {
            "Colorless" -> 0xFF958B87
            "Dark" -> 0xFF1D1E1C
            "Dragon" -> 0xFF81690D
            "Fairy" -> 0xFFE03A83
            "Fighting" -> 0xFFA63414
            "Fire" -> 0xFF932B2B
            "Grass" -> 0xFF517805
            "Lightning" -> 0xFFFAB536
            "Metal" -> 0xFF8A776E
            "Psychic" -> 0xFFA65E9A
            "Water" -> 0xFF6890F0
            else -> 0xFFFFFFFF
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_quick_card_overview
    }
}