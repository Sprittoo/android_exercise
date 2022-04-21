package com.example.norman_lee.comicapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    EditText editTextComicNo;
    Button buttonGetComic;
    TextView textViewTitle;
    ImageView imageViewComic;

    String comicNo;
    public static final String TAG = "Logcat";
    final String ERROR_NO_NETWORK = "No Network";
    final String ERROR_NOT_VALID = "Comic No Not Valid";
    final String ERROR_MALFORMED_URL = "Malformed URL";
    final String ERROR_BAD_JSON = "Bad JSON Response";
    final String ERROR_HTTPS_ERROR = "HTTPS Error";

    class Container<T> {
        T value;

        Container() {
            this.value = null;
        }

        void set(T x) {
            this.value = x;
        }

        T get() {
            return this.value;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODOok 6.0 Study the Utils class and see what methods are available for you
        //TODOok 6.1 Ensure that Android Manifest has permissions for internet and has orientation fixed
        //TODOok 6.2 Get references to widgets
        editTextComicNo = findViewById(R.id.editTextComicNo);
        buttonGetComic = findViewById(R.id.buttonGetComic);
        textViewTitle = findViewById(R.id.textViewTitle);
        imageViewComic = findViewById(R.id.imageViewComic);
        //TODOok 6.3 Set up setOnClickListener for the button
        buttonGetComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(MainActivity.this)) {
                    comicNo = editTextComicNo.getText().toString();
                    getComic(comicNo);
                }
            }
        });

        //TODOok 6.4 Retrieve the user input from the EditText
        //TODO 6.5 - 6.9 Modify getComic below 

        //TODO 6.10 If network is active, call the getComic method with the userInput

    }


    //TODO 6.5 - 6.9 ****************
    //TODO you are reminded that new Runnable{} is an anonymous inner class
    //TODO 6.5 Make sure getComic has the signature getComic(final String userInput); make sure an executor and a handler are instantiated
    //TODO 6.6 (background work) create a final Container<Bitmap> cBitmap object which will be used for commmunication between the main thread and the child thread
    //TODO 6.7 (background work) Call Utils.getImageURLFromXkcdApi to get the image URL from comicNo
    //TODO 6.8 (background work)Call Utils.getBitmap using the URL to get the bitmap
    //TODO 6.9 (UI thread work)Assign the Bitmap downloaded to imageView. The bitmap may be null.


    void getComic(final String userInput) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Container<Bitmap> cBitmap = new Container<Bitmap>();

                try {
                    URL finalURL = new URL(Utils.getImageURLFromXkcdApi(comicNo));
                    Bitmap bitmap = Utils.getBitmap(finalURL);
                    cBitmap.set(bitmap);
                } catch (IOException ioe) {
                    Log.d(MainActivity.class.toString(), "Network exception");
                } catch (JSONException je) {
                    Log.d(MainActivity.class.toString(), "The returned json is invalid");
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.imageViewComic.setImageBitmap(cBitmap.get());
                    }
                });
            }
        });
    }
}



