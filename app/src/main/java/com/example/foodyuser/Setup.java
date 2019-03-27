package com.example.foodyuser;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setup extends AppCompatActivity {

    private CircleImageView profilePicture;
    private ImageButton save, back;
    private FloatingActionButton editImage;
    private EditText name, email, address, phoneNumber, bio;
    private TextView errorName, errorMail, errorAddress, errorPhone, errorBio;
    private final int GALLERY_REQUEST_CODE = 1;
    private final int REQUEST_CAPTURE_IMAGE = 100;
    private final String PROFILE_IMAGE = "ProfileImage.jpg";
    private final String PLACEHOLDER_CAMERA="PlaceCamera.jpg";
    private String placeholderPath;

    //Shared Preferences definition
    Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        init();

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickImageDialog();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        edit.putString("name", name.getText().toString());
        edit.putString("email", email.getText().toString());
        edit.putString("address", address.getText().toString());
        edit.putString("phoneNumber", phoneNumber.getText().toString());
        edit.putString("bio", bio.getText().toString());
        edit.apply();

        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("name", name.getText().toString());
        outState.putString("email", email.getText().toString());
        outState.putString("address", address.getText().toString());
        outState.putString("phoneNumber", phoneNumber.getText().toString());
        outState.putString("bio", bio.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        name.setText(savedInstanceState.getString("name", getResources().getString(R.string.name_hint)));
        email.setText(savedInstanceState.getString("email", getResources().getString(R.string.email_hint)));
        address.setText(savedInstanceState.getString("address", getResources().getString(R.string.address_hint)));
        phoneNumber.setText(savedInstanceState.getString("phoneNumber", getResources().getString(R.string.phone_hint)));
        bio.setText(savedInstanceState.getString("bio", getResources().getString(R.string.bio_hint)));

        name.clearFocus();
        email.clearFocus();
        address.clearFocus();
        phoneNumber.clearFocus();
        bio.clearFocus();
    }

    protected void onPause(){
        super.onPause();

    }

    private void updateSave(){
        String username = name.getText().toString();
        String regx = "^[\\p{L} .'-]+$";
        Pattern regex = Pattern.compile(regx);
        Matcher nameMatcher = regex.matcher(username);

        String userNumber = phoneNumber.getText().toString();

        View errorLine = findViewById(R.id.email_error_line);

        if(!nameMatcher.matches() || !PhoneNumberUtils.isGlobalPhoneNumber(userNumber)
                || userNumber.length() == 0 || !Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
            save.setImageResource(R.drawable.save_dis);
            save.setEnabled(false);
            save.setClickable(false);
        }else{
            save.setImageResource(R.drawable.save_white);
            save.setEnabled(true);
            save.setClickable(true);
        }

    }


    private void checkName(){
        String username = name.getText().toString();
        String regx = "^[\\p{L} .'-]+$";
        View errorLine = findViewById(R.id.name_error_line);
        Pattern regex = Pattern.compile(regx);
        Matcher matcher = regex.matcher(username);

        if(!matcher.matches()){
            errorName.setText(getResources().getString(R.string.error_name));
            errorLine.setBackgroundColor(getResources().getColor(R.color.errorColor,this.getTheme()));
            errorLine.setAlpha(1);
        }else{
            errorName.setText("");
            errorLine.setAlpha(0.2f);
            errorLine.setBackgroundColor(Color.BLACK);
        }

        updateSave();

    }

    private void checkNumber(){
        String regexpPhone = "^(([+]|00)39)?((3[1-6][0-9]))(\\d{7})$";
        final String userNumber = phoneNumber.getText().toString();

        View errorLine = findViewById(R.id.number_error_line);

        if(!Pattern.compile(regexpPhone).matcher(userNumber).matches()){
            errorPhone.setText(getResources().getString(R.string.error_number));
            errorLine.setBackgroundColor(getResources().getColor(R.color.errorColor,this.getTheme()));
            errorLine.setAlpha(1);
        }else{
            errorPhone.setText("");
            errorLine.setAlpha(0.2f);
            errorLine.setBackgroundColor(Color.BLACK);
        }

        updateSave();

    }

    private void checkMail(){
        View errorLine = findViewById(R.id.email_error_line);
        String regexpEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final String emailToCheck = email.getText().toString();

        if(!Pattern.compile(regexpEmail).matcher(emailToCheck).matches()) {
                errorMail.setText(getResources().getString(R.string.error_email));
                errorLine.setBackgroundColor(getResources().getColor(R.color.errorColor, this.getTheme()));
                errorLine.setAlpha(1);
        }else{
            errorMail.setText("");
            errorLine.setAlpha(0.2f);
            errorLine.setBackgroundColor(Color.BLACK);
        }

        updateSave();


    }

    private  void pickFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,GALLERY_REQUEST_CODE);

    }

    private void pickFromCamera(){
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager())!= null){

            File photoFile = createOrReplacePlaceholder();

            if(photoFile!=null){
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.foodyuser",
                        photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    private void init(){

        this.profilePicture = findViewById(R.id.profilePicture);
        this.editImage = findViewById(R.id.edit_profile_picture);
        this.name = findViewById(R.id.userName);
        this.email = findViewById(R.id.emailAddress);
        this.address = findViewById(R.id.address);
        this.phoneNumber = findViewById(R.id.phoneNumber);
        this.bio = findViewById(R.id.bio);

        //setup of the Shared Preferences to save value in (key, value) format
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences("myPreference", MODE_PRIVATE);
        edit = sharedPref.edit();

        this.errorName = findViewById(R.id.name_error);
        this.errorMail = findViewById(R.id.email_error);
        this.errorPhone = findViewById(R.id.number_error);
        this.errorAddress = findViewById(R.id.address_error);
        this.errorBio = findViewById(R.id.bio_error);

        this.back = findViewById(R.id.backButton);
        this.save = findViewById(R.id.saveButton);

        errorName.setText("");
        errorMail.setText("");
        errorPhone.setText("");
        errorAddress.setText("");
        errorBio.setText("");

        name.setText(sharedPref.getString("name", getResources().getString(R.string.name_hint)));
        email.setText(sharedPref.getString("email", getResources().getString(R.string.email_hint)));
        address.setText(sharedPref.getString("address", getResources().getString(R.string.address_hint)));
        phoneNumber.setText(sharedPref.getString("phoneNumber", getResources().getString(R.string.phone_hint)));
        bio.setText(sharedPref.getString("bio", getResources().getString(R.string.bio_hint)));
        edit.apply();

        Bitmap image = getBitmapFromFile();

        if(image != null)
            profilePicture.setImageBitmap(image);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkName();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkNumber();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkMail();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        updateSave();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){

                case REQUEST_CAPTURE_IMAGE:
                    File f = new File(placeholderPath);
                    startCrop(Uri.fromFile(f));
                    break;

                case GALLERY_REQUEST_CODE:
                    if(data !=null){
                        Uri imageUri = data.getData();

                        if(imageUri != null)
                            startCrop(imageUri);

                      // profilePicture.setImageURI(imageUri);

                    }
                    break;

                case  UCrop.REQUEST_CROP:
                    Bitmap bitmap = getBitmapFromFile();
                    if(bitmap != null){
                        profilePicture.setImageBitmap(bitmap);
                    }
                    break;
            }
        }
    }

    private void setBitmapPlaceholder(Bitmap bitmap){

        File f = new File(this.getFilesDir(), PLACEHOLDER_CAMERA);

        if(f.exists())
            this.deleteFile(f.getName());

        f = new File(this.getFilesDir(), PLACEHOLDER_CAMERA);

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        try{

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private File createOrReplacePlaceholder(){

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File f = new File(storageDir, PLACEHOLDER_CAMERA);

        if(f.exists())
            this.deleteFile(f.getName());

        f = new File(storageDir, PLACEHOLDER_CAMERA);

        placeholderPath = f.getPath();

       return f;
    }

    private void setBitmapProfile(Bitmap bitmap){

        File f = new File(this.getFilesDir(), PROFILE_IMAGE);

        if(f.exists())
            this.deleteFile(f.getName());

        f = new File(this.getFilesDir(), PROFILE_IMAGE);

//Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file

        try{

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromFile(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        File dest = new File(this.getFilesDir(), PROFILE_IMAGE);
        if(!dest.exists())
            return null;
        return  BitmapFactory.decodeFile(dest.getPath(), options);

    }

    private void showPickImageDialog(){
        final Item[] items = {
                new Item(getString(R.string.alert_dialog_image_gallery), R.drawable.collections_black),
                new Item(getString(R.string.alert_dialog_image_camera), R.drawable.camera_black)
        };
        ListAdapter arrayAdapter = new ArrayAdapter<Item>(
                this,
                R.layout.alert_dialog_item,
                R.id.tv1,
                items){
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = v.findViewById(R.id.tv1);
                ImageView iv = v.findViewById(R.id.iv1);
                iv.setImageDrawable(getDrawable(items[position].icon));
                return v;
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(Setup.this);
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle(getResources().getString(R.string.alert_dialog_image_title));
        builder.setCancelable(false);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                case 0:
                    pickFromGallery();
                    break;
                case 1:
                    pickFromCamera();
                    break;
                }
            }
        });
        builder.show();
    }

    private void startCrop(@NonNull Uri uri){

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(this.getFilesDir(), PROFILE_IMAGE)));


        uCrop.withAspectRatio(1,1);

        uCrop.withMaxResultSize(450,450);

        uCrop.withOptions(getCropOptions());



        uCrop.start(Setup.this);
    }

    private UCrop.Options getCropOptions(){
        UCrop.Options options= new UCrop.Options();

        options.setCompressionQuality(100);

        //Compress Type
        //options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        //options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        //UI
        options.setHideBottomControls(true);

        //Colors
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary, getTheme()));


        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL);

        options.setCircleDimmedLayer(true);

        options.setToolbarTitle(getResources().getString(R.string.crop_image));

        return options;


    }

}
