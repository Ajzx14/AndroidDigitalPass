package com.example.shiva.try1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class view_list extends AppCompatActivity implements View.OnClickListener {
    String stand_,address_profile,parents_occ,colleg_value,branch_profile,fname_profile,lname_profile,mname_profile,mobile_profile,p_mobile_profile,email_profile,paidtill_profile,route_profile,college_profile,bustype_profile;
    Bitmap Image_list=null;
    StorageReference storageReference;
    FirebaseStorage storage;
    Bitmap bitmap,bitmap1;
    CardView card_view;
    private RelativeLayout mRelativeLayout;
    private PopupWindow mPopupWindow;
    Spinner college;
   public ImageView image=null;
    Button college_search;
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    StorageReference ref;
    File localFile = null;
    private Context mContext;
    private Activity mActivity;
    File finalLocalFile;
    Handler  mVolHandler;
    Runnable mVolRunnable;
    SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

        setContentView(R.layout.activity_main_list);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        recyclerView = findViewById(R.id.recycler_view);
        //card_view =findViewById(R.id.list_card);
      college = findViewById(R.id.l_college2);
        college_search = findViewById(R.id.l_search2);
        college_search.setOnClickListener(this);
        mAdapter = new MoviesAdapter(movieList);
        mRelativeLayout =  findViewById(R.id.list_relativ);
        recyclerView.setHasFixedSize(true);
        card_view = findViewById(R.id.list_card);
       // search = findViewById(R.id.search_in);

        //image=findViewById(R.id.pop_image);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
         recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        mVolHandler = new Handler();
//         mVolRunnable = new Runnable() {
//            public void run() {
//                card_view.setVisibility(View.INVISIBLE);
//            }
//        };
       // final Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale);
        recyclerView.setAdapter(mAdapter);
        //
        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movieList.get(position);
                //card_view.startAnimation(anim);
                Toast.makeText(getApplicationContext(), movie.getfname() + " is selected!", Toast.LENGTH_SHORT).show();
              //
//                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//
//                // Inflate the custom layout/view
//                View customView = inflater.inflate(R.layout.popup_view,null);
//                mPopupWindow = new PopupWindow(
//                        customView,
//                        RelativeLayout.LayoutParams.WRAP_CONTENT,
//                        RelativeLayout.LayoutParams.WRAP_CONTENT
//                );
//
//                // Set an elevation value for popup window
//                // Call requires API level 21
//
//                TextView name = findViewById(R.id.tv);
//                ImageView image = findViewById(R.id.pop_image);
//                Bitmap im = movie.getImage();
//                image.setImageBitmap(im);
//                name.setText(movie.getfname());
//                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
//
//                // Set a click listener for the popup window close button
//                closeButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        // Dismiss the popup window
//                        mPopupWindow.dismiss();
//                    }
//
//                });
//                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);

            }

            @Override
            public void onLongClick(View view, int position) {
                Movie movie = movieList.get(position);
                Toast.makeText(getApplicationContext(), movie.getfname() + " is selected!", Toast.LENGTH_SHORT).show();
                String s=movie.getmobile();
                String c =movie.getcollege();
                Bundle basket= new Bundle();
                basket.putString("searchValue", s);
                basket.putString("college",c);
                Intent a=new Intent(view_list.this,profile.class);
                a.putExtras(basket);
                startActivity(a);
            }
        }));
      // college_search.setOnClickListener(this);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(view_list.this, home.class));
//
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.l_search2:
                //recyclerView.set;
                colleg_value = college.getSelectedItem().toString().trim();
                dataRetrive_new();
              //  progressDialog.dismiss();
                break;
        }


    }

    public void dataRetrive_new(){
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Featching Data...");
//        progressDialog.show();
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference(colleg_value);
      //  DatabaseReference zone1Ref = (DatabaseReference) zonesRef.child("college").orderByKey().equalTo(colleg_value);
       ref = storageReference;


        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
         finalLocalFile = localFile;

        zonesRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                    branch_profile =  postSnapshot.child("branch").getValue(String.class);

                    bustype_profile =  postSnapshot.child("bus type").getValue(String.class);

                    college_profile = postSnapshot.child("college").getValue(String.class);

                    email_profile = postSnapshot.child("email").getValue(String.class);

                    fname_profile = postSnapshot.child("first name").getValue(String.class);

                    lname_profile = postSnapshot.child("last name").getValue(String.class);

                    mname_profile = postSnapshot.child("middle name").getValue(String.class);

                    mobile_profile =(String) postSnapshot.child("mobile").getValue(String.class);

//                    ref.child("images/").getFile(localFile)
//                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                                    // Successfully downloaded data to local file
//                                    // ...
//                                    Image_list = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
//
//                                    //  photo.setImageBitmap(bitmap);
//                                    // progressDialog.dismiss();
//                                    // dataRetrive(imageId); return bitmap;
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle failed download
//                            // ...
//                        }
//                    });

                    paidtill_profile = postSnapshot.child("paid till").getValue(String.class);



                    route_profile = postSnapshot.child("route").getValue(String.class);

                    GoToImage(fname_profile,lname_profile,mname_profile,mobile_profile,email_profile,paidtill_profile,route_profile,bustype_profile,stand_,college_profile);


                }
          //  progressDialog.dismiss();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(profile.this,""+databaseError,Toast.LENGTH_LONG).show();
            }
        });



    }

    private void GoToImage(final String fname_, final String lname_, final String mname_, final String mobile_, final String email_, final String paidtill_, final String route_, final String bustype_, final String stand_1,final String college_profile) {
        StorageReference islandRef = storageReference.child("images/"+mobile_profile);
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Image_list = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Movie data = new Movie(Image_list,fname_,lname_,mname_,mobile_,email_,paidtill_,route_,bustype_,stand_1,college_profile);
                movieList.add(data);
                mAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });




    }


    public void dataRetrive(final String UniqeId){

        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference();
       // DatabaseReference zone1Ref = zonesRef.child(UniqeId);
        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                address_profile = dataSnapshot.child("address").getValue(String.class);
//                branch_profile =  dataSnapshot.child("branch").getValue(String.class);
//
//                bustype_profile =  dataSnapshot.child("bus type").getValue(String.class);
//
//                college_profile = dataSnapshot.child("college").getValue(String.class);
//
//                email_profile = dataSnapshot.child("email").getValue(String.class);
//
//                fname_profile = dataSnapshot.child("first name").getValue(String.class);
//
//                lname_profile = dataSnapshot.child("last name").getValue(String.class);
//
//                mname_profile = dataSnapshot.child("middle name").getValue(String.class);
//
//                mobile_profile = dataSnapshot.child("mobile").getValue(String.class);
//
//                paidtill_profile = dataSnapshot.child("paid till").getValue(String.class);
//
//                p_mobile_profile = dataSnapshot.child("parents mobile").getValue(String.class);
//
//                route_profile = dataSnapshot.child("route").getValue(String.class);
            for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                college_profile = postSnapshot.child("college").getValue(String.class);
                if(college_profile == UniqeId){
                    mobile_profile = postSnapshot.child("mobile").getValue(String.class);
                    //college_data(mobile_profile);
                }
            }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(view_list.this,""+databaseError,Toast.LENGTH_LONG).show();
            }
        });


    }

//    private void college_data(String UniqeId) {
//
//        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference(UniqeId);
//        //  DatabaseReference zone1Ref = zonesRef.child(UniqeId);
//        zonesRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                stand_ =  dataSnapshot.child("stand").getValue(String.class);
//
//                bustype_profile =  dataSnapshot.child("bus type").getValue(String.class);
//
//
//
//                email_profile = dataSnapshot.child("email").getValue(String.class);
//
//                fname_profile = dataSnapshot.child("first name").getValue(String.class);
//
//                lname_profile = dataSnapshot.child("last name").getValue(String.class);
//
//                mname_profile = dataSnapshot.child("middle name").getValue(String.class);
//
//                mobile_profile = dataSnapshot.child("mobile").getValue(String.class);
//
//                paidtill_profile = dataSnapshot.child("paid till").getValue(String.class);
//
//
//
//                route_profile = dataSnapshot.child("route").getValue(String.class);
//
//                Movie data = new Movie(fname_profile,lname_profile,mname_profile,mobile_profile,email_profile,paidtill_profile,route_profile,bustype_profile,stand_);
//                movieList.add(data);
//                mAdapter.notifyDataSetChanged();
//               // Movie data1 = new Movie("","")
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(view_list.this,""+databaseError,Toast.LENGTH_LONG).show();
//            }
//        });


    }


