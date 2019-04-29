package com.bkongara.smartyoutubesearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import android.os.Handler;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText searchTxt;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private Handler handler;
    private List<VideoItem> searchResults;
    private YoutubeAdaptor youtubeAdaptor;


    @Override
    protected  void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        searchTxt = (EditText)findViewById(R.id.search_input);
        recyclerView = (RecyclerView) findViewById(R.id.videos_recycler_view);
        progressDialog.setTitle("Searching Youtube...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        handler = new Handler();
        searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){

                    progressDialog.setMessage("Finding videos for "+v.getText().toString());
                    progressDialog.show();
                    searchYoutube(v.getText().toString());
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    return false;
                }
                return true;
            }
        });
    }

    private void searchYoutube(final String keywords){

        new Thread(){

            public void run(){

                YoutubeConnector yc = new YoutubeConnector(MainActivity.this);
                searchResults = yc.search(keywords);
                handler.post(new Runnable(){

                    public void run(){
                        fillYoutubeVideos();
                        progressDialog.dismiss();
                    }
                });
            }
        }.start();
    }

    private void fillYoutubeVideos(){

        youtubeAdaptor = new YoutubeAdaptor(getApplicationContext(),searchResults);
        recyclerView.setAdapter(youtubeAdaptor);
        youtubeAdaptor.notifyDataSetChanged();
    }
}
