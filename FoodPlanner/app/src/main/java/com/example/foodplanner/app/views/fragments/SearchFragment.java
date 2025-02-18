package com.example.foodplanner.app.views.fragments;

import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.app.adapters.NamesAdapter;
import com.example.foodplanner.app.adapters.SearchAdapter;
import com.example.foodplanner.app.navigation.NetworkChangeReceiver;
import com.example.foodplanner.app.views.viewhelpers.AllDataView;
import com.example.foodplanner.app.views.viewhelpers.SearchViewModel;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.pojos.Category;
import com.example.foodplanner.data.pojos.Countries;
import com.example.foodplanner.data.pojos.Data;
import com.example.foodplanner.data.pojos.Ingredient;
import com.example.foodplanner.data.remote.network.MealRemoteDataSource;
import com.example.foodplanner.data.remote.network.SearchRemoteDataSource;
import com.example.foodplanner.data.repo.RemoteMealRepository;
import com.example.foodplanner.data.repo.SearchRepository;
import com.example.foodplanner.app.adapters.Listener;
import com.example.foodplanner.presenter.MealPresenter;
import com.example.foodplanner.presenter.SearchPresenter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import androidx.lifecycle.ViewModelProvider;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;


public class SearchFragment extends Fragment implements AllDataView, Listener {
    RecyclerView country_recycler, ingredient_recycler, category_recycler, recyclerMealsName;
    SearchAdapter countryAdapter, ingredientAdapter, categoryAdapter;
    NamesAdapter namesAdapter;

    SearchPresenter presenter;
    EditText editText;
    Disposable observable;
    boolean isLoading = false;

    // Our ViewModel instance
    private SearchViewModel viewModel;
    private NetworkChangeReceiver networkChangeReceiver;
    private ScrollView scrollable;
    private ProgressBar catLoadingProgress;
    private ProgressBar countLoadingProgress;
    private ProgressBar ingLoadingProgress;
    private ImageView loading_image;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Log.i("TAG", "onCreateView: hgsdye");
        scrollable = view.findViewById(R.id.scrollView);
        loading_image = view.findViewById(R.id.loading_image);


        catLoadingProgress = view.findViewById(R.id.cat_loading_progress);
        countLoadingProgress = view.findViewById(R.id.count_loading_progress);
        ingLoadingProgress = view.findViewById(R.id.ing_loading_progress);

        networkChangeReceiver = new NetworkChangeReceiver(scrollable,this);
        // Check the network connection initially
        if (!networkChangeReceiver.isNetworkAvailable(getContext())) {
            scrollable.setVisibility(View.GONE);
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.wait)
                    .into(loading_image);
            loading_image.setVisibility(View.VISIBLE);
        } else {
            scrollable.setVisibility(View.VISIBLE);
            loading_image.setVisibility(View.GONE);

        }
        getActivity().registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        MealPresenter mealPresenter = new MealPresenter(
                new MealFragment(),
                RemoteMealRepository.getInstance(MealRemoteDataSource.getInstance())
        );

        country_recycler = view.findViewById(R.id.country_recycler);
        setupRecyclerView(country_recycler);
        countryAdapter = new SearchAdapter(new ArrayList<>(), getContext(), this,mealPresenter);
        country_recycler.setAdapter(countryAdapter);

        ingredient_recycler = view.findViewById(R.id.ingredient_recycler);
        setupRecyclerView(ingredient_recycler);
        ingredientAdapter = new SearchAdapter(new ArrayList<>(), getContext(), this,mealPresenter);
        ingredient_recycler.setAdapter(ingredientAdapter);

        category_recycler = view.findViewById(R.id.category_recycler);
        setupRecyclerView(category_recycler);
        categoryAdapter = new SearchAdapter(new ArrayList<>(), getContext(), this,mealPresenter);
        category_recycler.setAdapter(categoryAdapter);

        presenter = new SearchPresenter(this, SearchRepository.getInstance(SearchRemoteDataSource.getInstance()));
        editText = view.findViewById(R.id.et_search_recipes);
        recyclerMealsName = view.findViewById(R.id.meal_name_recycler);
        recyclerMealsName.setLayoutManager(new LinearLayoutManager(getContext()));
        namesAdapter = new NamesAdapter(new ArrayList<>(),mealPresenter);
        recyclerMealsName.setAdapter(namesAdapter);

        if(scrollable.getVisibility()==View.VISIBLE){
            invisibleCategory();
            invisibleIngredient();
            invisibleCountry();

        }


        // (Optional) Also observe other lists to update your SearchAdapters:
        viewModel.getCountriesList().observe(getViewLifecycleOwner(), countries -> {
            countryAdapter.updateData(countries);
        });
        viewModel.getIngredientsList().observe(getViewLifecycleOwner(), ingredients -> {
            ingredientAdapter.updateData(ingredients);
        });
        viewModel.getCategoriesList().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.updateData(categories);
        });
        if (viewModel.getCategoriesList().getValue().isEmpty()) {
            Log.i("TAG", "onCreateView: list"+viewModel.getCategoriesList().getValue());
            presenter.getCategories("categories", null);

        }else{
            Log.i("TAG", "onCreateView: uuefrh");
        }
        recyclerListener(category_recycler);
        recyclerListener(country_recycler);
        recyclerListener(ingredient_recycler);

        // Observe the filtered names list from the ViewModel
        viewModel.getFilteredNames().observe(getViewLifecycleOwner(), filtered -> {
            namesAdapter.updateData(filtered); // Assume NamesAdapter has an updateData() method
        });



        // Set up an RxJava Observable for the EditText input (for debouncing)
        observable = Observable.create(emitter -> {
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            emitter.onNext(s);
                        }
                        @Override
                        public void afterTextChanged(Editable s) {}
                    });
                })
                .map(charSequence -> charSequence.toString())
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::filterNames, Throwable::printStackTrace);

        return view;
    }

    @Override
    public void showData(List<Data> dataList) {
        if (dataList.isEmpty()) return;
        if (dataList.get(0) instanceof Ingredient) {
            viewModel.updateIngredients(dataList);
            visibleIngredient();
        } else if (dataList.get(0) instanceof Category) {
            viewModel.updateCategories(dataList);
            visibleCategory();
        } else if (dataList.get(0) instanceof Countries) {
            viewModel.updateCountries(dataList);
            visibleCountry();
        } else if (dataList.get(0) instanceof Meal) {
            // For Meals, update the list of all names
            List<String> names = new ArrayList<>();
            for (Data data : dataList) {
                Log.d("TAG", "showData: " + data);
                names.add(data.getStrMeal());
            }
            viewModel.updateAllNames(names);
        }
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    // Filtering logic: update the filteredNames LiveData in the ViewModel.
    private void filterNames(String query) {
        List<String> filtered = new ArrayList<>();
        // If query is not empty, filter the allNames list.
        if (!query.isEmpty()) {
            if (query.length() == 1) {
                presenter.getProducts("letter", query);
            }
            List<String> allNames = viewModel.getAllNames().getValue();
            if (allNames != null) {
                for (String name : allNames) {
                    if (name.toLowerCase().contains(query.toLowerCase())) {
                        filtered.add(name);
                    }
                }
            }
            recyclerMealsName.setVisibility(View.VISIBLE);
        } else {
            recyclerMealsName.setVisibility(View.GONE);
        }
        viewModel.updateFilteredNames(filtered);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        int horizontalSpacing = (int) (10 * getResources().getDisplayMetrics().density);
        int verticalSpacing = (int) (5 * getResources().getDisplayMetrics().density);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(horizontalSpacing, verticalSpacing));
    }

    // A simple ItemDecoration for grid spacing.
    class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private final int horizontalSpacing;
        private final int verticalSpacing;
        public GridSpacingItemDecoration(int horizontalSpacing, int verticalSpacing) {
            this.horizontalSpacing = horizontalSpacing - 10;
            this.verticalSpacing = verticalSpacing;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = horizontalSpacing;
            outRect.right = horizontalSpacing;
            outRect.top = verticalSpacing;
            outRect.bottom = verticalSpacing;
        }
    }

    private void recyclerListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager == null) return;

                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                    Log.i("TAG", "onScrollStateChanged: lastVisibleItemPosition = "
                            + lastVisibleItemPosition + ", totalItemCount = " + totalItemCount);

                    if (lastVisibleItemPosition >= totalItemCount - 1) {
                        isLoading = true;
                        if (recyclerView == category_recycler) {
                            loadCountries();
                        } else if (recyclerView == ingredient_recycler) {
                            loadIngredient();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void loadCountries() {
        if (viewModel.getCountriesList().getValue().isEmpty()) {
            presenter.getCountries("countries", null);
            isLoading = false;
        }
    }

    private void loadIngredient() {
        if (viewModel.getIngredientsList().getValue().isEmpty()) {
            presenter.getIngredient("ingredients", null);
            isLoading = false;
        }
    }
    public void refreshPage() {
        if (networkChangeReceiver.isNetworkAvailable(getContext())) {
//            presenter.getProducts("letter", "a");  // Adjust as needed to re-fetch the data
//            presenter.getRecommend();
        } else {
            Toast.makeText(getContext(), "No network connection", Toast.LENGTH_SHORT).show();
        }
    }
    public void visibleCategory(){
        category_recycler.setVisibility(View.VISIBLE);
        catLoadingProgress.setVisibility(View.GONE);
    }
    public void visibleCountry(){
        country_recycler.setVisibility(View.VISIBLE);
        countLoadingProgress.setVisibility(View.GONE);
    }
    public void visibleIngredient(){
        ingredient_recycler.setVisibility(View.VISIBLE);
        ingLoadingProgress.setVisibility(View.GONE);
    }

    public void invisibleCategory(){
        catLoadingProgress.setVisibility(View.VISIBLE);
        category_recycler.setVisibility(View.GONE);
    }
    public void invisibleCountry(){
        countLoadingProgress.setVisibility(View.VISIBLE);
        country_recycler.setVisibility(View.GONE);
    }
    public void invisibleIngredient(){
        ingLoadingProgress.setVisibility(View.VISIBLE);
        ingredient_recycler.setVisibility(View.GONE);
    }
}
