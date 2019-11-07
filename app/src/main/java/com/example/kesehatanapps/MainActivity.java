package com.example.kesehatanapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = MainActivity.class.getSimpleName();
    private TextView textViewjson;
    private Button btnScan;
    private ProgressDialog pDialog;
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewjson = findViewById(R.id.jsontext);
        btnScan = findViewById(R.id.buttonScan);

        btnScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject object = new JSONObject(result.getContents());
                } catch (JSONException e) {
                    e.printStackTrace();
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url = "https://rajabrawijaya.ub.ac.id/api/getInfo19?data=" + result.getContents() + "&key=022";

                    pDialog = new ProgressDialog(this);
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(false);
                    showpDialog();


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());

                            try {
                                String nim = response.getString("nim");
                                String name = response.getString("nama");
                                String JK = response.getString("jenis_kelamin");
                                String Fakultas = response.getString("fakultas");
                                String riwayatPenyakit = response.getString("riwayat_penyakit");
                                String alergiObat = response.getString("alergi_obat");
                                String alergiMakanan = response.getString("alergi_makanan");

                                jsonResponse = "";
                                jsonResponse += "NIM: " + nim + "\n\n";
                                jsonResponse += "Nama: " + name + "\n\n";
                                jsonResponse += "Jenis Kelamin: " + JK + "\n\n";
                                jsonResponse += "Fakultas: " + Fakultas + "\n\n";
                                jsonResponse += "Riwayat Penyakit: " + riwayatPenyakit + "\n\n";
                                jsonResponse += "Alergi Obat: " + alergiObat + "\n\n";
                                jsonResponse += "Alergi Makanan: " + alergiMakanan + "\n\n";

                                textViewjson.setText(jsonResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hidepDialog();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            hidepDialog();
                        }
                    });

                    AppController.getInstance().addToRequestQueue(jsonObjectRequest);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.input_nim:
                Intent intent = new Intent(MainActivity.this, ManualNIMActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}