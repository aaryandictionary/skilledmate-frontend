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
import com.app.miniproject.Database.SkillsList.SkillsListData;
import com.app.miniproject.R;

import java.util.ArrayList;
import java.util.List;

public class TagListAdapter extends ArrayAdapter<SkillsListData> {

    private List<SkillsListData>skillsListDataList=new ArrayList<>();

    public TagListAdapter(@NonNull Context context, int resource, @NonNull List<SkillsListData> objects) {
        super(context, 0, objects);
        skillsListDataList=objects;
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
            listViewItem= LayoutInflater.from(getContext()).inflate(R.layout.items_tag_list_selection,parent,false);
        }

        SkillsListData skillsListData=getItem(position);

        TextView txtTagName=listViewItem.findViewById(R.id.txtTagName);
        txtTagName.setText(skillsListData.getSkillName());

        return listViewItem;
    }

    private Filter itemFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            List<SkillsListData> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(skillsListDataList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (SkillsListData skillsListData : skillsListDataList) {
                    if (skillsListData.getSkillName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(skillsListData);
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
            return ((SkillsListData)resultValue).getSkillName();
        }
    };
}
