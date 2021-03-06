/* Group: Aoong Aoong
 * Members: Tanaporn 5888124, Kanjanaporn 5888178, Patipon 5888218
 */
package com.example.earthpatipon.recipeschef;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.earthpatipon.recipeschef.database.AppDatabase;
import com.example.earthpatipon.recipeschef.entity.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.input_username) EditText usernameInput;
    @BindView(R.id.input_password) EditText passwordInput;
    @BindView(R.id.input_confirmPassword) EditText confirmPasswordInput;
    @BindView(R.id.button_signup) Button signupButton;

    private ProgressDialog progressDialog;
    private String userName;
    private String passWord;
    private String confirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this, R.style.AppTheme_White_Dialog);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    private void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        this.userName = usernameInput.getText().toString();
        this.passWord = passwordInput.getText().toString();

        // Call thread to access DAO and check username is taken or not
        new insertAsyncTask(this.userName, this.passWord).execute();
    }


    private void onSignupSuccess() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User(userName, passWord);
                AppDatabase.getInstance(getApplicationContext()).userDao().insert(user);
            }
        }).start();

        signupButton.setEnabled(true);

        Toast.makeText(getApplicationContext(), "Sign up Successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish(); // this method is to call the rest of android lifecycle component i.e, onDestroy
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void onSignupFailed() {

        Toast.makeText(getApplicationContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
        signupButton.setEnabled(true);
    }

    private boolean validate() {

        boolean valid = true;

        userName = usernameInput.getText().toString();
        passWord = passwordInput.getText().toString();
        confirmPassword = confirmPasswordInput.getText().toString();

        if (userName.isEmpty() || userName.length() < 3) {
            usernameInput.setError("at least 3 characters");
            valid = false;
        } else {
            usernameInput.setError(null);
        }

        if (passWord.isEmpty() || passWord.length() < 4 || passWord.length() > 10) {
            passwordInput.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordInput.setError(null);
        }

        if (confirmPassword.isEmpty() || confirmPassword.length() < 4 || confirmPassword.length() > 10 || !(confirmPassword.equals(passWord))) {
            confirmPasswordInput.setError("Password Do not match");
            valid = false;
        } else {
            confirmPasswordInput.setError(null);
        }

        return valid;
    }

    private class insertAsyncTask extends AsyncTask<String, Void, Boolean> {

        private boolean exist;
        private String userName;
        private String passWord;

        insertAsyncTask(String username, String password) {
            this.userName = username;
            this.passWord = password;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            User temp = AppDatabase.getInstance(getApplicationContext()).userDao().findByName(userName);
            if(temp == null)
                exist = false;
            else
                exist = true;
            return exist;
        }

        @Override
        protected void onPostExecute(Boolean exist) {
            progressDialog.dismiss();
            if(!exist){
               onSignupSuccess();
            }
            else{
               Toast.makeText(getApplicationContext(), "Username already exist", Toast.LENGTH_SHORT).show();
               //can set some interval
               signupButton.setEnabled(true);
            }
        }
    }
}
