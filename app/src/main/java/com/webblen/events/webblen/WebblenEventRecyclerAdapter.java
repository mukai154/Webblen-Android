package com.webblen.events.webblen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by codelation on 3/27/18.
 */

public class WebblenEventRecyclerAdapter extends RecyclerView.Adapter<WebblenEventRecyclerAdapter.ViewHolder> {

    public List<WebblenEvent> webblenEventList;
    public Context context;

    //Firebase
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public WebblenEventRecyclerAdapter(List<WebblenEvent> webblenEventList){
        this.webblenEventList = webblenEventList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        String eventKey = webblenEventList.get(position).getEventKey();

        String eventPathToImage = webblenEventList.get(position).getPathToImage();
        holder.setEventCardImg(eventPathToImage);

        String eventAuthName = webblenEventList.get(position).getAuthor();
        holder.setEventCardAuthName(eventAuthName);

        String eventTitle = webblenEventList.get(position).getTitle();
        holder.setEventCardTitle(eventTitle);

        String description = webblenEventList.get(position).getDescription();
        holder.setEventCardDescription(description);

        int eventViews = webblenEventList.get(position).getViews();
        holder.setEventCardViews(eventViews);


        firebaseFirestore.collection("usernames").document(eventAuthName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    String uid = task.getResult().getString("uid");

                    firebaseFirestore.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){

                                if(task.getResult().exists()){

                                    String profile_pic = task.getResult().getString("profile_pic");
                                    //If username or pic is null...
                                    if (profile_pic == null){
                                        holder.eventCardAuthorImg.setMaxWidth(1);
                                    } else {
                                        holder.setEventCardAuthorImg(profile_pic);
                                    }
                                }

                            } else {

                                String error = task.getException().getMessage();
                                Toast.makeText(context, "Load Error: " + error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {

                    //Firebase Exception

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return webblenEventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private ImageView eventCardImg;
        private CircleImageView eventCardAuthorImg;
        private TextView eventCardAuthName;
        private TextView eventCardTitle;
        private TextView eventCardDay;
        private TextView eventCardMonth;
        private TextView eventCardYear;
        private TextView eventCardDescription;
        private ImageView eventCardInt1;
        private ImageView eventCardInt2;
        private ImageView eventCardInt3;
        private TextView eventCardViews;



        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setEventCardImg(String pathToImage) {
            eventCardImg = mView.findViewById(R.id.eventCardImg);
            Uri imageUri = Uri.parse(pathToImage);
            Glide.with(context).load(pathToImage).into(eventCardImg);
        }

        public void setEventCardAuthorImg(String profilePic) {
            eventCardAuthorImg = mView.findViewById(R.id.eventCardAuthorImg);
            Uri imageUri = Uri.parse(profilePic);
            Glide.with(context).load(imageUri).into(eventCardAuthorImg);
        }

        public void setEventCardAuthName(String eventAuthor) {
            eventCardAuthName = mView.findViewById(R.id.eventCardAuthName);
            eventCardAuthName.setText(eventAuthor);
        }

        public void setEventCardTitle(String eventTitle) {
            eventCardTitle = mView.findViewById(R.id.eventCardTitle);
            eventCardTitle.setText(eventTitle);
        }

        public void setEventCardDay(String eventDay) {
            eventCardDay = mView.findViewById(R.id.eventCardDay);
            eventCardDay.setText(eventDay);
        }

        public void setEventCardMonth(String eventMonth) {
            eventCardMonth = itemView.findViewById(R.id.eventCardMnth);
            eventCardMonth.setText(eventMonth);
        }

        public void setEventCardYear(String eventYear) {
            eventCardYear = mView.findViewById(R.id.eventCardYear);
            eventCardYear.setText(eventYear);
        }

        public void setEventCardDescription(String descriptionText) {
            eventCardDescription = mView.findViewById(R.id.eventCardDescription);
            eventCardDescription.setText(descriptionText);
        }

        public void setEventCardInt1(String interest1Image) {
            eventCardInt1 = mView.findViewById(R.id.eventCardInt1);
            eventCardInt1.setImageResource(Utilities.categoryToDrawable(interest1Image.toUpperCase()));
        }

        public void setEventCardInt2(String interest2Image) {
            eventCardInt2 = mView.findViewById(R.id.eventCardInt2);
            eventCardInt2.setImageResource(Utilities.categoryToDrawable(interest2Image.toUpperCase()));
        }

        public void setEventCardInt3(String interest3Image) {
            eventCardInt3 =  (ImageView) itemView.findViewById(R.id.eventCardInt3);
            eventCardInt3.setImageResource(Utilities.categoryToDrawable(interest3Image.toUpperCase()));

        }

        public void setEventCardViews(int eventViews) {
            eventCardViews = mView.findViewById(R.id.eventCardViews);
            eventCardViews.setText(String.valueOf(eventViews));
        }
    }
}