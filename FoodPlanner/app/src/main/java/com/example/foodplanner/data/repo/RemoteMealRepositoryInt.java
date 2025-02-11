package com.example.foodplanner.data.repo;

import com.example.foodplanner.data.remote.network.NetworkCallback;

public interface RemoteMealRepositoryInt {
    public void getMeal(NetworkCallback networkCallback, String type, String name);

}
