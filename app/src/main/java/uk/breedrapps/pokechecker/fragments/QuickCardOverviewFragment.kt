package uk.breedrapps.pokechecker.fragments

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import kotlinx.android.synthetic.main.fragment_quick_card_overview.*
import uk.breedrapps.pokechecker.R
import uk.breedrapps.pokechecker.model.PokemonCard
import java.lang.Exception
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

            val bundle = Bundle().apply {
                putString("image", card?.imageUrl)
                putStringArrayList("types", card?.types as ArrayList<String>?)
                putString("id", card?.id)
            }

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
        quick_overview_background.background = generateBackground(types?.get(0))
        Glide.with(this).load(imageUrl).crossFade().into(object : GlideDrawableImageViewTarget(quick_overview_card) {
            override fun onResourceReady(resource: GlideDrawable?, animation: GlideAnimation<in GlideDrawable>?) {
                super.onResourceReady(resource, animation)
                quick_overview_progress.visibility = View.GONE
            }

            override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                super.onLoadFailed(e, errorDrawable)
                quick_overview_progress.visibility = View.GONE
            }
        })
    }

    private fun  generateBackground(type: String?): GradientDrawable? {
        return GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                intArrayOf(
                        typeToColor(type).toInt(),
                        Color.argb(230, 0, 0, 0)
                )
        ).apply {
            gradientType = GradientDrawable.LINEAR_GRADIENT
            gradientRadius = 0f
        }
    }

    private fun typeToColor(type: String?): Long {
        return when(type) {
            "Colorless" -> 0xFF958B87
            "Darkness", "Dark" -> 0xFF1D1E1C
            "Dragon" -> 0xFF81690D
            "Fairy" -> 0xFFE03A83
            "Fighting" -> 0xFFA63414
            "Fire" -> 0xFF932B2B
            "Grass" -> 0xFF517805
            "Lightning" -> 0xFFFAB536
            "Metal" -> 0xFF8A776E
            "Psychic" -> 0xFFA65E9A
            "Water" -> 0xFF6890F0
            else -> 0xFFDBDBDB
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_quick_card_overview
    }
}