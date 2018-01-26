package it.uniroma1.dis.exam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import it.uniroma1.dis.exam.R;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1313;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //configure
        //https://developers.google.com/identity/sign-in/android/start-integrating
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_Client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //check already signed user or if it is logout
        Intent i = getIntent();
        if (i!= null && i.getStringExtra("logout") != null)
            signOut();
        else {
            Log.e("else","here else");
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        }
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final ProgressDialog progress = ProgressDialog.show(this, "dialog title",
                    "dialog message", true);
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.e("token",account.getIdToken());
            //try log in auth/google_oauth2
            String url = getString(R.string.url_backend) + "signin";
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // prepare the Request
            Gson gson = new Gson();
            final String jsonString = account.getIdToken();
            JSONObject obj = null;
            try {
                obj = new JSONObject();
                obj.put("idToken", jsonString);
            } catch (JSONException e) {
                Log.e("Response", e.getMessage());
            }
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("Response", response.toString());
                                SharedPreferences loginData = getApplicationContext().getSharedPreferences(
                                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = loginData.edit();
                                editor.putString("idToken",jsonString.trim());
                                editor.putString("user_id", account.getId());
                                editor.putString("url_photo", account.getPhotoUrl().toString());
                                editor.putString("displayName", account.getDisplayName());
                                editor.putString("email", account.getEmail());
                                editor.commit();
                                //now intent
                                if (progress.isShowing()) progress.dismiss();
                                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);
                            }catch(Exception e){
                                Log.e("Error", e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error.Response", error.toString());
                            if (progress.isShowing()) progress.dismiss();
                            Toast.makeText(getApplicationContext(), "GENERIC CONNECTION ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            // add it to the RequestQueue
            queue.add(getRequest);
        } catch (ApiException e) {
            Log.d("ERROR", "ERRORE GENERICO DI LOG IN");
            e.printStackTrace();
            Log.w("ERROR", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void signOut(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "DISCONNECTED", Toast.LENGTH_SHORT);
                    }
                });
    }


}

