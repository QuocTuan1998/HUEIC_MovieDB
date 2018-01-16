package com.fstyle.androidtrainning.screen.profile;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.fstyle.androidtrainning.R;
import com.fstyle.androidtrainning.model.MovieUser;
import com.fstyle.androidtrainning.screen.signin.SignInActivity;
import com.fstyle.androidtrainning.util.Constant;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ossierra on 12/27/17.
 */

public class ProfileActivity extends Fragment
        implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.text_name)
    TextView nameAccount;

    @BindView(R.id.text_email)
    TextView email;

    @BindView(R.id.image_profile)
    ImageView imageProfile;

    @BindView(R.id.btn_signout)
    Button btnSignout;

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mUserCloudEndPoint;
    private FirebaseDatabase mFirebaseInstance;
    private GoogleApiClient mGoogleApiClient;
    private String mUserName;
    private String mUserEmail;
    private String mUserId;

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        ButterKnife.bind(this, view);
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this.getActivity(), SignInActivity.class));
        } else {
            mUserName = mFirebaseUser.getDisplayName();
            mUserEmail = mFirebaseUser.getEmail();
            if (mFirebaseUser.getPhotoUrl() != null) {
                Glide.with(view.getContext()).load(mFirebaseUser.getPhotoUrl()).into(imageProfile);
            }
            nameAccount.setText(mUserName);
            email.setText(mUserEmail);
            mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                    .enableAutoManage(this.getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();
            //save profile of user to firebase
            saveProfileUser(mUserEmail);
        }
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUserName = Constant.ANONYMOUS;
                startActivity(new Intent(view.getContext(), SignInActivity.class));
            }
        });
        return view;
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, R.string.connection_failed + connectionResult.getErrorMessage());
    }

    private void saveProfileUser(final String userEmail) {
        mFirebaseInstance = FirebaseDatabase.getInstance();

        // store app title to 'app_title' node
        mFirebaseInstance.getReference(Constant.APP_NODE_FIREBASE)
                .setValue(Constant.APP_TITLE_FIREBASE);
        // get reference to 'users' node
        mUserCloudEndPoint = mFirebaseInstance.getReference(Constant.USERS_NODE_FIREBASE);
        // Check for already existed user
        mUserCloudEndPoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isExistUser = false;
                for (DataSnapshot categorySnapshot: dataSnapshot.getChildren()) {
                    MovieUser user = categorySnapshot.getValue(MovieUser.class);
                    if (user.getEmail().equals(userEmail)) {
                        isExistUser = true;
                        break;
                    }
                }
                if (!isExistUser) {
                    createUser();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //        mGoogleApiClient.stopAutoManage(getActivity());
        //        mGoogleApiClient.disconnect();
    }

    private void createUser() {
        if (TextUtils.isEmpty(mUserId)) {
            mUserId = mUserCloudEndPoint.push().getKey();
        }
        MovieUser user = new MovieUser();
        user.setName(mUserEmail);
        user.setName(mUserName);
        ArrayList<HashMap<String, String>> favouriteFilm = new ArrayList<HashMap<String, String>>();

        user.setFavouriteMovies(favouriteFilm);
        mUserCloudEndPoint.child(mUserId).setValue(user);
    }
}
