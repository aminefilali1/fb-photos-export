package com.amineprojs.mcchf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.facebook.HttpMethod.GET;

public class AlbumPhotosActivity extends AppCompatActivity {

    TextView textViewAlbumName;
    LinearLayout linearLayoutPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_photos);

        textViewAlbumName = (TextView) findViewById(R.id.albumName);
        linearLayoutPhotos = (LinearLayout) findViewById(R.id.linearLayoutPhotos);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String albumId = extras.getString("albumId");
            String albumName = extras.getString("albumName");
            textViewAlbumName.setText(albumName);
            getMyPhotos(albumId);
        }
    }

    public void getMyPhotos(final String albumId) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                parameters,
                GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            if (response.getError() == null) {
                                JSONObject jsonObjectRoot = response.getJSONObject();
                                if (jsonObjectRoot.has("data")) {
                                    JSONArray jsonArrayData = jsonObjectRoot.optJSONArray("data");
                                    for (int i = 0; i < jsonArrayData.length(); i++)  {
                                        JSONObject jsonObjectAlbum = jsonArrayData.getJSONObject(i);
                                        JSONArray jsonArrayPhotos = jsonObjectAlbum.getJSONArray("images");
                                        if(jsonArrayPhotos.length() > 0) {
                                            String photoUrl = jsonArrayPhotos.getJSONObject(0).getString("source");
                                            ImageView imageView = new ImageView(AlbumPhotosActivity.this);
                                            LinearLayout.LayoutParams layoutParamsImage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                                            layoutParamsImage.setMargins(0,0,0,20);
                                            layoutParamsImage.gravity = Gravity.CENTER;
                                            imageView.setLayoutParams(layoutParamsImage);
                                            Picasso.with(AlbumPhotosActivity.this).load(photoUrl).into(imageView);
                                            linearLayoutPhotos.addView(imageView);
                                        }
                                    }
                                }
                            } else {
                                Log.v("RESPONSE", response.getError().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
        }).executeAsync();
    }

}
