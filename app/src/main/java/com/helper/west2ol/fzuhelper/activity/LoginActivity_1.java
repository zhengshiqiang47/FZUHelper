package com.helper.west2ol.fzuhelper.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.crashlytics.android.Crashlytics;
import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.Exam;
import com.helper.west2ol.fzuhelper.bean.User;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.util.CalculateUtil;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;
import com.helper.west2ol.fzuhelper.util.HttpUtil;
import com.wang.avi.AVLoadingIndicatorView;

import io.fabric.sdk.android.Fabric;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * login activity by linyoung
 * 测试学号123456  密码123456
 */
public class LoginActivity_1 extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final String TAG ="LoginActivity_1" ;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.
    private AutoCompleteTextView mAccountView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private Switch mRem_passwords;
    private AVLoadingIndicatorView loadingView;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    //从Logincheck.asp获取的id和num

    private String id_2;//从LOGIN_CHK_XS获取,后面获取web信息的唯一标识码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login_1);
        //沉浸状态栏
        setLayout();
        // Set up the login form.
        mAccountView = (AutoCompleteTextView) findViewById(R.id.account);
        populateAutoComplete();

        loadingView = (AVLoadingIndicatorView) findViewById(R.id.login_loading);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                Log.i(TAG, "onEditorAction: Id:"+id);
                if ( id == EditorInfo.IME_NULL||id==6) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mRem_passwords = (Switch) findViewById(R.id.rem_passwords);

        //记住密码功能
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            mAccountView.setText(account);
            mPasswordView.setText(password);
            mRem_passwords.setChecked(true);
        }
    }

    private void setLayout() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mAccountView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid account address.
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            login(account,password);
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            if (show) {
                TranslateAnimation translateAnimation = new TranslateAnimation(0, -mLoginFormView.getRight(), 0, 0);
                translateAnimation.setFillAfter(true);
                translateAnimation.setDuration(400l);
                translateAnimation.setInterpolator(new AccelerateInterpolator());
                mLoginFormView.setAnimation(translateAnimation);
                mLoginFormView.setAnimation(translateAnimation);
                TranslateAnimation translateAnimation2 = new TranslateAnimation(loadingView.getRight(),0 , 0, 0);
                translateAnimation2.setFillAfter(true);
                translateAnimation2.setDuration(400l);
                translateAnimation2.setInterpolator(new AccelerateInterpolator());
                loadingView.setAnimation(translateAnimation2);
                loadingView.show();
            }else {
                TranslateAnimation translateAnimation = new TranslateAnimation(-mLoginFormView.getRight(), 0, 0, 0);
                translateAnimation.setFillAfter(true);
                translateAnimation.setDuration(400l);
                translateAnimation.setInterpolator(new AccelerateInterpolator());
                mLoginFormView.setAnimation(translateAnimation);
                TranslateAnimation translateAnimation2 = new TranslateAnimation(0, loadingView.getRight(), 0, 0);
                translateAnimation2.setFillAfter(true);
                translateAnimation2.setDuration(400l);
                translateAnimation2.setInterpolator(new AccelerateInterpolator());
                loadingView.setAnimation(translateAnimation2);
                loadingView.hide();
            }
//        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity_1.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mAccountView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void login(final String muser,final String passwd){
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                User user=new User();
                user.setFzuPasssword(passwd);
                user.setFzuAccount(muser);
                DBManager dbManager=new DBManager(getApplicationContext());
                List<User> users=dbManager.queryUserList();
                boolean isExist=false;
                for (User resUser : users) {
                    Log.i(TAG,"user "+resUser.getFzuAccount());
                    if (resUser.getFzuAccount().equals(user.getFzuAccount())){
                        resUser.setFzuPasssword(user.getFzuPasssword());
                        resUser.setIsLogin(true);
                        dbManager.updateUser(resUser);
                        isExist=true;
                        break;
                    }
                }
                if (!isExist) {
                    user.setIsLogin(true);
                    dbManager.insertUser(user);
                }
                DefaultConfig.get().setUserAccount(user.getFzuAccount());
                String loginResponse =null;
                try {
                    loginResponse = HttpUtil.Login(getApplicationContext(),user);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onNext(loginResponse);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "登录失败,请检查网络连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String loginResponse) {
                switch (loginResponse){
                    case "网络错误":

                        break;
                    case "密码错误":
                        Log.i(TAG, "密码错误");
                        break;
                    case "登录成功":
                        editor = pref.edit();
                        if (mRem_passwords.isChecked()) {
                            editor.putBoolean("remember_password", true);
                            editor.putString("account", muser);
                            editor.putString("password", passwd);
                        } else {
                            editor.clear();
                        }
                        editor.apply();
                        Intent intent = new Intent(LoginActivity_1.this , MainContainerActivity.class);
                        intent.putExtra("id" , id_2);
                        startActivity(intent);
                        finish();
                        return;
                    default:
                        Log.i(TAG,"未知错误");
                }
                Toast.makeText(getApplicationContext(), loginResponse, Toast.LENGTH_SHORT).show();
                mPasswordView.setError(loginResponse);
                mPasswordView.requestFocus();
                showProgress(false);
            }
        });
    }
}