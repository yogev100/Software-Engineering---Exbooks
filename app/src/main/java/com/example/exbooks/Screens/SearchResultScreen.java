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
        try {
            FindCorrectBooks();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        bookModels.add(new Book("מה אומר","בעונה","בגבג",123,1,true,"asdfasdf2sadf",false));
//        bookModels.add(new Book("51asdf","Holut","assdfsddfg",123,2,true,"asdfasdf2sadf",false));
//        bookModels.add(new Book("dd","kaki","eee",123,2,true,"asdfasdf2sadf",false));
//        bookModels.add(new Book("fff","pipi","aswwwdfg",123,1,true,"asdfasdf2sadf",false));
//        bookModels.add(new Book("sdfs","shlsul","qqqq",500,0,true,"asdfasdf2sadf",false));
//        bookModels.add(new Book("ccc","mama","ffff",500,2,true,"asdfasdf2sadf",false));
//        bookModels.add(new Book("kjmhj","nana","gssgs",500,1,true,"asdfasdf2sadf",false));





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

    private void FindCorrectBooks() throws InterruptedException {
        books_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot category:snapshot.getChildren()){
                    System.out.println(category.getKey());
                    if(checkCategory(category.getKey())) {
                        for (DataSnapshot book : category.getChildren()) {
                            Book b=book.getValue(Book.class);
                            if(b!=null&&b.isFor_change()){
                                System.out.println(b.getBook_name());
                                bookModels.add(new Book(b));
                            }
                        }
                    }
                }
                for(int i=0; i<bookModels.size(); i++){
                    if(!bookName.isEmpty()){
                        if(!isSubstring(bookModels.get(i).getBook_name(),bookName)){
                            bookModels.remove(i);
                            System.out.println("name@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

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
                System.out.println("Sise:"+bookModels.size());
                adapter=new BookFormAdapter(bookModels,getApplicationContext());
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean checkCategory(String category) {
        if(category.equals("רומן")&&roman){
            return true;
        }
        else if(category.equals("מתח ופעולה")&&metach){
            return true;
        }
        else if(category.equals("ביוגרפיה")&&bio){
            return true;
        }
        else if(category.equals("בישול")&&cooking){
            return true;
        }
        else if(category.equals("מדע בדיוני ופנטזיה")&&fantasy){
            return true;
        }
        else if(category.equals("ילדים ונוער")&&children){
            return true;
        }
        else if(category.equals("אימה")&&horror){
            return true;
        }
        else if(category.equals("היסטוריה")&&history){
            return true;
        }
        else if(category.equals("יהדות")&&religous){
            return true;
        }
        else if(category.equals("פוליטיקה")&&politics){
            return true;
        }
        else if(category.equals("הורות")&&parenting){
            return true;
        }
        else if(category.equals("לימוד")&&educational){
            return true;
        }
        return false;
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
