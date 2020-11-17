package com.app.miniproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.miniproject.Adapters.MyTeamAdapter;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.TeamInterface;
import com.app.miniproject.Database.Teams.MyTeamResponse;
import com.app.miniproject.Database.UserInterface;
import com.app.miniproject.Models.MyTeamsModel;
import com.app.miniproject.Utils.DataProcessor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyTeams extends Fragment implements MyTeamAdapter.MyTeamEvents{
    RecyclerView recyclerMyTeams;
    MyTeamAdapter myTeamAdapter;

    DataProcessor dataProcessor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_teams, container, false);
        recyclerMyTeams=view.findViewById(R.id.recyclerMyTeams);
        recyclerMyTeams.setHasFixedSize(true);
        recyclerMyTeams.setLayoutManager(new LinearLayoutManager(getContext()));
        myTeamAdapter=new MyTeamAdapter(getContext(),this);
        recyclerMyTeams.setAdapter(myTeamAdapter);

        dataProcessor=DataProcessor.getInstance(getContext());

        loadData();
        return view;
    }


    private void loadData(){
        TeamInterface teamInterface= Database.getClient(getContext()).create(TeamInterface.class);
        teamInterface.getMyTeams(dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<MyTeamResponse>() {
            @Override
            public void onResponse(Call<MyTeamResponse> call, Response<MyTeamResponse> response) {
                if (response.isSuccessful()){
                    myTeamAdapter.setMyTeams(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<MyTeamResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMyTeamClick(Integer teamId) {
        Intent intent=new Intent(getContext(),TeamDetails.class);
        intent.putExtra("type","EDIT");
        intent.putExtra("teamId",teamId);
        startActivity(intent);
    }
}