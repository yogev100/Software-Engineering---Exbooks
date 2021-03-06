package com.example.exbooks.Screens;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.exbooks.Objects.Book;
import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.example.exbooks.Users.Manager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * This class represents an upload book screen,
 * the user can to upload a book with/without image and write information about the book, like:
 * Book name, author, category, num of pages, condition and for change/for donate.
 */

public class UploadScreen extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    EditText book_name;
    EditText author_name;
    String category;
    EditText num_pages;
    int book_cond;
    RadioGroup radio_group;
    boolean for_change;
    Spinner category_spinner, cond_spinner;
    Button add;
    ImageButton image_btn;
    Uri image_uri;

    private FirebaseAuth cAuth;
    private DatabaseReference bookRef;
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_screen);
        this.initSpinners();

        radio_group = findViewById(R.id.add_radio_group);

        add = findViewById(R.id.add_button);
        add.setOnClickListener(this);

        book_name = (EditText) findViewById(R.id.add_book_name);
        author_name = (EditText) findViewById(R.id.add_book_author);
        num_pages = (EditText) findViewById(R.id.add_num_pages);

        //image button
        image_btn = findViewById(R.id.camera_btn);
        image_btn.setOnClickListener(this);

        cAuth = FirebaseAuth.getInstance();
        bookRef = FirebaseDatabase.getInstance().getReference("Books");
        storageRef = FirebaseStorage.getInstance().getReference();

    }

    /*
    function that initializing the category and condition spinners
     */
    public void initSpinners() {
        // Category spinner
        category_spinner = (Spinner) findViewById(R.id.category_spinner);

        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(UploadScreen.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.category));

        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(category_adapter);


        // Consition spinner
        cond_spinner = (Spinner) findViewById(R.id.cond_spinner);

        ArrayAdapter<String> cond_adapter = new ArrayAdapter<>(UploadScreen.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.מצב));

        cond_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cond_spinner.setAdapter(cond_adapter);
    }

    /*
    function that cast the condition string to integer number
     */
    public int condition_casting() {
        int ans = -1;
        switch (cond_spinner.getSelectedItem().toString()) {
            case "כמו חדש":
                ans = 0;
                break;
            case "משומש":
                ans = 1;
                break;
            case "מעט קרוע":
                ans = 2;
                break;
            default:
                ans = 1;
                break;
        }
        return ans;
    }

    @Override
    public void onClick(View v) {
        // image button pressed - get permissions for user camera
        if (v == image_btn){
            // request runtime permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                   checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //permission not enabled, request it
                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    //show popup to request permissions
                    requestPermissions(permission, PERMISSION_CODE); //request for permission
                }else{
                    //permission already granted
                    openCamera();
                }
            }
        }else { // the user pressed on upload, execute check for data inserted

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String bookName = book_name.getText().toString();
            String autohrName = author_name.getText().toString();
            String numPages = num_pages.getText().toString();

            category = category_spinner.getSelectedItem().toString();
            book_cond = condition_casting();

            int radio_id = radio_group.getCheckedRadioButtonId();
            RadioButton radio_button = findViewById(radio_id);

            // validation checks
            if (bookName.isEmpty()) {
                book_name.setError("Book name is required!");
                book_name.requestFocus();
                return;
            }
            if (autohrName.isEmpty()) {
                author_name.setError("Author name is required!");
                author_name.requestFocus();
                return;
            }
            if (numPages.isEmpty()) {
                num_pages.setError("Number of pages is required!");
                num_pages.requestFocus();
                return;
            }
            if (radio_button == null) {
                Toast.makeText(UploadScreen.this, "You must choose change or donate!", Toast.LENGTH_LONG).show();
                return;
            }
            if (radio_button.getId() == R.id.change_radio_button) {
                for_change = true;
            } else if (radio_button.getId() == R.id.donate_radio_button) {
                for_change = false;
            }

            final String book_id = bookRef.child(category).push().getKey();// get book id for unique key
            DonateCheck();
            BuildAndAddBook(bookName, autohrName, numPages, user, book_id);
        }
    }

    /*
    function that check if the inserted book is for donate and if it is - update the 'number of donated book' field in managers tree
     */
    private void DonateCheck() {
        if(!for_change){   // donated
            final DatabaseReference numRef = FirebaseDatabase.getInstance().getReference().child("ManagerTools").child("num_of_books_donated");
            numRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer i = snapshot.getValue(Integer.class);
                    if (i != null) {
                        numRef.setValue(++i);         // the num of books is ++, because we added a book.
                    }
                    else{
                        numRef.setValue(1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    /*
    function that initializing the camera of the user to be ready for take a picture
     */
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera();
                }else{
                    //permission from popup was denied
                    Toast.makeText(UploadScreen.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /*
    function that set the image of the user inside the image view
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //set the image captured to our imageButton
            image_btn.setImageURI(image_uri);
        }
    }

    /*
    function that update the new inserted book into the firebase, and check if the user is client or manager
     */
    private void addBookToDB(final DatabaseReference managerRoot, final DatabaseReference clientRoot,
                             final String uid, final String book_id,final DatabaseReference book_ref, final Book new_book){
            clientRoot.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Client client = snapshot.getValue(Client.class);
                    if (client != null) {       // its client
                        book_ref.child("cityOwner").setValue(client.getCity());
                        client.getMy_books().add(book_id);
                        clientRoot.child(uid).setValue(client);
                        bookRef.child(new_book.getCategory()).child(book_id).child("book_id").setValue(book_id);
                        Toast.makeText(UploadScreen.this, "Your Book upload successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UploadScreen.this, CustomerMenu.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UploadScreen.this, "Something was wrong!", Toast.LENGTH_LONG);
                }
            });
            managerRoot.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Manager manager = snapshot.getValue(Manager.class);
                    if (manager != null) {      //its manager
                        book_ref.child("cityOwner").setValue(manager.getCity());
                        manager.getMy_books().add(book_id);
                        managerRoot.child(uid).setValue(manager);
                        bookRef.child(new_book.getCategory()).child(book_id).child("book_id").setValue(book_id);
                        Toast.makeText(UploadScreen.this, "Your Book upload successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UploadScreen.this, ManagerMenu.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UploadScreen.this, "Something was wrong!", Toast.LENGTH_LONG).show();
                }
            });
        }

    /*
       function that insert the new book and the new image the user took(if he took) to the firebase,
       include to the user books list

     */
    private void BuildAndAddBook(final String bookName, final String autohrName, final String numPages, final FirebaseUser user, final String book_id) {
        if (image_uri != null) {
            final StorageReference fileRef = storageRef.child(book_id + "." + getFileExtension(image_uri));// insert to image the firebase storage
            fileRef.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            final Book new_book = new Book(bookName, category, autohrName, Integer.parseInt(numPages), book_cond, for_change, user.getUid(), true);

                            bookRef.child(new_book.getCategory()).child(book_id).setValue(new_book).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        DatabaseReference managerRoot = FirebaseDatabase.getInstance().getReference("Users").child("Managers");
                                        DatabaseReference clientRoot = FirebaseDatabase.getInstance().getReference("Users").child("Clients");
                                        addBookToDB(managerRoot, clientRoot, cAuth.getCurrentUser().getUid(), book_id,bookRef.child(new_book.getCategory()).child(book_id), new_book);

                                    } else {
                                        Toast.makeText(UploadScreen.this,
                                                "Failed to upload! Try again!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadScreen.this, "Uploading Failed !!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{ // the user don't took a picture
            final Book new_book = new Book(bookName, category, autohrName, Integer.parseInt(numPages), book_cond, for_change, user.getUid(), false);

            bookRef.child(new_book.getCategory()).child(book_id).setValue(new_book).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        DatabaseReference managerRoot = FirebaseDatabase.getInstance().getReference("Users").child("Managers");
                        DatabaseReference clientRoot = FirebaseDatabase.getInstance().getReference("Users").child("Clients");
                        addBookToDB(managerRoot, clientRoot, cAuth.getCurrentUser().getUid(), book_id,bookRef.child(new_book.getCategory()).child(book_id),new_book);

                    } else {
                        Toast.makeText(UploadScreen.this,
                                "Failed to upload! Try again!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /*
    function that extract the type of the file
     */
    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}
