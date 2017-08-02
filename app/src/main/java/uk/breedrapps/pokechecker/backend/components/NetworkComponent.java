package uk.breedrapps.pokechecker.backend.components;

import javax.inject.Singleton;

import dagger.Component;
import uk.breedrapps.pokechecker.activities.BaseActivity;
import uk.breedrapps.pokechecker.backend.modules.ApplicationModule;
import uk.breedrapps.pokechecker.backend.modules.NetworkingModule;
import uk.breedrapps.pokechecker.backend.modules.TcgApiModule;

/**
 * Component interface to generate DaggerNetworkComponent
 */

@Singleton
@Component(modules={ApplicationModule.class, NetworkingModule.class, TcgApiModule.class})
public interface NetworkComponent {
    void inject(BaseActivity activity);
}