package com.example.foodplanner.app.register;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.app.navigation.NetworkUtils;
import com.example.foodplanner.data.user.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    TextView name, email;
    Button logOut;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SessionManager sessionManager = new SessionManager(view.getContext());
        mAuth = FirebaseAuth.getInstance();
        NetworkUtils networkUtils = new NetworkUtils(requireContext(), new NetworkUtils.NetworkStateListener() {
            @Override
            public void onNetworkAvailable() {
                Log.d("TAG", "Network is available");
            }

            @Override
            public void onNetworkLost() {
                Log.d("TAG", "Network is lost");
            }
        });

        name = view.findViewById(R.id.tvName);
        email = view.findViewById(R.id.tvEmail);
        logOut = view.findViewById(R.id.btnLogout);

        name.setText(sessionManager.getUserName());
        email.setText(sessionManager.getUserEmail());


        logOut.setOnClickListener(v -> {
            if (networkUtils.isNetworkAvailable(requireContext())){
                new SessionManager(requireContext()).clearUserSession();
                mAuth.signOut();
                sessionManager.saveUser(null, null, null);
                Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_mainFragment);
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
                bottomNavigationView.setVisibility(View.GONE);
            }else {
                Toast.makeText(getContext(), "No internet ", Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }
}