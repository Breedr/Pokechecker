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

        main_ken_burns.setScaleType(ImageView.ScaleType.CENTER_CROP)
        main_ken_burns.initResourceIDs(
                mutableListOf(
                        R.drawable.flashfire_header2
                )
        )

        main_recycler_view.adapter = SetListAdapter(this)

        val decoration = DividerItemDecoration(this, OrientationHelper.VERTICAL)
        decoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.main_divider))

        main_recycler_view.addItemDecoration(decoration)
        main_recycler_view.layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_fall)

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
                        (main_recycler_view.adapter as SetListAdapter).setData(list)
                        main_recycler_view.scheduleLayoutAnimation()
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
        val intent = Intent(this, SetActivity::class.java)
        intent.putExtra("set", set)
        startActivity(intent)
    }

    private fun showLoadingDialog(show: Boolean) {
        refresh_layout.isRefreshing = show
    }

}
