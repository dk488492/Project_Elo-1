package group.hashtag.projectelo.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import group.hashtag.projectelo.Handlers.UserHandler;
import group.hashtag.projectelo.R;

/**
 * Created by amant on 10/02/18.
 */

public class UserProfile extends AppCompatActivity {
    TextView title;
    TextView usernameText;
    TextView wishlistCounter;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;

    private ImageButton messages;
    private ImageButton reviews;
    private LinearLayout btnWishlist;
    private CircleImageView displayImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Todo: May have to figure out a better way of storing the name coz it is updating with a long delay


        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.user_profile);

        mDatabase2 = FirebaseDatabase.getInstance().getReference("User_device").child("Wishlist").child(auth.getUid());

        messages = findViewById(R.id.imageButton);
        reviews= findViewById(R.id.imageButton2);
        displayImageView = (CircleImageView) findViewById(R.id.userDisplayPic);


        title = findViewById(R.id.title_toolbar);
        usernameText = findViewById(R.id.usernameTextView);
        wishlistCounter = findViewById(R.id.wishlistCounter);
        btnWishlist = findViewById(R.id.btn_wish);

        mDatabase.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserHandler user = dataSnapshot.getValue(UserHandler.class);
                usernameText.setText(user.getName());
                loadDisplayPics(user.getDisplayPicss());
                Log.e(UserProfile.class.getCanonicalName(), "Username: " + user.getDisplayPicss() + ", email " + user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(UserProfile.class.getCanonicalName(), "Failed to read value.", error.toException());
            }
        });

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long wlCounter = dataSnapshot.getChildrenCount();
                wishlistCounter.setText(Long.toString(wlCounter));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wishlist = new Intent(UserProfile.this, Wishlist.class);
                startActivity(wishlist);
            }
        });

        Typeface ReemKufi_Regular = Typeface.createFromAsset(getAssets(), "fonts/ReemKufi-Regular.ttf");


        Toolbar toolbar = findViewById(R.id.toolbar);
        title.setTypeface(ReemKufi_Regular);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    //TODO: Need to work on settings page which will double as profile edit page.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_wheel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void loadDisplayPics(String url){
        Picasso.get().load(url).fit().error(R.drawable.cover).placeholder(R.drawable.male).into(displayImageView);

    }
}
