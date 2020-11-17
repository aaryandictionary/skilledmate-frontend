package com.app.miniproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.miniproject.Database.CollegeList.CollegeListData;
import com.app.miniproject.R;

import java.util.ArrayList;
import java.util.List;

public class CollegeListAdapter extends ArrayAdapter<CollegeListData> {

    private List<CollegeListData>collegeListDataList=new ArrayList<>();

    public CollegeListAdapter(@NonNull Context context, int resource, @NonNull List<CollegeListData> objects) {
        super(context, 0, objects);
        collegeListDataList=objects;
    }

    @Override
    public int getCount() {
        return collegeListDataList.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem=convertView;

        if (listViewItem==null){
            listViewItem= LayoutInflater.from(getContext()).inflate(R.layout.items_college_list_selection,parent,false);
        }

        CollegeListData collegeListData=collegeListDataList.get(position);

        TextView txtCollege=listViewItem.findViewById(R.id.txtCollegeName);
        txtCollege.setText(collegeListData.getCollegeName());

        return listViewItem;
    }

    private Filter itemFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            List<CollegeListData> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(collegeListDataList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CollegeListData collegeListData : collegeListDataList) {
                    if (collegeListData.getCollegeName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(collegeListData);
                    }
                }
            }
            filterResults.values = suggestions;
            filterResults.count = suggestions.size();

            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List)results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((CollegeListData)resultValue).getCollegeName();
        }
    };
}
