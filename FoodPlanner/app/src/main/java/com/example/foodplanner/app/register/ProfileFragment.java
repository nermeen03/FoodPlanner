package com.example.foodplanner.app.register;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.example.foodplanner.data.user.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    TextView name,email;
    Button logOut;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchInternet;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SessionManager sessionManager = new SessionManager(view.getContext());
        mAuth = FirebaseAuth.getInstance();

        name = view.findViewById(R.id.tvName);
        email = view.findViewById(R.id.tvEmail);
        switchInternet = view.findViewById(R.id.switchInternet);
        logOut = view.findViewById(R.id.btnLogout);

        name.setText(sessionManager.getUserName());
        email.setText(sessionManager.getUserEmail());

        sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        boolean isInternetAllowed = sharedPreferences.getBoolean("InternetAccess", true);
        switchInternet.setChecked(isInternetAllowed);

        switchInternet.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("InternetAccess", isChecked);
            editor.apply();

            if (isChecked) {
                Toast.makeText(getContext(), "Internet Access Enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Internet Access Disabled", Toast.LENGTH_SHORT).show();
            }

            toggleInternetAccess(isChecked);
        });

        logOut.setOnClickListener(v->{
            mAuth.signOut();
            sessionManager.saveUser(null,null,null);
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.mainFragment);


        });

        return view;
    }
    private void toggleInternetAccess(boolean enable) {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            if (enable) {
                NetworkRequest request = new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build();
                cm.requestNetwork(request, new ConnectivityManager.NetworkCallback() {});
            } else {
                Toast.makeText(getContext(), "Internet disabled in app logic", Toast.LENGTH_SHORT).show();
            }
        }
    }
}