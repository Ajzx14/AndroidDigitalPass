package com.example.shiva.try1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class renew extends AppCompatActivity implements View.OnClickListener {
    ImageButton scan_,delete,edit,view_,menu;
    Button save,search_button;
    TextView re_fname,re_mnaem,re_paid,re_lname,re_email,r_mob;
    EditText re_amount,search_id;
    ImageView re_photo;
    Spinner re_bustype,re_amount_type;
    String searchv,default_paidtill;
    Spinner college_name;
    CardView re_view;
    String address_profile,branch_profile,fname_profile,lname_profile,mname_profile,mobile_profile,p_mobile_profile,email_profile,paidtill_profile,route_profile,college_profile,bustype_profile;
    StorageReference storageReference;
    FirebaseStorage storage;
    String id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

        setContentView(R.layout.activity_renew);

        //re_view.setVisibility(View.VISIBLE);
        re_fname = findViewById(R.id.r_f_name);
        re_lname = findViewById(R.id.r_l_name);
        re_amount = findViewById(R.id.r_amount);
        college_name = findViewById(R.id.spinner_college_renew);
        re_amount_type = findViewById(R.id.r_amounttype);
        re_bustype = findViewById(R.id.r_bustype);
        re_photo = findViewById(R.id.r_image);
        re_mnaem = findViewById(R.id.r_m_name);
        re_email = findViewById(R.id.r_email);
        scan_ = findViewById(R.id.scan);
        r_mob = findViewById(R.id.r_number);
        delete =findViewById(R.id.floatingActionButton_delete);
        edit = findViewById(R.id.floatingActionButton_edit);
        view_ = findViewById(R.id.floatingActionButton_view);
        menu = findViewById(R.id.floatingActionButton_menu);
        save = findViewById(R.id.re_submit);
        search_id = findViewById(R.id.re_id);
        search_button = findViewById(R.id.search);
        re_view = findViewById(R.id.card_re);
        re_paid = findViewById(R.id.r_paidtill);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        menu.setOnClickListener(this);
        search_button.setOnClickListener(this);
        save.setOnClickListener(this);
        edit.setOnClickListener(this);
        view_.setOnClickListener(this);
        delete.setOnClickListener(this);
        scan_.setOnClickListener(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        default_paidtill = sp.getString("paidtill","NULL");
        re_paid.setText(default_paidtill);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.scan:


                if (!isFinishing()) {
                    Toast.makeText(this,"not finished",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.search:
               // Fetch_show(search_id.getText().toString());
                re_view.setVisibility(View.VISIBLE);
                id = search_id.getText().toString().trim();
                fetchImage(id);
                break;
            case R.id.re_submit:
                submit_data(r_mob.getText().toString());
                re_view.setVisibility(View.INVISIBLE);
                break;
            case R.id.floatingActionButton_delete:
                delete_data(r_mob.getText().toString());
                delete.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.INVISIBLE);
                re_view.setVisibility(View.INVISIBLE);

                break;
            case R.id.floatingActionButton_menu:
                edit.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                break;
            case R.id.floatingActionButton_edit:
                if(r_mob.getText().toString() != null){
                    String s=r_mob.getText().toString();
                    String c = college_profile;
                    Bundle basket_edit= new Bundle();
                    basket_edit.putString("searchValue_edit", s);
                    basket_edit.putString("college",c);
                    Intent edit=new Intent(renew.this,edit_form.class);
                    edit.putExtras(basket_edit);
                    startActivity(edit);
                    overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

                }
                delete.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.INVISIBLE);
                re_view.setVisibility(View.INVISIBLE);

                break;
            case R.id.floatingActionButton_view:
                String c = college_profile;

                Bundle basket_view= new Bundle();
                basket_view.putString("college",c);
                basket_view.putString("searchValue", r_mob.getText().toString());
                Intent view_=new Intent(renew.this,profile.class);
                view_.putExtras(basket_view);
                startActivity(view_);
                overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

                break;
        }
    }

    private void delete_data(String deleteId) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Deleting Data...");
        progressDialog.show();
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference(deleteId);
        zonesRef.removeValue();
        progressDialog.dismiss();
        Toast.makeText(this,"Node Deleted",Toast.LENGTH_LONG).show();
    }

    private void submit_data(String updateId) {
        String busType,amountType,paidTill,amount;
        amount = re_amount.getText().toString();
        busType = re_bustype.getSelectedItem().toString();
        amountType = re_amount_type.getSelectedItem().toString();
        paidTill = re_paid.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Data...");
        progressDialog.show();
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference(college_name.getSelectedItem().toString());
        DatabaseReference zone1Ref = zonesRef.child(updateId);
        zone1Ref.child("fee").setValue(amount);
        zone1Ref.child("bus type").setValue(busType);
        zone1Ref.child("amount type").setValue(amountType);
        zone1Ref.child("paid till").setValue(paidTill);
        progressDialog.dismiss();

    }



    public void fetchImage(String ImageId){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Featching Image...");
        progressDialog.show();
        StorageReference ref = storageReference.child("images/"+ ImageId);
      //  StorageReference ref2 = ref.child("9978385759");
        //gs://materiallogindemo-master-15ceb.appspot.com/images
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
                        Bitmap bitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                        re_photo.setImageBitmap(bitmap);
                        progressDialog.dismiss();
                        dataRetrive(id);
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
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference(college_name.getSelectedItem().toString());
        DatabaseReference zone1Ref = zonesRef.child(UniqeId);
        zone1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                address_profile = dataSnapshot.child("address").getValue(String.class);
                branch_profile =  dataSnapshot.child("branch").getValue(String.class);

                bustype_profile =  dataSnapshot.child("bus type").getValue(String.class);

                college_profile = dataSnapshot.child("college").getValue(String.class);

                email_profile = dataSnapshot.child("email").getValue(String.class);

                fname_profile = dataSnapshot.child("first name").getValue(String.class);

                lname_profile = dataSnapshot.child("last name").getValue(String.class);

                mname_profile = dataSnapshot.child("middle name").getValue(String.class);

                mobile_profile = dataSnapshot.child("mobile").getValue(String.class);

                paidtill_profile = dataSnapshot.child("paid till").getValue(String.class);

                p_mobile_profile = dataSnapshot.child("parents mobile").getValue(String.class);

                route_profile = dataSnapshot.child("route").getValue(String.class);

                re_email.setText(email_profile);
                re_fname.setText(fname_profile);
                re_lname.setText(lname_profile);
                re_mnaem.setText(mname_profile);
                r_mob.setText(mobile_profile);
                re_paid.setText(paidtill_profile);
            progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(renew.this,""+databaseError,Toast.LENGTH_LONG).show();
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(renew.this, home.class));
      //  overridePendingTransition(R.anim.exit_from, R.anim.enter_to);

    }

}
