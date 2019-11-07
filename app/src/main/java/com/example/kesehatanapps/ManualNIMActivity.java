package com.example.kesehatanapps;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ManualNIMActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtNIM;
    Button btnSubmit;
    TextView hasil;
    private ProgressDialog pDialog;
    private String jsonResponse;
    private static String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_nim);

        edtNIM = findViewById(R.id.edt_nim);
        btnSubmit = findViewById(R.id.btn_submit);
        hasil = findViewById(R.id.text);

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            String nim = edtNIM.getText().toString().trim();
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://rajabrawijaya.ub.ac.id/api/getInfoNim19?nim=" + nim + "&key=022";

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

                        hasil.setText(jsonResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    hidepDialog();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   hasil.setText("Data tidak ditemukan");
                    hidepDialog();
                }
            });

            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
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
}
