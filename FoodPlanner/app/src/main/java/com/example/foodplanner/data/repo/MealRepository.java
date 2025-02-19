package com.example.foodplanner.data.repo;

import io.reactivex.rxjava3.core.Observable;
import com.example.foodplanner.data.local.MealsLocalDataSourceInt;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.remote.network.MealRemoteDataSourceInt;
import com.example.foodplanner.data.remote.network.NetworkCallback;
import java.util.List;

public class MealRepository implements MealRepositoryInt{
    MealsLocalDataSourceInt productLocalDataSource;
    MealRemoteDataSourceInt productRemoteDataSource;
    private static MealRepository repo = null;

    public MealRepository(MealsLocalDataSourceInt productLocalDataSource, MealRemoteDataSourceInt productRemoteDataSource) {
        this.productLocalDataSource = productLocalDataSource;
        this.productRemoteDataSource = productRemoteDataSource;
    }
    public static MealRepository getInstance(MealsLocalDataSourceInt localDataSource, MealRemoteDataSourceInt productRemoteDataSource){
        if(repo==null){
            repo = new MealRepository(localDataSource,productRemoteDataSource);
        }
        return repo;
    }

    @Override
    public Observable<List<Meal>> getProducts(String name) {
        return productLocalDataSource.getStoredMeals(name);
    }

    @Override
    public void getProducts(NetworkCallback networkCallback,String type,String name) {
        productRemoteDataSource.makeNetworkCall(networkCallback,type,name);
    }

    @Override
    public void insertOneProduct(Meal meal) {
        productLocalDataSource.insetProd(meal);
    }

    @Override
    public void getRecommend(NetworkCallback networkCallback) {
        productRemoteDataSource.makeNetworkCall(networkCallback,"recommend",null);
    }

    @Override
    public void deleteProduct(Meal meal) {
        productLocalDataSource.deleteProd(meal);
    }
}
