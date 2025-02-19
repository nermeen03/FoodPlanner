package com.example.foodplanner.app.register;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.user.SessionManager;
import com.example.foodplanner.data.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class FirebaseHelper {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    public void saveUserToFireStore(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(user.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d("TAG", "User data saved successfully!"))
                .addOnFailureListener(e -> Log.e("TAG", "Error saving user data", e));
    }

    public void saveFavoriteMeal(String userId, String mealId, String mealName, String mealImg) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> meal = new HashMap<>();
        meal.put("name", mealName);
        meal.put("img", mealImg);

        db.collection("users").document(userId)
                .collection("favorites").document(mealId)
                .set(meal)
                .addOnSuccessListener(aVoid -> Log.d("TAG", "Favorite meal saved!"))
                .addOnFailureListener(e -> Log.e("TAG", "Error saving favorite meal", e));
    }

    public void deleteFavoriteMeal(String userId, String mealId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId)
                .collection("favorites").document(mealId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("TAG", "Favorite meal deleted successfully!"))
                .addOnFailureListener(e -> Log.e("TAG", "Error deleting favorite meal", e));
    }

    public Observable<List<Meal>> getFavoriteMeals(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return Observable.create(emitter -> {
            db.collection("users").document(userId)
                    .collection("favorites")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Meal> meals = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            String mealName = document.getString("name");
                            String mealImg = document.getString("img");
                            String mealId = document.getId();
                            Meal mealInfo = new Meal(mealId, mealName, mealImg, userId);
                            meals.add(mealInfo);
                            Log.d("TAG", "Favorite Meal: " + mealName);
                        }
                        emitter.onNext(meals);
                        emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("TAG", "Error fetching favorite meals", e);
                        emitter.onError(e);
                    });
        });
    }


    public void saveMealPlan(String userId, String date, String mealID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> mealPlan = new HashMap<>();
        mealPlan.put("meal", mealID);

        db.collection("users").document(userId)
                .collection("mealPlans").document(date)
                .set(mealPlan)
                .addOnSuccessListener(aVoid -> Log.d("TAG", "Meal plan saved for " + date))
                .addOnFailureListener(e -> Log.e("TAG", "Error saving meal plan", e));
    }

    public void deleteMealFromPlan(String userId, String date, String mealID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference mealPlanRef = db.collection("users").document(userId)
                .collection("mealPlans").document(date);
        mealPlanRef.update("meal." + mealID, null)
                .addOnSuccessListener(aVoid -> Log.d("TAG", "Meal " + mealID + " deleted from meal plan on " + date))
                .addOnFailureListener(e -> Log.e("TAG", "Error deleting meal from meal plan", e));
    }

    public void getMealPlan(String userId, String date) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(userId)
                .collection("mealPlans").document(date)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String meal = document.getString("meal");
                        Log.d("TAG", "Meal Plan for " + date + ": " + meal);
                    } else {
                        Log.d("TAG", "No meal plan found for this date.");
                    }
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error fetching meal plan", e));
    }

    public void createAccount(String email, String password, String name, View view, Context context) {
        SessionManager sessionManager = new SessionManager(context);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "createUserWithEmail: success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            sessionManager.saveUser(user.getUid(), user.getDisplayName(), user.getEmail());
                            User newUser = new User(user.getUid(), name, email);
                            saveUserToFireStore(newUser);
                            Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_homeFragment);
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            Log.d("TAG", "User profile updated.");
                                            user.reload().addOnCompleteListener(info -> fetchUserDetails());

                                        }
                                    });

                            user.sendEmailVerification()
                                    .addOnCompleteListener(verificationTask -> {
                                        if (verificationTask.isSuccessful()) {
                                            Log.d("TAG", "Verification email sent.");
                                        }
                                    });
                        }
                    } else {
                        Log.w("TAG", "createUserWithEmail: failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                            Log.e("TAG", "Weak password!");
                        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Log.e("TAG", "Invalid email format!");
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Log.e("TAG", "Email is already in use!");
                        }
                    }
                });
    }

    public void signIn(String email, String password, View view, Context context) {
        SessionManager sessionManager = new SessionManager(context);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            sessionManager.saveUser(user.getUid(), user.getDisplayName(), user.getEmail());
                            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_homeFragment);
                        }
                    } else {
                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                    }

                });
    }

    public String fetchUserDetails() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            boolean emailVerified = user.isEmailVerified();
            String uid = user.getUid();

            Log.d("TAG", "User Info: " + name + ", " + email + ", " + uid);

            if (!emailVerified) {
                Log.w("TAG", "User email is not verified!");
            }
            return uid;
        }
        return null;
    }

}
