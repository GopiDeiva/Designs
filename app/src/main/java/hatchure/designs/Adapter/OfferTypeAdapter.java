package hatchure.designs.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hatchure.designs.InStoreAds;
import hatchure.designs.Interfaces.ICustomClickEvent;
import hatchure.designs.PostAds;
import hatchure.designs.R;

public class OfferTypeAdapter extends RecyclerView.Adapter {
    Fragment mContext;
    ArrayList offerList;
    public OfferTypeAdapter(Fragment mContext, ArrayList offerList) {
        this.mContext = mContext;
        this.offerList = offerList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new OfferListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        OfferListHolder holder = (OfferListHolder) viewHolder;
        holder.offerType.setText(offerList.get(position).toString());
        holder.offerType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ICustomClickEvent) mContext).OnCustomClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (offerList != null) {
            return offerList.size();
        }
        Log.i("towny size", "size 0");
        return 0;
    }
}

class OfferListHolder extends RecyclerView.ViewHolder {
    TextView offerType;
    OfferListHolder(View itemView) {
        super(itemView);
        offerType = itemView.findViewById(R.id.offerTypeItem);
    }
}