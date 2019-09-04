package com.example.shiva.try1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
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

public class ScannedBarcodeActivity extends AppCompatActivity implements View.OnClickListener {


    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    Spinner id;
    Bitmap bitmap;
    String address_profile,branch_profile,fname_profile,lname_profile,mname_profile,mobile_profile,p_mobile_profile,email_profile,paidtill_profile,route_profile,college_profile,bustype_profile;
TextView fullname,email_show,mobile,paidtill,bustype;
    String intentData = "";
    boolean isEmail = false;
    String searchv;
    CardView card;
    ImageView pic,profile;
    ImageView back;
    Vibrator vibe;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        id = findViewById(R.id.spinner_id);
        fullname = findViewById(R.id.full_name_show);
        email_show = findViewById(R.id.email_show);
        mobile = findViewById(R.id.mobile_show);
        paidtill = findViewById(R.id.paidtill_show);
        pic =findViewById(R.id.profile_pic_show);
        back = findViewById(R.id.go_back);
        back.setOnClickListener(this);
        profile =findViewById(R.id.go_profile);
        profile.setOnClickListener(this);
        bustype = findViewById(R.id.bustype_show);
        card = findViewById(R.id.show_card);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        initViews();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ScannedBarcodeActivity.this, home.class));
      //  overridePendingTransition(R.anim.exit_from, R.anim.enter_to);


    }
    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction_re);


        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intentData.length() > 0) {
                    if (isEmail)
                        startActivity(new Intent(ScannedBarcodeActivity.this, EmailActivity.class).putExtra("email_address", intentData));
                    else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
                    }
                }


            }
        });
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                        cameraSource.start(surfaceView.getHolder());

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    vibe.vibrate(100);

                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarcodeValue.setText(intentData);
                                isEmail = true;
                                btnAction.setText("ADD CONTENT TO THE MAIL");
                            } else {
                                isEmail = false;
                                btnAction.setText("LAUNCH URL");
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);
                                //card.setVisibility(View.VISIBLE);
                                slideUp(card);
                              //  surfaceView.setVisibility(View.INVISIBLE);
                                slideDown(surfaceView);
                                fetchImage(intentData);
                                txtBarcodeValue.setText("");
//                                String s=intentData;
//                                String value= id.getSelectedItem().toString();
//                                Bundle basket= new Bundle();
//                                basket.putString("searchValue", s);
//                                basket.putString("college",value);
//                                Intent a=new Intent(ScannedBarcodeActivity.this,scanned_show.class);
//                                a.putExtras(basket);
//                                startActivity(a);

                            }

                        }
                    });

                }
            }
        });
    }
    public void fetchImage(final String imageId){

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
                        pic.setImageBitmap(bitmap);
                       // progressDialog.dismiss();
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

        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference(id.getSelectedItem().toString());
        DatabaseReference zone1Ref = zonesRef.child(UniqeId);
        zone1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                address_profile = dataSnapshot.child("address").getValue(String.class);
                branch_profile =  dataSnapshot.child("branch").getValue(String.class);

                bustype_profile =  dataSnapshot.child("bus type").getValue(String.class);

                college_profile = dataSnapshot.child("college").getValue(String.class);

                email_profile = dataSnapshot.child("email").getValue(String.class);
                //amount_value = dataSnapshot.child("fee").getValue(String.class);

                fname_profile = dataSnapshot.child("first name").getValue(String.class);

                lname_profile = dataSnapshot.child("last name").getValue(String.class);

                mname_profile = dataSnapshot.child("middle name").getValue(String.class);

                mobile_profile = dataSnapshot.child("mobile").getValue(String.class);

                paidtill_profile = dataSnapshot.child("paid till").getValue(String.class);

                p_mobile_profile = dataSnapshot.child("parents mobile").getValue(String.class);

                route_profile = dataSnapshot.child("route").getValue(String.class);


                fullname.setText(fname_profile+"\t"+lname_profile);
                bustype.setText(bustype_profile);

                email_show.setText(email_profile);

                mobile.setText(mobile_profile);
                paidtill.setText(paidtill_profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ScannedBarcodeActivity.this,""+databaseError,Toast.LENGTH_LONG).show();
            }

        });
       // progressDialog.dismiss();


    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();


    }

    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.INVISIBLE);
    }

    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.go_back:
               // card.setVisibility(View.INVISIBLE);
                slideDown(card);
              //  surfaceView.setVisibility(View.VISIBLE);
                surfaceView.setVisibility(View.VISIBLE);
                slideUp(surfaceView);
                break;
            case R.id.go_profile:
                String s=mobile.getText().toString();
                String value= id.getSelectedItem().toString();
                Bundle basket= new Bundle();
                basket.putString("searchValue", s);
                basket.putString("college",value);
                Intent a=new Intent(ScannedBarcodeActivity.this,profile.class);
                a.putExtras(basket);
                startActivity(a);
                overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

                break;
        }
    }
}
