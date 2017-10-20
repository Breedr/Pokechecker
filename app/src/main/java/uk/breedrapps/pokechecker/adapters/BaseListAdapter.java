package uk.breedrapps.pokechecker.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import uk.breedrapps.pokechecker.R;

/**
 * Created by edgeorge on 02/08/2017.
 */

public abstract class BaseListAdapter<T> extends RecyclerView.Adapter<BaseListAdapter.ViewHolder> {

    static final int MIN_FUZZY_MATCH = 60;

    private final List<T> initialData;
    private final List<T> filteredData;
    private final OnItemClickedListener<T> listener;
    private RecyclerView recyclerView;

    BaseListAdapter(OnItemClickedListener<T> listener) {
        initialData = new ArrayList<>();
        filteredData = new ArrayList<>();
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
        T object = filteredData.get(holder.getAdapterPosition());
        holder.view.setOnClickListener(v -> listener.onItemClicked(object));
        bind(object, holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    private void setFilteredData(List<T> data) {
        filteredData.clear();
        filteredData.addAll(data);
        notifyDataSetChanged();
        if(recyclerView != null) recyclerView.scheduleLayoutAnimation();
    }

    abstract void bind(T object, BaseListAdapter.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    public List<T> getData() {
        return filteredData;
    }

    public void setData(List<T> newData) {
        initialData.clear();
        initialData.addAll(newData);
        setFilteredData(newData);
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

    public void filter(String query) {

        if(TextUtils.isEmpty(query)){
            setFilteredData(initialData);
            return;
        }

        Observable.fromIterable(initialData)
                .filter(filterPredicate(query))
                .toList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setFilteredData);
    }

    abstract Predicate<T> filterPredicate(String query);

}
