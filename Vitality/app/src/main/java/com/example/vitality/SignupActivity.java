package com.example.vitality;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vitality.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity
{
    private ActivitySignupBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle saveInstanceStates) {
        super.onCreate(saveInstanceStates);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();

        binding.BackToLoginTextView.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });

        binding.SignupButton.setOnClickListener(v -> {
            ValidationSignup();
        });

    }

    private void ValidationSignup() {
        binding.emailInputText.getText();
        boolean isEmailValid = false;
        if (binding.emailInputText.getText().toString().isEmpty()) {
            //empty
            binding.emailInputTextError.setError("Email could not empty");
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailInputText.getText().toString()).matches()) {
            //invalid format
            binding.emailInputTextError.setError("This is not a valid email address");
            isEmailValid = false;
        } else {
            binding.emailInputTextError.setErrorEnabled(false);
            isEmailValid = true;
        }

        if (isEmailValid) {
            binding.progressBar.setVisibility(View.VISIBLE);


            mAuth.createUserWithEmailAndPassword(binding.emailInputText.getText().toString(), binding.PasswordInputText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");
                                binding.progressBar.setVisibility(View.GONE);
                                Toast.makeText(SignupActivity.this, "Successful Sign Up", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}
