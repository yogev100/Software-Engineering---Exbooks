package com.example.exbooks.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.exbooks.R;

/**
 * This class represents the search screen.
 * With that screen the user can filter his search.
 */
public class SearchScreen extends AppCompatActivity implements View.OnClickListener {
    public String category;
    CheckBox roman,metach,bio,cooking,fantasy,children,horror,history,religous,politics,parenting,educational;
    TextView bookName;
    TextView startPage,endPage;
    CheckBox newCond,usedCond,tornCond;
    TextView authorView;
    TextView citySearch;
    Button search;
    CheckBox[] c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        roman=(CheckBox)findViewById(R.id.Roman_checkBox);
        metach=(CheckBox)findViewById(R.id.Metach_checkBox);
        bio=(CheckBox)findViewById(R.id.Biography_checkBox);
        cooking=(CheckBox)findViewById(R.id.cooking_checkBox);
        fantasy=(CheckBox)findViewById(R.id.fantasy_checkBox);
        children=(CheckBox)findViewById(R.id.Child_checkBox);
        horror=(CheckBox)findViewById(R.id.horror_checkBox);
        history=(CheckBox)findViewById(R.id.history_checkBox);
        religous=(CheckBox)findViewById(R.id.religous_checkBox);
        politics=(CheckBox)findViewById(R.id.politics_checkBox);
        parenting=(CheckBox)findViewById(R.id.parenting_checkBox);
        educational=(CheckBox)findViewById(R.id.educational_checkBox);

        bookName=(TextView)findViewById(R.id.bookName_Textview);
        startPage=(TextView)findViewById(R.id.startBookPage_textview);
        endPage=(TextView)findViewById(R.id.endBookPage_textview);

        newCond=(CheckBox)findViewById(R.id.newCond_checkBox);
        usedCond=(CheckBox)findViewById(R.id.usedCond_checkBox);
        tornCond=(CheckBox)findViewById(R.id.tornCond_checkBox);
        c=new CheckBox[]{roman,metach,bio,cooking,fantasy,children,horror,history,religous,politics,parenting,educational};
        authorView=(TextView)findViewById(R.id.authorBookId);
        citySearch=(TextView)findViewById(R.id.citySearch_textView);

        search=(Button)findViewById(R.id.search_button);
        search.setOnClickListener(this);
    }

    /*
    when the user click on the search button - we check all the inserted data,
    if some of the data incorrect - he gets error message
    else - he will navigate to the result screen
     */
    @Override
    public void onClick(View view) {
        String bkNm=bookName.getText().toString().trim();
        String strtPg=startPage.getText().toString().trim();
        String endPg=endPage.getText().toString().trim();
        String athr=authorView.getText().toString().trim();
        String cty=citySearch.getText().toString().trim();


        if(view==search){
            if(allempty()&&bkNm.isEmpty()&&strtPg.isEmpty()&&endPg.isEmpty()&&athr.isEmpty()&&cty.isEmpty()){
                bookName.setError("Please fill at least one of the filters");
                bookName.requestFocus();
                return;
            }
            if((strtPg.isEmpty()&&(!endPg.isEmpty()))||((!strtPg.isEmpty())&&endPg.isEmpty())){
                startPage.setError("please fill the full range of pages");
                startPage.requestFocus();
                endPage.setError("please fill the full range of pages");
                endPage.requestFocus();
                return;
            }
            if(!newCond.isChecked()&&!usedCond.isChecked()&&!tornCond.isChecked()){
                newCond.setError("Please fill one of the condition check box");
                newCond.requestFocus();
                usedCond.setError("Please fill one of the condition check box");
                usedCond.requestFocus();
                tornCond.setError("Please fill one of the condition check box");
                tornCond.requestFocus();
                return;
            }
            int start_page = 0;
            int end_page = 5000;
            if( !strtPg.isEmpty() && !endPg.isEmpty()) {
                start_page = Integer.parseInt(strtPg);
                end_page = Integer.parseInt(endPg);
            }

            // we pass all the inserted data to the result screen for filtering
            Intent intent=new Intent(SearchScreen.this,SearchResultScreen.class);
            intent.putExtra("romnanC",roman.isChecked()).putExtra("metachC",metach.isChecked()).putExtra("biographyC",bio.isChecked())
                    .putExtra("cookingC",cooking.isChecked()).putExtra("fantasyC",fantasy.isChecked()).putExtra("childrenC",children.isChecked())
                    .putExtra("horrorC",horror.isChecked()).putExtra("historyC",history.isChecked()).putExtra("religousC",religous.isChecked())
                    .putExtra("politicsC",politics.isChecked()).putExtra("parenting",parenting.isChecked()).putExtra("eduacationalC",educational.isChecked())
                    .putExtra("bookName",bkNm).putExtra("startPage",start_page).putExtra("endPage",end_page)
                    .putExtra("newCondC",newCond.isChecked()).putExtra("usedCondC",usedCond.isChecked()).putExtra("tornCondC",tornCond.isChecked())
                    .putExtra("authorText",athr).putExtra("citySearchText",cty);
            startActivity(intent);
        }

    }

    /*
    function that return true if all the category does not checked
     */
    private boolean allempty(){
        for(int i=0;i<c.length;i++){
            if(c[i].isChecked()) return false;
        }
        return true;
    }

}