package com.courtesycarsredhill.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.courtesycarsredhill.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentDetailFragment extends Fragment {

    String documentImage = "";
    String documentName = "";
    String expiryDate = "";

    public DocumentDetailFragment() {
    }

    public static DocumentDetailFragment newInstance(String documentImage, String documentName, String expiryDate) {
        DocumentDetailFragment fragment = new DocumentDetailFragment();
        Bundle args = new Bundle();
        args.putString("DocumentImage", documentImage);
        args.putString("DocumentName", documentName);
        args.putString("ExpiryDate", expiryDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("DocumentImage")) {
                documentImage = getArguments().getString("DocumentImage");
            }
            if (getArguments().containsKey("DocumentName")) {
                documentName = getArguments().getString("DocumentName");
            }
            if (getArguments().containsKey("ExpiryDate")) {
                expiryDate = getArguments().getString("ExpiryDate");
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        return inflater.inflate(R.layout.fragment_document_detail, container, false);
    }

    private void init() {

    }
}