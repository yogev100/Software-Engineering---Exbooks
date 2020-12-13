package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.exbooks.Objects.Book;
import com.example.exbooks.Adapters.BookFormAdapter;
import com.example.exbooks.R;
import com.example.exbooks.Users.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

/**
 * This class represents the results of the search books screen
 * with that class the user can see the books that he find
 */

public class SearchResultScreen extends AppCompatActivity implements View.OnClickListener{

    Boolean roman,metach,bio,cooking,fantasy,children,horror,history,religous,politics,parenting,educational;
    String bookName;
    int startPage,endPage;
    Boolean newCond,usedCond,tornCond;
    String bookAuthor;
    String citySearch;
    ArrayList<Book> bookModels;
    ListView listView;
    private static BookFormAdapter adapter;
    DatabaseReference books_ref;
    Button menu_btn;
    boolean is_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_screen);
        isClient(); // check client or manager
        VarInit(); // initializing all the variables

        menu_btn = (Button)findViewById(R.id.backToMenuFromSearch_Button);
        menu_btn.setOnClickListener(this);
        listView=(ListView)findViewById(R.id.list_book_form);
        bookModels=new ArrayList<>();
        try {
            FindCorrectBooks(); // extract from firebase all the correct book to the user search
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    function that get the values from the previous intent and initializing all the variables
     */
    public void VarInit(){
        Intent intent=getIntent();
        roman = intent.getBooleanExtra("romnanC",false);
        metach = intent.getBooleanExtra("metachC",false);
        bio = intent.getBooleanExtra("biographyC",false);
        cooking = intent.getBooleanExtra("cookingC",false);
        fantasy = intent.getBooleanExtra("fantasyC",false);
        children = intent.getBooleanExtra("childrenC",false);
        horror = intent.getBooleanExtra("horrorC",false);
        history = intent.getBooleanExtra("historyC",false);
        religous = intent.getBooleanExtra("religousC",false);
        politics = intent.getBooleanExtra("politicsC",false);
        parenting = intent.getBooleanExtra("parenting",false);
        educational = intent.getBooleanExtra("eduacationalC",false);

        bookName=intent.getStringExtra("bookName");
        startPage=intent.getIntExtra("startPage",0);
        endPage=intent.getIntExtra("endPage",10000);
        newCond = intent.getBooleanExtra("newCondC",false);
        usedCond = intent.getBooleanExtra("usedCondC",false);
        tornCond = intent.getBooleanExtra("tornCondC",false);

        bookAuthor=intent.getStringExtra("authorText");
        citySearch=intent.getStringExtra("citySearchText");

        books_ref = FirebaseDatabase.getInstance().getReference("Books");
    }

    /*
    function that extract the correct books from firebase depend on the user filtering
     */
    private void FindCorrectBooks() throws InterruptedException {
        books_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // go through all the books in which category
                for (DataSnapshot category:snapshot.getChildren()){
                    if(checkCategory(category.getKey())) {
                        for (DataSnapshot book : category.getChildren()) {
                            Book b=book.getValue(Book.class);
                            if(b!=null&&b.isFor_change()){ // add the book to the list
                                bookModels.add(new Book(b));
                            }
                        }
                    }
                }
                //a loop that filtering the current list by remove all the unwanted books
                for(int i=0; i<bookModels.size(); i++){

                    if(bookModels.get(i).getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        bookModels.remove(i);
                        i--;
                        continue;
                    }
                    if(!bookName.isEmpty()){
                        if(!isSubstring(bookModels.get(i).getBook_name(),bookName)){
                            bookModels.remove(i);
                            i--;
                            continue;
                        }
                    }
                    if(!bookAuthor.isEmpty()){
                        if(!isSubstring(bookModels.get(i).getAuthor_name(),bookAuthor)){
                            bookModels.remove(i);
                            i--;
                            continue;
                        }
                    }
                    if(!citySearch.isEmpty()){
                        if(!citySearch.equals(bookModels.get(i).getCityOwner())){
                            bookModels.remove(i);
                            i--;
                            continue;
                        }
                    }
                    if(!(startPage<= bookModels.get(i).getNum_pages() && bookModels.get(i).getNum_pages() <= endPage)){
                        bookModels.remove(i);
                        i--;
                        continue;
                    }

                    if(!newCond){
                        if(bookModels.get(i).condString().equals("New book")){
                            bookModels.remove(i);
                            i--;
                            continue;
                        }
                    }
                    if(!usedCond){
                        if(bookModels.get(i).condString().equals("Used Book")){
                            bookModels.remove(i);

                            i--;
                            continue;
                        }
                    }
                    if(!tornCond){
                        if(bookModels.get(i).condString().equals("Little Torn")){
                            bookModels.remove(i);

                            i--;
                            continue;
                        }
                    }
                }
                // set the adapter to the list for show all of them in custom layout
                adapter=new BookFormAdapter(bookModels,getApplicationContext(),"search",getSupportFragmentManager());
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*
    side function that return true when the category is marked,
    otherwise - return false
     */
    private boolean checkCategory(String category) {
        if(category.equals("Roman")&&roman){
            return true;
        }
        else if(category.equals("Thriller and action")&&metach){
            return true;
        }
        else if(category.equals("Biography")&&bio){
            return true;
        }
        else if(category.equals("Cooking")&&cooking){
            return true;
        }
        else if(category.equals("Science fiction and fantasy")&&fantasy){
            return true;
        }
        else if(category.equals("Children and teenager")&&children){
            return true;
        }
        else if(category.equals("Horror")&&horror){
            return true;
        }
        else if(category.equals("History")&&history){
            return true;
        }
        else if(category.equals("Religion")&&religous){
            return true;
        }
        else if(category.equals("Politics")&&politics){
            return true;
        }
        else if(category.equals("Parenting")&&parenting){
            return true;
        }
        else if(category.equals("Educational")&&educational){
            return true;
        }
        return false;
    }

    /*
    side function that return true when the second string is substring of the first string,
    otherwise - return false
     */
    private boolean isSubstring(final String i_StringForSearch, final String i_SubStringToFind) {
        int j = 0;
        for (int i = 0; i < i_StringForSearch.length(); i++) {
            if (i_StringForSearch.charAt(i) == i_SubStringToFind.charAt(j)) {
                j++;
                if (j == i_SubStringToFind.length()) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    function that navigate the user to the correct menu screen depend on the user type (client or manager)
     */
    @Override
    public void onClick(View v) {
        if (v == menu_btn){
            if(is_client){
                startActivity(new Intent(SearchResultScreen.this, CustomerMenu.class));
                finish();
            }
            else{
                startActivity(new Intent(SearchResultScreen.this, ManagerMenu.class));
                finish();
            }
        }
    }

    /*
    function that assign to the 'is_client' variable true when the current user is client,
    otherwise - assign false
     */
    private void isClient() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        DatabaseReference ClientRoot = FirebaseDatabase.getInstance().getReference("Users").child("Clients");
        ClientRoot.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client client = snapshot.getValue(Client.class);
                if (client != null) {
                    is_client = true;
                }
                else{
                    is_client = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchResultScreen.this, "Something was wrong!", Toast.LENGTH_LONG).show();
            }
        });

    }
}
