package com.app.miniproject.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Models.ExpertiseModel;
import com.app.miniproject.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ExpertiseAdapter extends RecyclerView.Adapter<ExpertiseAdapter.ExpertiseViewHolder> {

    Context mCxt;
    List<ExpertiseModel>expertiseModels=new ArrayList<>();
    ExpertiseClickEvents expertiseClickEvents;

    public ExpertiseAdapter(Context mCxt, ExpertiseClickEvents expertiseClickEvents) {
        this.mCxt = mCxt;
        this.expertiseClickEvents = expertiseClickEvents;
    }

    public void setExpertiseList(List<ExpertiseModel>expertiseList){
        expertiseModels=expertiseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpertiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCxt).inflate(R.layout.items_tutor_post,parent,false);
        return new ExpertiseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpertiseViewHolder holder, int position) {
        ExpertiseModel expertiseModel=expertiseModels.get(position);
        holder.txtTitle.setText(expertiseModel.getExpertiseTitle());
        holder.txtDescription.setText(expertiseModel.getExpertiseDescription());
        holder.txtPriceQuote.setText(expertiseModel.getExpertisePrice());
        Glide.with(mCxt).load(expertiseModel.getExpertiseImage()).into(holder.imgPost);

        holder.cardTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expertiseClickEvents.onExpertiseClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return expertiseModels.size();
    }

    public class ExpertiseViewHolder extends RecyclerView.ViewHolder{
        CardView cardTutor;
        ImageView imgPost;
        TextView txtTitle,txtDescription,txtPriceQuote;

        public ExpertiseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTutor=itemView.findViewById(R.id.cardTutor);
            imgPost=itemView.findViewById(R.id.imgPost);
            txtTitle=itemView.findViewById(R.id.txtTitle);
            txtDescription=itemView.findViewById(R.id.txtDescription);
            txtPriceQuote=itemView.findViewById(R.id.txtPriceQuote);
        }
    }

    public interface ExpertiseClickEvents{
        void onExpertiseClick();
    }
}
