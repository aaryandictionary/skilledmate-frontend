package com.app.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.app.miniproject.Utils.DataProcessor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    DataProcessor dataProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        dataProcessor=DataProcessor.getInstance(this);
        mAuth=FirebaseAuth.getInstance();

        dataProcessor.saveBoolean(DataProcessor.SHARE_VIEWED,true);

        getReferralCode();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        },1500);
    }

    private void checkUser(){
        if (mAuth.getCurrentUser()!=null){
//            if (dataProcessor.getBoolean(DataProcessor.PROFILE_COMPLETE)){
                Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            /*}else{
                Intent intent=new Intent(SplashScreen.this,CompleteProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }*/
        }else{
            Intent intent=new Intent(SplashScreen.this,Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void getReferralCode(){
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.d("TAGNEW",deepLink.toString());

                        }

                        try {
                            String shareId=deepLink.toString().substring(deepLink.toString().lastIndexOf("/")+1);
                            String leftover=deepLink.toString().substring(0,deepLink.toString().lastIndexOf("/"));
                            String shareType=leftover.substring(leftover.lastIndexOf("/")+1);
                            dataProcessor.saveString(DataProcessor.SHARE_TYPE,shareType);
                            dataProcessor.saveString(DataProcessor.SHARE_ID,shareId);
                            dataProcessor.saveBoolean(DataProcessor.SHARE_VIEWED,false);
                            Toast.makeText(SplashScreen.this,"SHID "+shareId+" TYPE "+shareType,Toast.LENGTH_SHORT).show();
//                            dataProcessor.saveString(DataProcessor.REFERRAL_USER,referalId);
//                            Log.d("TAGNEW",referalId);
                        }catch (Exception e){

                        }

                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "getDynamicLink:onFailure", e);
                    }
                });
    }
}