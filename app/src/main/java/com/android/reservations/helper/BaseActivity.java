package com.android.reservations.helper;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.reservations.R;
import com.google.android.material.textfield.TextInputEditText;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Boolean isNotEmpty(TextInputEditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError(getString(R.string.empty_field));
            return false;
        } else {
            return true;
        }
    }

    public Boolean isNotEmpty2(AutoCompleteTextView editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError(getString(R.string.empty_field));
            return false;
        } else {
            return true;
        }
    }

    public Boolean isValidEmail(TextInputEditText editText) {
        if (Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches()) {
            return true;
        } else {
            editText.setError(getString(R.string.invalid_email));
            return false;
        }
    }

}
