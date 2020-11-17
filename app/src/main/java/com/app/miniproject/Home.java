package com.app.miniproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.miniproject.Adapters.CourseListAdapter;
import com.app.miniproject.Adapters.EventAdapter;
import com.app.miniproject.Adapters.HomeAdapter;
import com.app.miniproject.Adapters.PostAdapter;
import com.app.miniproject.Adapters.ProjectAdapter;
import com.app.miniproject.Database.Course.CourseListResponse;
import com.app.miniproject.Database.CourseInterface;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.Event.EventListResponse;
import com.app.miniproject.Database.Post.PostData;
import com.app.miniproject.Database.Post.PostDataResponse;
import com.app.miniproject.Database.PostInterface;
import com.app.miniproject.Database.PostLike.PostLikeModel;
import com.app.miniproject.Database.PostLike.PostLikeResponse;
import com.app.miniproject.Database.TeamInterface;
import com.app.miniproject.Models.ExpertiseModel;
import com.app.miniproject.Models.HomeModel;
import com.app.miniproject.Models.PostModel;
import com.app.miniproject.Models.ProjectModel;
import com.app.miniproject.Models.UserModel;
import com.app.miniproject.Utils.DataProcessor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment implements PostAdapter.PostClickEvents,EventAdapter.EventClickEvent,CourseListAdapter.CourseClickEvents {

    RecyclerView recyclerHome,recyclerEvents,recyclerCourses;
    PostAdapter postAdapter;
    EditText textSearch;

    NestedScrollView nestedScroll;
    DataProcessor dataProcessor;

    EventAdapter eventAdapter;
    CourseListAdapter courseListAdapter;

    RelativeLayout relEvents,relCourses;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeView(view);

        postAdapter=new PostAdapter(getContext(),this,0);
        recyclerHome.setAdapter(postAdapter);

        eventAdapter=new EventAdapter(getContext(),this);
        recyclerEvents.setAdapter(eventAdapter);

        courseListAdapter=new CourseListAdapter(getContext(),this);
        recyclerCourses.setAdapter(courseListAdapter);

        dataProcessor=DataProcessor.getInstance(getContext());

        /*recyclerHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy<=0){
                    textSearch.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.bottom_navigation_main).setVisibility(View.VISIBLE);
                }else{
                    textSearch.setVisibility(View.GONE);
                    getActivity().findViewById(R.id.bottom_navigation_main).setVisibility(View.GONE);
                }
            }
        });*/

        /*nestedScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
//                    Log.i(TAG, "Scroll DOWN");
//                    textSearch.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.bottom_navigation_main).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.floatBtnAdd).setVisibility(View.GONE);
                }
                if (scrollY < oldScrollY) {
//                    Log.i(TAG, "Scroll UP");
//                    textSearch.setVisibility(View.GONE);
                    getActivity().findViewById(R.id.bottom_navigation_main).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.floatBtnAdd).setVisibility(View.VISIBLE);
                }
                if (scrollY==oldScrollY){
                    getActivity().findViewById(R.id.bottom_navigation_main).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.floatBtnAdd).setVisibility(View.VISIBLE);
                }
            }
        });*/

        getUpcomingEvents();
        getCoursesForMe();
        loadPosts();
        return view;
    }

    private void getUpcomingEvents(){
        TeamInterface teamInterface=Database.getClient(getContext()).create(TeamInterface.class);
        teamInterface.getEventsForMe(dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<EventListResponse>() {
            @Override
            public void onResponse(Call<EventListResponse> call, Response<EventListResponse> response) {
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        if (response.body().getData().size()==0)
                            relEvents.setVisibility(View.GONE);
                        else relEvents.setVisibility(View.VISIBLE);

                        eventAdapter.setEventList(response.body().getData());
                        relEvents.setVisibility(View.VISIBLE);
                    }else {
                        relEvents.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<EventListResponse> call, Throwable t) {

            }
        });

    }

    private void getCoursesForMe(){
        CourseInterface courseInterface=Database.getClient(getContext()).create(CourseInterface.class);
        courseInterface.getCourseSuggestions(dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<CourseListResponse>() {
            @Override
            public void onResponse(Call<CourseListResponse> call, Response<CourseListResponse> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        if (response.body().getData().size()==0)
                            relCourses.setVisibility(View.GONE);
                        else relCourses.setVisibility(View.VISIBLE);

                        courseListAdapter.setCourseList(response.body().getData());
                        relCourses.setVisibility(View.VISIBLE);
                    }else {
                        relCourses.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<CourseListResponse> call, Throwable t) {

            }
        });
    }

    private void loadPosts(){
        PostInterface postInterface= Database.getClient(getContext()).create(PostInterface.class);
        postInterface.getAllPosts(dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<PostDataResponse>() {
            @Override
            public void onResponse(Call<PostDataResponse> call, Response<PostDataResponse> response) {
//                Toast.makeText(getContext(),"CODE "+response.code(),Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                    postAdapter.addPostToList(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<PostDataResponse> call, Throwable t) {
//                Toast.makeText(getContext(),"FAIL "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void initializeView(View view){
        relEvents=view.findViewById(R.id.relEvents);
        relCourses=view.findViewById(R.id.relCourses);

        textSearch=view.findViewById(R.id.textSearch);
        nestedScroll=view.findViewById(R.id.nestedScroll);

        recyclerHome=view.findViewById(R.id.recyclerHome);
        recyclerHome.setHasFixedSize(true);
        recyclerHome.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerEvents=view.findViewById(R.id.recyclerEvents);
        recyclerEvents.setHasFixedSize(true);
        recyclerEvents.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        recyclerCourses=view.findViewById(R.id.recyclerCourses);
        recyclerCourses.setHasFixedSize(true);
        recyclerCourses.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
    }

    @Override
    public void onLikeClick(Integer postId,Integer position) {
        postAdapter.swapLike(position);
        likePost(postId,position);
    }

    @Override
    public void onCommentClick(PostData postData) {
        openPostDetails(postData);
    }

    @Override
    public void onShareClick(Integer postId) {
        createLink("POST",String.valueOf(postId));
    }

    @Override
    public void onUserClick(Integer userId) {
        Intent intent=new Intent(getContext(),UserProfile.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }

    @Override
    public void onPostClick(PostData postData) {
        openPostDetails(postData);
    }

    @Override
    public void onTeamClick(Integer teamId) {
        Intent intent=new Intent(getContext(),TeamDetails.class);
        intent.putExtra("teamId",teamId);
        startActivity(intent);
    }

    @Override
    public void onEventDetailsClick(Integer eventId) {
        Intent intent=new Intent(getContext(),EventDetails.class);
        intent.putExtra("eventId",eventId);
        startActivity(intent);
    }


    private void likePost(Integer postId, final Integer position){
        PostLikeModel postLikeModel=new PostLikeModel();
        postLikeModel.setPost_id(postId);
        postLikeModel.setLiker_id(dataProcessor.getInteger(DataProcessor.USER_ID));

        PostInterface postInterface=Database.getClient(getContext()).create(PostInterface.class);
        postInterface.swapPostLike(postLikeModel).enqueue(new Callback<PostLikeResponse>() {
            @Override
            public void onResponse(Call<PostLikeResponse> call, Response<PostLikeResponse> response) {
                if (response.isSuccessful()){

                }else {
                    postAdapter.swapLike(position);
                }
            }

            @Override
            public void onFailure(Call<PostLikeResponse> call, Throwable t) {
                postAdapter.swapLike(position);
            }
        });
    }


    private void openPostDetails(PostData postData){
        Intent intent=new Intent(getContext(),PostDetail.class);
        intent.putExtra("postId",postData.getId());
        startActivity(intent);
    }

    @Override
    public void onEventClick(Integer eventId,Integer teamId) {
        Intent intent=new Intent(getContext(),EventDetails.class);
        intent.putExtra("eventId",eventId);
        intent.putExtra("teamId",teamId);
        startActivity(intent);
    }

    @Override
    public void onCourseClick(Integer courseId) {
        Intent intent=new Intent(getContext(),CourseDetails.class);
        intent.putExtra("courseId",courseId);
        startActivity(intent);
    }

    private void createLink(String type,String id){
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.kvgames.com/"))
                .setDomainUriPrefix("mpgcet.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        String shareLinkText="https://mpgcet.page.link/?" +
                "link=https://www.kvgames.com/".concat(type).concat("/").concat(id)+
                "&apn="+getContext().getPackageName() +
                "&st=Skilled Mate" +
                "&sd=Post from Skilled Mate" +
                "&si=http://kvgames.com/logo.jpg";

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(shareLinkText))
                .buildShortDynamicLink()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT,shortLink.toString());
                            intent.setType("text/plain");
                            startActivity(intent);
                        }
                    }
                });
    }
}