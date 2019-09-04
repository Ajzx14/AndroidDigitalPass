package com.example.shiva.try1;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.example.shiva.try1.QRGContents;
import com.example.shiva.try1.QRGEncoder;
import com.example.shiva.try1.QRGSaver;
import com.google.zxing.WriterException;
import com.example.shiva.try1.Settings;

import static android.provider.Telephony.Mms.Part.FILENAME;
import static java.security.AccessController.getContext;


public class new_form extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "qrcode";
    public EditText f_name,m_name,l_name,address,mobile,p_mobile,email_new,stand,route,branch,p_occ,paidtill,amount,dob;
    public Spinner college,bustype,amounttype;
    private Button save;
    private SimpleDateFormat dateFormatter;
    final int CAMERA_CAPTURE = 1;
    final int PIC_CROP = 3;
    private Uri picUri;
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
    private DatePickerDialog fromDatePickerDialog;
    PendingIntent pendingIntent;
    Bitmap bitmap;
    String toEmail,student,full_name,mob;
    public static File newFile = null;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] data_image,qr_image;
    File qr_file;
    String default_paidtill;
     //  AppCompatImageView iv = (AppCompatImageView)findViewById(R.id);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

        setContentView(R.layout.activity_new_form);
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
        dob.setOnClickListener(this);
        dob.setInputType(InputType.TYPE_NULL);
        dob.requestFocus();
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
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        default_paidtill = sp.getString("paidtill","NULL");
        paidtill.setText(default_paidtill);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dob.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(new_form.this, home.class));
       // overridePendingTransition(R.anim.exit_from, R.anim.enter_to);

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


public void newNotification(String college,String mob,String fullname){
    String c = college;

    Bundle basket_view= new Bundle();
    basket_view.putString("college",c);
    basket_view.putString("searchValue", mob);
    Intent intent = new Intent(this, profile.class);
    intent.putExtras(basket_view);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.check)
            .setContentTitle(fullname)
            .setContentText(mob)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);
}
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
                            Toast.makeText(new_form.this, "Uploaded", Toast.LENGTH_SHORT).show();
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
                            Uri path = FileProvider.getUriForFile(new_form.this,BuildConfig.APPLICATION_ID + ".provider",file);
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
                                String shareMessage = "Dear "+student+",\n\n\nGreetings From SHREE BARAHMANI TRAVELS,\n\n\nThis Email Contains your digital pass PFA.\nPlease keep it with you while on board It will be your Proof Of Payment for your following years.\n\n\n\nNOTE: Please do not share this with anyone\nTHANK YOU ENJOY YOUR RIDE..!!\nFor ANY issue or any information EMAIL back or contact bleow given..!\nJITUBHAI PATEL : +91-9436753509\nDEEPAKBHAI PATEL : +91-9427300082\nPARTH PATEL : +91-9408639198";
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
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(new_form.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                            progressDialog.dismiss();
                        }
                    });
            clear();

        }

    }
    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(img, selectedImage);
        return img;
    }


    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }



    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
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
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
      // today.getDate();
        DatabaseReference myRef = database.getReference(college.getSelectedItem().toString());
         mob = mobile.getText().toString();
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
        myRef.child(mob).child("enrooled Date").setValue(date);
        toEmail = email_new.getText().toString();
        student = f_name.getText().toString();
         full_name =f_name.getText().toString()+""+l_name.getText().toString();
        return mob;
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }}

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//
//            // user is returning from cropping the image
//            if (requestCode == CROP_PIC) {
//                picUri = data.getData();
//                performCrop();
//                // get the returned data
//                Bundle extras = data.getExtras();
//                // get the cropped bitmap
//                Bitmap thePic = extras.getParcelable("data");
//                //ImageView picView = (ImageView) findViewById(R.id.picture);
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
////            picUri = data.getData();
////            performCrop();
////
////            // new_bit=rotateImage(photo,90);
//            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            data_image = baos.toByteArray();
//                s_image.setImageBitmap(thePic);
//            }
//        }
//    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap new_bit;
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // new_bit=rotateImage(photo,90);
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data_image = baos.toByteArray();
            s_image.setImageBitmap(photo);

        }


        }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void performCrop(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
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
            Toast.makeText(new_form.this, "Created..& Sent", Toast.LENGTH_SHORT).show();
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
                fromDatePickerDialog.show();

                break;
            case R.id.s_image:
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

                }

                break;
            case R.id.form_submit:
               String picName = store();
               // newNotification(college.getSelectedItem().toString().trim(),mob,full_name);

                uploadImage(picName);
              //  sendEmail();
                Toast.makeText(new_form.this,"All Data hase been saved",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
