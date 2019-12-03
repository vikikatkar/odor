package com.mga.vikram.odor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment implements View.OnClickListener, OnActivityChangeListener{
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private static final String TAG = "G-SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    View loginView;

    SignInButton signInButton;
    TextView statusView;

    Button loginLogoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loginView = inflater.inflate(R.layout.fragment_home,null);

        //TBD:  Move this to main activity or Reporter
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        //TBD:  Move this to main activity or Reporter
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Set the dimensions of the sign-in button.
        signInButton = loginView.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

        //signOutButton = loginView.findViewById(R.id.sign_out_button);
        //signOutButton.setOnClickListener(this);

        statusView = loginView.findViewById(R.id.sign_in_status_message);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if( signOut ){
            signOut();
        }
        return loginView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "HomeFragment : onStart" );

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Verifier reporter = Verifier.getInstance();
        reporter.setFirebaseUser(currentUser);
        reporter.updateLocation(getActivity().getApplicationContext());
        checkIfVerifier(reporter);

        updateReporterUI(reporter);
    }

    private void updateUI(FirebaseUser currentUser) {
        Log.d(TAG, "HomeFragment : updateUI" );

        Verifier reporter = Verifier.getInstance();

        reporter.setFirebaseUser(currentUser);
        reporter.updateLocation(getActivity().getApplicationContext());
        checkIfVerifier(reporter);

        updateReporterUI(reporter);
    }

    private void updateReporterUI(Reporter reporter){
        Log.d(TAG, "HomeFragment : updateReporterUI" );

        String message = "Please Sign In to report odor!";



        if( reporter != null && reporter.isLoggedIn() ) {
            message = "Welcome ! " + reporter.getDisplayName() ;
            if ( reporter.isLocationAvailable() ){
                //message += " : " + reporter.getEmailId() + " : Location : " + reporter.getLat() + " : " + reporter.getLng();

                Toast.makeText(getView().getContext(), message, Toast.LENGTH_SHORT).show();

                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new OdorReportSubmissionFragment(), "OdorReportSubmissionFragment").commit();

            }
            onLoginSuccessFull(getView());
            signInButton.setEnabled(false);
  //          signOutButton.setEnabled(true);
        }else{
            signInButton.setEnabled(true);
//            signOutButton.setEnabled(false);
        }

        statusView.setText(message);
        //startActivity(new Intent(self, MapsActivity.class));
    }

    void checkIfVerifier(Reporter reporter){
        if( ! reporter.isLoggedIn() ){
            return;
        }
        String url = getString(R.string.server_base_url)+"/odor/verifier/"+reporter.getEmailId().hashCode();

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        if( str.equals("true") ){
                            Verifier verifier = Verifier.getInstance();
                            verifier.enableVerifier();
                            Log.i("checkIfVerifier: "," Success");
                        }else{
                            Log.i("checkIfVerifier:" ," Not a verifier");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("checkIfVerifier", "Error on request : "+error.getMessage());
            }
        });
        queue.add(stringRequest);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            //case R.id.sign_out_button:
            //    signOut();
            //    break;

        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateReporterUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(),
                        new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateReporterUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void hideProgressDialog() {
    }

    private void showProgressDialog() {
    }

    public void signOut() {
        // Firebase sign out
        mAuth.signOut();
        FirebaseAuth.getInstance().signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
            new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    updateReporterUI(null);
                }
            });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(getActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateReporterUI(null);
                    }
                });
    }


    //To communicate with MainActivity
    //Ask Main Activity to implement my listener

    public interface OnLoginStatusChangeListener{
        public void onLoginItemSelected(String signal);
    }

    private OnLoginStatusChangeListener onLoginStatusChangeListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if( context instanceof OnLoginStatusChangeListener){
            onLoginStatusChangeListener = ( OnLoginStatusChangeListener )context;
        }else{
            throw new ClassCastException(context.toString() + "must implement OnLoginStatusChangeListener");
        }
    }

    void onLoginSuccessFull(View view){
        onLoginStatusChangeListener.onLoginItemSelected("login-success");
    }
    void onLogoutSuccessFull(View view){
        onLoginStatusChangeListener.onLoginItemSelected("logout-success");
    }



    // To receive communication from MainActivity
    boolean signOut=false;

    public void onActivityMessage(String signal){
        if( signal.equals("sign-out") ){
            signOut = true;
            Verifier.getInstance().logout();
        }
    }
}

