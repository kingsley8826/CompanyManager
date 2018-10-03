package com.example.tuanfpt.companymanager.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuanfpt.companymanager.R;
import com.example.tuanfpt.companymanager.adapter.ImageAdapter;
import com.example.tuanfpt.companymanager.adapter.OnImageItemSelected;
import com.example.tuanfpt.companymanager.models.Company;
import com.example.tuanfpt.companymanager.models.Image;
import com.example.tuanfpt.companymanager.models.ImageRequest;
import com.example.tuanfpt.companymanager.models.JSONAddMaintainSendForm;
import com.example.tuanfpt.companymanager.models.Maintain;
import com.example.tuanfpt.companymanager.models.PendingImage;
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
import java.text.DateFormat;
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

    @BindView(R.id.rvImageBefore)
    RecyclerView rvImageBefore;
    @BindView(R.id.rvImageAfter)
    RecyclerView rvImageAfter;

    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.layoutExistData)
    LinearLayout layoutExistData;

    @BindView(R.id.rgTrait)
    RadioGroup rgTrait;
    @BindView(R.id.rgProblem)
    RadioGroup rgProblem;
    @BindView(R.id.edtNote)
    EditText edtNote;

    @BindView(R.id.layoutAddMaintainInfo)
    LinearLayout layoutAddMaintainInfo;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnFab)
    FloatingActionButton btnFab;

    private StorageReference mStorageRef;

    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    ArrayList<String> periodItems;
    ArrayList<String> yearItems;
    ArrayList<Image> imagesBefore;
    ArrayList<Image> imagesAfter;

    private ImageAdapter imageBeforeAdapter;
    private ImageAdapter imageAfterAdapter;

    private ArrayList<PendingImage> pendingImages;

    private String companyId;
    private Company company;
    private ImageRequest currentImageRequest;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
        addListener();
        if (companyId != null) {
            getCompanyById();
        }
    }

    private void init() {
        ButterKnife.bind(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        companyId = (String) getIntent().getSerializableExtra(Constant.COMPANY_ID);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Đang xử lý...");
        dialog.setCancelable(false);
        resetData();
        // init spinner
        periodItems = new ArrayList<>();
        yearItems = new ArrayList<>();
        periodItems.add(Constant.KY_2);
        periodItems.add(Constant.KY_1);
        for (int i = currentYear; i > currentYear - 10; i--) {
            yearItems.add(String.valueOf(i));
        }
        filterSpinner(spinnerPeriod, periodItems);
        filterSpinner(spinnerYear, yearItems);

        initRecycleView();
    }

    private void resetData() {
        imagesBefore = new ArrayList<>();
        imagesAfter = new ArrayList<>();
        pendingImages = new ArrayList<>();
    }

    private void addListener() {
        spinnerPeriod.setOnItemSelectedListener(this);
        spinnerYear.setOnItemSelectedListener(this);
        btnSave.setOnClickListener(this);
        btnFab.setOnClickListener(this);
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
        // chỉ nên truyền 1 object khác địa chỉ vào thì ms đồng bộ được data của recycleView với biến imagesBefore
        ArrayList<Image> newImages = new ArrayList<>(imgBefore);
        imageBeforeAdapter.updateRecyclerView(newImages);
    }

    private void resetRecycleViewAfter(ArrayList<Image> imgAfter) {

        if (imgAfter == null) {
            imgAfter = new ArrayList<>();
            imgAfter.add(new Image("", "", Constant.ADD_BUTTON));
        }
        ArrayList<Image> newImages = new ArrayList<>(imgAfter);
        imageAfterAdapter.updateRecyclerView(newImages);
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
                if (pendingImages == null || pendingImages.size() == 0 || !checkExistImage()) {
                    showToast("Vui lòng chụp ảnh trước và sau khi bảo dưỡng");
                    return;
                }
                if (!dialog.isShowing())
                    dialog.show();
                uploadFile();
                break;
            case R.id.btnFab:
                showMap(company.getName() + ", " + company.getAddress());
                break;
        }
    }

    private boolean checkExistImage() {
        boolean beforeExist = false;
        boolean afterExist = false;
        for (PendingImage pendingImage : pendingImages) {
            if (pendingImage.getState().equals(Constant.EXIST_STATE)) {
                if (pendingImage.getType().equals(Constant.BEFORE)) {
                    beforeExist = true;
                } else {
                    afterExist = true;
                }
            }
        }
        return beforeExist && afterExist;
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
    public void onItemBeforeSelected(Image image, int position) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            if (image.getType().equals(Constant.ADD_BUTTON)) {
                currentImageRequest = new ImageRequest(position, Constant.BEFORE);
                takePhoto(Constant.BEFORE, position);
            }
        } else {
            showToast(getString(R.string.no_camera));
        }
    }

    // image after click
    @Override
    public void onItemAfterSelected(Image image, int position) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            if (image.getType().equals(Constant.ADD_BUTTON)) {
                currentImageRequest = new ImageRequest(position, Constant.AFTER);
                takePhoto(Constant.AFTER, position);
            }
        } else {
            showToast(getString(R.string.no_camera));
        }
    }

    public void takePhoto(String type, int position) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                currentImageRequest.setPhotoPath(photoFile.getAbsolutePath());
            } catch (IOException ignored) {
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);

                if (pendingImages == null) pendingImages = new ArrayList<>();
                pendingImages.add(new PendingImage(photoURI, position, type, Constant.INIT_STATE));
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, currentImageRequest.getRequestCode());
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
            if (currentImageRequest != null && requestCode == currentImageRequest.getRequestCode()) {
                if (currentImageRequest.getType().equals(Constant.BEFORE)) {
                    Log.d("tuandm", imagesBefore.size() + "");
                    if (currentImageRequest.getRequestCode() >= 0 && currentImageRequest.getRequestCode() < imagesBefore.size()) {
                        imagesBefore.get(currentImageRequest.getRequestCode()).setPathInDevice(currentImageRequest.getPhotoPath());
                        imagesBefore.get(currentImageRequest.getRequestCode()).setType(Constant.DEVICE_PATH);
                        imagesBefore.add(new Image("", "", Constant.ADD_BUTTON));
                        resetRecycleViewBefore(imagesBefore);
                    }
                } else {
                    if (currentImageRequest.getRequestCode() >= 0 && currentImageRequest.getRequestCode() < imagesAfter.size()) {
                        imagesAfter.get(currentImageRequest.getRequestCode()).setPathInDevice(currentImageRequest.getPhotoPath());
                        imagesAfter.get(currentImageRequest.getRequestCode()).setType(Constant.DEVICE_PATH);
                        imagesAfter.add(new Image("", "", Constant.ADD_BUTTON));
                        resetRecycleViewAfter(imagesAfter);
                    }
                }
                if (pendingImages != null && pendingImages.size() > 0)
                    pendingImages.get(pendingImages.size() - 1).setState(Constant.EXIST_STATE);
            }
        }
    }

    private void uploadFile() {

        for (final PendingImage pendingImage : pendingImages) {
            if (pendingImage.getState().equals(Constant.EXIST_STATE)) {
                StorageReference riversRef = mStorageRef.child(Constant.FIREBASE_FOLDER + pendingImage.getUri().getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(pendingImage.getUri());

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pendingImage.setState(Constant.UPLOAD_FAIL);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getUrl(pendingImage);
                    }
                });
            }
        }
    }

    private void getUrl(final PendingImage pendingImage) {
        final StorageReference ref = mStorageRef.child(Constant.FIREBASE_FOLDER + pendingImage.getUri().getLastPathSegment());
        UploadTask uploadTask = ref.putFile(pendingImage.getUri());

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
                    pendingImage.setUrl(downloadUri.toString());
                    pendingImage.setState(Constant.UPLOAD_SUCCESS);
                    if (checkUploadDoneAll()) {
                        postMaintain();
                    }
                } else {
                    pendingImage.setState(Constant.UPLOAD_FAIL);
                }
            }
        });
    }

    private boolean checkUploadDoneAll() {
        for (PendingImage pendingImage : pendingImages) {
            if (pendingImage.getState().equals(Constant.EXIST_STATE)) {
                return false;
            }
        }
        return true;
    }

    private void postMaintain() {

        String companyId = company.getId();
        String period = periodItems.get(spinnerPeriod.getSelectedItemPosition());
        String year = yearItems.get(spinnerYear.getSelectedItemPosition());
        String trait = ((RadioButton) findViewById(rgTrait.getCheckedRadioButtonId())).getText().toString();
        String problem = ((RadioButton) findViewById(rgProblem.getCheckedRadioButtonId())).getText().toString();
        String date = android.text.format.DateFormat.format("dd/MM/yyyy", new Date()).toString();
        String employeeName = this.getSharedPreferences(Constant.COMPANY_KEY, MODE_PRIVATE).getString(Constant.USERNAME, "");
        String note = edtNote.getText().toString();

        ArrayList<String> imgBefore = new ArrayList<>();
        ArrayList<String> imgAfter = new ArrayList<>();
        for (PendingImage pendingImage : pendingImages) {
            if (pendingImage.getState().equals(Constant.UPLOAD_SUCCESS)) {
                if (pendingImage.getType().equals(Constant.BEFORE)) {
                    imgBefore.add(pendingImage.getUrl());
                } else {
                    imgAfter.add(pendingImage.getUrl());
                }
            }
        }

        JSONAddMaintainSendForm jsonAddMaintainSendForm
                = new JSONAddMaintainSendForm(companyId, period, imgBefore, imgAfter, trait, problem, year, date, employeeName, note);

        RetrofitContext.postMaintain(jsonAddMaintainSendForm).enqueue(new Callback<Company>() {
            @Override
            public void onResponse(@NonNull Call<Company> call, @NonNull Response<Company> response) {
                if (response.code() == 200) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                    showDialog("Lưu lại thành công");
                    init();
                    getCompanyById();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Company> call, @NonNull Throwable t) {
                showDialog("Lưu lại thất bại");
                resetData();
            }
        });
    }

    private void showMap(String address) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:0,0?q=" + address)); //lat lng or address query
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}