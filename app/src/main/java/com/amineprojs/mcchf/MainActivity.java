package com.amineprojs.mcchf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.HttpMethod.GET;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    List<Album> listAlbums;
    ListView listViewAlbums;
    Button buttonDisconnect;
    TextView textViewAlbums, textViewHelloMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

        listAlbums = new ArrayList<>();
        listViewAlbums = (ListView) findViewById(R.id.listViewAlbums);
        buttonDisconnect = (Button) findViewById(R.id.disconnect);
        textViewAlbums = (TextView) findViewById(R.id.textViewAlbums);
        textViewHelloMsg = (TextView) findViewById(R.id.helloMsg);
        buttonDisconnect.setOnClickListener(this);

        loginButton = (LoginButton)findViewById(R.id.loginButton);
        loginButton.setReadPermissions("public_profile", "user_photos");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.GONE);
                textViewHelloMsg.setVisibility(View.GONE);
                textViewAlbums.setVisibility(View.VISIBLE);
                getMyAlbums(loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Login cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });

        checkIfUserConnected();
    }

    private void checkIfUserConnected() {
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            getMyAlbums(profile.getId());
            loginButton.setVisibility(View.GONE);
            textViewHelloMsg.setVisibility(View.GONE);
            textViewAlbums.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getMyAlbums(String userId) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + userId + "/albums",
                null,
                GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            if (response.getError() == null) {
                                JSONObject jsonObjectRoot = response.getJSONObject();
                                if (jsonObjectRoot.has("data")) {
                                    JSONArray jsonArrayData = jsonObjectRoot.optJSONArray("data");
                                    for (int i = 0; i < jsonArrayData.length(); i++) {
                                        JSONObject jsonObjectAlbum = jsonArrayData.getJSONObject(i);
                                        String albumId = jsonObjectAlbum.optString("id");
                                        String albumName = jsonObjectAlbum.optString("name");
                                        Album album = new Album(albumId, albumName);
                                        listAlbums.add(album);
                                    }
                                    AlbumAdapter albumAdapter = new AlbumAdapter(MainActivity.this, listAlbums);
                                    listViewAlbums.setAdapter(albumAdapter);
                                    listViewAlbums.setOnItemClickListener(MainActivity.this);
                                }
                            } else {
                                Log.d("Error", response.getError().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String albumId = ((TextView) view.findViewById(R.id.albumId)).getText().toString();
        String albumName = ((TextView) view.findViewById(R.id.albumName)).getText().toString();
        Intent intent = new Intent(MainActivity.this, AlbumPhotosActivity.class);
        intent.putExtra("albumId", albumId);
        intent.putExtra("albumName", albumName);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonDisconnect) {
            LoginManager.getInstance().logOut();
            SharedPreferences sharedPreferences = getSharedPreferences(getPackageName() + ".userInfos", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("status", "disconnected");
            editor.apply();
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
    }
}