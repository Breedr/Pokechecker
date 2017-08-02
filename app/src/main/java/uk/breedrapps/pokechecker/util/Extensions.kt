package uk.breedrapps.pokechecker.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by edgeorge on 28/07/2017.
 */

// Observable
fun <T> Observable<T>.subscribeOnIo() = this.subscribeOn(Schedulers.io())
fun <T> Observable<T>.subscribeOnNewThread() = this.subscribeOn(Schedulers.newThread())
fun <T> Observable<T>.observeOnMainThread() = this.observeOn(AndroidSchedulers.mainThread())
fun <T> Observable<T>.webObservable() = this.subscribeOnNewThread().observeOnMainThread()

// Image View
fun ImageView.loadImage(context: Context, url: String) = Glide.with(context).load(url).into(this)
fun ImageView.loadImage(context: Context, resource: Int) = Glide.with(context).load(resource).into(this)