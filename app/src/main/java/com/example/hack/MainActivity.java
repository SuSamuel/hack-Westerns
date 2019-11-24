package com.example.hack;

//android imports
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
// JSON imports
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
// okhttp3 imports
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Response";
    // all our widgets
    TextView view1;
    String mMessage;
    EditText pregnancy;
    EditText glucose;
    EditText bloodPress;
    EditText skinThick;
    EditText insulin;
    EditText bmi;
    EditText diabetesPedi;
    EditText age;
    Button enter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // instantiates all the widgets
        view1 = findViewById(R.id.textView);
        pregnancy = findViewById(R.id.Pregnancy);
        glucose = findViewById((R.id.Glucose));
        bloodPress = findViewById(R.id.BloodPressure);
        skinThick = findViewById(R.id.Skinthickness);
        insulin = findViewById(R.id.Insulin);
        bmi = findViewById(R.id.BMI);
        diabetesPedi = findViewById(R.id.DiabetesPedi);
        age = findViewById(R.id.Age);
        enter = findViewById(R.id.enter);

        // when the user presses the button
        enter.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                // make sure that the values changed
                if ((!pregnancy.getText().toString().equals("Pregnancy?")) && (!glucose.getText().toString().equals("Glucose Level?"))
                        && (!bloodPress.getText().toString().equals("Blood Pressure?")) && (!skinThick.getText().toString().equals("Skin Thickness?"))
                && (!insulin.getText().toString().equals("Insulin Level?")) && (!bmi.getText().toString().equals("BMI?"))
                && (!diabetesPedi.getText().toString().equals("Genetic Synthesis Value?")) && (!age.getText().toString().equals("Age?"))){
                    // try to run the method with every variable as a float
                    try{
                        postRequest(Float.parseFloat(pregnancy.getText().toString()),Float.parseFloat(glucose.getText().toString()),
                                Float.parseFloat(bloodPress.getText().toString()), Float.parseFloat(skinThick.getText().toString()),
                                Float.parseFloat(insulin.getText().toString()), Float.parseFloat(bmi.getText().toString()),
                                Float.parseFloat(diabetesPedi.getText().toString()), Float.parseFloat(age.getText().toString()));
                    }
                    // if it doesn't work it failed
                    catch (IOException e){
                        System.out.println("failed");
                    }
                }

            }
        });

    }
    // makes the data into a json file and tries to send it to the server
    public void postRequest(float preg, float glu, float blood, float skin, float insulin, float bmi, float dia, float age) throws IOException {
        // the type of data we are sending
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        /* NOTE
           NOTE
           IMPORTANT
           if using the same python server, you need to run it and host it with ngrok
           if  using ngrok, the url will change every single time you rerun the program
           make sure to change the url to the new ngrok url, and dont forget the "/info"

        */
        String url = "http://617e13c7.ngrok.io/info";

        // create OkHttpClient object
        OkHttpClient client = new OkHttpClient();
        // create JSON file
        JSONObject postdata = new JSONObject();
        // try to add a key : value pair into the JSON object, with the variables as floats
        try {
            postdata.put("Pregnancies", Float.toString(preg));
            postdata.put("Glucose", Float.toString(glu));
            postdata.put("BloodPres", Float.toString(blood));
            postdata.put("Skinthick", Float.toString(skin));
            postdata.put("Insulin", Float.toString(insulin));
            postdata.put("BMI", Float.toString(bmi));
            postdata.put("DiabetesPedi", Float.toString(dia));
            postdata.put("Age", Float.toString(age));
        // if it doesn't work it failed
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            // if it doesn't work, it failed
            @Override
            public void onFailure(Call call, IOException e) {
                mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            // if the server sends back information
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // turn the JSON file into a string
                mMessage = response.body().string();
                //make sure this doesnt run in the background
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // cleans the mMessage into just a true or false
                        String finalMessage = mMessage.substring(11);
                        finalMessage = finalMessage.substring(0, finalMessage.indexOf("e")+1);
                        // set the viewText to the result
                        view1.setText(finalMessage);
                        // Stuff that updates the UI

                    }
                });
                Log.e(TAG, mMessage);

            }
        });
    }
}
