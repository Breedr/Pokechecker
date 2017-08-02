package uk.breedrapps.pokechecker.util;

import android.support.annotation.CallSuper;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by edgeorge on 28/07/2017.
 */

public class DisposingObserver<T> implements Observer<T> {
    @Override
    @CallSuper
    public void onSubscribe(Disposable disposable) {
        DisposableManager.add(disposable);
    }

    @Override
    public void onNext(T list) {}

    @Override
    public void onError(Throwable e) {}

    @Override
    public void onComplete() {}
}