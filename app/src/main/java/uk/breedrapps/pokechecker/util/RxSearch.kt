package uk.breedrapps.pokechecker.util

import android.support.v7.widget.SearchView

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by edgeorge on 10/08/2017.
 */

object RxSearch {

    fun fromSearchView(searchView: SearchView): Observable<String> {

        val subject = BehaviorSubject.create<String>()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                subject.onNext(newText)
                return true
            }
        })

        return subject
    }
}