package uk.breedrapps.pokechecker.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import javax.inject.Inject;

import uk.breedrapps.pokechecker.PokecheckerApp;
import uk.breedrapps.pokechecker.R;
import uk.breedrapps.pokechecker.backend.PokemonTcgApi;
import uk.breedrapps.pokechecker.util.DisposableManager;

/**
 * Created by edgeorge on 28/07/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Nullable
    Toolbar toolbar;

    @Inject
    PokemonTcgApi api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ((PokecheckerApp)getApplication()).getNetworkComponent().inject(this);
        setToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        DisposableManager.dispose();
        super.onDestroy();
    }

    @LayoutRes
    abstract int getLayout();

    void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) setSupportActionBar(toolbar);
    }
}
