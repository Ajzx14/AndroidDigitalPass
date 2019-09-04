package com.example.shiva.try1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import static java.lang.System.exit;

public class home extends AppCompatActivity implements View.OnClickListener {
    CardView scan, new_p, renewid, list, settings, report;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
//    private TextView txtPermissions;
//    private Button btnCheckPermissions;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (ActivityCompat.checkSelfPermission(home.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            //cameraSource.start(surfaceView.getHolder());
//           // exit(0);
//            if (ActivityCompat.checkSelfPermission(home.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                //  cameraSource.start(surfaceView.getHolder());
//            } else {
//                ActivityCompat.requestPermissions(home.this, new
//                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
//            }
//        } else {
//            ActivityCompat.requestPermissions(home.this, new
//                    String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
//            if (ActivityCompat.checkSelfPermission(home.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                //  cameraSource.start(surfaceView.getHolder());
//            } else {
//                ActivityCompat.requestPermissions(home.this, new
//                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
//            }
//        }
//        if (ActivityCompat.checkSelfPermission(home.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            //cameraSource.start(surfaceView.getHolder());
//            // exit(0);
//        } else {
//            ActivityCompat.requestPermissions(home.this, new
//                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
//        }
//

        setContentView(R.layout.activity_home);


        scan = (CardView) findViewById(R.id.scan);
        new_p = (CardView) findViewById(R.id.new_p);
        renewid = (CardView) findViewById(R.id.re_new);
        list = (CardView) findViewById(R.id.view);
        settings = (CardView) findViewById(R.id.settings);
        report = (CardView) findViewById(R.id.report);

        scan.setOnClickListener(this);
        new_p.setOnClickListener(this);
        renewid.setOnClickListener(this);
        list.setOnClickListener(this);
        settings.setOnClickListener(this);
        report.setOnClickListener(this);
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        premissions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan:
                String s_id="";
                Bundle basket_rescan= new Bundle();
                basket_rescan.putString("scan_value", s_id);
                Intent a=new Intent(home.this,ScannedBarcodeActivity.class);
                a.putExtras(basket_rescan);
                startActivity(a);
                overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

                break;
            case R.id.new_p:
                startActivity(new Intent(home.this, new_form.class));
                overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

                break;
            case R.id.re_new:
                Bundle basket= new Bundle();
                basket.putString("scan_value", "");
                Intent a1=new Intent(home.this,renew.class);
                a1.putExtras(basket);
                startActivity(a1);
                overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

                startActivity(new Intent(home.this, renew.class));
                break;
            case R.id.view:
                startActivity(new Intent(home.this, MainActivity_search.class));
                overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

                break;
            case  R.id.settings:
                startActivity(new Intent(home.this, Settings.class));
                overridePendingTransition(R.anim.enter_to, R.anim.exit_from);

                break;
            case R.id.report:
                startActivity(new Intent(home.this, report.class));
                overridePendingTransition(R.anim.enter_to, R.anim.exit_from);



        }
    }
    public void premissions(){
        if(ActivityCompat.checkSelfPermission(home.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(home.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(home.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(home.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(home.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(home.this,permissionsRequired[2])){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(home.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//                        sentToSettings = true;
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        Uri uri = Uri.fromParts("package", getPackageName(), null);
//                        intent.setData(uri);
//                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
//                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(home.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

            //   txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                proceedAfterPermission();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(home.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(home.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(home.this,permissionsRequired[2])){
                //txtPermissions.setText("Permissions Required");
                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(home.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(home.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
      //  txtPermissions.setText("We've got all permissions");
        Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
    }
    
    
    
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
           // startActivity(new Intent(home.this,login.class));
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}