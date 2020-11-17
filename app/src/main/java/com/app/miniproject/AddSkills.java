package com.app.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.app.miniproject.Adapters.TagListAdapter;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.Register.RegisterModel;
import com.app.miniproject.Database.Register.RegisterResponse;
import com.app.miniproject.Database.Register.RegisterUserData;
import com.app.miniproject.Database.SkillsList.SkillsListData;
import com.app.miniproject.Database.SkillsList.SkillsListResponse;
import com.app.miniproject.Database.UserInterface;
import com.app.miniproject.Utils.DataProcessor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSkills extends AppCompatActivity implements View.OnClickListener {
    ChipGroup chipGroup;

    Button btnFinish;

    AutoCompleteTextView autoCompleteSkills;

    List<Integer>selectedSkills=new ArrayList<>();
    ContentLoadingProgressBar progress;
    DataProcessor dataProcessor;

    FirebaseAuth mAuth;
    TagListAdapter tagListAdapter;

    String token=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_skills);
        initializeView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        dataProcessor=DataProcessor.getInstance(this);
        mAuth=FirebaseAuth.getInstance();

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()){
                    btnFinish.setVisibility(View.GONE);
                    progress.show();
                    autoCompleteSkills.setEnabled(false);
                    chipGroup.setEnabled(false);
                    registerUser();
                }
            }
        });

        getToken();
        getTagList();
    }


    private void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()){
                    token=task.getResult().getToken();
                }
            }
        });
    }

    private void registerUser(){
        RegisterModel registerModel=new RegisterModel();
        registerModel.setPhone(dataProcessor.getString(DataProcessor.MOBILE));
        registerModel.setCollegeId(dataProcessor.getInteger(DataProcessor.COLLEGE_ID));
        registerModel.setEmail(dataProcessor.getString(DataProcessor.EMAIL));
        registerModel.setName(dataProcessor.getString(DataProcessor.NAME));
        registerModel.setPassword(mAuth.getCurrentUser().getUid());
        registerModel.setToken(token);

        Integer ss[]=new Integer[selectedSkills.size()];

        int i=0;
        for (Integer skill:selectedSkills){
            ss[i]=skill;
            i++;
        }
        registerModel.setSkills(ss);

        UserInterface userInterface=Database.getClient(this).create(UserInterface.class);
        userInterface.registerUser(registerModel).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getCode()==1){
                        saveData(response.body().getData());
                    }else if (response.body().getCode()==2){
                        Toast.makeText(AddSkills.this,"User already exist. Login",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AddSkills.this,Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(AddSkills.this,"Something went wrong. Try again later",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddSkills.this,"ERROR "+response.code()+response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(AddSkills.this,"FAIL "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveData(RegisterUserData registerUserData){
        dataProcessor.saveInteger(DataProcessor.USER_ID,registerUserData.getId());

        Intent intent=new Intent(AddSkills.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private boolean validateFields(){
        if (selectedSkills.size()<=0){
            showSnakbar("Select at least 3 Skills or Interest");
            return false;
        }
        if (token==null){
            showSnakbar("Token error. Try again");
            return false;
        }
        return true;
    }

    private void getTagList(){
        UserInterface userInterface= Database.getClient(this).create(UserInterface.class);
        userInterface.getSkillsList().enqueue(new Callback<SkillsListResponse>() {
            @Override
            public void onResponse(Call<SkillsListResponse> call, Response<SkillsListResponse> response) {
                if (response.isSuccessful()){
                    /*tagListAdapter=new TagListAdapter(AddSkills.this,R.layout.items_tag_list_selection,response.body().getData());
                    autoCompleteSkills.setAdapter(tagListAdapter);*/
                    renderTags(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<SkillsListResponse> call, Throwable t) {

            }
        });
    }


    private void renderTags(List<SkillsListData>skillsListData){
        for (SkillsListData skills:skillsListData){
            final Chip chip=new Chip(AddSkills.this);
            chip.setText(skills.getSkillName());
            chip.setTag(String.valueOf(skills.getId()));
            chip.setCloseIconVisible(false);
            chip.setChipStrokeWidth(1);
            chip.setChipStrokeColorResource(R.color.colorAccent);
            chip.setCheckable(true);
            chip.setClickable(true);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (chip.isChecked()){
                        selectedSkills.add(Integer.valueOf(String.valueOf(chip.getTag())));
                    }else {
                        selectedSkills.remove(Integer.valueOf(String.valueOf(chip.getTag())));
                    }
                }
            });

//            selectedSkills.add(tagListAdapter.getItem(position).getId());
            chipGroup.addView(chip);
        }
    }


    private void showSnakbar(String message){
        final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackBar.show();
    }

    private void initializeView(){
        autoCompleteSkills=findViewById(R.id.autoCompleteSkills);
        chipGroup=findViewById(R.id.chipGroup);
        progress=findViewById(R.id.progress);
        btnFinish=findViewById(R.id.btnFinish);


        autoCompleteSkills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                tagListAdapter.remove(tagListAdapter.getItem(position));
//                strings.remove(autoComplete.getItem(position));
//                autoComplete.notifyDataSetChanged();
                autoCompleteSkills.setText("");
            }
        });
    }

    @Override
    public void onClick(View v) {
        Chip chip=(Chip)v;
        /*strings.add(chip.getText().toString());
        autoComplete.notifyDataSetChanged();*/
//        tagListAdapter.add(chip.getText().toString());
        selectedSkills.remove(chip.getText().toString());
        chipGroup.removeView(chip);
    }
}