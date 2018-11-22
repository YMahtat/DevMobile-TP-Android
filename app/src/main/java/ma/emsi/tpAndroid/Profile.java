package ma.emsi.tpAndroid;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Profile extends Activity implements Response.ErrorListener, Response.Listener<JSONObject> {

    private static final String DEBUGTAG = "PROFILE";
    public static final String _urlWebServices = "http://belatar.name/tests/";
    public static final String _webService = "profile.php?login=test&passwd=test";
    Object walu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ma.emsi.tpAndroid.R.layout.activity_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //new WSTask().execute(_urlWebService,"profile.php?login=test&passwd=test");
        //RequestQueue queue = Volley.newRequestQueue(this);
        String urlWebService = _urlWebServices + _webService ;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlWebService, null, this, this);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
        //queue.add(request);
    }

    public void enregistrer(View view) {
        Toast.makeText(this, getString(ma.emsi.tpAndroid.R.string.prfl_enrg), Toast.LENGTH_LONG)
             .show();
    }



    public void test(View view){
        Toast.makeText(this, "Sir tl3ab", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject json) {
        Etudiant etudiant = null;
        if(json.has("error")) {
            try {
                Log.e(DEBUGTAG, json.getString("error"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {


            try {
                etudiant =  new Etudiant(
                        json.getInt("id"),
                        json.getString("nom"),
                        json.getString("prenom"),
                        null,
                        json.getString("classe"),
                        json.getString("phone")
                );

                if(etudiant == null)
                {
                    return;
                }
                else
                {
                    EditText inputNom = findViewById(ma.emsi.tpAndroid.R.id.inputNom);
                    EditText inputPrenom = findViewById(ma.emsi.tpAndroid.R.id.inputPrenom);
                    EditText inputClasse = findViewById(ma.emsi.tpAndroid.R.id.inputClasse);
                    final ImageView imgProfil = findViewById(ma.emsi.tpAndroid.R.id.IMG_Profil);
                    inputNom.setText(etudiant.getNom());
                    inputPrenom.setText(etudiant.getPrenom());
                    inputClasse.setText(etudiant.getClasse());
                    imgProfil.setImageBitmap(etudiant.getPhoto());
                    VolleySingleton.getInstance(this).getImageLoader().get(_urlWebServices + json.getString("photo"), new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                            imgProfil.setImageBitmap(response.getBitmap());
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private class WSTask extends AsyncTask<String, Void, Etudiant>{

        @Override
        protected Etudiant doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0] + strings[1]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                InputStream in = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String resultat = "", ligne = "";
                while ((ligne = reader.readLine()) != null)
                    resultat += ligne + "\n";

                Log.d(DEBUGTAG, resultat);
                con.disconnect();
                JSONObject json = new JSONObject(resultat);
                if(json.has("error")) Log.e(DEBUGTAG, json.getString("error"));
                else {

                    URL urlPhoto = new URL( Profile._urlWebServices + json.getString("photo"));
                    HttpURLConnection conToGetPhoto = (HttpURLConnection) urlPhoto.openConnection();
                    in = conToGetPhoto.getInputStream();


                    Etudiant  etd =  new Etudiant(
                                            json.getInt("id"),
                                            json.getString("nom"),
                                            json.getString("prenom"),
                                            BitmapFactory.decodeStream(in),
                                            json.getString("classe"),
                                            json.getString("phone")
                                        );
                    conToGetPhoto.disconnect();
                    return etd;
                }
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Etudiant etudiant) {
            super.onPostExecute(etudiant);
            EditText inputNom = findViewById(ma.emsi.tpAndroid.R.id.inputNom);
            EditText inputPrenom = findViewById(ma.emsi.tpAndroid.R.id.inputPrenom);
            EditText inputClasse = findViewById(ma.emsi.tpAndroid.R.id.inputClasse);
            ImageView imgProfil = findViewById(ma.emsi.tpAndroid.R.id.IMG_Profil);

            inputNom.setText(etudiant.getNom());
            inputPrenom.setText(etudiant.getPrenom());
            inputClasse.setText(etudiant.getClasse());
            imgProfil.setImageBitmap(etudiant.getPhoto());
        }
    }
}
