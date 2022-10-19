package com.courtesycarsredhill.interfaces;

import com.courtesycarsredhill.model.CheckListData;

import java.util.ArrayList;

public interface OnDismissedCall {
    void onDismissCalled(int val1, int val2);
    void onEquipmentSelected(String action, ArrayList<CheckListData> values);
    void onFeedbackGiven(int val, String comment);

}
