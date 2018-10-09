package com.example.tuanfpt.companymanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.example.tuanfpt.companymanager.R;
import com.example.tuanfpt.companymanager.models.Maintain;
import com.example.tuanfpt.companymanager.utilities.Constant;

import java.util.ArrayList;

import butterknife.BindView;

public class MaintainAdapter extends RecyclerView.Adapter<MaintainAdapter.MaintainViewHolder> {

    private ArrayList<Maintain> maintains;
    private ArrayList<Maintain> cacheMaintains;

    public MaintainAdapter(ArrayList<Maintain> maintains) {

        this.maintains = new ArrayList<>();
        this.maintains.addAll(maintains);
        this.cacheMaintains = maintains;
    }

    @NonNull
    @Override
    public MaintainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maintain, parent, false);
        return new MaintainAdapter.MaintainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaintainViewHolder holder, int position) {
        holder.bindData(maintains.get(position));
    }

    @Override
    public int getItemCount() {
        return maintains.size();
    }

    public void update(String dateFrom, String dateTo, String mother, String child) {
        maintains.clear();
        for (Maintain maintain : cacheMaintains) {
            if ((compareDate(dateFrom, maintain.getDate()) == 0 || compareDate(dateFrom, maintain.getDate()) == -1)
                    && (compareDate(dateTo, maintain.getDate()) == 0 || compareDate(dateTo, maintain.getDate()) == 1)) {
                if (mother.equals(Constant.ALL) && child.equals(Constant.ALL)) {
                    maintains.add(maintain);
                } else if (!mother.equals(Constant.ALL) && child.equals(Constant.ALL)) {
                    if (maintain.getMother().equals(mother)) maintains.add(maintain);
                } else if (mother.equals(Constant.ALL) && !child.equals(Constant.ALL)) {
                    if (maintain.getChild().equals(child)) maintains.add(maintain);
                } else {
                    if (maintain.getChild().equals(child) && maintain.getMother().equals(mother)) {
                        maintains.add(maintain);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    private int compareDate(String dateCompare, String originDate) {
        String[] firstTime = dateCompare.split("/");
        String[] secondTime = originDate.split("/");
        if (firstTime.length != 3 && secondTime.length != 3) {
            return -2;
        } else {
            String firstDay = firstTime[0];
            String firstMonth = firstTime[1];
            String firstYear = firstTime[2];
            String secondDay = secondTime[0];
            String secondMonth = secondTime[1];
            String secondYear = secondTime[2];

            if (Integer.parseInt(firstYear) == Integer.parseInt(secondYear)) {
                if (Integer.parseInt(firstMonth) == Integer.parseInt(secondMonth)) {
                    return Integer.compare(Integer.parseInt(firstDay), Integer.parseInt(secondDay));
                } else if (Integer.parseInt(firstMonth) > Integer.parseInt(secondMonth)) {
                    return 1;
                } else {
                    return -1;
                }
            } else if (Integer.parseInt(firstYear) > Integer.parseInt(secondYear)) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    class MaintainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtName)
        EditText txtName;
        @BindView(R.id.txtMotherName)
        EditText txtMotherName;
        @BindView(R.id.txtTrait)
        EditText txtTrait;
        @BindView(R.id.txtError)
        EditText txtError;
        @BindView(R.id.txtEmployeeName)
        EditText txtEmployeeName;
        @BindView(R.id.txtDate)
        EditText txtDate;
        @BindView(R.id.txtNote)
        EditText txtNote;

        MaintainViewHolder(View itemView) {
            super(itemView);
            txtMotherName = itemView.findViewById(R.id.txtMotherName);
            txtName = itemView.findViewById(R.id.txtName);
            txtTrait = itemView.findViewById(R.id.txtTrait);
            txtError = itemView.findViewById(R.id.txtError);
            txtEmployeeName = itemView.findViewById(R.id.txtEmployeeName);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtNote = itemView.findViewById(R.id.txtNote);
        }

        void bindData(Maintain maintain) {
            txtMotherName.setText(maintain.getMother());
            txtName.setText(maintain.getChild());
            txtTrait.setText(maintain.getTrait());
            txtError.setText(maintain.getProblem());
            txtEmployeeName.setText(maintain.getEmployeeName());
            txtDate.setText(maintain.getDate());
            txtNote.setText(maintain.getNote());
        }
    }
}
