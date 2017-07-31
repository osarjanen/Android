package com.example.android.booksearch;

import android.graphics.Bitmap;
import java.io.Serializable;

/**
 * Hold information of the book
 */

class Book implements Serializable {
    private String mIsbn;
    private String mAuthor;
    private String mTitle;
    private String mDescription;
    private Bitmap mImage;
    private String mWebReaderLink;


    /**
     * hold information of books
     * @param isbn isbn of the book
     * @param author author of the book
     * @param title title of the book
     * @param description short description of the book
     */
    Book(String isbn, String author, String title,String description, Bitmap image, String webReaderLink) {
        mIsbn = isbn;
        mAuthor = author;
        mTitle = title;
        mDescription = description;
        mImage = image;
        mWebReaderLink = webReaderLink;
    }


    String getAuthor() {
        return mAuthor;
    }

    String getTitle() {
        return mTitle;
    }

    String getIsbn() {
        return mIsbn;
    }

    String getDescription() {
        if (mDescription.length() > 100) {
            return mDescription.substring(0, 100) + "...";
        }
        else return mDescription;
    }

    Bitmap getImage() {
        return mImage;
    }

    String getWebReaderLink() {
        return mWebReaderLink;
    }

}
