package com.example.android.booksearch;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * makes http request for asked book info and parses the JSON data to list view
 */

 class Query {

    private static final String LOG_TAG = Query.class.getName();

    public Query() {

    }

    /**
     * Parse JSON into book objects
     * @param jsonResponse String response from http query
     * @return array list of books parsed from JSON
     */
    private static ArrayList<Book> parseBooks(String jsonResponse) {


        // Create an empty ArrayList that is filled from JSON parsing
        ArrayList<Book> books = new ArrayList<>();

        // Try to parse the JSON_RESPONSE
        try {
            JSONObject response =  new JSONObject(jsonResponse);

            JSONArray items = response.getJSONArray("items");
            for(int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                JSONObject accessInfo = item.getJSONObject("accessInfo");
                JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");
                books.add(new Book(getIsbn(industryIdentifiers),
                        volumeInfo.getString("authors"),
                        volumeInfo.getString("title"),
                        volumeInfo.getString("description"),
                        getBookThumbnail(volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail")),
                        accessInfo.getString("webReaderLink")));
            }

        } catch (JSONException e) {
            //log error
            Log.e(LOG_TAG, "Problem parsing the books JSON results", e);
        }


        // Return the list of books
        return books;
    }

    /**
     * Get image for book thumbnail
     * @param url URL of thumbnail image
     * @return image from given url
     */
    private static Bitmap getBookThumbnail (String url) {

        InputStream in;
        Bitmap bookImage = null;
        try {
            in = new URL(url).openStream();
            bookImage = BitmapFactory.decodeStream(in);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "error receiving thumbnail");
        }


        return bookImage;
    }

    /**
     * get isbn of the book. Primarily return updated 13 character version. there could be both in varied order or there could be just either
     * @param identifier array of isbn
     * @return isbn number of the book
     */
    private static String getIsbn(JSONArray identifier) {
        if (identifier != null) {
            try {
                if (identifier.getJSONObject(0).getString("type").equals("ISBN_13")) {
                    return identifier.getJSONObject(0).getString("identifier");
                }
                else if (identifier.getJSONObject(1).getString("type").equals("ISBN_13")){
                    return identifier.getJSONObject(1).getString("identifier");
                }
                else return identifier.getJSONObject(0).getString("identifier");
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error paring identifier");
            }
        }
        return "";
    }

    /*
        Handle http querying and JSON parsing in an Async task and use loader take care of resources
     */
     static class bookAsync extends AsyncTaskLoader<ArrayList<Book>> {


        private String mUrl;

        bookAsync(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        public ArrayList<Book> loadInBackground() {
            //create URL from string
            URL url = createUrl(mUrl);
            //get json from http get response
            String jsonResponse = makeHttpRequest(url);
            //parse json and return ArrayList<Book>
            return parseBooks(jsonResponse);
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }


        /**
         *
         * @param stringUrl string for query url
         * @return URL of string passed in
         */
        private URL createUrl(String stringUrl) {
            URL url;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         *
         * @param url URL to open connect to
         * @return complete response from server
         */
        private String makeHttpRequest (URL url) {
            String response = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            if (url == null) {
                return response;
            }

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    response = readFromStream(inputStream);
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error making connection");
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }

                if(inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException exception) {
                        Log.e(LOG_TAG, "Error closing inputStream", exception);
                    }
                }

            }
            return response;

        }

        /**
         *
         * @param inputStream input stream of http request
         * @return response from server
         * @throws IOException Error reading stream
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }


    }
}
