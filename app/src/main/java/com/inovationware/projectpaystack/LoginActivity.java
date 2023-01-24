package com.inovationware.projectpaystack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SetupUI();
    }

    private void SetupUI() {
        setTitle(getString(R.string.app_name));
    }
}