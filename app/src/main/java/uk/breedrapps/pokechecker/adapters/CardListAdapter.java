package uk.breedrapps.pokechecker.adapters;

import android.text.TextUtils;

import uk.breedrapps.pokechecker.R;
import uk.breedrapps.pokechecker.model.PokemonCard;

/**
 * Created by edgeorge on 02/08/2017.
 */

public class CardListAdapter extends BaseListAdapter<PokemonCard> {
    public CardListAdapter(OnItemClickedListener<PokemonCard> listener) {
        super(listener);
    }

    @Override
    void bind(PokemonCard card, BaseListAdapter.ViewHolder holder, int position) {
        holder.icon.setImageResource(R.drawable.unchecked);
        holder.title.setText(card.getName());
        holder.description.setText(getDescriptionText(card));
    }

    private String getDescriptionText(PokemonCard card) {

        if(TextUtils.isEmpty(card.getSubtype())) {
            return card.getSupertype();
        }

        return String.format("%s - %s", card.getSubtype(), card.getSupertype());
    }
}

