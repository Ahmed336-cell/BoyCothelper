package com.elm.boycothelper.activites;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elm.boycothelper.R;
import com.elm.boycothelper.model.ProductsJson1;
import com.elm.boycothelper.model.ProductJson2;
import com.elm.boycothelper.pojo.Constants;
import com.elm.boycothelper.retrofit.UPCService2;
import com.elm.boycothelper.retrofit.UPCService1;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

   private  MaterialButton camera,gallery,scan;
   private ImageView barcodeImg;
   private LinearLayout layout;
   private TextView country,product,organization,boycot ;

   private static final int CAMERA_REQUEST_CODE =100;
    private static final int STORAGE_REQUEST_CODE =101;
    private String[] cameraPermession;
    private String[] storagePermession;

    private Uri imageUri = null;
    private BarcodeScannerOptions barcodeScannerOptions;
    private BarcodeScanner barcodeScanner;
    private static final String TAG = "MAIN_TAG";
    private List<String> companies;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camera = findViewById(R.id.camerabtn);
        gallery = findViewById(R.id.gallerybtn);
        scan = findViewById(R.id.scanbtn);
        barcodeImg = findViewById(R.id.imageVU);
        progressBar = findViewById(R.id.progressBarbarcode);
        companies = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_REFRENCE);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(String.class);
                companies.add(value);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cameraPermession = new String[]{Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermession = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        barcodeScannerOptions = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                        .build();

        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions);


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermession()){
                    pickIMageCamera();
                }else{
                    requestCameraPermession();
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    if (checkStoragePermession()) {
                        pickImage();
                    } else {
                        requestStoragePermession();
                    }
                }

                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        pickImage();
                    } else {
                        requestStoragePermissionForAndroid11Plus();
                    }
                }
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null){
                    Toast.makeText(MainActivity.this, getString(R.string.pickImage), Toast.LENGTH_SHORT).show();
                }else{
                    detectResult();
                }

            }
        });



    }

    private void requestStoragePermissionForAndroid11Plus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(intent);
                }
            }
        }
    }


    private void detectResult() {
        try {
            InputImage inputImage = InputImage.fromFilePath(this,imageUri);
            Task<List<Barcode>>barcodeResults = barcodeScanner.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            exteactBarCodeData(barcodes);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, getString(R.string.faildscann), Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e){

            Toast.makeText(MainActivity.this, getString(R.string.faildscann), Toast.LENGTH_SHORT).show();

        }
    }

    private void exteactBarCodeData(List<Barcode> barcodes) {
        progressBar.setVisibility(View.VISIBLE);// Show ProgressBar
        if (barcodes.isEmpty()){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, getString(R.string.correctbar), Toast.LENGTH_SHORT).show();
            return;
        }
        for (Barcode barcode : barcodes) {
            Rect bounds = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();
            String upcCode = barcode.getRawValue();
            Log.d(TAG, "exractBarCodeInfo: rawValue " + upcCode);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.upcdatabase.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            String apiKay = "EB82E172C73F00431D2B7C3FB01C8858";
            UPCService1 UPCService1 = retrofit.create(UPCService1.class);
            Call<ProductsJson1> call = UPCService1.getProductData(upcCode, apiKay);
            call.enqueue(new Callback<ProductsJson1>() {
                @Override
                public void onResponse(Call<ProductsJson1> call, Response<ProductsJson1> response) {
                    progressBar.setVisibility(View.GONE); // Show ProgressBar
                    if (response.isSuccessful()) {
                        ProductsJson1 productsJson1Response = response.body();
                        if (productsJson1Response != null) {
                            String name = productsJson1Response.getTitle();
                            String coun = productsJson1Response.getBrand();
                            String description = productsJson1Response.getDescription();


                            boolean isBoycott = false;
                            for (int i = 0; i < companies.size(); i++) {
                                if (coun != null && coun.toLowerCase().contains(companies.get(i).toLowerCase()) ||
                                        name != null && name.toLowerCase().contains(companies.get(i).toLowerCase()) ||
                                        description != null && description.toLowerCase().contains(companies.get(i).toLowerCase())) {
                                    isBoycott = true;
                                    break;
                                }
                            }
                            showBarcodeDataDialog(name,coun,isBoycott);


                        } else {
                            // Handle the case where no data is found
                            callSecondAPI(upcCode);
                        }
                    } else {
                        callSecondAPI(upcCode);
                    }
                }

                @Override
                public void onFailure(Call<ProductsJson1> call, Throwable t) {
                    progressBar.setVisibility(View.GONE); // Show ProgressBar

                    Toast.makeText(MainActivity.this, getString(R.string.check_connection) , Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void pickImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLanucher.launch(intent);
    }

    private final ActivityResultLauncher<Intent>galleryActivityResultLanucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imageUri = data.getData();
                        barcodeImg.setImageURI(imageUri);
                    }else{
                        Toast.makeText(MainActivity.this, getString(R.string.cancelled), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private void pickIMageCamera(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE ,"Barcode or QR code");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Take a picture of Barcode pr QR code");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        cameraActivityResultLanucher.launch(intent);
    }

    private final ActivityResultLauncher <Intent>cameraActivityResultLanucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        Log.d(TAG,"onActivityResult : image " +imageUri);
                        barcodeImg.setImageURI(imageUri);
                    }else{
                        Toast.makeText(MainActivity.this, getString(R.string.cancelled), Toast.LENGTH_SHORT).show();

                    }
                }
            }
    );

    private boolean checkStoragePermession(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }
    private void requestStoragePermession(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Handle storage permission for Android 11+
            requestStoragePermissionForAndroid11Plus();
        } else {
            // Request storage permission for Android 10 and below
            ActivityCompat.requestPermissions(this, storagePermession, STORAGE_REQUEST_CODE);
        }    }

    private boolean checkCameraPermession(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        boolean resultcamera = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        return result && resultcamera;
    }
    private void requestCameraPermession(){
        ActivityCompat.requestPermissions(this,cameraPermession,CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted= grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted= grantResults[1]==PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted){

                        pickIMageCamera();
                    }else {
                        Toast.makeText(this, getString(R.string.cameraper), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean storageAccepted= grantResults[0]==PackageManager.PERMISSION_GRANTED;

                    if( storageAccepted){

                        pickImage();
                    }else {
                        Toast.makeText(this, getString(R.string.storageper), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }


    private void callSecondAPI(String upcCode) {
        progressBar.setVisibility(View.VISIBLE); // Show ProgressBar
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("https://barcode-monster.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UPCService2 UPCService2 = retrofit2.create(UPCService2.class);
        Call<ProductJson2> call2 = UPCService2.getItems2(upcCode);
        call2.enqueue(new Callback<ProductJson2>() {
            @Override
            public void onResponse(Call<ProductJson2> call, Response<ProductJson2> response) {
                progressBar.setVisibility(View.GONE); // Show ProgressBar

                if (response.isSuccessful()) {
                    ProductJson2 productJson2 = response.body();
                    if (productJson2 != null) {
                        String name = productJson2.getCompany();
                        String description = productJson2.getDescription();


                        boolean isBoycott = false;
                        for (int i = 0; i < companies.size(); i++) {
                            if (name != null && name.toLowerCase().contains(companies.get(i).toLowerCase()) ||
                                    description != null && description.toLowerCase().contains(companies.get(i).toLowerCase())) {
                                isBoycott = true;
                                break;
                            }
                        }

                        showBarcodeDataDialog(name,description,isBoycott);


                    } else {
                        progressBar.setVisibility(View.GONE); // Show ProgressBar
                        // No data found in the second API
                        displayNoDataFoundMessage();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductJson2> call, Throwable t) {
                // Second API request failed
                progressBar.setVisibility(View.GONE); // Show ProgressBar
                displayNoDataFoundMessage();
            }
        });
    }

    // Method to display "No data found" message
    private void displayNoDataFoundMessage() {
        Toast.makeText(MainActivity.this, getString(R.string.nodata), Toast.LENGTH_SHORT).show();
    }

    private void showBarcodeDataDialog(String name, String coun, boolean isBoycott) {
        // Create a custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialogebox_barcode, null);

        // Find views in the custom layout
        TextView productNameTextView = dialogView.findViewById(R.id.productname);
        TextView organizationNameTextView = dialogView.findViewById(R.id.organizationname);
        TextView boycottTextView = dialogView.findViewById(R.id.boycott);
        TextView nameLabel = dialogView.findViewById(R.id.productlabel);
        TextView organLabel = dialogView.findViewById(R.id.organLabel);

        // Set data to views
        if (!name.isEmpty()&& !coun.isEmpty()){
            productNameTextView.setText(name);
            organizationNameTextView.setText(coun);
            nameLabel.setVisibility(View.VISIBLE);
            productNameTextView.setVisibility(View.VISIBLE);
            organizationNameTextView.setVisibility(View.VISIBLE);
            organLabel.setVisibility(View.VISIBLE);


        }else if (name.isEmpty() && !coun.isEmpty()){
            organizationNameTextView.setText(coun);
            nameLabel.setVisibility(View.GONE);
            productNameTextView.setVisibility(View.GONE);
            organizationNameTextView.setVisibility(View.VISIBLE);
            organLabel.setVisibility(View.VISIBLE);

        }else if (coun.isEmpty() &&!name.isEmpty()){
            productNameTextView.setText(name);
            nameLabel.setVisibility(View.VISIBLE);
            productNameTextView.setVisibility(View.VISIBLE);
            organizationNameTextView.setVisibility(View.GONE);
            organLabel.setVisibility(View.GONE);
        }else {  nameLabel.setVisibility(View.GONE);
            productNameTextView.setVisibility(View.GONE);
            organizationNameTextView.setVisibility(View.GONE);
            organLabel.setVisibility(View.GONE);

        }


        if (isBoycott) {
            boycottTextView.setText(getString(R.string.dont_buy));
            boycottTextView.setTextColor(Color.parseColor("#FF0000"));
        } else {
            boycottTextView.setText(getString(R.string.buy));
            boycottTextView.setTextColor(Color.parseColor("#008000"));
        }

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle OK button click if needed
                dialog.dismiss();
            }
        });

        // Show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

