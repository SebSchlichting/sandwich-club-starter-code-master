package com.udacity.sandwichclub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * The correct sandwich is determined from the handed over position (from the Intent) via JSON description.
 * The exact sandwich description is shown in this class.
 */
public class DetailActivity extends AppCompatActivity {


    public static final String EXTRA_POSITION = "extra_position";
    private static final int UNKNOWN_POSITION = -1;

    @BindView(R.id.image_iv)
    ImageView image_iv;

    @BindView(R.id.name_of_sandwich_tv)
    TextView name_of_sandwich_tv;

    @BindView(R.id.origin_tv)
    TextView oringin_tv;

    @BindView(R.id.description_tv)
    TextView description_tv;

    @BindView(R.id.ingredients_tv)
    TextView ingredients_tv;

    @BindView(R.id.also_known_tv)
    TextView also_known_tv;


    //Butterknife
    private Unbinder unbinder;


    /**
     *
     * From the current position, the sandwich description is determined.
     * In addition, the corresponding image is loaded via a predetermined URL.
     *
     * @param savedInstanceState If non-null, this Activity is being re-constructed
     *                           from a previous saved state as given here
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        unbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        int currentPosition = UNKNOWN_POSITION;
        if (intent != null) {
            currentPosition = intent.getIntExtra(EXTRA_POSITION, UNKNOWN_POSITION);
        }
        if (currentPosition == UNKNOWN_POSITION) {
            // EXTRA_POSITION not found in the intent
            closeOnError();
            return;
        }


        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[currentPosition];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data not found
            closeOnError();
            return;
        }


        new SandwichImageTask().execute(sandwich.getImage());

        oringin_tv.setText(sandwich.getPlaceOfOrigin());
        description_tv.setText(sandwich.getDescription());
        name_of_sandwich_tv.setText(sandwich.getMainName());
        ingredients_tv.setText(sandwich.getIngredients());
        also_known_tv.setText(sandwich.getAlsoKnownAs());



    }
    /**
     * The binder is reset, when the view is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * The processing cancels with an info toast, when no Sandwich object could be created.
     */
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * The Class loads an Image via the URL and stores it into the Surface.
     */
    @SuppressLint("StaticFieldLeak")
    public class SandwichImageTask extends AsyncTask<String, Void, Bitmap> {
        private static final String NOT_SUCCESFULLY_RECEIVED_RESPONSE_CODE =
                "The Request was not succesfully received";
        private static final int RESPONSE_OK = 200;
        private static final String IMAGE_URL_IS_WRONG = "Image URL is wrong";
        private static final String ERROR_WITH_THE_CONNECTION = "Error with the connection";
        private static final String ERROR_WITH_THE_INPUT_STREAM = "Error with the input stream";
        private static final int FIRST_ELEMENT = 0;
        private static final int READ_TIMEOUT = 5000; /* milliseconds */
        private static final int CONNECT_TIMEOUT = 7000; /* milliseconds */
        private String TAG = SandwichImageTask.class.getSimpleName();

        /**
         * A correct URL Is created from the URL string. It is used to load the Bitmap of the Image.
         *
         * @param params contain the URL String
         * @return the image bitmap
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            URL url = createUrl(params[FIRST_ELEMENT]);
            return makeHttpRequest(url);
        }

        /**
         * A correct URL Is created from the URL string.
         *
         * @param stringUrl string of the url
         * @return URL object or an error
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(TAG, IMAGE_URL_IS_WRONG, e);
            }
            return url;

        }

        /**
         * The URL object is used to load the image Bitmap.
         *
         * @param url object
         * @return the Bitmap of the Image
         */
        private Bitmap makeHttpRequest(URL url) {
            Bitmap bitmap = null;
            if (url != null) {
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(READ_TIMEOUT);
                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setDoInput(true);

                    if (connection.getResponseCode() == RESPONSE_OK) {
                        inputStream = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    } else {
                        Log.e(TAG, NOT_SUCCESFULLY_RECEIVED_RESPONSE_CODE
                                + connection.getResponseCode());
                    }
                } catch (IOException e) {
                    Log.e(TAG, ERROR_WITH_THE_CONNECTION, e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            Log.e(TAG, ERROR_WITH_THE_INPUT_STREAM, e);
                        }
                    }
                }
            }
            return bitmap;
        }

        /**
         * the Image will be displayed.
         *
         * @param bitmap of the image
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                image_iv.setImageBitmap(bitmap);
            }
        }
    }
}



