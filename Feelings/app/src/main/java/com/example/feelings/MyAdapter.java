package com.example.feelings;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Users> profiles;

    public MyAdapter(Context c , ArrayList<Users> p)
    {
        context = c;
        profiles = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.fullName.setText(profiles.get(position).getFullName());
            holder.phoneNumber.setText(profiles.get(position).getPhoneNumber());
            holder.checkBtn.setVisibility(View.VISIBLE);
            //holder.Onclick(position);
        
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView fullName,phoneNumber;
        Button checkBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Profile.class);
                    intent.putExtra("fullName",  profiles.get(getAdapterPosition()).getFullName());
                    intent.putExtra("wphoneNumber",  profiles.get(getAdapterPosition()).getWphoneNumber());
                    intent.putExtra("birthDate", (CharSequence) profiles.get(getAdapterPosition()).getBirthDate());
                    intent.putExtra("anniversaryDate", (CharSequence) profiles.get(getAdapterPosition()).getAnniversaryDate());
                    intent.putExtra("address", (CharSequence) profiles.get(getAdapterPosition()).getAddress());
                    intent.putExtra("key", (CharSequence) profiles.get(getAdapterPosition()).getPhoneNumber());
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
//                    Intent intent = new Intent(context, Profile.class);
//                    context.startActivity(intent);
//                }
//            });
//        }

    }

}
