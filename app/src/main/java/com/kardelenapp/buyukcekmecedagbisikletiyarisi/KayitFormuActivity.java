package com.kardelenapp.buyukcekmecedagbisikletiyarisi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.entity.mime.Header;
import io.paperdb.Paper;

public class KayitFormuActivity extends AppCompatActivity {

    private EditText editText_adi;
    private EditText editText_telefon;
    private Spinner spinner_dogum;
    private EditText editText_email;
    private Spinner spinner_il;
    private EditText editText_lisansno;
    private Spinner spinner_secin;
    private EditText editText_kulubu;

    private ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banka_faizi_hesaplama);
        Paper.init(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Kayıt Formu");

        progress = new ProgressDialog(this);
        progress.setMessage("Kayıt Gönderiliyor...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);

        editText_adi = (EditText) findViewById(R.id.editText_adi);
        editText_telefon = (EditText) findViewById(R.id.editText_telefon);
        spinner_dogum = (Spinner) findViewById(R.id.spinner_dogum);
        editText_email = (EditText) findViewById(R.id.editText_email);
        spinner_il = (Spinner) findViewById(R.id.spinner_il);
        editText_lisansno = (EditText) findViewById(R.id.editText_lisansno);
        spinner_secin = (Spinner) findViewById(R.id.spinner_secim);
        editText_kulubu = (EditText) findViewById(R.id.editText_kulubu);

        /**** array düzenleme ****/
        List<String> list_dogumTarihleri =  new ArrayList<String>();
        for (int i = 1900; i< 2018; i ++){
            list_dogumTarihleri.add(String.valueOf(i));
        }

        List<String> list_il =  new ArrayList<String>();
        list_il = new TextListReader(this,"iller.txt").getList();

        List<String> list_secim =  new ArrayList<String>();
        list_secim = new TextListReader(this,"secim.txt").getList();

        /**** adapterler ****/
        ArrayAdapter<String> adapter_dogumTarihleri = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, list_dogumTarihleri);
        adapter_dogumTarihleri.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter_il = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, list_il);
        adapter_il.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter_secim = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, list_secim);
        adapter_secim.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        /*** adapter set ***/

        spinner_dogum.setAdapter(adapter_dogumTarihleri);
        spinner_il.setAdapter(adapter_il);
        spinner_secin.setAdapter(adapter_secim);


        spinner_dogum.setSelection(0);
        spinner_il.setSelection(0);
        spinner_secin.setSelection(0);


        LinearLayout layout = (LinearLayout) findViewById(R.id.adsContainer);
        AdsController adsController = new AdsController(this);
        adsController.loadBanner(layout);

        Button button= (Button) findViewById(R.id.button_kaydol);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String adi;
                String telefon;
                String dogum;
                String email;
                String il;
                String lisansno;
                String secim;
                String kulubu;

                adi = editText_adi.getText().toString().trim();
                telefon = editText_telefon.getText().toString().trim();
                dogum = spinner_dogum.getSelectedItem().toString();
                email = editText_email.getText().toString().trim();
                il = spinner_il.getSelectedItem().toString();
                lisansno = editText_lisansno.getText().toString().trim();
                secim = spinner_secin.getSelectedItem().toString();
                kulubu = editText_kulubu.getText().toString().trim();

                if (adi.equals("")){
                    alarm("Lütfen isim girin.");
                    return;
                }
                if (telefon.equals("")){
                    alarm("Lütfen telefon girin.");
                    return;
                }
                if (email.equals("")){
                    alarm("Lütfen email girin.");
                    return;
                }
                if (lisansno.equals("")){
                    alarm("Lütfen lisans no girin.");
                    return;
                }
                if (kulubu.equals("")){
                    alarm("Lütfen kulüp girin.");
                    return;
                }

                RequestParams params = new RequestParams();
                params.add("adi",adi);
                params.add("telefon",telefon);
                params.add("dogum",dogum);
                params.add("email",email);
                params.add("il",il);
                params.add("lisansno",lisansno);
                params.add("secim",secim);
                params.add("kulubu",kulubu);

                AsyncHttpClient client = new AsyncHttpClient();
                client.post("http://46.101.203.167/form/selam.py",params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started
                        progress.show();

                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                        Toast.makeText(getApplicationContext(), "Kaydınız Yapılmıştır. Teşekkürler.", Toast.LENGTH_SHORT).show();


                        editText_adi.setText("");
                        editText_telefon.setText("");;
                        spinner_dogum.setSelection(0);
                        editText_email.setText("");
                        spinner_il.setSelection(0);
                        editText_lisansno.setText("");;
                        spinner_secin.setSelection(0);
                        editText_kulubu.setText("");

                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                        Toast.makeText(getApplicationContext(), "Hata!", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });





            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {

            case R.id.about:
                Intent myIntent = new Intent(this, Hakkinda.class);
                this.startActivity(myIntent);

                break;
        }
        return true;
    }

    public void alarm(String str){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getApplicationContext());
        }
        builder.setTitle("Uyarı!")
                .setMessage(str)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
