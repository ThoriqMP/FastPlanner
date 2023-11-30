package com.example.analyzenutrisi;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlanAdapter extends ArrayAdapter<PlanModel> {

    public PlanAdapter(@NonNull Context context, int resource, @NonNull List<PlanModel> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView namaPlanTextView = convertView.findViewById(R.id.namaPlan);
        TextView descPlanTextView = convertView.findViewById(R.id.descPlan);
        TextView hargaPlanTextView = convertView.findViewById(R.id.hargaPlan);

        PlanModel plan = getItem(position);

        if (plan != null) {
            namaPlanTextView.setText(plan.getNama());
            descPlanTextView.setText(plan.getDesc());
            hargaPlanTextView.setText(plan.getHarga());
        }

        return convertView;
    }
}
