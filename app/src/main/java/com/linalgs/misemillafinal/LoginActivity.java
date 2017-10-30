package com.linalgs.misemillafinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private String correoR, contraseñaR, edcorreo,edcont, urlG,urlF,correoF,nameF;
    private EditText eCorreo,eContraseña;
    //google
    GoogleApiClient mGoogleApiClient;
    private int RC_SING_IN=123;
    //facebook
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        eCorreo = (EditText) findViewById(R.id.eCorreo) ;
        eContraseña =(EditText) findViewById(R.id.eContraseña);

        //loggin con google--------------------------------
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),"Error en Loggin", Toast.LENGTH_SHORT).show();
                    }
                }/* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        //facebook----------------------------------------------------
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Bundle bFacebookData=getFacebookData(object);
                        correoF = bFacebookData.getString("email");
                        urlF = bFacebookData.getString("profile_pic");
                        nameF = (bFacebookData.getString("first_name")+" "+ bFacebookData.getString("last_name"));
                        goMainActivity();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,first_name, last_name, email, gender,birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Login cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Login error", Toast.LENGTH_SHORT).show();

            }
        });
        //----------------end facebook--------------------
    }
    private  Bundle getFacebookData(JSONObject object){
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");
            try {
                URL profile_pic = new URL("https://graph.facebook.com/"+ id+ "/picture?width=200&height=200");
                bundle.putString("profile_pic", profile_pic.toString());
            }catch (MalformedURLException e){
                e.printStackTrace();
                return null;
            }
            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            return bundle;
        }
        catch(JSONException e) {
            Log.d("logrado","Error parsing JSON");}

        return null;
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SING_IN);
    }
    @Override
    protected  void  onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1234 &&  resultCode == RESULT_OK){
            correoR = data.getExtras().getString("correo");
            contraseñaR = data.getExtras().getString("contraseña");
            Toast.makeText(this,correoR,Toast.LENGTH_SHORT).show();
        }else if (requestCode == RC_SING_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
    public void iniciar (View view){

        edcorreo = eCorreo.getText().toString();
        edcont = eContraseña.getText().toString();

       // if (optLog==1){
         //   Bundle extras = getIntent().getExtras();
           // correoR = extras.getString("correo");

           // contraseñaR = extras.getString("contraseña");
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.putInt("optLog",1);
        editor.commit();

        if (edcorreo.equals("")){
            Toast.makeText(getApplicationContext(),"Ingrese el correo",Toast.LENGTH_SHORT).show();
        }
        else if (edcont.equals("")){
            Toast.makeText(getApplicationContext(),"Ingrese la contraseña",Toast.LENGTH_SHORT).show();
        }else if ((correoR.equals(edcorreo))&&(contraseñaR.equals(edcont))){

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

            editor.putString("correo",correoF);
            editor.putString("contraseña",contraseñaR);
            editor.remove("urlf");
            editor.commit();
            intent.putExtra("correo",correoR);
            intent.putExtra("contraseña",contraseñaR);
            startActivity(intent);
            //startActivityForResult(intent, 1234);
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"Los Datos no Considen", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        Log.d("google", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(getApplicationContext(),acct.getDisplayName(),Toast.LENGTH_SHORT).show();
            prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            editor = prefs.edit();
            editor.putInt("optLog",2);
            editor.putString("correo",acct.getEmail());
            editor.putString("contraseña",acct.getDisplayName());
            editor.putString("urlf", acct.getPhotoUrl().toString());
            editor.commit();
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("correo", acct.getEmail());
            intent.putExtra("contraseña",acct.getDisplayName());
            intent.putExtra("urlf",acct.getPhotoUrl().toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getApplicationContext(),"Error en Loggin",Toast.LENGTH_SHORT).show();
        }
    }
    public void Registrese(View view){
        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivityForResult(intent, 1234);
    }
    private void goMainActivity() {
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor =prefs.edit();
        editor.putInt("optLog",3);
        editor.putString("correo",correoF);
        editor.putString("contraseña",nameF);
        editor.putString("urlf", urlF);
        editor.commit();

        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        //Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }
}
