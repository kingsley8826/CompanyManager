package com.example.tuanfpt.companymanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tuanfpt.companymanager.R;
import com.example.tuanfpt.companymanager.adapter.ImageAdapter;
import com.example.tuanfpt.companymanager.adapter.OnImageItemSelected;
import com.example.tuanfpt.companymanager.models.Company;
import com.example.tuanfpt.companymanager.models.Image;
import com.example.tuanfpt.companymanager.models.ImageRequestCode;
import com.example.tuanfpt.companymanager.models.Maintain;
import com.example.tuanfpt.companymanager.models.Period;
import com.example.tuanfpt.companymanager.network.RetrofitContext;
import com.example.tuanfpt.companymanager.utilities.Constant;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, OnImageItemSelected {

    @BindView(R.id.txtName)
    EditText txtName;
    @BindView(R.id.txtMotherName)
    EditText txtMotherName;
    @BindView(R.id.txtPhoneNumber)
    EditText txtPhoneNumber;
    @BindView(R.id.txtAddress)
    EditText txtAddress;


    @BindView(R.id.layoutLastMaintain)
    CardView layoutLastMaintain;
    @BindView(R.id.txtTrait)
    EditText txtTrait;
    @BindView(R.id.txtError)
    EditText txtError;
    @BindView(R.id.txtEmployeeName)
    EditText txtEmployeeName;
    @BindView(R.id.txtDate)
    EditText txtDate;
    @BindView(R.id.txtNote)
    EditText txtNote;

    @BindView(R.id.spinner_period)
    Spinner spinnerPeriod;
    @BindView(R.id.spinner_year)
    Spinner spinnerYear;

//    @BindView(R.id.btnAddBefore)
//    Button btnAddBefore;
//    @BindView(R.id.btnAddAfter)
//    Button btnAddAfter;
//
//    @BindView(R.id.imv_before_1)
//    ImageView imvBefore1;
//    private final int TAKE_PICTURE_1 = 1;
//    private String currentPhotoPath1;
//
//    @BindView(R.id.imv_before_2)
//    ImageView imvBefore2;
//    private final int TAKE_PICTURE_2 = 2;
//    private String currentPhotoPath2;
//
//    @BindView(R.id.imv_before_3)
//    ImageView imvBefore3;
//    private final int TAKE_PICTURE_3 = 3;
//    private String currentPhotoPath3;
//
//    @BindView(R.id.imv_before_4)
//    ImageView imvBefore4;
//    private final int TAKE_PICTURE_4 = 4;
//    private String currentPhotoPath4;
//
//
//    @BindView(R.id.imv_after_1)
//    ImageView imvAfter1;
//    private final int TAKE_PICTURE_5 = 5;
//    private String currentPhotoPath5;
//
//    @BindView(R.id.imv_after_2)
//    ImageView imvAfter2;
//    private final int TAKE_PICTURE_6 = 6;
//    private String currentPhotoPath6;
//
//    @BindView(R.id.imv_after_3)
//    ImageView imvAfter3;
//    private final int TAKE_PICTURE_7 = 7;
//    private String currentPhotoPath7;
//
//    @BindView(R.id.imv_after_4)
//    ImageView imvAfter4;
//    private final int TAKE_PICTURE_8 = 8;
//    private String currentPhotoPath8;

    @BindView(R.id.rvImageBefore)
    RecyclerView rvImageBefore;
    @BindView(R.id.rvImageAfter)
    RecyclerView rvImageAfter;

    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.layoutExistData)
    LinearLayout layoutExistData;

    @BindView(R.id.layoutAddMaintainInfo)
    LinearLayout layoutAddMaintainInfo;
    @BindView(R.id.btnSave)
    Button btnSave;

    private StorageReference mStorageRef;
    private Uri imageUri;
    UploadTask uploadTask;

    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    ArrayList<String> periodItems = new ArrayList<>();
    ArrayList<String> yearItems = new ArrayList<>();
    ArrayList<Image> imagesBefore = new ArrayList<>();
    ArrayList<Image> imagesAfter = new ArrayList<>();

    private ImageAdapter imageBeforeAdapter;
    private ImageAdapter imageAfterAdapter;

    private String companyId;
    private Company company;
    private ImageRequestCode currentImageRequestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        addListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (companyId != null) {
            getCompanyById();
        }
    }

    private void init() {
        ButterKnife.bind(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        companyId = (String) getIntent().getSerializableExtra(Constant.COMPANY_ID);

        // init spinner
        periodItems.add(Constant.KY_2);
        periodItems.add(Constant.KY_1);
        for (int i = currentYear; i > currentYear - 20; i--) {
            yearItems.add(String.valueOf(i));
        }
        filterSpinner(spinnerPeriod, periodItems);
        filterSpinner(spinnerYear, yearItems);

        initRecycleView();
    }

    private void addListener() {
        spinnerPeriod.setOnItemSelectedListener(this);
        spinnerYear.setOnItemSelectedListener(this);
        btnSave.setOnClickListener(this);
    }

    private void initRecycleView() {
        rvImageBefore.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageBeforeAdapter = new ImageAdapter(imagesBefore, this, Constant.BEFORE);
        rvImageBefore.setAdapter(imageBeforeAdapter);

        rvImageAfter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAfterAdapter = new ImageAdapter(imagesAfter, this, Constant.AFTER);
        rvImageAfter.setAdapter(imageAfterAdapter);
    }

    private void resetRecycleViewBefore(ArrayList<Image> imgBefore) {
        if (imgBefore == null) {
            imgBefore = new ArrayList<>();
            imgBefore.add(new Image("", "", Constant.ADD_BUTTON));
        }
        imageBeforeAdapter.updateRecyclerView(imgBefore);
    }

    private void resetRecycleViewAfter(ArrayList<Image> imgAfter) {

        if (imgAfter == null) {
            imgAfter = new ArrayList<>();
            imgAfter.add(new Image("", "", Constant.ADD_BUTTON));
        }
        imageAfterAdapter.updateRecyclerView(imgAfter);
    }

    private void getCompanyById() {
        RetrofitContext.getCompanyById(companyId).enqueue(new Callback<Company>() {
            @Override
            public void onResponse(@NonNull Call<Company> call, @NonNull Response<Company> response) {
                if (response.code() == 200) {
                    company = response.body();
                    if (company != null) {
                        bindData();
                    }
                } else {
                    showToast(getString(R.string.internet_error));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Company> call, @NonNull Throwable t) {
                showToast(getString(R.string.internet_error));
            }
        });
    }

    private void bindData() {

        // bind data company
        txtName.setText(company.getName());
        txtMotherName.setText(company.getMother());
        txtPhoneNumber.setText(company.getPhone());
        txtAddress.setText(company.getAddress());

        // bind data last period
        ArrayList<Maintain> maintains = company.getMaintenances();
        Maintain lastMaintain = maintains != null ? maintains.get(maintains.size() - 1) : null;
        if (lastMaintain != null) {
            layoutLastMaintain.setVisibility(View.VISIBLE);
            txtTrait.setText(lastMaintain.getTrait());
            txtError.setText(lastMaintain.getProblem());
            txtEmployeeName.setText(lastMaintain.getEmployeeName());
            txtDate.setText(lastMaintain.getDate());
            txtNote.setText(lastMaintain.getNote());
        } else {
            layoutLastMaintain.setVisibility(View.GONE);
        }

        // bind date selected period
        Period selectedPeriod = new Period(spinnerPeriod.getSelectedItem().toString(), spinnerYear.getSelectedItem().toString());
        bindDataPeriod(selectedPeriod);
    }

    private void bindDataPeriod(Period selectedPeriod) {

        if (company == null) return;
        Maintain selectedMaintain = getMaintainByPeriod(selectedPeriod);
        if (selectedMaintain != null) { // show data and hide some layout
            tvNoData.setVisibility(View.GONE);
            layoutExistData.setVisibility(View.VISIBLE);
            layoutAddMaintainInfo.setVisibility(View.GONE);

            bindDataImageView(selectedMaintain);
        } else {
            if (isCurrentPeriod(selectedPeriod)) { // employee add maintain information
                tvNoData.setVisibility(View.GONE);
                layoutExistData.setVisibility(View.VISIBLE);
                layoutAddMaintainInfo.setVisibility(View.VISIBLE);

                bindDataImageView(null);

            } else { // period is in the future or in the past and no maintain
                tvNoData.setVisibility(View.VISIBLE);
                layoutExistData.setVisibility(View.GONE);
            }
        }
    }

    private Maintain getMaintainByPeriod(Period period) {
        ArrayList<Maintain> maintains = company.getMaintenances();
        if (maintains == null) return null;
        for (Maintain maintain : maintains) {
            if (maintain.getPeriod().equals(period.getPeriod()) && maintain.getYear().equals(period.getYear())) {
                return maintain;
            }
        }
        return null;
    }

    private boolean isCurrentPeriod(Period period) {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        String currentPeriod = currentMonth <= 6 ? Constant.KY_1 : Constant.KY_2;
        return period.getPeriod().equals(currentPeriod) && period.getYear().equals(String.valueOf(currentYear));
    }

    private void bindDataImageView(Maintain selectedMaintain) {

        if (selectedMaintain == null) {
            resetRecycleViewBefore(null);
            resetRecycleViewAfter(null);
            return;
        }

        ArrayList<String> beforeImages = selectedMaintain.getImageBefore();
        ArrayList<String> afterImages = selectedMaintain.getImageAfter();

        ArrayList<Image> imagesBefore = new ArrayList<>();
        ArrayList<Image> imagesAfter = new ArrayList<>();
        for (int i = 0; i < beforeImages.size(); i++) {
            imagesBefore.add(new Image("", beforeImages.get(i), Constant.URL_PATH));
        }
        for (int i = 0; i < afterImages.size(); i++) {
            imagesAfter.add(new Image("", afterImages.get(i), Constant.URL_PATH));
        }
        resetRecycleViewBefore(imagesBefore);
        resetRecycleViewAfter(imagesAfter);
    }

    private void filterSpinner(Spinner spinner, ArrayList<String> data) {
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSave:
                break;
        }
    }

    // spinner click
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Period selectedPeriod = new Period(spinnerPeriod.getSelectedItem().toString(), spinnerYear.getSelectedItem().toString());
        bindDataPeriod(selectedPeriod);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    // image before click
    @Override
    public void onItemBeforeSelected(int position) {
        currentImageRequestCode = new ImageRequestCode(position, Constant.BEFORE);
        takePhoto();
    }

    // image after click
    @Override
    public void onItemAfterSelected(int position) {
        currentImageRequestCode = new ImageRequestCode(position, Constant.AFTER);
        takePhoto();
    }


    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                currentImageRequestCode.setPhotoPath(photoFile.getAbsolutePath());
            } catch (IOException ignored) {
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                imageUri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, currentImageRequestCode.getRequestCode());
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (currentImageRequestCode != null && requestCode == currentImageRequestCode.getRequestCode()) {
                if (currentImageRequestCode.getType().equals(Constant.BEFORE)) {
                    if (currentImageRequestCode.getRequestCode() >= 0 && currentImageRequestCode.getRequestCode() < imagesBefore.size()) {
                        imagesBefore.get(currentImageRequestCode.getRequestCode()).setPathInDevice(currentImageRequestCode.getPhotoPath());
                        imagesBefore.get(currentImageRequestCode.getRequestCode()).setType(Constant.DEVICE_PATH);
                        imagesBefore.add(new Image("", "", Constant.ADD_BUTTON));
                        resetRecycleViewBefore(imagesBefore);
                    }
                } else {
                    if (currentImageRequestCode.getRequestCode() >= 0 && currentImageRequestCode.getRequestCode() < imagesAfter.size()) {
                        imagesAfter.get(currentImageRequestCode.getRequestCode()).setPathInDevice(currentImageRequestCode.getPhotoPath());
                        imagesAfter.get(currentImageRequestCode.getRequestCode()).setType(Constant.DEVICE_PATH);
                        imagesAfter.add(new Image("", "", Constant.ADD_BUTTON));
                        resetRecycleViewAfter(imagesAfter);
                    }
                }
            }
        }
    }

    private void uploadFile() {
        StorageReference riversRef = mStorageRef.child("imagetuandm/" + imageUri.getLastPathSegment());
        uploadTask = riversRef.putFile(imageUri);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                showToast("upload fail" + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                showToast("upload success");
                getUrl();
            }
        });
    }

    private void getUrl() {
        final StorageReference ref = mStorageRef.child("imagetuandm/" + imageUri.getLastPathSegment());
        uploadTask = ref.putFile(imageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d("tuandm", "succc");
                    Log.d("tuandm", downloadUri.toString());
                } else {
                    Log.d("tuandm", "failll");
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}


//    private void updateRecyclerViewBefore(ArrayList<Image> images) {
//        imagesBefore = images;
//        imageBeforeAdapter = new ImageAdapter(imagesBefore);
//        rvImageBefore.setAdapter(imageBeforeAdapter);
//    }
//
//    private void updateRecyclerViewAfter(ArrayList<Image> images) {
//        imagesAfter = images;
//        imageAfterAdapter = new ImageAdapter(imagesAfter);
//        rvImageAfter.setAdapter(imageAfterAdapter);
//    }
//
//    private void setPicture(ImageView imvPhoto) {
//        // Get the dimensions of the View
//        int targetW = imvPhoto.getWidth();
//        int targetH = imvPhoto.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        imvPhoto.setImageBitmap(bitmap);
//        uploadFile();
//    }
//
//    private void goToCamera(int type){
//
//        Intent intent = new Intent(DetailActivity.this, TakeImageActivity.class);
//        startActivityForResult(intent, type);
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//    }
