package studentapp.schoolsafety.com;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import studentapp.schoolsafety.com.MultiPager.Paginator;

import static android.content.Context.MODE_PRIVATE;

public class SurveryData extends Fragment {

    private SurveyAdapter adapter;//yh wala adapter jaega na? sahi  aagayy...? chala k dekho ab

    private List<SurveyItem> surveyList;



//    public static final int Total_Num_Questions = 15;
//    public static final int Questions_per_page = 3;
//    public static final int Remaining_Questions = Total_Num_Questions % Questions_per_page;
//    public static final int Last_page = Total_Num_Questions/Questions_per_page;
    //Adapter adapter;
    Button sendSurvey;
    //Paginator p = new Paginator();
    //private int TotalPages = Paginator.Total_Num_Questions / Paginator.Questions_per_page;
    private int CurrentPage = 0;
    SharedPreferences prefs;
    String accessToken;
    ArrayList<SurveyItem> items;
    ArrayList<SurveyItem> surveyItemsResultList;
    RecyclerView recyclerView;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = inflater.inflate(R.layout.survey, container, false);

        prefs  = getContext().getSharedPreferences(Constant.USER_PREFS, MODE_PRIVATE);//object of shared pref

        accessToken = prefs.getString("access_token","");
        //Survey Work


        recyclerView = v.findViewById(R.id.recycler_view);

        //Recycler Properties


        //Button
        sendSurvey = (Button) v.findViewById(R.id.btnSendSurvey);
        sendSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                JSONArray surveyResultArray = new JSONArray();

                try
                {
                    surveyItemsResultList = new ArrayList<>();
                    SurveyItem surveyItemResult;

                    JSONObject requestObject = new JSONObject();

                    Gson gson = new Gson();

                    for(int i = 0; i < items.size(); i++)
                    {
                        surveyItemResult = new SurveyItem();
                        SurveyItem surveyItem = items.get(i);

                        surveyItemResult.setComment("string");
                        surveyItemResult.setSelectOption(surveyItem.getSelectOption());
                        surveyItemResult.setQuestionId(surveyItem.getQuestionId());

                        try
                        {
                            String newSurveyItemResult = gson.toJson(surveyItemResult);
                            surveyResultArray.put(new JSONObject(newSurveyItemResult));
                        }
                        catch(Exception e)
                        {
                            Toast.makeText(getContext(), "JSON array Exception Occured", Toast.LENGTH_SHORT).show();
                            Log.v("json_array",e.toString());
                        }

                    }

                    try
                    {
                        Log.v("requestObjectArray",surveyResultArray.toString());
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(getContext(), "JSON Object exception occured", Toast.LENGTH_SHORT).show();
                        Log.v("json_object",e.toString());
                    }
                }
                catch(Exception e)
                {
                    Toast.makeText(getContext(), "Button Exception", Toast.LENGTH_SHORT).show();
                    Log.v("json_button",e.toString());
                }


                SendQuestionData(surveyResultArray,accessToken);
            }




        });


        //Survery Work
        GetQuestionData("1");


        return v;

    }



private void GetQuestionData(String assessmentID )
{
    String url = Constant.QUESTIONS_API;//yahan id deni hay na  aby wo kiu denge jb link mai hi dala hua to

    final HashMap<String,String> headers = new HashMap<>();

    headers.put("Authorization","bearer "+ accessToken);
    headers.put("Content-Type","application/json");

    StringRequest getUserStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            SurveyItem surveyItem;
            items = new ArrayList<>();

            try
            {
                JSONObject responseObject = new JSONObject(response);

                JSONArray surveyArray = responseObject.getJSONArray("content");

                Toast.makeText(getContext(), responseObject.toString(), Toast.LENGTH_LONG).show();

                JSONObject survey_Data;

                for (int i = 0; i < surveyArray.length(); i++)
                {
                    survey_Data = surveyArray.getJSONObject(i);

                    surveyItem = new SurveyItem();
                    surveyItem.setQuestion(survey_Data.getString("text"));
                    surveyItem.setQuestionId(survey_Data.getInt("questionId"));
                    items.add(surveyItem);
                }

                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                adapter = new SurveyAdapter(items);

                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(adapter);

                Log.v("LOC", "Map Retrieved Successfully");


            } catch (Exception e) {

                Toast.makeText(getContext(), "Question Error", Toast.LENGTH_SHORT).show();
                Log.v("GET_QUESTION", e.toString());

            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getContext(), "Question Error", Toast.LENGTH_SHORT).show();
            Log.v("GET_QUESTION", error.toString());
        }
    })
    {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = headers;
            return params;
        }
    };

    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
    requestQueue.add(getUserStringRequest);
}


private void SendQuestionData(JSONArray surveyResultArray, String access_token) {

    String url = Constant.SEND_SURVEY_DATA_API;
    final HashMap<String, String> headers = new HashMap<>();

    headers.put("Content-Type", "application/json");
    headers.put("Authorization", "bearer " + access_token);

    JSONObject object = new JSONObject();
    try {
        object.put("", surveyResultArray);
    } catch (Exception e) {
        Toast.makeText(getContext(), "json object exception", Toast.LENGTH_SHORT).show();
        Log.v("Json_object_exception", e.toString());
    }


    CustomJsonRequest jsonArrayRequest = new CustomJsonRequest(Request.Method.POST, url, surveyResultArray, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response)
        {

            try {
                JSONObject responseObject = response;

                boolean content = response.getBoolean("content");
                if (content == true) {
                    Log.v("survey_result", responseObject.toString());
                    Toast.makeText(getContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.v("survey_result", responseObject.toString());
                    Toast.makeText(getContext(), "Error Occured", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Log.v("survey_result", e.toString());
                Toast.makeText(getContext(), "survey result exception", Toast.LENGTH_SHORT).show();
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error)
        {
            Log.v("survey_result", error.toString());
            Toast.makeText(getContext(), "survey_result_error_response " + error.toString(), Toast.LENGTH_SHORT).show();

        }
    })
    {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = headers;
            return params;
        }
    };

    RequestQueue getUserRequestQueue = Volley.newRequestQueue(getContext());
    getUserRequestQueue.add(jsonArrayRequest);

    };


//    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
//        @Override
//        public void onResponse(JSONObject response) {
//            try {
//                JSONObject responseObject = response;
//
//                boolean content = response.getBoolean("content");
//                if (content == true) {
//                    Log.v("survey_result", responseObject.toString());
//                    Toast.makeText(getContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.v("survey_result", responseObject.toString());
//                    Toast.makeText(getContext(), "Error Occured", Toast.LENGTH_SHORT).show();
//                }
//
//
//            } catch (Exception e) {
//                Log.v("survey_result", e.toString());
//                Toast.makeText(getContext(), "survey result exception", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            Log.v("survey_result", error.toString());
//            Toast.makeText(getContext(), "survey result ErrorResponse", Toast.LENGTH_SHORT).show();
//
//        }
//    }) {
//        @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            Map<String, String> params = headers;
//            return params;
//        }
//    };
//
//    RequestQueue getUserRequestQueue = Volley.newRequestQueue(getContext());
//    getUserRequestQueue.add(request);


//    JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, url, surveyResultArray, new Response.Listener<JSONArray>() {
//        @Override
//        public void onResponse(JSONArray response) {
//            try {
//                JSONObject responseObject = response.getJSONObject(0);
//                Log.v("survey_result", responseObject.toString());
//                Toast.makeText(getContext(), "survey_result " + responseObject.toString(), Toast.LENGTH_SHORT).show();
//
//            } catch (Exception e) {
//                Log.v("survey_result", e.toString());
//                Toast.makeText(getContext(), "survey_result_exception_error " + e.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            Log.v("survey_result", error.toString());
//            Toast.makeText(getContext(), "survey_result_error_response " + error.toString(), Toast.LENGTH_SHORT).show();
//
//        }
//    }) {
//        @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            Map<String, String> params = headers;
//            return params;
//        }
//    };
//
//    RequestQueue getUserRequestQueue = Volley.newRequestQueue(getContext());
//    getUserRequestQueue.add(arrayRequest);
}








