package uk.breedrapps.pokechecker.adapters;

import io.reactivex.functions.Predicate;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import uk.breedrapps.pokechecker.model.PokemonSet;
import uk.breedrapps.pokechecker.util.Utils;

/**
 * Created by edgeorge on 31/07/2017.
 */

public class SetListAdapter extends BaseListAdapter<PokemonSet> {
    public SetListAdapter(OnItemClickedListener<PokemonSet> listener) {
        super(listener);
    }

    @Override
    void bind(PokemonSet set, BaseListAdapter.ViewHolder holder, int position) {
        holder.title.setText(set.getName());
        holder.description.setText(set.getSeries());
        Utils.loadImage(holder.icon, set.iconUrl());
    }

    @Override
    Predicate<PokemonSet> filterPredicate(String query) {
        return set -> FuzzySearch.partialRatio(query, set.getName()) >= MIN_FUZZY_MATCH;
    }
}
