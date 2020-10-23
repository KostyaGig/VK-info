package ru.kostya.vkinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import ru.kostya.vkinfo.utils.NewTworkUtils;

import static ru.kostya.vkinfo.utils.NewTworkUtils.generateURL;

public class MainActivity extends AppCompatActivity {
    private EditText searchEd;
    private Button searchBtn;
    private TextView infoTv;
    private TextView errorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEd = findViewById(R.id.ed_search);
        searchBtn = findViewById(R.id.searchBtn);
        infoTv = findViewById(R.id.infoTextView);
        errorTv = findViewById(R.id.errorTv);

        infoTv.setVisibility(View.VISIBLE);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            URL generatedURL;
            @Override
            public void onClick(View view) {
                String edUrl = searchEd.getText().toString();
                if (!TextUtils.isEmpty(edUrl)) {
                     generatedURL = generateURL(edUrl);
                } else {
                    Toast.makeText(MainActivity.this, "Введи id usera", Toast.LENGTH_SHORT).show();
                    return;
                }
                VkAsyncTask vkAsyncTask = new VkAsyncTask();
                vkAsyncTask.execute(generatedURL);
            }

        });
    }
    public static String getResponseFromUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput){
                return scanner.next();
            }
            else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }

    }
    class VkAsyncTask extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {

            String response = null;
            try {
                response = getResponseFromUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s){
            String name = null;
            String secondName = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("response");
                    JSONObject jsonObject2 = array.getJSONObject(0);
                    name = jsonObject2.getString("first_name");
                    secondName = jsonObject2.getString("last_name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String resulting ="Name: " + name + "\n" + "Second name: " + secondName;
                infoTv.setText(resulting);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Загрузка");
            progressDialog.create();
            progressDialog.show();
        }
    }

}