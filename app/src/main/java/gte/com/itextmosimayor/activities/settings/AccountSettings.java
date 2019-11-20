package gte.com.itextmosimayor.activities.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import gte.com.itextmosimayor.R;

public class AccountSettings extends AppCompatActivity {

    TextView txtFullname;
    ImageView imgPP;
    ProgressBar progressBar;
    Button btnSave;
    ImageButton btnBack;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    String currentUID;
    Uri imgURI;
    String imgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_account_settings);

        //storage
        storageReference = FirebaseStorage.getInstance().getReference("imguploads");

        //database
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseDatabase = FirebaseDatabase.getInstance();
        //currentUID
        currentUID = FirebaseAuth.getInstance().getUid();

        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);
        txtFullname = findViewById(R.id.txtFullname);
        imgPP = findViewById(R.id.imgPP);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setImageResource(R.drawable.ic_arrow_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            imgPP.setClipToOutline(true);

        imgPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(AccountSettings.this);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        //Initialize Firebase
        databaseReference.child(currentUID).child("img").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                imgURL = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                Picasso.get().load(imgURL).fit().centerInside().into(imgPP);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
            }
        });

        databaseReference.child(currentUID).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                txtFullname.setText(Objects.requireNonNull(dataSnapshot.getValue()).toString());
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imgURI = result.getUri();
                Picasso.get().load(imgURI).fit().centerInside().placeholder(R.drawable.ic_person).into(imgPP);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadImage() {
        if (imgURI != null) {
            final StorageReference fileReference = storageReference.child(currentUID + ".jpg");
            fileReference.putFile(imgURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    databaseReference.child(currentUID).child("img").setValue(uri.toString());
                                    Toast.makeText(AccountSettings.this, "Upload successful", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NotNull Exception e) {
                            Toast.makeText(AccountSettings.this, "Failed to upload image. Please try again", Toast.LENGTH_LONG).show();
                            Log.e("UploadFailed", e.toString());
                            progressBar.setVisibility(View.GONE);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });
        } else
            Toast.makeText(AccountSettings.this, "Please select image", Toast.LENGTH_LONG).show();
    }
}