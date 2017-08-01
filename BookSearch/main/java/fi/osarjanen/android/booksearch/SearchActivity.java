package fi.osarjanen.android.booksearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class SearchActivity extends AppCompatActivity {

    private EditText edIsbn;
    private EditText edTitle;
    private EditText edAuthor;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /*
    handle our custom menu button press
    open new activity and pass built url from edit texts
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemID = item.getItemId();
        if(itemID == R.id.action_search) {
            Intent search = new Intent(SearchActivity.this, ResultActivity.class);
            search.putExtra("URL", buildUrlString());
            startActivity(search);
            return true;
        }

        else return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        edIsbn = (EditText) findViewById(R.id.isbn_edit_text);
        edTitle = (EditText) findViewById(R.id.title_edit_text);
        edAuthor = (EditText) findViewById(R.id.author_edit_text);

    }

    /**
     * Building url for query
     * @return built url base + queries and replace white spaces with %20. also extract final plus character
     */
    private String buildUrlString() {
        String finalString = "https://www.googleapis.com/books/v1/volumes?q=";
        String isbn = edIsbn.getText().toString();
        String title = edTitle.getText().toString();
        String author = edAuthor.getText().toString();

        if (!title.equals("")) {
            finalString += title + "+";
        }

        if (!isbn.equals("")) {
            finalString += "isbn:" + isbn + "+";
        }


        if (!author.equals("")) {
            finalString += "inauthor:" + author + "+";
        }

        //return error if no search values given
        if(isbn.equals("") && title.equals("") && author.equals("")) {
            return "Empty";
        } else return finalString.substring(0,finalString.length()-1).replaceAll(" ", "%20");
    }


}
