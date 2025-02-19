package com.example.foodplanner.app.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    private EditText emailEditText, passwordEditText;
    private TextView tvSignup;
    private Button signInButton;
    private FirebaseHelper firebaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        emailEditText = view.findViewById(R.id.etEmail);
        passwordEditText = view.findViewById(R.id.etPassword);
        signInButton = view.findViewById(R.id.btnLogin);
        tvSignup = view.findViewById(R.id.tvSignup);

        firebaseHelper = new FirebaseHelper();

        signInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            firebaseHelper.signIn(email, password, view, getContext());

        });
        tvSignup.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.signUpFragment);
        });

        return view;
    }

}