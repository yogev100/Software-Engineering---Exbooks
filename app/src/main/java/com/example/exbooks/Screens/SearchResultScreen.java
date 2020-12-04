package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.ConstraintsChangedListener;

import com.example.exbooks.Objects.Book;
import com.example.exbooks.Objects.BookFormAdapter;
import com.example.exbooks.R;

import java.util.ArrayList;


public class SearchResultScreen extends AppCompatActivity {

    ScrollView sv;

    Boolean roman,metach,bio,cooking,fantasy,children,horror,history,religous,politics,parenting,educational;
    String bookName;
    String startPage,endPage;
    Boolean newCond,usedCond,tornCond;
    String authorView;
    String freeSearch;
    ArrayList<Book> bookModels;
    ListView listView;
    private static BookFormAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_screen);
        VarInit();


        listView=(ListView)findViewById(R.id.list);
        bookModels=new ArrayList<>();
        bookModels.add(new Book("asdfasdf","Horror","asdfg",123,1,true,"asdfasdf2sadf",false));
        bookModels.add(new Book("51asdf","Holut","assdfsddfg",123,2,true,"asdfasdf2sadf",false));
        bookModels.add(new Book("dd","kaki","eee",123,2,true,"asdfasdf2sadf",false));
        bookModels.add(new Book("fff","pipi","aswwwdfg",123,1,true,"asdfasdf2sadf",false));
        bookModels.add(new Book("sdfs","shlsul","qqqq",500,0,true,"asdfasdf2sadf",false));
        bookModels.add(new Book("ccc","mama","ffff",500,2,true,"asdfasdf2sadf",false));
        bookModels.add(new Book("kjmhj","nana","gssgs",500,1,true,"asdfasdf2sadf",false));

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


//        sv=(ScrollView)findViewById(R.id.search_scrollView);

//        ConstraintLayout c = (ConstraintLayout)findViewById(R.id.bookForm_Layout);
//        sv.addView(c);


        //for
//        LinearLayout newBook=(LinearLayout)findViewById(R.id.search_scrollView2);
//        ConstraintSet setNew = new ConstraintSet();
//        setNew.clone(newBook);

        //Button 1:
//        Button button = new Button(this);
//        newBook.addView(button);
//        setNew.connect(button.getId(), ConstraintSet.LEFT, newBook.getId(), ConstraintSet.LEFT, 0);
//        setNew.connect(button.getId(), ConstraintSet.RIGHT, newBook.getId(), ConstraintSet.RIGHT, 0);
//        setNew.connect(button.getId(), ConstraintSet.BOTTOM, newBook.getId(), ConstraintSet.BOTTOM, 0);
//        setNew.constrainWidth(button.getId(), ConstraintSet.MATCH_CONSTRAINT);
//        setNew.constrainHeight(button.getId(), 200);
//        setNew.applyTo(newBook);


        //Textview Name
//        TextView nmtxtview = new TextView(this);
//        nmtxtview.setText("Harry Potter");
//        newBook.addView(nmtxtview);
//        setNew.connect(nmtxtview.getId(), ConstraintSet.LEFT, newBook.getId(), ConstraintSet.LEFT, 0);
//        setNew.connect(nmtxtview.getId(), ConstraintSet.RIGHT, newBook.getId(), ConstraintSet.RIGHT, 0);
//        setNew.connect(nmtxtview.getId(), ConstraintSet.BOTTOM, newBook.getId(), ConstraintSet.BOTTOM, 0);
//        setNew.constrainWidth(nmtxtview.getId(), ConstraintSet.MATCH_CONSTRAINT);
//        setNew.constrainHeight(nmtxtview.getId(), 200);
//        setNew.applyTo(newBook);

        //Textview Category
//        TextView ctgrynme = new TextView(this);
//        ctgrynme.setText("Horror");
//        newBook.addView(ctgrynme);
//        setNew.connect(ctgrynme.getId(), ConstraintSet.LEFT, newBook.getId(), ConstraintSet.LEFT, 0);
//        setNew.connect(ctgrynme.getId(), ConstraintSet.RIGHT, newBook.getId(), ConstraintSet.RIGHT, 0);
//        setNew.connect(ctgrynme.getId(), ConstraintSet.BOTTOM, newBook.getId(), ConstraintSet.BOTTOM, 0);
//        setNew.constrainWidth(ctgrynme.getId(), ConstraintSet.MATCH_CONSTRAINT);
//        setNew.constrainHeight(ctgrynme.getId(), 200);
//        setNew.applyTo(newBook);

        //Textview Category
//        TextView athr = new TextView(this);
//        athr.setText("JK Rollin");
//        newBook.addView(athr);
//        setNew.connect(athr.getId(), ConstraintSet.LEFT, newBook.getId(), ConstraintSet.LEFT, 0);
//        setNew.connect(athr.getId(), ConstraintSet.RIGHT, newBook.getId(), ConstraintSet.RIGHT, 0);
//        setNew.connect(athr.getId(), ConstraintSet.BOTTOM, newBook.getId(), ConstraintSet.BOTTOM, 0);
//        setNew.constrainWidth(athr.getId(), ConstraintSet.MATCH_CONSTRAINT);
//        setNew.constrainHeight(athr.getId(), 200);
//        setNew.applyTo(newBook);

        //Textview Condition
//        TextView cnd = new TextView(this);
//        cnd.setText("Used");
//        newBook.addView(cnd);
//        setNew.connect(cnd.getId(), ConstraintSet.LEFT, newBook.getId(), ConstraintSet.LEFT, 0);
//        setNew.connect(cnd.getId(), ConstraintSet.RIGHT, newBook.getId(), ConstraintSet.RIGHT, 0);
//        setNew.connect(cnd.getId(), ConstraintSet.BOTTOM, newBook.getId(), ConstraintSet.BOTTOM, 0);
//        setNew.constrainWidth(cnd.getId(), ConstraintSet.MATCH_CONSTRAINT);
//        setNew.constrainHeight(cnd.getId(), 200);
//        setNew.applyTo(newBook);

        //Textview Category
//        TextView city = new TextView(this);
//        city.setText("Netanya");
//        newBook.addView(city);
//        setNew.connect(city.getId(), ConstraintSet.LEFT, newBook.getId(), ConstraintSet.LEFT, 0);
//        setNew.connect(city.getId(), ConstraintSet.RIGHT, newBook.getId(), ConstraintSet.RIGHT, 0);
//        setNew.connect(city.getId(), ConstraintSet.BOTTOM, newBook.getId(), ConstraintSet.BOTTOM, 0);
//        setNew.constrainWidth(city.getId(), ConstraintSet.MATCH_CONSTRAINT);
//        setNew.constrainHeight(city.getId(), 200);
//        setNew.applyTo(newBook);

//        sv.addView(newBook);
        //end-fors




    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R., menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
        startPage=intent.getStringExtra("startPage");
        endPage=intent.getStringExtra("endPage");

        newCond = intent.getBooleanExtra("newCondC",false);
        usedCond = intent.getBooleanExtra("usedCondC",false);
        tornCond = intent.getBooleanExtra("tornCondC",false);

        authorView=intent.getStringExtra("authorText");
        freeSearch=intent.getStringExtra("freeSearchText");
    }
}
