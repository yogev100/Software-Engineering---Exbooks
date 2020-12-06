package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbooks.Objects.Book;
import com.example.exbooks.Objects.BookFormAdapter;
import com.example.exbooks.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SearchResultScreen extends AppCompatActivity {

    ScrollView sv;

    Boolean roman,metach,bio,cooking,fantasy,children,horror,history,religous,politics,parenting,educational;
    String bookName;
    int startPage,endPage;
    Boolean newCond,usedCond,tornCond;
    String bookAuthor;
    String freeSearch;
    ArrayList<Book> bookModels;
    ArrayList<Book> temp;
    ListView listView;
    private static BookFormAdapter adapter;
    DatabaseReference books_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_screen);

        VarInit();


        listView=(ListView)findViewById(R.id.list_book_form);
        bookModels=new ArrayList<>();
        FindCorrectBooks();
//        bookModels.add(new Book("מה אומר","בעונה","בגבג",123,1,true,"asdfasdf2sadf",false));
//        bookModels.add(new Book("51asdf","Holut","assdfsddfg",123,2,true,"asdfasdf2sadf",false));
//        bookModels.add(new Book("dd","kaki","eee",123,2,true,"asdfasdf2sadf",false));
//        bookModels.add(new Book("fff","pipi","aswwwdfg",123,1,true,"asdfasdf2sadf",false));
//        bookModels.add(new Book("sdfs","shlsul","qqqq",500,0,true,"asdfasdf2sadf",false));
//        bookModels.add(new Book("ccc","mama","ffff",500,2,true,"asdfasdf2sadf",false));
//        bookModels.add(new Book("kjmhj","nana","gssgs",500,1,true,"asdfasdf2sadf",false));

        adapter=new BookFormAdapter(bookModels,getApplicationContext());


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book dataModel= bookModels.get(position);
                Toast.makeText(SearchResultScreen.this, "you clic on an item", Toast.LENGTH_LONG).show();
//                Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();

            }
        });



    }

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

        System.out.println("roman:"+roman);
        bookName=intent.getStringExtra("bookName");
        startPage=intent.getIntExtra("startPage",0);
        endPage=intent.getIntExtra("endPage",10000);
        System.out.println("start page:"+startPage);
        System.out.println("end page:"+endPage);
        newCond = intent.getBooleanExtra("newCondC",false);
        usedCond = intent.getBooleanExtra("usedCondC",false);
        tornCond = intent.getBooleanExtra("tornCondC",false);

        bookAuthor=intent.getStringExtra("authorText");
        freeSearch=intent.getStringExtra("freeSearchText");

        books_ref = FirebaseDatabase.getInstance().getReference("Books");
        System.out.println(bookName.isEmpty());
    }

    private void FindCorrectBooks() {
        temp = new ArrayList<>();
        if(roman){
            books_ref.child("רומן").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s: snapshot.getChildren()){
                        Book book = s.getValue(Book.class);
                        if(book != null && book.isFor_change()) {
                            System.out.println(book.getBook_name());
                            bookModels.add(new Book(book));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(metach){
            books_ref.child("מתח ופעולה").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s: snapshot.getChildren()){
                        Book book = s.getValue(Book.class);
                        if(book != null && book.isFor_change()) {
//                            bookModels.add(book);
                            bookModels.add(new Book(book));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(bio){
            books_ref.child("ביוגרפיה").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s: snapshot.getChildren()){
                        Book book = s.getValue(Book.class);
                        if(book != null && book.isFor_change()) {
//                            bookModels.add(book);
                            bookModels.add(new Book(book));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(cooking){
            books_ref.child("בישול").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s: snapshot.getChildren()){
                        Book book = s.getValue(Book.class);
                        if(book != null && book.isFor_change()) {
//                            bookModels.add(book);
                            bookModels.add(new Book(book));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(fantasy){
            books_ref.child("מדע בדיוני ופנטזיה").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s: snapshot.getChildren()){
                        Book book = s.getValue(Book.class);
                        if(book != null && book.isFor_change()) {
                            //                            bookModels.add(book);
                            bookModels.add(new Book(book));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(children){
            books_ref.child("ילדים ונוער").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s: snapshot.getChildren()){
                        Book book = s.getValue(Book.class);
                        if(book != null && book.isFor_change()) {
                            bookModels.add(new Book(book));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(horror){
            books_ref.child("אימה").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s: snapshot.getChildren()){
                        Book book = s.getValue(Book.class);
                        if(book != null && book.isFor_change()) {
                            bookModels.add(new Book(book));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(history){
            books_ref.child("היסטוריה").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s: snapshot.getChildren()){
                        Book book = s.getValue(Book.class);
                        if(book != null && book.isFor_change()) {
                            bookModels.add(new Book(book));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(religous){
            books_ref.child("יהדות").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s: snapshot.getChildren()){
                        Book book = s.getValue(Book.class);
                        if(book != null && book.isFor_change()) {
                            bookModels.add(new Book(book));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(politics){
            books_ref.child("פוליטיקה").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s: snapshot.getChildren()){
                        Book book = s.getValue(Book.class);
                        if(book != null && book.isFor_change()) {
                            bookModels.add(new Book(book));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(parenting){
            books_ref.child("הורות").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s: snapshot.getChildren()){
                        Book book = s.getValue(Book.class);
                        if(book != null && book.isFor_change()) {
                            bookModels.add(new Book(book));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(educational){
            books_ref.child("לימוד").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s: snapshot.getChildren()){
                        Book book = s.getValue(Book.class);
                        if(book != null && book.isFor_change()) {
                            bookModels.add(new Book(book));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
//        bookModels.add(new Book("מה אומר","בעונה","בגבג",123,1,true,"asdfasdf2sadf",false));
//        bookModels.remove(bookModels.size()-1);
        System.out.println("before" + bookModels.size());
        for(int i=0; i<bookModels.size(); i++){
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
            if(!(startPage<= bookModels.get(i).getNum_pages() && bookModels.get(i).getNum_pages() <= endPage)){
                bookModels.remove(i);
                i--;
                continue;
            }

//            if(newCond){
//               if(!bookModels.get(i).condString().equals("New book")){
//                   bookModels.remove(i);
//                   i--;
//                   continue;
//               }
//            }
//            if(usedCond){
//                if(!bookModels.get(i).condString().equals("Used Book")){
//                    bookModels.remove(i);
//                    i--;
//                    continue;
//                }
//            }
//            if(tornCond){
//                if(!bookModels.get(i).condString().equals("Little Torn")){
//                    bookModels.remove(i);
//                    i--;
//                    continue;
//                }
//            }
        }
        System.out.println("after" + bookModels.size());
        for( int i=0; i<bookModels.size(); i++){
            System.out.println(bookModels.get(i).getBook_name());
        }

//        bookModels = temp;

    }

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
}
