package uk.breedrapps.pokechecker.adapters;

import com.bumptech.glide.Glide;

import uk.breedrapps.pokechecker.model.PokemonSet;

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
        Glide.with(holder.icon.getContext()).load(set.iconUrl()).into(holder.icon);
    }
}
