package uk.breedrapps.pokechecker.activities

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.OrientationHelper
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_set.*
import uk.breedrapps.pokechecker.R
import uk.breedrapps.pokechecker.adapters.BaseListAdapter
import uk.breedrapps.pokechecker.adapters.CardListAdapter
import uk.breedrapps.pokechecker.model.PokemonCard
import uk.breedrapps.pokechecker.model.PokemonSet
import uk.breedrapps.pokechecker.util.DisposingObserver
import uk.breedrapps.pokechecker.util.webObservable
import java.util.concurrent.TimeUnit

/**
 * Created by edgeorge on 02/08/2017.
 */
class SetActivity : BaseActivity(), BaseListAdapter.OnItemClickedListener<PokemonCard>,
        AppBarLayout.OnOffsetChangedListener {

    companion object {
        private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.75f
        private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
        private val ALPHA_ANIMATIONS_DURATION : Long = 200
        private var mIsTheTitleVisible = false
        private var mIsTheTitleContainerVisible = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        set_appbar.addOnOffsetChangedListener(this)

        set_ken_burns.setScaleType(ImageView.ScaleType.CENTER_CROP)
        set_ken_burns.initResourceIDs(
                mutableListOf(
                        R.drawable.flashfire_header2
                )
        )

        val decoration = DividerItemDecoration(this, OrientationHelper.VERTICAL)
        decoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.main_divider))

        set_recycler_view.adapter = CardListAdapter(this)
        set_recycler_view.addItemDecoration(decoration)
        set_recycler_view.layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_fall)

        val set = intent.getParcelableExtra<PokemonSet>("set")

        set_title.text = set.name
        set_main_title.text = set.name
        set_main_subtitle.text = getString(R.string.released, set.releaseDate)

        Glide.with(this).load(set.logoUrl()).into(set_image)

        api.getCardsForSet(set.code, 250)
                .webObservable()
                .doOnError(Throwable::printStackTrace)
                .doOnSubscribe { _ -> showLoadingDialog(true) }
                .doFinally { showLoadingDialog(false) }
                .debounce(5, TimeUnit.SECONDS)
                .map { it.cards }
                .map { it.sortedWith(compareBy(PokemonCard::adjustedId)) }
                .subscribe(object : DisposingObserver<List<PokemonCard>>() {
                    override fun onNext(list: List<PokemonCard>) {
                        (set_recycler_view.adapter as CardListAdapter).setData(list)
                        set_recycler_view.scheduleLayoutAnimation()
                    }
                })
    }

    private fun showLoadingDialog(show: Boolean) {

    }

    override fun getLayout(): Int {
        return R.layout.activity_set
    }

    override fun onItemClicked(item: PokemonCard?) {1
        Log.d("SetActivity", item.toString())
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val maxScroll = appBarLayout?.totalScrollRange
        val percentage = Math.abs(verticalOffset).div(maxScroll!!.toFloat())
        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(toolbar!!, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                mIsTheTitleVisible = true
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(toolbar!!, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                mIsTheTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimations(set_linearlayout_title, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                mIsTheTitleContainerVisible = false
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimations(set_linearlayout_title, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                mIsTheTitleContainerVisible = true
            }
        }
    }

    private fun startAlphaAnimations(linearLayout: LinearLayout, duration: Long, visibility: Int) {
        for (index in 0..linearLayout.childCount - 1) {
            startAlphaAnimation(linearLayout.getChildAt(index), duration, visibility)
        }
    }

    private fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
        val alphaAnimation = if (visibility == View.VISIBLE)
            AlphaAnimation(0f, 1f)
        else
            AlphaAnimation(1f, 0f)

        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
    }
}