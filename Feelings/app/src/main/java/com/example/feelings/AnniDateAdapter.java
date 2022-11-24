package com.example.feelings;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnniDateAdapter extends RecyclerView.Adapter<AnniDateAdapter.myViewHolder> {

    Context context;
    ArrayList<Users> profiles;

    public AnniDateAdapter(Context c , ArrayList<Users> p)
    {
        context = c;
        profiles = p;
    }


    @NonNull
    @Override
    public AnniDateAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnniDateAdapter.myViewHolder(LayoutInflater.from(context).inflate(R.layout.wish_cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AnniDateAdapter.myViewHolder holder, int position) {
        holder.fullName.setText(profiles.get(position).getFullName());
        holder.phoneNumber.setText(profiles.get(position).getPhoneNumber());
        holder.checkBtn.setVisibility(View.VISIBLE);
        //holder.Onclick(position);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView fullName,phoneNumber;
        Button checkBtn;

        public myViewHolder(View view) {
            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AnniMessage.class);
                    intent.putExtra("key", (CharSequence) profiles.get(getAdapterPosition()).getPhoneNumber());
                    intent.putExtra("fullName", (CharSequence) profiles.get(getAdapterPosition()).getFullName());
                    intent.putExtra("wphoneNumber",  profiles.get(getAdapterPosition()).getWphoneNumber());
                    view.getContext().startActivity(intent);
                }
            });


            fullName = (TextView) itemView.findViewById(R.id.full_name);
            phoneNumber = (TextView) itemView.findViewById(R.id.phNo);
            checkBtn = (Button) itemView.findViewById(R.id.check_details);
        }

//        public void Onclick(final int position)
//        {
//            checkBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, Message.class);
//                    context.startActivity(intent);
//                }
//            });
//        }

    }
}
