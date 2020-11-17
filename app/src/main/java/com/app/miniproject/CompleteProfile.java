package com.app.miniproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.miniproject.Adapters.CollegeListAdapter;
import com.app.miniproject.Database.CollegeList.CollegeListResponse;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.Register.RegisterModel;
import com.app.miniproject.Database.UserInterface;
import com.app.miniproject.Utils.DataProcessor;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteProfile extends AppCompatActivity {

    EditText textName,textEmail;
    AutoCompleteTextView autoCompleteCollege;
    Button btnNext;
    List<String> strings=new ArrayList<>();
    CollegeListAdapter collegeListAdapter;
    ContentLoadingProgressBar progress;

    String selectedCollege=null;
    Integer selectedCollegeId=-1;
    DataProcessor dataProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        initializeView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        dataProcessor=DataProcessor.getInstance(this);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()){
                    btnNext.setVisibility(View.GONE);
                    progress.show();
                    textName.setEnabled(false);
                    textEmail.setEnabled(false);
                    autoCompleteCollege.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            saveData();
                        }
                    },1500);
                }
            }
        });

        autoCompleteCollege.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (collegeListAdapter.getItem(position)!=null)
                selectedCollege=collegeListAdapter.getItem(position).getCollegeName();
                autoCompleteCollege.setText(collegeListAdapter.getItem(position).getCollegeName());
                selectedCollegeId=collegeListAdapter.getItem(position).getId();
            }
        });

        getCollegeList();

    }


    private void saveData(){
        dataProcessor.saveString(DataProcessor.NAME,textName.getText().toString());
        dataProcessor.saveString(DataProcessor.EMAIL,textEmail.getText().toString());
        dataProcessor.saveString(DataProcessor.COLLEGE,selectedCollege);
        dataProcessor.saveInteger(DataProcessor.COLLEGE_ID,selectedCollegeId);

        Intent intent=new Intent(CompleteProfile.this,AddSkills.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showSnakbar(String message){
        final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackBar.show();
    }

    private boolean validateFields(){
        if (TextUtils.isEmpty(textName.getText().toString())){
            showSnakbar("Enter your Name");
            return false;
        }
        if (TextUtils.isEmpty(textEmail.getText().toString())){
            showSnakbar("Enter your Email");
            return false;
        }
        if (TextUtils.isEmpty(selectedCollege)|selectedCollege==null){
            showSnakbar("Enter your College");
            return false;
        }

        return true;
    }

    private void getCollegeList(){
        UserInterface userInterface= Database.getClient(this).create(UserInterface.class);
        userInterface.getCollegeList().enqueue(new Callback<CollegeListResponse>() {
            @Override
            public void onResponse(Call<CollegeListResponse> call, Response<CollegeListResponse> response) {
                Toast.makeText(CompleteProfile.this,"Code "+response.code(),Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                    collegeListAdapter=new CollegeListAdapter(CompleteProfile.this,R.layout.items_college_list_selection,response.body().getData());
                    autoCompleteCollege.setAdapter(collegeListAdapter);
                    collegeListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CollegeListResponse> call, Throwable t) {

            }
        });
    }

    private void initializeView(){
        textName=findViewById(R.id.textName);
        textEmail=findViewById(R.id.textEmail);
        autoCompleteCollege=findViewById(R.id.autoCompleteCollege);
        btnNext=findViewById(R.id.btnNext);
        progress=findViewById(R.id.progress);
//
//        String[] collegeList={"Apeejay Institute of Technology, School of Architecture & Planning","IEC College of Engineering and Technology","Ram-Eesh Institute of Vocational and Technical Education","Galgotias Institute of Management & Technology","Galgotia's College of Engineering and Technology","Harlal Institute of Management & Technology","Greater Noida Institute of Technology","Noida Institute of Engineering and Technology","IILM College of Engineering and Technology","Global Institute of Information Technology","Mangalmay Institute of Engineering & Technology","Sky line Institute of Engineering & Technology","Llyod Institute of Management & Technology","GL Bajaj Institute of Technology and Management","United College of Engineering and Research","Rakshpal Bahadur Management Institute","United Institute of Management","H.I.M.T. College of Pharmacy","I.I.M.T. College of Engineering","I.T.S. Engineering College","Accurate Institute of Management and Technology","Innovative College of Pharmacy","Dronacharya Group of Institutions","NIMT Institute of Hospital & Pharma Management","I.I.M.T. College of Pharmacy","Greater Noida Institute of Technology (MBA Institute)","Noida Institute of Engineering & Technology (MCA Institute)","Patronage Institute of Management Studies","B.B.S. Institute of Pharmaceutical & Allied Science","Spectrum Institute of Pharmaceutical Science & Research","KCC Institute of Technology and Management","Prince Institute of Innovative Technology","Sarvottam Institute of Technology & Management","KCC Institute of Management","Lloyd Business School","Accurate Institute of Advanced Management","Sky Line Institute of Management & Technology","FMG Academy Group of Institutions","Mangalmay Institute of Engineering and Technology, Knowledge Park","G.L Bajaj Institute of Management and Research","Greater Noida Institute of Business Management","Accurate Institute of Architercture & Panning","Greater Noida College of Technology","Metro College of Health Sciences & Research","Ch. Charan Singh College of Pharmacy","Skyline Institute of Pharmacy","Maharishi Institute of Management"};
//
//
//        strings.addAll(Arrays.asList(collegeList));



//        autoComplete=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,strings);
//        autoCompleteCollege.setAdapter(autoComplete);


    }



}