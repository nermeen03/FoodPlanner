package com.example.foodplanner.app.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.data.user.SessionManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainFragment extends Fragment {

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        SignInButton signInButton = view.findViewById(R.id.btnGoogleSignIn);
        signInButton.setOnClickListener(v -> signIn());

        Button btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_signInFragment));

        Button btnSignUp = view.findViewById(R.id.btnSignUp);
        btnSignIn.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_signUpFragment));

        SessionManager sessionManager = new SessionManager(getContext());
        Button btnGuest = view.findViewById(R.id.btnGuest);
        btnGuest.setOnClickListener(v -> {
            sessionManager.saveUser("guest", "null", "null");
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_homeFragment);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            SessionManager sessionManager = new SessionManager(requireContext());
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                Log.d("GoogleSignIn", "Already signed in: " + account.getEmail());
                sessionManager.saveUser(account.getId(), account.getDisplayName(), account.getEmail());
                Navigation.findNavController(requireView()).navigate(R.id.action_mainFragment_to_homeFragment);
            } else {
                Log.d("GoogleSignIn", "No signed-in account found.");
            }
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
