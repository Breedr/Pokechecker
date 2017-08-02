package uk.breedrapps.pokechecker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.breedrapps.pokechecker.R;

/**
 * Created by edgeorge on 02/08/2017.
 */

public abstract class BaseListAdapter<T> extends RecyclerView.Adapter<BaseListAdapter.ViewHolder> {

    private final List<T> data;
    private final OnItemClickedListener<T> listener;

    BaseListAdapter(OnItemClickedListener<T> listener) {
        data = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.set_list_item, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseListAdapter.ViewHolder holder, int position) {
        T object = data.get(holder.getAdapterPosition());
        holder.view.setOnClickListener(v -> listener.onItemClicked(object));
        bind(object, holder, position);
    }

    abstract void bind(T object, BaseListAdapter.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<T> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView icon;
        TextView title;
        TextView description;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.set_background);
            title = itemView.findViewById(android.R.id.text1);
            description = itemView.findViewById(android.R.id.text2);
            icon = itemView.findViewById(R.id.set_icon_image_view);
        }
    }

    public interface OnItemClickedListener<T> {
        void onItemClicked(T item);
    }

}
