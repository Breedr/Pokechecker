package uk.breedrapps.pokechecker.views;

import android.support.design.widget.AppBarLayout;

/**
 * Created by edgeorge on 10/06/2016.
 */

public abstract class AppBarStateChangedListener implements AppBarLayout.OnOffsetChangedListener {

    public enum AppBarState {
        EXPANDED,
        COLLAPSED,
        IDLE,
        NONE
    }

    private AppBarState currentState = AppBarState.NONE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            updateIfNotCurrent(appBarLayout, AppBarState.EXPANDED);
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            updateIfNotCurrent(appBarLayout, AppBarState.COLLAPSED);
        } else {
            updateIfNotCurrent(appBarLayout, AppBarState.IDLE);
        }
    }

    private void updateIfNotCurrent(AppBarLayout appBarLayout, AppBarState state) {
        if (currentState != state) {
            onAppBarLayoutStateChanged(appBarLayout, state, currentState);
        }
        currentState = state;
    }

    public abstract void onAppBarLayoutStateChanged(AppBarLayout appBarLayout, AppBarState newState, AppBarState previousState);
}
