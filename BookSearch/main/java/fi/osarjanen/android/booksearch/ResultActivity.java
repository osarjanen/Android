package fi.osarjanen.android.booksearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import android.app.LoaderManager;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * List results in own activity
 */

public class ResultActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    private String mUrl;
    private ListAdapter mAdapter;

    @Override
    public void onLoaderReset(android.content.Loader<ArrayList<Book>> loader) {
        if(mAdapter != null) {
            mAdapter.clear();
        }
    }



    @Override
    public android.content.Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        return new Query.bookAsync(this, mUrl);
    }

    @Override
    public void onLoadFinished(android.content.Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.search_progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        if(books != null && books.size() > 0) {
            mAdapter = new ListAdapter(ResultActivity.this, books);
            ListView listView = (ListView) findViewById(R.id.results_lw);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Book book = mAdapter.getItem(position);
                    if(book != null) {
                        Intent webReader = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getWebReaderLink()));
                        startActivity(webReader);
                    }

                }
            });
        }
        else if (books != null && books.size() == 0) {
            TextView error = (TextView) findViewById(R.id.error_text_view);
            error.setText(R.string.no_results);
        }
        else {
            TextView error = (TextView) findViewById(R.id.error_text_view);
            error.setText(R.string.invalid_json);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //get url String from SearchActivity where edit texts is handled
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
                mUrl = extras.getString("URL");
                //catch empty searches and display message that tells to add search arguments
                if (mUrl != null && !mUrl.equals("Empty")) {
                    //show progress indicator
                    setContentView(R.layout.activity_result);
                    //async loader to handle http request and json parsing
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(1, null, this);
                } else {
                    setContentView(R.layout.activity_result);
                    TextView error = (TextView) findViewById(R.id.error_text_view);
                    error.setText(R.string.empty_search_error);
                }
            }

        else {
            setContentView(R.layout.activity_result);
            TextView error = (TextView) findViewById(R.id.error_text_view);
            error.setText(R.string.url_null_error);
        }



    }

}






