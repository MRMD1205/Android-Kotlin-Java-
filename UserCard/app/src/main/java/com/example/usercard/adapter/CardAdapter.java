package com.example.usercard.adapter;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.usercard.databinding.ItemsBinding;
import com.example.usercard.model.CardDetails;
import com.example.usercard.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

public class CardAdapter extends PagerAdapter {

    private List<CardDetails> cardDetailsList;
    private Context context;
    private OnclickListener listener;

    public CardAdapter(List<CardDetails> cardDetailsList, Context context, OnclickListener listener) {
        this.cardDetailsList = cardDetailsList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return cardDetailsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        ItemsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.items, null, false);

        CardDetails cardDetails = cardDetailsList.get(position);

        binding.setUser(cardDetails);

        container.addView(binding.getRoot(), 0);

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardDetailsList.get(position).setValue(binding.evValue.getText().toString().trim());
                listener.onClick(position);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface OnclickListener {
        void onClick(int position);
    }

}
