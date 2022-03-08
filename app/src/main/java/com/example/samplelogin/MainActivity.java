package com.example.samplelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends YouTubeBaseActivity {
private ProgressBar loadingpb;
private EditText etuname;
private Button btnlogin,youtube;
YouTubePlayerView youTubePlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        youTubePlayerView=findViewById(R.id.youtube_player_view);
        loadingpb=findViewById(R.id.idLoadingPB);
        etuname=findViewById(R.id.etUsername);
        btnlogin=findViewById(R.id.btnLogin);
        youtube=findViewById(R.id.youtube);

        btnlogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                postDataUsingVolley(etuname.getText().toString());
                YouTubePlayer.OnInitializedListener listener = new YouTubePlayer.OnInitializedListener()
                {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b)
                    {
                        youTubePlayer.loadVideo(etuname.getText().toString());
                        youTubePlayer.play();
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult)
                    {
                        Toast.makeText(getApplicationContext(),
                                "Initialization Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                };
                youTubePlayerView.initialize("AIzaSyBEQpvEN4_2dqTa1s1IJxPs7Kcob4gKm8A",listener);
            }
        });
        youtube.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gotoUrl("https://www.youtube.com/");
            }
        });

    }
    private void gotoUrl(String S)
    {
        Uri uri = Uri.parse(S);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
    // this is for login
    private void postDataUsingVolley(final String uname) {

        // url to post our data
        String url = "http://192.168.43.9/login.php";
        // loading pogress bar this is optional
        loadingpb.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                loadingpb.setVisibility(View.GONE);

                // on below line we are displaying a success toast message.
                //Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are passing our response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);

                    // below are the strings which we
                    // extract from our json object.
                    String result = respObj.getString("result");
                    String username = respObj.getString("uname");

                    // we just toast the value we got from API, 1 for success, 0 otherwise
                    Toast.makeText(MainActivity.this, "inserted " + result + "," + username, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("uname", uname);
               // params.put("pass", pass);
                // put some code for verfication that the post came from your mobile app
                params.put("login", "some-code-here");

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
    public static String getYoutubeId(String url)
    {
        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";

        Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        return null;
    }
}