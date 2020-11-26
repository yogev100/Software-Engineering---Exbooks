package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbooks.Objects.Book;
import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.example.exbooks.Users.Manager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UploadScreen extends AppCompatActivity implements View.OnClickListener{

    EditText book_name;
    EditText author_name;
    String category;
    EditText num_pages;
    int book_cond;
    RadioGroup radio_group;
    boolean for_change;
    Spinner category_spinner, cond_spinner;
    Button add;
    Button capture_btn;
    ImageView image_btn;

    private FirebaseAuth cAuth;
    private DatabaseReference bookRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_screen);
        this.initSpinners();

        radio_group = findViewById(R.id.add_radio_group);

        add = findViewById(R.id.add_button);
        add.setOnClickListener(this);

        book_name = (EditText)findViewById(R.id.add_book_name);
        author_name = (EditText)findViewById(R.id.add_book_author);
        num_pages = (EditText)findViewById(R.id.add_num_pages);

        cAuth = FirebaseAuth.getInstance();
        bookRef= FirebaseDatabase.getInstance().getReference("Books");

    }

    public void initSpinners(){
        category_spinner = (Spinner) findViewById(R.id.category_spinner);

        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(UploadScreen.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.קטגוריה));

        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(category_adapter);

        cond_spinner = (Spinner) findViewById(R.id.cond_spinner);

        ArrayAdapter<String> cond_adapter = new ArrayAdapter<>(UploadScreen.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.מצב));

        cond_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cond_spinner.setAdapter(cond_adapter);
    }

    public int condition_casting(){
        int ans = -1;
        switch (cond_spinner.getSelectedItem().toString()){
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(user.getUid());

        String bookName = book_name.getText().toString();
        String autohrName = author_name.getText().toString();
        String numPages = num_pages.getText().toString();

        category = category_spinner.getSelectedItem().toString();
        book_cond = condition_casting();

        int radio_id = radio_group.getCheckedRadioButtonId();
        RadioButton radio_button = findViewById(radio_id);

        if(bookName.isEmpty()){
            book_name.setError("Book name is required!");
            book_name.requestFocus();
            return;
        }
        if(autohrName.isEmpty()){
            author_name.setError("Author name is required!");
            author_name.requestFocus();
            return;
        }
        if(numPages.isEmpty()){
            num_pages.setError("Number of pages is required!");
            num_pages.requestFocus();
            return;
        }
        if(radio_button ==null){
            Toast.makeText(UploadScreen.this,"You must choose change or donate!",Toast.LENGTH_LONG).show();
            return;
        }
        if (radio_button.getId() == R.id.change_radio_button) {
            for_change = true;
        } else if (radio_button.getId() == R.id.change_radio_button) {
            for_change = false;
        }

        Book new_book = new Book(bookName, category, autohrName, Integer.parseInt(numPages), book_cond, for_change, user.getUid());
        final String book_id = bookRef.child(new_book.getCategory()).push().getKey();

        bookRef.child(new_book.getCategory()).child(book_id).setValue(new_book).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    DatabaseReference managerRoot = FirebaseDatabase.getInstance().getReference("Users").child("Managers");
                    DatabaseReference clientRoot = FirebaseDatabase.getInstance().getReference("Users").child("Clients");
                    addBookToDB(managerRoot, clientRoot, cAuth.getCurrentUser().getUid(), book_id);
//                   FirebaseDatabase.getInstance().getReference("Users").child("Clients");
                    Toast.makeText(UploadScreen.this,"Your Book upload successfully",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(UploadScreen.this, ProfileScreen.class));
                    finish();
                }else{
                    Toast.makeText(UploadScreen.this,
                            "Failed to upload! Try again!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void addBookToDB(final DatabaseReference managerRoot, final DatabaseReference clientRoot, final String uid, final String book_id) {
        clientRoot.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client client = snapshot.getValue(Client.class);

                if(client!=null){
                    client.getMy_books().add(book_id);
                    clientRoot.child(uid).setValue(client);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UploadScreen.this,"Something was wrong!",Toast.LENGTH_LONG);
            }
        });
        managerRoot.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manager manager = snapshot.getValue(Manager.class);

                if(manager!=null){
                    manager.getMy_books().add(book_id);
                    managerRoot.child(uid).setValue(manager);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UploadScreen.this,"Something was wrong!",Toast.LENGTH_LONG);
            }
        });
    }
}