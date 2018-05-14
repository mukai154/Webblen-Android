package com.webblen.events.webblen.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.webblen.events.webblen.Activities.EventInfoActivity;
import com.webblen.events.webblen.Activities.EventTableActivity;
import com.webblen.events.webblen.Objects.WebblenEvent;
import com.webblen.events.webblen.R;
import com.webblen.events.webblen.Recycler_Views.EventViewHolder;
import com.webblen.events.webblen.Utilities;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WebblenEventAdapter extends RecyclerView.Adapter<WebblenEventAdapter.EventViewHolder> {

    public Context context;
    public List<WebblenEvent> webblenEventList;

    public WebblenEventAdapter(Context context, List<WebblenEvent> webblenEventList){
        this.context = context;
        this.webblenEventList = webblenEventList;
    }

    @Override
    public WebblenEventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_list_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WebblenEventAdapter.EventViewHolder holder, int position) {

        WebblenEvent event = webblenEventList.get(position);

        //Event Image
        String eventPathToImage = event.getPathToImage();
        if (eventPathToImage.isEmpty() || eventPathToImage.contentEquals("") || eventPathToImage == null){
            holder.eventCardImg.getLayoutParams().height = 0;
            holder.eventCardImg.setVisibility(View.INVISIBLE);
        } else {
            Glide.with(context).load(eventPathToImage).into(holder.eventCardImg);
        }

        //Title
        holder.eventCardTitle.setText(event.getTitle());


        //Author
        String eventAuthor = "@" + event.getAuthor();
        holder.eventCardAuthName.setText(eventAuthor);

        //Description
        holder.eventCardDescription.setText(event.getDescription());

        //Views
        Integer eventViews = event.getViews();
        String eventViewsString = String.valueOf(eventViews);
        holder.eventCardViews.setText(eventViewsString);


        //Event Interests...
        ArrayList<String> eventList = event.getCategories();
        //Interest 1
        holder.eventCardInt1.setBackgroundResource(Utilities.categoryToDrawable(eventList.get(0)));


        //Interest 2
        if (eventList.size() < 2){
            holder.eventCardInt2.setVisibility(View.INVISIBLE);
        } else {
            holder.eventCardInt2.setBackgroundResource(Utilities.categoryToDrawable(eventList.get(1)));
        }

        //Interest 2
        if (eventList.size() < 3){
            holder.eventCardInt3.setVisibility(View.INVISIBLE);
        } else {
            holder.eventCardInt3.setBackgroundResource(Utilities.categoryToDrawable(eventList.get(2)));
        }

    }

    @Override
    public int getItemCount() {
        return webblenEventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        private ImageView eventCardImg;
        private CircleImageView eventCardAuthorImg;
        private TextView eventCardAuthName;
        private TextView eventCardTitle;
        private TextView eventCardDescription;
        private ImageView eventCardInt1;
        private ImageView eventCardInt2;
        private ImageView eventCardInt3;
        private TextView eventCardViews;

        public EventViewHolder(View itemView){
            super(itemView);

            eventCardImg = itemView.findViewById(R.id.eventCardImg);
            eventCardAuthorImg = itemView.findViewById(R.id.eventCardAuthorImg);
            eventCardAuthName = itemView.findViewById(R.id.eventCardAuthName);
            eventCardTitle = itemView.findViewById(R.id.eventCardTitle);
            eventCardDescription = itemView.findViewById(R.id.eventCardDescription);
            eventCardInt1 = itemView.findViewById(R.id.eventCardInt1);
            eventCardInt2 = itemView.findViewById(R.id.eventCardInt2);
            eventCardInt3 =  itemView.findViewById(R.id.eventCardInt3);
            eventCardViews = itemView.findViewById(R.id.eventCardViews);
        }

    }
}
