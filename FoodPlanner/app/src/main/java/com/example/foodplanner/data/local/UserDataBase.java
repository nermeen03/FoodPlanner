package com.example.foodplanner.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodplanner.data.user.User;
import com.example.foodplanner.data.user.UserDao;

@Database(entities = {User.class}, version = 3)

public abstract class UserDataBase extends RoomDatabase {
    public abstract UserDao userDao();
    private static UserDataBase instance = null;
    public static UserDataBase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            UserDataBase.class,
                            "userDb"
                    ).fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
