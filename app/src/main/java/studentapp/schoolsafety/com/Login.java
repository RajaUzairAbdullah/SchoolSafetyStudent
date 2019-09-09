package studentapp.schoolsafety.com;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button login;
    EditText username;
    EditText password;
    ProgressDialog pDialog;
    SharedPreferences prefs;
    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.txtuserename);
        password = (EditText) findViewById(R.id.txtpassword);

        login = (Button) findViewById(R.id.buttonlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check text view for empty strings
                validate(username.getText().toString(),password.getText().toString());
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }



    }



    public void validate(String nationalID, String Password)
    {
        final HashMap<String,String> param = new HashMap<>();
        param.put("UserName", nationalID);
        param.put("Password", Password);
        param.put("grant_type", "password");


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Logging In...");
        pDialog.setCancelable(false);
        pDialog.show();

        new  AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... voids)
            {

                String tempMessage = "";
                try
                {
                    SendLoginDataToServer(param);
                }
                catch (Exception e)
                {
                    pDialog.dismiss();
                    Log.v("login",e.getMessage().toString());
                }
                return tempMessage;
            }
        }.execute(null, null, null);

    }

    private void SendLoginDataToServer(final HashMap<String, String> param)
    {
        String url = Constant.LOGIN_API;
        StringRequest loginStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject responseObject = new JSONObject(response);

                    if(responseObject.has("access_token"))
                    {
                        accessToken = responseObject.getString("access_token");
                        Log.v("LOGIN_API","Login Successful");
                        //new function to get user data
                        GetUserData(accessToken, username.getText().toString());
                        GetForumData(accessToken);
                    }


                }
                catch(Exception e)
                {
                    pDialog.dismiss();
                    Toast.makeText(Login.this, "Exception Occured", Toast.LENGTH_SHORT).show();
                    Log.v("LOGIN_API",e.getMessage());
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        pDialog.dismiss();
                        Toast.makeText(Login.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                        Log.v("LOGIN_API", error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = param;
                return params;
            }
        };

        RequestQueue loginRequestQueue = Volley.newRequestQueue(this);
        loginRequestQueue.add(loginStringRequest);
    }



    private void GetUserData(String token, final String nationalID)
    {
        final HashMap<String, String> headers = new HashMap<>();

        headers.put("Authorization","bearer "+token);
        headers.put("Content-Type","application/x-www-form-urlencoded");

        new  AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... voids)
            {

                String tempMessage = "";
                try
                {
                    GetUserDataFromServer(headers, nationalID);
                }
                catch (Exception e)
                {
                    pDialog.dismiss();
                    Log.v("login",e.getMessage().toString());
                }
                return tempMessage;
            }
        }.execute(null, null, null);

    }

    private void GetUserDataFromServer(final HashMap<String, String> headers, String nationalID)
    {
        String url = Constant.GET_USER_DATA_API + nationalID;
        StringRequest getUserStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                pDialog.dismiss();
                try
                {
                    JSONObject responseObject = new JSONObject(response);
//                    Toast.makeText(Login.this, responseObject.toString(), Toast.LENGTH_SHORT).show();


                    //getting user data in strings
                    JSONObject contentObject = responseObject.getJSONObject("content");
                    String userName = contentObject.getString("name");
                    String userEmail = contentObject.getString("email");
                    Log.v("GET_USER","Data Retrieved Successfully");

                    //save in shared prefs
                    prefs = getSharedPreferences(Constant.USER_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString("access_token",accessToken);
                    editor.putString("user_name", userName);
                    editor.putString("user_email", userEmail);

                    editor.commit();


                    //change activity
                    Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                    startActivity(intent);
                    finish();

                }
                catch(Exception e)
                {
                    pDialog.dismiss();
                    Toast.makeText(Login.this, "Exception Occured in Get user", Toast.LENGTH_SHORT).show();
                    Log.v("GET_USER",e.toString());
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        pDialog.dismiss();
                        Toast.makeText(Login.this, "Cannot Fetch User Data", Toast.LENGTH_SHORT).show();
                        Log.v("GET_USER", error.toString());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = headers;
                return params;
            }
        };

        RequestQueue getUserRequestQueue = Volley.newRequestQueue(this);
        getUserRequestQueue.add(getUserStringRequest);

    }

    private void GetForumData(String access_token) {
        String url = Constant.GET_FORUM;
        final HashMap<String, String> headers = new HashMap<>();
//        Log.v("TOke",access_token);

        headers.put("Authorization","bearer "+access_token);
        headers.put("Content-Type","application/json");

        StringRequest getUserStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                ForumData FD = new ForumData();
                pDialog.dismiss();
                try
                {
                    JSONObject responseObject = new JSONObject(response);
                    //getting user data in strings
                    JSONArray ForumArray= responseObject.getJSONArray("content");

//                    Toast.makeText(Login.this, responseObject.toString(), Toast.LENGTH_LONG).show();


                    for (int i =0;i<ForumArray.length();i++){

                        JSONObject forum_Data =   ForumArray.getJSONObject(i);


                        FD.setName(forum_Data.getString("name"));
                        FD.setForumId(forum_Data.getString("forumId"));
                        FD.setStartDate(forum_Data.getString("startDate"));
                        FD.setEndDate(forum_Data.getString("endDate"));
                        FD.setLongitude(forum_Data.getString("locationLongitude"));
                        FD.setLatitude(forum_Data.getString("locationLatitude"));
                        FD.setTrainingBagId(forum_Data.getString("trainingBagId"));



                    }

                    Log.v("LOC","Map Retrieved Successfully");

                    //save in shared prefs
                    prefs = getSharedPreferences(Constant.USER_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                  //  ForumData FD = new ForumData();
                    editor.putString("Startdate", FD.getStartDate());
                    editor.putString("Enddata", FD.getEndDate());
                    editor.putString("Longitude", FD.getLongitude());
                    editor.putString("Latitude", FD.getLatitude());
                    editor.putString("ForumId",FD.getForumId());
                    editor.putString("TrainingBagId",FD.getTrainingBagId());



                    Log.v("DATA ",FD.getStartDate());

                    editor.commit();

                    finish();

                }
                catch(Exception e)
                {
                    pDialog.dismiss();
                    Toast.makeText(Login.this, "Location Error", Toast.LENGTH_SHORT).show();
                    Log.v("GETT_USER",e.toString());
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        pDialog.dismiss();
                        Toast.makeText(Login.this, "Cannot Fetch Location Data", Toast.LENGTH_SHORT).show();
                        Log.v("GET_USER", error.toString());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = headers;
                return params;
            }
        };

        RequestQueue getUserRequestQueue = Volley.newRequestQueue(this);
        getUserRequestQueue.add(getUserStringRequest);


    }

}
