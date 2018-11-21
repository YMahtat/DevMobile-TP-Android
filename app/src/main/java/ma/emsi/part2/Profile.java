package ma.emsi.part2;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Profile extends Activity {

    private static final String DEBUGTAG = "PROFILE";
    public static final String _urlWebService = "http://belatar.name/tests/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new WSTask().execute(_urlWebService,"profile.php?login=test&passwd=test");
    }

    public void enregistrer(View view) {
        Toast.makeText(this, getString(R.string.prfl_enrg), Toast.LENGTH_LONG)
             .show();
    }



    public void test(View view){
        Toast.makeText(this, "Sir tl3ab", Toast.LENGTH_LONG)
                .show();
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

                    URL urlPhoto = new URL( Profile._urlWebService + json.getString("photo"));
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
            EditText inputNom = findViewById(R.id.inputNom);
            EditText inputPrenom = findViewById(R.id.inputPrenom);
            EditText inputClasse = findViewById(R.id.inputClasse);
            ImageView imgProfil = findViewById(R.id.IMG_Profil);

            inputNom.setText(etudiant.getNom());
            inputPrenom.setText(etudiant.getPrenom());
            inputClasse.setText(etudiant.getClasse());
            imgProfil.setImageBitmap(etudiant.getPhoto());


        }
    }
}
