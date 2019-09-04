package com.example.shiva.try1;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class scanned_show extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Value";
    String searchv,amount_value,colloege_id;
    StorageReference storageReference;
    private scanned_adepter mAdapter;
    FirebaseStorage storage;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String address_profile,branch_profile,fname_profile,lname_profile,mname_profile,mobile_profile,p_mobile_profile,email_profile,paidtill_profile,route_profile,college_profile,bustype_profile;
    String date_value,scanned_date;
    public TextView f_name,m_name,l_name,address,mobile,p_mobile,email_new,stand,route,branch,p_occ,paidtill,amount,dob,college,bustype,amounttype;
    ImageView photo;
    Bitmap bitmap;
    Button scan_again;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_show);
        Bundle gt=getIntent().getExtras();
        searchv =gt.getString("searchValue");
        colloege_id = gt.getString("college");
        scan_again = findViewById(R.id.show_scan);
        scan_again.setOnClickListener(this);
        f_name = findViewById(R.id.show_name);
        m_name = findViewById(R.id.profile_mname);
      //  l_name = findViewById(R.id.profile_lname);
      //  address = findViewById(R.id.profile_address);
        mobile = findViewById(R.id.show_mobile);
        p_mobile = findViewById(R.id.show_pmobile);
        email_new = findViewById(R.id.show_email);
        // stand = findViewById(R.id.);
        route = findViewById(R.id.show_route);
        //branch = findViewById(R.id.profile_branch);
        //  p_occ = findViewById(R.id.profile_);
        paidtill = findViewById(R.id.show_paidtill);
        //  amount = findViewById(R.id.profil);
        //dob = findViewById(R.id.profile);
        photo = findViewById(R.id.show_image);
        //  s_image.setOnClickListener(this);
        //  save = findViewById(R.id.form_submit);
        // save.setOnClickListener(this);
        college = findViewById(R.id.show_college);
        bustype = findViewById(R.id.show_bustype);
      //  mAdapter = new scanned_adepter(movieList);
        //  amounttype = findViewById(R.id.amounttype);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        fetchImage(searchv);

    }
    public void fetchImage(final String imageId){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Featching Image...");
        progressDialog.show();
        StorageReference ref = storageReference.child("images/"+imageId);
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalLocalFile = localFile;
        ref.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...
                        bitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                        photo.setImageBitmap(bitmap);
                        progressDialog.dismiss();
                        dataRetrive(imageId);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });

    }

    public void dataRetrive(String UniqeId){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Featching Data...");
        progressDialog.show();
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference(colloege_id);
        DatabaseReference zone1Ref = zonesRef.child(UniqeId);
        zone1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                address_profile = dataSnapshot.child("address").getValue(String.class);
                branch_profile =  dataSnapshot.child("branch").getValue(String.class);

                bustype_profile =  dataSnapshot.child("bus type").getValue(String.class);

                college_profile = dataSnapshot.child("college").getValue(String.class);

                email_profile = dataSnapshot.child("email").getValue(String.class);
                amount_value = dataSnapshot.child("fee").getValue(String.class);

                fname_profile = dataSnapshot.child("first name").getValue(String.class);

                lname_profile = dataSnapshot.child("last name").getValue(String.class);

                mname_profile = dataSnapshot.child("middle name").getValue(String.class);

                mobile_profile = dataSnapshot.child("mobile").getValue(String.class);

                paidtill_profile = dataSnapshot.child("paid till").getValue(String.class);

                p_mobile_profile = dataSnapshot.child("parents mobile").getValue(String.class);

                route_profile = dataSnapshot.child("route").getValue(String.class);
                date_value = dataSnapshot.child("enrolled date").getValue(String.class);
              //  address.setText(address_profile);
              //  branch.setText(branch_profile);
                bustype.setText(bustype_profile);
                college.setText(college_profile);
                email_new.setText(email_profile);
                String fullname =fname_profile+"\t"+lname_profile;
                f_name.setText(fullname);
               // l_name.setText(lname_profile);
                m_name.setText(mname_profile);
                mobile.setText(mobile_profile);
                paidtill.setText(paidtill_profile);
                p_mobile.setText(p_mobile_profile);
                route.setText(route_profile);
                // addScannedData(searchv,college_profile);
//                scanned_users data = new scanned_users(bitmap,fname_profile,lname_profile,mname_profile,mobile_profile,amount_value);
//                movieList.add(data);
//                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(scanned_show.this,""+databaseError,Toast.LENGTH_LONG).show();
            }

        });
        progressDialog.dismiss();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("Profile", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("Profile", "nothing on backstack, calling super");
            super.onBackPressed();
        }


    }
    @Override
    public void onClick(View view) {
        startActivity(new Intent(scanned_show.this,ScannedBarcodeActivity.class));

    }
}
