@file:JvmName("Utils")
package uk.breedrapps.pokechecker.util

import android.content.SharedPreferences
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
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
fun ImageView.loadImage(url: String) = Glide.with(this.context).load(url).into(this)
fun ImageView.loadImage(resource: Int) = Glide.with(this.context).load(resource).into(this)

inline fun FragmentManager.withTransaction(func: FragmentTransaction.() -> Unit) {
    val trans = beginTransaction()
    trans.func()
    trans.commit()
}

inline fun SharedPreferences.withPreferenceEditor(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}