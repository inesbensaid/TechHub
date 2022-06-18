package com.example.techhub;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Payment extends AppCompatActivity {

    DatabaseReference ref;
    int val;

    private static final int REQUEST_CODE = 1234;
    private final String API_GET_TOKEN="https://192.168.100.7/braintree/main.php";
    private final String API_CHECKOUT="https://192.168.100.7/braintree/checkout.php";

    private String token, amount;
    HashMap<String,String> paramsHash;
    Button btn_pay;
    ImageView BackButton;
    EditText edit_amount;
    LinearLayout group_payment;
    PaymentInfo paymentInfo = new PaymentInfo();
    String message;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = currentUser.getUid();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("message");


        token = "sandbox_d58mdwpt_499xy3t7mc3rbg5c";
        edit_amount=(EditText)findViewById(R.id.amount);
        btn_pay=(Button) findViewById(R.id.pay);
        BackButton=(ImageView) findViewById(R.id.back);
        group_payment=(LinearLayout)findViewById(R.id.layout);


        ref = FirebaseDatabase.getInstance().getReference().child("Payments");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = currentUser.getUid();


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent1 = new Intent(Payment.this, HomePage.class);
                        startActivity(intent1);
                        break;

                    case R.id.add:
                        Intent intent2 = new Intent(Payment.this, projects.class);
                        startActivity(intent2);
                        break;

                    case R.id.account:
                        Intent intent3 = new Intent(Payment.this, Requests.class);
                        startActivity(intent3);
                        break;
                    case R.id.setting:
                        Intent intent4 = new Intent(Payment.this, MorePage.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });


        handleSSLHandshake();
        new getToken().execute();

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPayment();
            }
        });




        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private class getToken extends AsyncTask {
        ProgressDialog mDailog;

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient<HttpClient<HttpClient>> client=new HttpClient<>();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(final String responseBody) {
                    mDailog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            group_payment.setVisibility(View.VISIBLE);
                            token=responseBody;
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    mDailog.dismiss();
                    Log.d("Err",exception.toString());
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDailog=new ProgressDialog(Payment.this,android.R.style.Theme_DeviceDefault_Light_Dialog);
            mDailog.setCancelable(false);
            mDailog.setMessage("Loading Wallet, Please Wait");
            mDailog.show();
        }

        @Override
        protected void onPostExecute(Object o){
            super.onPostExecute(o);
        }
    }

    private void submitPayment(){
        String payValue=edit_amount.getText().toString().trim();
        if(!payValue.isEmpty()) {
            val = Integer.parseInt(payValue); }
        if(!payValue.isEmpty())
        {
            if (val>0 && val<=10000) {
                DropInRequest dropInRequest = new DropInRequest().clientToken(token);
                startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
            } else
                Toast.makeText(this, "Enter a valid amount for payment [1,10000]", Toast.LENGTH_SHORT).show();

        }
        else
            Toast.makeText(this, "Enter a valid amount for payment", Toast.LENGTH_SHORT).show();

    }

    private void sendPayments(){
        RequestQueue queue= Volley.newRequestQueue(Payment.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, API_CHECKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.toString().contains("Successful")){
                            paymentInfo.setFrom(user_id);
                            paymentInfo.setTo(message);
                            paymentInfo.setAmount(amount);
                            ref.push().setValue(paymentInfo);
                            Toast.makeText(Payment.this, "Payment Success", Toast.LENGTH_SHORT).show();
                            finish();
                            //System.out.println("Successful");
                        }
                        else {
                            Toast.makeText(Payment.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                            //System.out.println("Failed");
                        }
                        Log.d("Response",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Err",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(paramsHash==null)
                    return null;
                Map<String,String> params=new HashMap<>();
                for(String key:paramsHash.keySet())
                {
                    params.put(key,paramsHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("Content-type","application/x-www-form-urlencoded");
                return params;
            }
        };
        RetryPolicy mRetryPolicy=new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        queue.add(stringRequest);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== REQUEST_CODE){
            if(resultCode==RESULT_OK)
            {
                DropInResult result=data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce= result.getPaymentMethodNonce();
                String strNounce=nonce.getNonce();
                if(!edit_amount.getText().toString().isEmpty())
                {
                    amount=edit_amount.getText().toString();
                    paramsHash=new HashMap<>();
                    paramsHash.put("amount",amount);
                    paramsHash.put("nonce",strNounce);

                    sendPayments();
                }
                else {
                    Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
            else if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(this, "User canceled", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Exception error=(Exception)data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("Err",error.toString());
            }
        }

    }

    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
}
