package com.webblen.events.webblen.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.webblen.events.webblen.R;
import com.webblen.events.webblen.Objects.WebblenEvent;

import java.util.List;

/**
 * Created by codelation on 3/31/18.
 */

class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    ItemClickListener itemClickListener;
    ImageView eventImg, eventAuthImg, catImg1, catImg2, catImg3;
    TextView eventAuthName, eventTitle, eventDesc, eventDay, eventMonth, eventYear, eventViews;
    String eventId;

    public ListItemViewHolder (View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        eventImg = (ImageView) itemView.findViewById(R.id.eventCardImg);
        catImg1 = (ImageView) itemView.findViewById(R.id.eventCardInt1);
        catImg2 = (ImageView) itemView.findViewById(R.id.eventCardInt2);
        catImg3 = (ImageView) itemView.findViewById(R.id.eventCardInt3);
        eventAuthImg = (ImageView) itemView.findViewById(R.id.eventCardAuthorImg);
        eventAuthName = (TextView) itemView.findViewById(R.id.eventCardAuthName);
        eventTitle = (TextView) itemView.findViewById(R.id.eventCardTitle);
        eventDay = (TextView) itemView.findViewById(R.id.eventCardDay);
        eventMonth = (TextView) itemView.findViewById(R.id.eventCardMnth);
        eventYear = (TextView) itemView.findViewById(R.id.eventCardYear);
        eventDesc = (TextView) itemView.findViewById(R.id.eventCardDescription);
        eventViews = (TextView) itemView.findViewById(R.id.eventCardViews);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }
}


public class ListItemAdapter extends  RecyclerView.Adapter<ListItemViewHolder> {

    public List<WebblenEvent> webblenEventList;
    public Context context;

    public ListItemAdapter(List<WebblenEvent> webblenEventList){
        this.webblenEventList = webblenEventList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_list_item, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        String eventKey = webblenEventList.get(position).getEventKey();

        String eventPathToImage = webblenEventList.get(position).getPathToImage();
        //holder.setEventCardImg(eventPathToImage);

        String eventAuthName = webblenEventList.get(position).getAuthor();
        holder.eventTitle.setText(eventAuthName);

        String eventTitle = webblenEventList.get(position).getTitle();
        holder.eventAuthName.setText(eventTitle);

        String description = webblenEventList.get(position).getDescription();
        holder.eventDesc.setText(description);

        int eventViews = webblenEventList.get(position).getViews();
        holder.eventViews.setText(eventViews);

    }


    @Override
    public int getItemCount() {
        return webblenEventList.size();
    }
}
