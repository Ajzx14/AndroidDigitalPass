package com.example.shiva.try1;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;


public class edit_form extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "qrcode";
    public EditText f_name,m_name,l_name,address,mobile,p_mobile,email_new,stand,route,branch,p_occ,paidtill,amount,dob;
    public Spinner college,bustype,amounttype,qrOption;
    private Button save;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public ImageView s_image;
    final Calendar myCalendar = Calendar.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;
    private StorageReference mStorageRef;
    private Uri filePath = null;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    String toEmail,colloege_id;
    public static File newFile = null;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] data_image,qr_image;
    File qr_file;
    String student,stand_,address_profile,parents_occ,fee,d_o_b,branch_profile,fname_profile,lname_profile,mname_profile,mobile_profile,p_mobile_profile,email_profile,paidtill_profile,route_profile,college_profile,bustype_profile;

    String searchv;
 //  AppCompatImageView iv = (AppCompatImageView)findViewById(R.id);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

        setContentView(R.layout.activity_edit);
        Bundle gt=getIntent().getExtras();
        searchv =gt.getString("searchValue_edit");
        colloege_id = gt.getString("college");
        f_name = findViewById(R.id.f_name);
        m_name = findViewById(R.id.m_name);
        l_name = findViewById(R.id.l_name);
        address = findViewById(R.id.address);
        mobile = findViewById(R.id.s_mobile);
        p_mobile = findViewById(R.id.p_mobile);
        email_new = findViewById(R.id.email__);
        stand = findViewById(R.id.Stand);
        route = findViewById(R.id.route);
        branch = findViewById(R.id.Branch);
        p_occ = findViewById(R.id.p_occupassion);
        paidtill = findViewById(R.id.paidtill);
        amount = findViewById(R.id.amount);
        dob = findViewById(R.id.dob);
        qrOption = findViewById(R.id.qr_option);
    s_image = findViewById(R.id.s_image);
    s_image.setOnClickListener(this);
        save = findViewById(R.id.form_submit);
        save.setOnClickListener(this);
        college =(Spinner) findViewById(R.id.college);
        bustype = findViewById(R.id.bustype);
        amounttype = findViewById(R.id.amounttype);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
if(searchv!=null){
    fetchImage(searchv);
}


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
                        Bitmap bitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                        s_image.setImageBitmap(bitmap);
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
                d_o_b = dataSnapshot.child("dob").getValue(String.class);
                email_profile = dataSnapshot.child("email").getValue(String.class);
                fee = dataSnapshot.child("fee").getValue(String.class);
                fname_profile = dataSnapshot.child("first name").getValue(String.class);

                lname_profile = dataSnapshot.child("last name").getValue(String.class);

                mname_profile = dataSnapshot.child("middle name").getValue(String.class);

                mobile_profile = dataSnapshot.child("mobile").getValue(String.class);

                paidtill_profile = dataSnapshot.child("paid till").getValue(String.class);

                p_mobile_profile = dataSnapshot.child("parents mobile").getValue(String.class);
                parents_occ = dataSnapshot.child("parents occupation").getValue(String.class);
                route_profile = dataSnapshot.child("route").getValue(String.class);
                stand_ = dataSnapshot.child("stand").getValue(String.class);
                address.setText(address_profile);
                branch.setText(branch_profile);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(edit_form.this, R.array.bustype, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                bustype.setAdapter(adapter);
                if (bustype_profile != null) {
                    int spinnerPosition = adapter.getPosition(bustype_profile);
                    bustype.setSelection(spinnerPosition);
                }

                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(edit_form.this, R.array.college, android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                bustype.setAdapter(adapter2);
                if (bustype_profile != null) {
                    int spinnerPosition = adapter2.getPosition(bustype_profile);
                    bustype.setSelection(spinnerPosition);
                }
//                bustype.set(bustype_profile);
//                college.setText(college_profile);
                dob.setText(d_o_b);
                amount.setText(fee);
                p_occ.setText(parents_occ);
                email_new.setText(email_profile);
                f_name.setText(fname_profile);
                l_name.setText(lname_profile);
                m_name.setText(mname_profile);
                mobile.setText(mobile_profile);
                paidtill.setText(paidtill_profile);
                p_mobile.setText(p_mobile_profile);
                route.setText(route_profile);
                stand.setText(stand_);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(edit_form.this,""+databaseError,Toast.LENGTH_LONG).show();
            }

        });

progressDialog.dismiss();
    }




    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    @Override
    public void onBackPressed() {

        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("Changes", "popping backstack");
            fm.popBackStack();
           // overridePendingTransition(R.anim.exit_from, R.anim.enter_to);

        } else {
            Log.i("edit", "nothing on backstack, calling super");
            super.onBackPressed();
           // overridePendingTransition(R.anim.exit_from, R.anim.enter_to);

        }

    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(myCalendar.getTime()));
    }
//////////////////////


//    public void firebaseUpload(Bitmap pic){
//
//        Uri file = Uri.fromFile(new File(String.valueOf(pic)));
//        StorageReference riversRef = mStorageRef.child("images/rivers.jpg");
//
//        riversRef.putFile(file)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Get a URL to the uploaded content
//                    //    Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        Toast.makeText(new_form.this,"image uploaded",Toast.LENGTH_LONG);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                        // ...
//                    }
//                });
//    }
//
//
///////////////
//public Uri getImageUri(Context inContext, Bitmap inImage) {
//    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//    String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//    return Uri.parse(path);
//}



    private void uploadImage(final String picName) {
       // Uri filept = Uri.fromFile(pic);
//        s_image.setDrawingCacheEnabled(true);
//Bitmap temp = s_image.getDrawingCache();


        if(data_image != null)
        {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ picName);
            ref.putBytes(data_image)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(edit_form.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            if(qrOption.getSelectedItem().toString().equals("YES")){
                                Bitmap nqr = genqr(picName);
                                String root = Environment.getExternalStorageDirectory().toString();
                                File myDir = new File(root + "/req_images");
                                myDir.mkdirs();
                                Random generator = new Random();
                                int n = 10000;
                                n = generator.nextInt(n);
                                String fname = "Image-" + n + ".jpg";
                                File file = new File(myDir, fname);
                                Log.i(TAG, "" + file);
                                if (file.exists())
                                    file.delete();
                                try {
                                    FileOutputStream out = new FileOutputStream(file);
                                    nqr.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                    out.flush();
                                    out.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Uri path = FileProvider.getUriForFile(edit_form.this,BuildConfig.APPLICATION_ID + ".provider",file);
                                Log.i("Send email", "");
                                String[] TO = {toEmail};
                                String[] CC = {""};
                                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                // set the type
                                shareIntent.setType("text/plain");
                                // add a subject

                                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                        "Digital Pass FOR SBT");
                                // build the body of the message to be shared
                                String shareMessage = "Dear "+student+",\n\n\nGreetings From SHREE BARAHMANI TRAVELS,\n\n\nThis Email Contains your UPDATED digital pass PFA.\nPlease keep it with you while on board It will be your Proof Of Payment for your following years.\n\n\n\nNOTE: Please do not share this with anyone\nTHANK YOU ENJOY YOUR RIDE..!!\nFor ANY issue or any information EMAIL back or contact bleow given..!\nJITUBHAI PATEL : +91-9436753509\nDEEPAKBHAI PATEL : +91-9427300082\nPARTH PATEL : +91-9408639198";
                                // add the message
                                shareIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                                shareIntent.putExtra(Intent.EXTRA_CC, CC);

                                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                                        shareMessage);
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                shareIntent.putExtra(android.content.Intent.EXTRA_STREAM,
                                        path);
                                // start the chooser for sharing
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivity(Intent.createChooser(shareIntent,
                                        "Please select sharing medium"));
                            }
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(edit_form.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
            clear();

        }

    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }



    private void clear() {
        s_image.setImageResource(R.drawable.camera);
        f_name.setText(null);
        m_name.setText(null);
        l_name.setText(null);
        address.setText(null);
        mobile.setText(null);
        p_mobile.setText(null);
        email_new.setText(null);
        stand.setText(null);
        route.setText(null);
        branch.setText(null);
        p_occ.setText(null);
        paidtill.setText(null);
        amount.setText(null);
        dob.setText(null);
    }

    public String store(){

        DatabaseReference myRef = database.getReference();
        String mob = mobile.getText().toString();
        myRef.child(mob).child("first name").setValue(f_name.getText().toString().trim());
        myRef.child(mob).child("last name").setValue(l_name.getText().toString().trim());
        myRef.child(mob).child("middle name").setValue(m_name.getText().toString().trim());
        myRef.child(mob).child("address").setValue(address.getText().toString().trim());
        myRef.child(mob).child("route").setValue(route.getText().toString().trim());
        myRef.child(mob).child("branch").setValue(branch.getText().toString().trim());
        myRef.child(mob).child("stand").setValue(stand.getText().toString().trim());
        myRef.child(mob).child("mobile").setValue(mobile.getText().toString().trim());
        myRef.child(mob).child("parents mobile").setValue(p_mobile.getText().toString().trim());
        myRef.child(mob).child("email").setValue(email_new.getText().toString().trim());
        myRef.child(mob).child("parents occupation").setValue(p_occ.getText().toString().trim());
        myRef.child(mob).child("dob").setValue(dob.getText().toString().trim());
        myRef.child(mob).child("fee").setValue(amount.getText().toString().trim());
        myRef.child(mob).child("paid till").setValue(paidtill.getText().toString().trim());
        myRef.child(mob).child("college").setValue(college.getSelectedItem().toString().trim());
        myRef.child(mob).child("amount type").setValue(amounttype.getSelectedItem().toString().trim());
        myRef.child(mob).child("bus type").setValue(bustype.getSelectedItem().toString().trim());
        toEmail = email_new.getText().toString();
        student = f_name.getText().toString();

        return mob;
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap new_bit;
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            new_bit=rotateImage(photo,90);
            new_bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data_image = baos.toByteArray();
            s_image.setImageBitmap(new_bit);

        }
    }
public Bitmap genqr(String inputValue){
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setTitle("Creating QR...");
    progressDialog.show();
    if (inputValue.length() > 0) {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        qrgEncoder = new QRGEncoder(
                inputValue, null,
                QRGContents.Type.TEXT,
                smallerDimension);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
           // s_image.setImageBitmap(bitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);



            progressDialog.dismiss();
            Toast.makeText(edit_form.this, "Created..& Sent", Toast.LENGTH_SHORT).show();
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }
    } else {
       // edtValue.setError("Required");
    }
    return bitmap;
}
    protected void sendEmail(Uri imageUri) {


//      /*  Intent emailIntent = new Intent(Intent.ACTION_SEND);
//
//
//
//        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setType("image/*");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_CC, CC);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Degital Pass For SBT");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
//        // accept any image
//
//        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(qr_file));
//        // then attach the file to the intent
//        startActivity(Intent.createChooser(emailIntent, "Send your email in:"));
//        finish();
//        Log.i("Finished", "");*/
//
//        final Intent emailIntent1 = new Intent(     android.content.Intent.ACTION_SEND);
//        emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        emailIntent1.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent1.putExtra(Intent.EXTRA_CC, CC);
//        emailIntent1.putExtra(Intent.EXTRA_SUBJECT, "Degital Pass For SBT");
//        emailIntent1.putExtra(Intent.EXTRA_STREAM, imageUri);
//        emailIntent1.setType("image/png");
//        startActivity(Intent.createChooser(emailIntent1, "Send email using"));
//        finish();

    }





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dob:
                new DatePickerDialog(edit_form.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.s_image:
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

                break;
            case R.id.form_submit:
               String picName = store();
                uploadImage(picName);
              //  sendEmail();
                Toast.makeText(edit_form.this,"All Data hase been saved",Toast.LENGTH_LONG).show();
                break;
        }
    }

}
