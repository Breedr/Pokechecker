package uk.breedrapps.pokechecker.activities

import android.app.SearchManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import kotlinx.android.synthetic.main.activity_set.*
import uk.breedrapps.pokechecker.R
import uk.breedrapps.pokechecker.adapters.BaseListAdapter
import uk.breedrapps.pokechecker.adapters.CardListAdapter
import uk.breedrapps.pokechecker.backend.PokemonTcgApi
import uk.breedrapps.pokechecker.fragments.QuickCardOverviewFragment
import uk.breedrapps.pokechecker.model.PokemonCard
import uk.breedrapps.pokechecker.model.PokemonSet
import uk.breedrapps.pokechecker.util.DisposingObserver
import uk.breedrapps.pokechecker.util.webObservable
import uk.breedrapps.pokechecker.views.AppBarStateChangedListener
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 * Created by edgeorge on 02/08/2017.
 */
class SetActivity : BaseActivity(), BaseListAdapter.OnItemClickedListener<PokemonCard>,
        AppBarLayout.OnOffsetChangedListener, SearchView.OnQueryTextListener {

    private lateinit var searchView: SearchView
    private lateinit var searchMenuItem: MenuItem

    companion object {
        private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.75f
        private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
        private val ALPHA_ANIMATIONS_DURATION : Long = 200
        private var pokemonSet: PokemonSet? = null
        private var isTitleVisible = false
        private var isTitleContainerVisible = true
        private var setLogoDrawable: Drawable? = null
        private var setImageError: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pokemonSet = intent.getParcelableExtra<PokemonSet>("pokemonSet")

        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupAppBarLayout()

        setupKenBurnsView()

        setupRecyclerView()

        configureViewsForSet()

        api.getCardsForSet(pokemonSet?.code, PokemonTcgApi.DEFAULT_PAGE_SIZE)
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

    override fun getLayout(): Int {
        return R.layout.activity_set
    }

    override fun onItemClicked(item: PokemonCard?) {
        Log.d("SetActivity", item.toString())

        val fragment = QuickCardOverviewFragment.newInstance(item)

        val transaction = supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out
                )


        transaction.replace(android.R.id.content, fragment)
                .addToBackStack(null).commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.set_menu, menu)
        configureSearchView(menu)
        return true
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val maxScroll = appBarLayout?.totalScrollRange
        val percentage = Math.abs(verticalOffset).div(maxScroll!!.toFloat())
        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("not implemented")
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("not implemented")
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!isTitleVisible) {
                startAlphaAnimation(toolbar!!, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                isTitleVisible = true
            }

        } else {

            if (isTitleVisible) {
                startAlphaAnimation(toolbar!!, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                isTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (isTitleContainerVisible) {
                startAlphaAnimations(set_linearlayout_title, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                isTitleContainerVisible = false
            }
        } else {
            if (!isTitleContainerVisible) {
                startAlphaAnimations(set_linearlayout_title, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                isTitleContainerVisible = true
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

    private fun configureViewsForSet() {
        set_title.text = pokemonSet?.name
        set_main_title.text = pokemonSet?.name
        set_main_subtitle.text = getString(R.string.released, pokemonSet?.releaseDate)

        set_back_button.setOnClickListener { _ -> finish(); }

        loadSetImage()
    }

    private fun setupRecyclerView() {
        val decoration = DividerItemDecoration(this, OrientationHelper.VERTICAL)
        decoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.main_divider))

        set_recycler_view.adapter = CardListAdapter(this)
        set_recycler_view.addItemDecoration(decoration)
        set_recycler_view.layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_fall)
    }

    private fun setupKenBurnsView() {
        set_ken_burns.setScaleType(ImageView.ScaleType.CENTER_CROP)
        set_ken_burns.initResourceIDs(
                mutableListOf(
                        R.drawable.flashfire_header2
                )
        )
    }

    private fun setupAppBarLayout() {
        set_appbar.addOnOffsetChangedListener(this)
        set_appbar.addOnOffsetChangedListener(object : AppBarStateChangedListener() {
            override fun onAppBarLayoutStateChanged(appBarLayout: AppBarLayout?, newState: AppBarState?, previousState: AppBarState?) {
                if (newState == AppBarState.COLLAPSED) {
                    Glide.with(this@SetActivity).load(pokemonSet?.iconUrl()).into(set_image)
                } else if (previousState == AppBarState.COLLAPSED) {
                    set_image.setImageDrawable(setLogoDrawable)
                } else if (newState == AppBarState.EXPANDED && setImageError) {
                    loadSetImage()
                }
            }
        })
    }

    private fun loadSetImage() {
        Glide.with(this).load(pokemonSet?.logoUrl()).error(R.drawable.default_set_logo).into(object : GlideDrawableImageViewTarget(set_image) {
            override fun onResourceReady(resource: GlideDrawable?, animation: GlideAnimation<in GlideDrawable>?) {
                super.onResourceReady(resource, animation)
                setLogoDrawable = resource
                setImageError = false
            }

            override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                super.onLoadFailed(e, errorDrawable)
                setLogoDrawable = errorDrawable
                setImageError = true
            }
        })
    }

    private fun showLoadingDialog(show: Boolean) {
        set_progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun configureSearchView(menu: Menu) {
        // Retrieve the SearchView and plug it into SearchManager
        searchMenuItem = menu.findItem(R.id.action_search)
        searchView = MenuItemCompat.getActionView(searchMenuItem) as SearchView
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        val search = menu.findItem(R.id.action_search).actionView as SearchView
        search.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        search.setOnQueryTextListener(this)
    }
}