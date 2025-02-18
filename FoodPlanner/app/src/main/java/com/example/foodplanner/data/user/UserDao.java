package com.example.foodplanner.data.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    User getUserById(String uid);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

}
