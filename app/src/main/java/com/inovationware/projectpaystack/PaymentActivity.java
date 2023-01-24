package com.inovationware.projectpaystack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import static com.inovationware.generalmodule.W.*;
public class PaymentActivity extends AppCompatActivity {
    Feedback feedback;

    Button buttonPayNow;
    TextView textAmount;
    AutoCompleteTextView dropService;

    RequestQueue queue;
    Map<String, String> headers;
    JSONObject body;

    private static final String[] services = new String[]{"VSAT-1", "VSAT-2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        feedback = new Feedback(getApplicationContext());

        SetupUI();

        queue = Volley.newRequestQueue(getApplicationContext());
        queue.start();
    }

    private void SetupUI() {
        setTitle(getString(R.string.PaymentsLabel));

        dropService = findViewById(R.id.dropService);
        ArrayAdapter<String> servicesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.service_drop_down, services);
        dropService.setAdapter(servicesAdapter);

        textAmount = findViewById(R.id.textAmount);
        textAmount.setInputType(InputType.TYPE_CLASS_NUMBER);

        buttonPayNow = findViewById(R.id.buttonPayNow);
        buttonPayNow.setOnClickListener(buttonPayNow_click);
    }

    View.OnClickListener buttonPayNow_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isEmpty(dropService) || content(dropService).equalsIgnoreCase("What do you want to pay for?")){feedback.toast("Select service to pay for"); return;}
            if (isEmpty(textAmount)){feedback.toast("Enter amount to pay"); return;}
            feedback.toast("Process in progress, one moment please");

            headers = new HashMap<>();
            headers.put("Authorization", getString(R.string.Authorization));
            headers.put("Content-Type", "application/json");
            body = new JSONObject();
            try {
                body.put("amount", String.valueOf(Integer.parseInt(content(textAmount)) * 100));
                body.put("email", getString(R.string.Email));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            queue.add(request);
        }
    };

    StringRequest request = new StringRequest(
            Request.Method.POST,
            "https://api.paystack.co/transaction/initialize",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String authorization_url = null;
                    String reference;
                    try {
                        JSONObject object = new JSONObject(response);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        if (status.equalsIgnoreCase("true") && message.equalsIgnoreCase("Authorization URL created")){
                            JSONObject data = object.getJSONObject("data");
                            authorization_url = data.getString("authorization_url");
                            reference = data.getString("reference"); // this will be needed later
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (authorization_url != null){
                        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(authorization_url));
                        startActivity(intent);
                    }
                    else{
                        feedback.toast("There was an error. Please try again after a while.");
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    feedback.toast("onErrorResponse");
                }
            }) {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return headers;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            try {
                return body.toString().getBytes("utf-8");
            } catch (UnsupportedEncodingException uee) {
                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using % s", body, " utf - 8");
                return null;
            }
        }
    };

}