package com.example.shiva.try1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class report extends AppCompatActivity implements View.OnClickListener {
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    TextView date, cash_value, chaque_value;
    Spinner college, type;
    Button search;
    CardView chaque, cash;
    String college_id, type_id, user_id, amount_type;
    String[] scanned_ids;
    int feecash, feechaque = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private SimpleDateFormat dateFormatter;
    private List<scanned_users> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private scanned_adepter mAdapter;
    StorageReference storageReference;
    FirebaseStorage storage;
    Bitmap Image_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        date = findViewById(R.id.report_date);
        search = findViewById(R.id.report_search);
        college = findViewById(R.id.report_college);
        type = findViewById(R.id.report_type);
        chaque = findViewById(R.id.chaque);
        cash = findViewById(R.id.cash);
        cash_value = findViewById(R.id.report_cash);
        chaque_value = findViewById(R.id.report_chaque);
        date.setOnClickListener(this);
        date.setInputType(InputType.TYPE_NULL);
        date.requestFocus();
        search.setOnClickListener(this);
        recyclerView = findViewById(R.id.scanned_list_view);
        //card_view =findViewById(R.id.list_card);
//        college = findViewById(R.id.l_college);
//        college_search = findViewById(R.id.l_search);
        mAdapter = new scanned_adepter(movieList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);
        //
        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                scanned_users movie = movieList.get(position);
                Toast.makeText(getApplicationContext(), " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(report.this, home.class));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.report_date:
                fromDatePickerDialog.show();
                break;
            case R.id.report_search:
                college_id = college.getSelectedItem().toString();
                type_id = type.getSelectedItem().toString();
//                if (type_id.equals("COLLECTION")) {
//                    cash.setVisibility(View.VISIBLE);
//                    chaque.setVisibility(View.VISIBLE);
//                    show_collection();
//
//                } else {
//                    show_scanned_profile();
//                }
                break;
        }

    }

    private void show_collection() {

        DatabaseReference myRef = database.getReference("scanned").child(date.getText().toString().trim()).child(college_id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_id = dataSnapshot.child("user id").getValue(String.class);
                getInfo(user_id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getInfo(final String user_id) {
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference(user_id);

        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    amount_type = postSnapshot.child("amount type").getValue(String.class);
                    if (amount_type.equals("CASH")) {
                        feecash = feecash + postSnapshot.child("fee").getValue(int.class);
                    } else {
                        feechaque = feechaque + postSnapshot.child("fee").getValue(int.class);
                    }
                }
                cash_value.setText(feecash);
                chaque_value.setText(feechaque);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(report.this, "" + databaseError, Toast.LENGTH_LONG).show();
            }

        });


    }

    private void show_scanned_profile() {
        DatabaseReference myRef = database.getReference("scanned").child(date.getText().toString()).child(college_id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user_id = ds.child("user id").getValue(String.class);
                    getInfo_scanned(user_id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getInfo_scanned(String user_id) {
//        StorageReference islandRef = storageReference.child("images/"+user_id);
//        final long ONE_MEGABYTE = 1024 * 1024;
//        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                // Data for "images/island.jpg" is returns, use this as needed
//                Image_list = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                scanned_users data = new scanned_users(Image_list);
//                movieList.add(data);
//                mAdapter.notifyDataSetChanged();
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });
//
//    }
    }
}