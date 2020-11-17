package com.app.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.Login.LoginData;
import com.app.miniproject.Database.Login.LoginModel;
import com.app.miniproject.Database.Login.LoginResponse;
import com.app.miniproject.Database.UserInterface;
import com.app.miniproject.Utils.DataProcessor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {


    EditText textMobile,textOtp;
    TextView txtChange,txtResendOtp,txtButtonTitle;
    RelativeLayout relOtp;
    RelativeLayout relLoginButton;
    ImageView imgInButton;

    ContentLoadingProgressBar progress;
    DataProcessor dataProcessor;

    ProgressDialog progressDialog;

    private String mVerificationId;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    CountDownTimer countDownTimer;

    String token=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeView();

        mAuth = FirebaseAuth.getInstance();
        dataProcessor=DataProcessor.getInstance(this);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        relLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()){
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    if (!TextUtils.isEmpty(textOtp.getText().toString())&&textOtp.getText().length()==6){
                        verifyVerificationCode(textOtp.getText().toString());
                    }else{
                        Toast.makeText(Login.this,"Enter a valid OTP",Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                }
            }
        });

        textMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==10){
                    txtChange.setVisibility(View.VISIBLE);
                    textMobile.setEnabled(false);
                    sendVerificationCode(s.toString());
                }
            }
        });

        txtResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(txtResendOtp.getText(),"Resend")){
                    if (textMobile.getText().length()==10){
                        resendVerificationCode(textMobile.getText().toString());
                        progressDialog.show();
                        txtResendOtp.setVisibility(View.GONE);
                    }
                }
            }
        });

        txtChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer!=null)
                countDownTimer.cancel();

                textMobile.setEnabled(true);

                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams)txtButtonTitle.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                txtButtonTitle.setLayoutParams(layoutParams);
                txtButtonTitle.setText("SEND OTP");
                progress.hide();
                imgInButton.setVisibility(View.VISIBLE);
                relLoginButton.setBackground(getResources().getDrawable(R.drawable.bg_btn_primary));
            }
        });

        getToken();
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


    private boolean validateFields(){
        if (TextUtils.isEmpty(textMobile.getText().toString())){
            Toast.makeText(Login.this,"Enter Mobile Number",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (textMobile.getText().length()!=10){
            Toast.makeText(Login.this,"Mobile Number should be 10 digits",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (token==null){
            showSnakbar("Token error. Try again");
            return false;
        }
        return true;
    }

    private void sendVerificationCode(String mobile) {
        progress.show();
        relLoginButton.setVisibility(View.GONE);
        txtButtonTitle.setVisibility(View.GONE);
        imgInButton.setVisibility(View.GONE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private void resendVerificationCode(String mobile){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+ mobile,
                60  ,
                TimeUnit.SECONDS,
                Login.this,
                mCallbacks,
                mResendToken);
    }

    private void startTimer(){
        txtResendOtp.setVisibility(View.VISIBLE);
        progressDialog.hide();

        countDownTimer= new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished/1000!=0){
                    txtResendOtp.setText("Resend in " + millisUntilFinished / 1000+" seconds");
                }else{
                    countDownTimer.onFinish();
                }
            }

            public void onFinish() {
                countDownTimer.cancel();
                txtResendOtp.setText("Resend");
                txtResendOtp.setTextColor(getResources().getColor(R.color.colorLightBlue));
            }

        }.start();
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                textOtp.setText(code);
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            showSnakbar("OTP Verification failed. Try again later");
            countDownTimer.cancel();
            progressDialog.hide();
            txtResendOtp.setText("Resend");
            txtResendOtp.setVisibility(View.VISIBLE);
            progressDialog.hide();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            showSnakbar("OTP Sent Successfully");
            relOtp.setVisibility(View.VISIBLE);
            mVerificationId = s;
            progress.hide();
            relLoginButton.setVisibility(View.VISIBLE);
            txtButtonTitle.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams)txtButtonTitle.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            txtButtonTitle.setLayoutParams(layoutParams);
            txtButtonTitle.setText("CONFIRM");
            relLoginButton.setBackground(getResources().getDrawable(R.drawable.bg_btn_accent));

            imgInButton.setVisibility(View.GONE);
            startTimer();
            mResendToken = forceResendingToken;
        }
    };

    private void verifyVerificationCode(String otp) {
        textOtp.setEnabled(false);

        progress.show();
        relLoginButton.setVisibility(View.GONE);
        txtButtonTitle.setVisibility(View.GONE);
        imgInButton.setVisibility(View.GONE);

        countDownTimer.cancel();
        txtResendOtp.setText("Verifying...");
        txtResendOtp.setTextColor(getResources().getColor(R.color.colorGray));
        progressDialog.hide();
        txtResendOtp.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this,"COde ",Toast.LENGTH_SHORT).show();
                            LoginModel loginModel=new LoginModel();
                            loginModel.setPhone(textMobile.getText().toString());
                            loginModel.setPassword(mAuth.getCurrentUser().getUid());
                            loginModel.setToken(token);
                            checkUser(loginModel);
                        }else {
                            Toast.makeText(Login.this,"fail ",Toast.LENGTH_SHORT).show();
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
//                                txtError.setText("Invalid verification code");
//                                textPassword.requestFocus();
                            }  catch(Exception e) {
//                                Log.e("TAG", e.getMessage());
//                                txtError.setText("Something went wrong");
                            }

                            countDownTimer.cancel();
                            txtResendOtp.setText("Resend");
                            txtResendOtp.setTextColor(getResources().getColor(R.color.colorLightBlue));
                            textOtp.setEnabled(true);

                            progress.hide();
                            relLoginButton.setVisibility(View.VISIBLE);
                            txtButtonTitle.setVisibility(View.VISIBLE);
                            RelativeLayout.LayoutParams layoutParams =
                                    (RelativeLayout.LayoutParams)txtButtonTitle.getLayoutParams();
                            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                            txtButtonTitle.setLayoutParams(layoutParams);
                            txtButtonTitle.setText("CONFIRM");
                            relLoginButton.setBackground(getResources().getDrawable(R.drawable.bg_btn_accent));

                            imgInButton.setVisibility(View.GONE);
                        }

                    }
                });
    }

    private void showSnakbar(String message){
        final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackBar.show();
    }

    private void checkUser(LoginModel loginModel){
        UserInterface userInterface= Database.getClient(this).create(UserInterface.class);
        userInterface.loginUser(loginModel).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Toast.makeText(Login.this,"CODE "+response.code()+" MESS "+response.message(),Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                    if (response.body().getCode()==1){
                        saveData(response.body().getData());
                    }else if (response.body().getCode()==-1){
                        dataProcessor.saveString(DataProcessor.MOBILE,textMobile.getText().toString());
                        Intent intent=new Intent(Login.this,CompleteProfile.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this,"fail "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveData(LoginData loginData){
        dataProcessor.saveString(DataProcessor.NAME,loginData.getName());
        dataProcessor.saveString(DataProcessor.MOBILE,loginData.getPhone());
        dataProcessor.saveInteger(DataProcessor.USER_ID,loginData.getId());
        dataProcessor.saveInteger(DataProcessor.COLLEGE_ID,loginData.getCollegeId());
        dataProcessor.saveString(DataProcessor.EMAIL,loginData.getEmail());
        dataProcessor.saveString(DataProcessor.USER_IMAGE,loginData.getUser_image());

        Intent intent=new Intent(Login.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void initializeView(){
        textMobile=findViewById(R.id.textMobile);
        textOtp=findViewById(R.id.textOtp);
        txtChange=findViewById(R.id.txtChange);
        txtResendOtp=findViewById(R.id.txtResendOtp);
        txtButtonTitle=findViewById(R.id.txtButtonTitle);
        relOtp=findViewById(R.id.relOtp);
        relLoginButton=findViewById(R.id.relLoginButton);
        imgInButton=findViewById(R.id.imgInButton);
        progress=findViewById(R.id.progress);
    }
}