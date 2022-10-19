package com.courtesycarsredhill.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.courtesycarsredhill.R;
import com.courtesycarsredhill.databinding.ItemContactListBinding;
import com.courtesycarsredhill.databinding.ItemDocumentListBinding;
import com.courtesycarsredhill.model.ContactListModel;
import com.courtesycarsredhill.ui.NavigationMainActivity;
import com.courtesycarsredhill.ui.fragment.DocumentDetailFragment;
import com.courtesycarsredhill.ui.fragment.DocumentFragment;
import com.courtesycarsredhill.util.AppConstants;
import com.courtesycarsredhill.util.Utils;

import java.util.ArrayList;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ContactListModel> documentList;

    public DocumentListAdapter(Context context, ArrayList<ContactListModel> documentList) {
        this.context = context;
        this.documentList = documentList;
    }

    @NonNull
    @Override
    public DocumentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDocumentListBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_document_list, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentListAdapter.ViewHolder holder, int position) {
        ContactListModel model = documentList.get(position);
        if (model != null) {
            holder.mBinder.tvDocumentName.setText(model.getTitle());
            holder.mBinder.tvDocumentDate.setText(model.getContactNo());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NavigationMainActivity) context).replaceFragment(AppConstants.NAVIGATION_KEY, DocumentDetailFragment.newInstance("", "", ""), false, false);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (documentList != null)
            return documentList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDocumentListBinding mBinder;

        ViewHolder(ItemDocumentListBinding mBinder) {
            super(mBinder.getRoot());
            this.mBinder = mBinder;
        }

    }
}
