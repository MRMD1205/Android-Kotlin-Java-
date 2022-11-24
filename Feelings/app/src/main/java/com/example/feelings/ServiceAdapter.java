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


public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder>{
    Context context;
    ArrayList<Services> profiles;

    public ServiceAdapter(Context c , ArrayList<Services> p)
    {
        context = c;
        profiles = p;
    }
    @NonNull
    @Override

  public ServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.servicecardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.MyViewHolder holder, int position) {
        holder.serviceName.setText(profiles.get(position).getServiceName());
        holder.servicePrice.setText(profiles.get(position).getServicePrice());
        holder.checkBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, servicePrice;
        Button checkBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ServiceProfile.class);
                    intent.putExtra("servicePrice",  profiles.get(getAdapterPosition()).getServicePrice());
                    intent.putExtra("key", (CharSequence) profiles.get(getAdapterPosition()).getServiceName());
                    view.getContext().startActivity(intent);
                }
            });

            serviceName = (TextView) itemView.findViewById(R.id.view_service_name);
            servicePrice = (TextView) itemView.findViewById(R.id.view_price);
            checkBtn = (Button) itemView.findViewById(R.id.check_details);
        }

    }
}
