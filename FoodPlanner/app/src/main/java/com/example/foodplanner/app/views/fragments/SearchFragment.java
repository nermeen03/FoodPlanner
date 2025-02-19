package com.example.foodplanner.app.views.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.app.adapters.Listener;
import com.example.foodplanner.app.adapters.NamesAdapter;
import com.example.foodplanner.app.adapters.SearchAdapter;
import com.example.foodplanner.app.navigation.NetworkUtils;
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
import com.example.foodplanner.presenter.MealPresenter;
import com.example.foodplanner.presenter.SearchPresenter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SearchFragment extends Fragment implements AllDataView, Listener {
    RecyclerView country_recycler, ingredient_recycler, category_recycler, recyclerMealsName;
    SearchAdapter countryAdapter, ingredientAdapter, categoryAdapter;
    NamesAdapter namesAdapter;

    SearchPresenter presenter;
    EditText editText;
    Disposable observable;
    boolean isLoading = false;

    private SearchViewModel viewModel;
    private NetworkUtils networkUtils;
    private ScrollView scrollable;
    private ProgressBar catLoadingProgress;
    private ProgressBar countLoadingProgress;
    private ProgressBar ingLoadingProgress;
    private ImageView loading_image;
    private String selectedFilter = "meal";
    private Chip categoryChip,countryChip,ingredientChip;
    private ChipGroup chip_group_filters;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        scrollable = view.findViewById(R.id.scrollView);
        loading_image = view.findViewById(R.id.loading_image);


        catLoadingProgress = view.findViewById(R.id.cat_loading_progress);
        countLoadingProgress = view.findViewById(R.id.count_loading_progress);
        ingLoadingProgress = view.findViewById(R.id.ing_loading_progress);

        networkUtils = new NetworkUtils(requireContext(), new NetworkUtils.NetworkStateListener() {
            @Override
            public void onNetworkAvailable() {
                Log.d("TAG", "Network is available");
            }

            @Override
            public void onNetworkLost() {
                Log.d("TAG", "Network is lost");
            }
        });
        networkUtils.registerNetworkCallback();

        if (!networkUtils.isNetworkAvailable(requireContext())) {
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

        categoryChip = view.findViewById(R.id.chip_category);
        countryChip = view.findViewById(R.id.chip_country);
        ingredientChip = view.findViewById(R.id.chip_ingredient);
        chip_group_filters = view.findViewById(R.id.chip_group_filters);

        if(scrollable.getVisibility()==View.VISIBLE){
            invisibleCategory();
            invisibleIngredient();
            invisibleCountry();
        }

        editText.setOnClickListener(v -> {
            chip_group_filters.setVisibility(View.VISIBLE);
            clearPreviousFilter();
        });

        categoryChip.setOnClickListener(v -> {
            selectedFilter = "category";
            resetChips();
            categoryChip.setChecked(true);
            updateSearchHint();
            NamesAdapter.selected = "category";
            clearPreviousFilter();
        });

        countryChip.setOnClickListener(v -> {
            selectedFilter = "country";
            resetChips();
            countryChip.setChecked(true);
            updateSearchHint();
            NamesAdapter.selected = "country";
            clearPreviousFilter();
        });

        ingredientChip.setOnClickListener(v -> {
            selectedFilter = "ingredient";
            resetChips();
            ingredientChip.setChecked(true);
            updateSearchHint();
            NamesAdapter.selected = "ingredient";
            clearPreviousFilter();
        });

        loadCategories();

        recyclerListener(category_recycler);
        recyclerListener(country_recycler);

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
                .map(Object::toString)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::filters, Throwable::printStackTrace);

        return view;
    }

    private void filters(String s) {
        switch (selectedFilter) {
            case "meal":
                filterNames(s);
                break;
            case "category":
                filterCategory(s);
                break;
            case "country":
                filterCountry(s);
                break;
            case "ingredient":
                filterIngredient(s);
                break;
        }
    }

    private void filterCategory(String query) {
        List<String> filter = new ArrayList<>();
        if (!query.isEmpty()) {
            Disposable disposableCountry = viewModel.getCategoriesList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(filtered -> {
                        for (Data data:filtered) {
                            if (data.getIngredient().toLowerCase().contains(query.toLowerCase()) ||
                                    data.getStrMeal().toLowerCase().contains(query.toLowerCase())) {
                                    filter.add(data.getIngredient());
                            }

                        }
                        namesAdapter.updateData(filter);
                        viewModel.updateFilteredNames(filter);
                        recyclerMealsName.setVisibility(filtered.isEmpty() ? View.GONE : View.VISIBLE);
                    }, throwable -> {
                        Log.e("TAG", "Error fetching allCategories", throwable);
                    });
        }
        else {
            recyclerMealsName.setVisibility(View.GONE);
        }
    }
    private void filterCountry(String query) {
        if (!query.isEmpty()) {
            Disposable disposableCountry = viewModel.getCountriesList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(filtered -> {
                        if(!filtered.isEmpty()) {
                            List<String> filter = new ArrayList<>();
                            for (Data data : filtered) {
                                if (data.getIngredient().toLowerCase().contains(query.toLowerCase()) ||
                                        data.getStrMeal().toLowerCase().contains(query.toLowerCase())) {
                                    filter.add(data.getIngredient());
                                }
                            }
                            namesAdapter.updateData(filter);
                            viewModel.updateFilteredNames(filter);
                            recyclerMealsName.setVisibility(filtered.isEmpty() ? View.GONE : View.VISIBLE);
                        }else{
                            loadCountries();
                        }
                    }, throwable -> {
                        Log.e("TAG", "Error fetching allCategories", throwable);
                    });
        }
        else {
            recyclerMealsName.setVisibility(View.GONE);
        }
    }
    private void filterIngredient(String query) {
        if (!query.isEmpty()) {
            Disposable disposableCountry = viewModel.getIngredientsList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(filtered -> {
                        if(!filtered.isEmpty()){
                            List<String> filter = new ArrayList<>();
                            for (Data data:filtered) {
                                if (data.getIngredient().toLowerCase().contains(query.toLowerCase()) ||
                                        data.getStrMeal().toLowerCase().contains(query.toLowerCase())) {
                                    filter.add(data.getIngredient());
                                }
                            }
                            namesAdapter.updateData(filter);
                            viewModel.updateFilteredNames(filter);
                            recyclerMealsName.setVisibility(filtered.isEmpty() ? View.GONE : View.VISIBLE);
                        }else{
                            loadIngredient();
                        }
                    }, throwable -> {
                        Log.e("TAG", "Error fetching allCategories", throwable);
                    });
        }
        else {
            recyclerMealsName.setVisibility(View.GONE);
        }
    }
    private void filterNames(String query) {
        List<String> filtered = new ArrayList<>();
        if (!query.isEmpty()) {
            if (query.length() == 1) {
                presenter.getProducts("letter", query);
            }
            Disposable disposable = viewModel.getAllNames()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(names -> {
                        if (names != null) {
                            for (String name : names) {
                                if (name.toLowerCase().contains(query.toLowerCase())) {
                                    filtered.add(name);
                                }
                            }
                        }
                        namesAdapter.updateData(filtered);
                        viewModel.updateFilteredNames(filtered);
                        recyclerMealsName.setVisibility(filtered.isEmpty() ? View.GONE : View.VISIBLE);
                    }, throwable -> {
                        Log.e("TAG", "Error fetching allNames", throwable);
                    });
        }
        else {
            recyclerMealsName.setVisibility(View.GONE);
        }
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
            Log.d("TAG", "showData: adding");
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
    private void setupRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        int horizontalSpacing = (int) (10 * getResources().getDisplayMetrics().density);
        int verticalSpacing = (int) (5 * getResources().getDisplayMetrics().density);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(horizontalSpacing, verticalSpacing));
    }

    static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private final int horizontalSpacing;
        private final int verticalSpacing;
        public GridSpacingItemDecoration(int horizontalSpacing, int verticalSpacing) {
            this.horizontalSpacing = horizontalSpacing - 10;
            this.verticalSpacing = verticalSpacing;
        }
        @Override
        public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
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
                        if (recyclerView == category_recycler) {
                            isLoading = false;
                            loadCountries();
                        } else if (recyclerView == country_recycler) {
                            isLoading = false;
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

    private void loadCategories(){
        Disposable disposableCategory = viewModel.getCategoriesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> {
                    if (categories.isEmpty()) {
                        Log.i("TAG", "Categories list is empty. Fetching categories...");
                        presenter.getCategories("categories", null);
                        categoryAdapter.updateData(categories);
                        categoryAdapter.notifyDataSetChanged();
                    } else {
                        if(category_recycler.getVisibility()==View.GONE){
                            visibleCategory();
                            visibleCountry();
                            visibleIngredient();
                        }
                        categoryAdapter.updateData(categories);
                        categoryAdapter.notifyDataSetChanged();
                        Log.i("TAG", "Categories list is already populated.");
                    }
                }, throwable -> {
                    Log.e("TAG", "Error fetching categories", throwable);
                });
    }
    private void loadCountries() {
        Disposable count = viewModel.getCountriesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(countries -> {
                    if (countries.isEmpty() && !isLoading) {
                        isLoading = true;
                        presenter.getCountries("countries", null);
                        countryAdapter.updateData(countries);
                    }else{
                        countryAdapter.updateData(countries);
                        countryAdapter.notifyDataSetChanged();
                    }
                }, throwable -> {
                    Log.e("TAG", "Error fetching countries", throwable);
                });
    }
    private void loadIngredient() {
        Disposable ing = viewModel.getIngredientsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ingredients -> {
                    if (ingredients.isEmpty() && !isLoading) {
                        Log.d("TAG", "loadIngredient: yes");
                        isLoading = true;
                        presenter.getIngredient("ingredients", null);
                        ingredientAdapter.updateData(ingredients);
                    }else{
                        Log.d("TAG", "loadIngredient: no");
                        ingredientAdapter.updateData(ingredients);
                        ingredientAdapter.notifyDataSetChanged();
                    }
                }, throwable -> {
                    // Handle any errors in the Observable
                    Log.e("TAG", "Error fetching ingredients", throwable);
                });
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
    private void resetChips() {
        categoryChip.setChecked(false);
        countryChip.setChecked(false);
        ingredientChip.setChecked(false);
    }
    private void updateSearchHint() {
        switch (selectedFilter) {
            case "category":
                editText.setHint("Search by Category...");
                break;
            case "country":
                editText.setHint("Search by Country...");
                break;
            case "ingredient":
                editText.setHint("Search by Ingredient...");
                break;
            default:
                editText.setHint("Search for Meals...");
        }
    }
    private void clearPreviousFilter() {
        if (!editText.getText().toString().isEmpty()) {
            editText.setText("");
        }
        recyclerMealsName.setVisibility(View.GONE);
    }

}
