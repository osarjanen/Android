package fi.osarjanen.android.booksearch;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = ListAdapter.class.getName();


    public ListAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.result_text_views, parent, false);
        }

        final Book currentBook = getItem(position);

        if(currentBook != null) {
            ImageView image = (ImageView) listItemView.findViewById(R.id.picture__image_view);
            if (currentBook.getImage() != null) {
                image.setImageBitmap(currentBook.getImage());
            }
            else image.setImageResource(R.drawable.error);

            TextView isbn = (TextView) listItemView.findViewById(R.id.isbn_text_view);
            isbn.setText(currentBook.getIsbn());

            TextView author = (TextView) listItemView.findViewById(R.id.author_text_view);
            author.setText(currentBook.getAuthor());

            TextView title = (TextView) listItemView.findViewById(R.id.title_text_view);
            title.setText(currentBook.getTitle());

            TextView description = (TextView) listItemView.findViewById(R.id.description_text_view);
            description.setText(currentBook.getDescription());
        }

        return listItemView;

    }
}
