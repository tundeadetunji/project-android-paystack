package com.inovationware.projectpaystack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inovationware.generalmodule.Feedback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Feedback feedback;
    Button buttonPayForService;
    TextView labelCaption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedback = new Feedback(getApplicationContext());

        SetupUI();

    }

    private void SetupUI() {
        setTitle(getString(R.string.app_name));
        buttonPayForService = findViewById(R.id.buttonPayForService);
        buttonPayForService.setOnClickListener(buttonSample_click);
        labelCaption = findViewById(R.id.labelCaption);
        labelCaption.setText("Kingstar\nPayment Services");
    }


    View.OnClickListener buttonSample_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
        }
    };


}

