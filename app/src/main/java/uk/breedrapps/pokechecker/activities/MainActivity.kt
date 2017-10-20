package uk.breedrapps.pokechecker.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.OrientationHelper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import uk.breedrapps.pokechecker.R
import uk.breedrapps.pokechecker.adapters.BaseListAdapter
import uk.breedrapps.pokechecker.adapters.SetListAdapter
import uk.breedrapps.pokechecker.model.PokemonSet
import uk.breedrapps.pokechecker.util.DisposingObserver
import uk.breedrapps.pokechecker.util.webObservable
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener, BaseListAdapter.OnItemClickedListener<PokemonSet> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        main_ken_burns.apply {
            setScaleType(ImageView.ScaleType.CENTER_CROP)
            initResourceIDs(
                    mutableListOf(
                            R.drawable.flashfire_header
                    )
            )
        }

        main_recycler_view.adapter = SetListAdapter(this)

        val decoration = DividerItemDecoration(this, OrientationHelper.VERTICAL)
        decoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.main_divider))

        main_recycler_view.apply {
            addItemDecoration(decoration)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(this@MainActivity, R.anim.layout_anim_fall)
        }

        refresh_layout.setOnRefreshListener(this)

        loadSets()
    }

    private fun loadSets() {
        api.sets.webObservable()
                .doOnError(Throwable::printStackTrace)
                .doOnSubscribe { _ -> showLoadingDialog(true) }
                .doFinally { showLoadingDialog(false) }
                .debounce(5, TimeUnit.SECONDS)
                .map { it.sets }
                .subscribe(object : DisposingObserver<List<PokemonSet>>() {
                    override fun onNext(list: List<PokemonSet>) {
                        (main_recycler_view.adapter as SetListAdapter).data = list
                    }
                })
    }

    internal override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun onRefresh() {
        loadSets()
    }

    override fun onItemClicked(set: PokemonSet?) {
        Intent(this, SetActivity::class.java).apply {
            putExtra("pokemonSet", set)
            startActivity(this)
        }
    }

    private fun showLoadingDialog(show: Boolean) {
        refresh_layout.isRefreshing = show
    }

}
