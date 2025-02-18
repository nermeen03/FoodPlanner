package com.example.foodplanner.app.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.data.user.SessionManager;
import com.example.foodplanner.databinding.FragmentMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

public class MainFragment extends Fragment {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001; // Unique request code for Google Sign-In

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Configure Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1084835448807-gbqbbb5iodi7qagnfisrrttoh19petok.apps.googleusercontent.com") // Use your Web Client ID here
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        Button btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v ->{
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_signInFragment);
        });

        Button btnSignUp = view.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v ->{
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_signUpFragment);
        });

        SessionManager sessionManager = new SessionManager(getContext());
        Button btnGuest = view.findViewById(R.id.btnGuest);
        btnGuest.setOnClickListener(v ->{
            sessionManager.saveUser("guest");
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_homeFragment);
        });

        // Find Google Sign-In button and set click listener
        SignInButton signInButton = view.findViewById(R.id.btnGoogleSignIn);
        signInButton.setOnClickListener(v -> signIn());  // Trigger sign-in when button is clicked

        return view;
    }

    // Trigger Google Sign-In
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN); // Start Google Sign-In intent
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result from Google Sign-In
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task); // Handle the result
        }
    }

    // Handle Google Sign-In result
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // Sign-In successful, get the signed-in account information
            GoogleSignInAccount account = completedTask.getResult();
            String email = account.getEmail();
            String displayName = account.getDisplayName();

            // Show a success message or proceed with your app's flow
            Toast.makeText(getContext(), "Welcome " + displayName, Toast.LENGTH_SHORT).show();

            // You can use this account's ID token to authenticate with your backend if needed
            String idToken = account.getIdToken();  // Example: send this token to your server

        } catch (Exception e) {
            // Sign-In failed, handle the failure
            Log.w("GoogleSignIn", "signInResult:failed", e);
            Toast.makeText(getContext(), "Sign-In failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
