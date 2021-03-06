/* Group: Aoong Aoong
 * Members: Tanaporn 5888124, Kanjanaporn 5888178, Patipon 5888218
 */
package com.example.earthpatipon.recipeschef;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;

import com.example.earthpatipon.recipeschef.adapter.HomeAdapter;
import com.example.earthpatipon.recipeschef.adapter.LikeAdapter;
import com.example.earthpatipon.recipeschef.adapter.SearchAdapter;
import com.example.earthpatipon.recipeschef.fragment.HomeFragment;
import com.example.earthpatipon.recipeschef.fragment.ProfileFragment;
import com.example.earthpatipon.recipeschef.fragment.SearchFragment;
import com.example.earthpatipon.recipeschef.database.AppDatabase;
import com.example.earthpatipon.recipeschef.entity.Recipe;
import com.example.earthpatipon.recipeschef.entity.RecipeCard;
import com.example.earthpatipon.recipeschef.entity.User;
import com.example.earthpatipon.recipeschef.entity.UserLike;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private TextView username;

    private HomeAdapter homeAdapter;
    private SearchAdapter searchAdapter;
    private LikeAdapter likeAdapter;

    private List<RecipeCard> cardList;
    private List<RecipeCard> likeCardList;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCurrentUser(); // get User session
        initCardList(); // get list of Recipe
        initToolbar(); // get Toolbar
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCardList(this, cardList, user.getUserID());
        likeCardList = updateLikeCardList(this, cardList, user.getUserID());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item) == true) {
            return true;
        }
        return onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                getSupportActionBar().setTitle("Home");
                replaceFragment(HomeFragment.class);
                break;
            case R.id.search:
                getSupportActionBar().setTitle("Search");
                replaceFragment(SearchFragment.class);
                break;
            case R.id.profile:
                getSupportActionBar().setTitle("Profile");
                replaceFragment(ProfileFragment.class);
                break;
            case R.id.logout:
                Intent logout_intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(logout_intent);
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getCurrentUser() {
        String userName = "";

        final String sender = this.getIntent().getExtras().getString("SENDER_KEY");
        if (sender != null) {
            Intent intent = getIntent();
            userName = intent.getStringExtra("NAME_KEY");
        }

        user = AppDatabase.getInstance(getApplicationContext()).userDao().findByName(userName);
    }

    private void initCardList() {

        cardList = new ArrayList<>();
        likeCardList = new ArrayList<>();


        List<Recipe> recipes = AppDatabase.getInstance(this).recipeDao().getAllRecipe();

        for(Recipe r: recipes){
            RecipeCard card = new RecipeCard();
            card.setId(r.getRecipeID());
            card.setCardName((r.getRecipeName()));
            card.setIsLiked(0);
            this.cardList.add(card);
        }

        likeCardList = updateLikeCardList(this, cardList, user.getUserID());

        homeAdapter = new HomeAdapter(this, cardList, user);
        searchAdapter = new SearchAdapter(this, cardList);
        likeAdapter = new LikeAdapter(this, likeCardList, user);

        replaceFragment(HomeFragment.class);
    }

    public static void updateCardList(Context context, List<RecipeCard> cardList, int userID) {

        List<UserLike> ulList = AppDatabase.getInstance(context).userLikeDao().findByUserID(userID);
        // reset isLiked of each card
        for (RecipeCard card : cardList) {
            card.setIsLiked(0);
        }
        // set isLiked of each card
        for (UserLike ul : ulList) {
            int likedRecipeID = ul.getRecipeID();
            for (RecipeCard card : cardList) {
                if (card.getId() == likedRecipeID) {
                    card.setIsLiked(1);
                    break;
                }
            }
        }
    }

    public static List<RecipeCard> updateLikeCardList(Context context, List<RecipeCard> cardList, int userID) {

        List<RecipeCard> likeCardList = new ArrayList<>();
        List<UserLike> ulList = AppDatabase.getInstance(context).userLikeDao().findByUserID(userID);
        for (UserLike ul : ulList) {
            int likedRecipeID = ul.getRecipeID();
            for (RecipeCard card : cardList) {
                if (card.getId() == likedRecipeID) {
                    likeCardList.add(card);
                    break;
                }
            }
        }
        return  likeCardList;
    }

    private void initToolbar(){

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.action_open, R.string.action_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeader = navigationView.getHeaderView(0);

        username = navigationHeader.findViewById(R.id.user_name);
        username.setText(user.getUserName());
    }


    private void replaceFragment(Class fragmentClass) {

        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    public User getUser(){ return user; }

    public List<RecipeCard> getCardList() {
        return cardList;
    }

    public List<RecipeCard> getLikeCardList() {
        return likeCardList;
    }

    public HomeAdapter getHomeAdapter() {
        return homeAdapter;
    }

    public SearchAdapter getSearchAdapter() {
        return searchAdapter;
    }

    public LikeAdapter getLikeAdapter() {
        likeCardList = updateLikeCardList(this, cardList, user.getUserID());
        likeAdapter = new LikeAdapter(this, likeCardList, user);
        return likeAdapter;
    }
}

