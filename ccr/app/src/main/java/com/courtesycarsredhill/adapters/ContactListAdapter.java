package com.courtesycarsredhill.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.ItemContactListBinding;
import com.courtesycarsredhill.databinding.ItemEquipmentQuestionBinding;
import com.courtesycarsredhill.model.CheckListData;
import com.courtesycarsredhill.model.ContactListModel;
import com.courtesycarsredhill.util.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ContactListModel> contactList;

    public ContactListAdapter(Context context, ArrayList<ContactListModel> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactListBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_contact_list, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListAdapter.ViewHolder holder, int position) {
        ContactListModel model = contactList.get(position);
        if (model!=null){
            holder.bind(model);
            holder.mBinder.tvOffice.setText(model.getTitle());
            holder.mBinder.tvOfficeNumber.setText(model.getContactNo());
            holder.mBinder.tvOfficeNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.dialdNumber(context, model.getContactNo());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (contactList != null)
            return contactList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemContactListBinding mBinder;

        ViewHolder(ItemContactListBinding mBinder) {
            super(mBinder.getRoot());
            this.mBinder = mBinder;
        }

        void bind(ContactListModel item) {
            mBinder.setItem(item);
            mBinder.executePendingBindings();
        }
    }
}
