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
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class Packages extends Fragment {
    SharedPreferences prefs;
    RecyclerView recyclerView;
    private PackageAdapter adapter;
    ArrayList<PackageItem> packageList;

    String PackageName, PackagefileName;
    String accessToken, TrainingBagId;
    Button pckgitem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.meetingpackages, container, false);
        prefs = getContext().getSharedPreferences(Constant.USER_PREFS, MODE_PRIVATE);//object of shared pref
        PackageName = prefs.getString("content", "");
        PackagefileName = prefs.getString("PckgName", "");
        accessToken = prefs.getString("access_token", "");
        TrainingBagId = prefs.getString("TrainingBagId", "");
//        String url = "https://esstest-net.t4edu.com/SafetyDay";
        GetPackageList(accessToken,TrainingBagId);

//        fillExampleList();
        //Set RecyclerView

        RecyclerView recyclerView = v.findViewById(R.id.packages_recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new PackageAdapter(packageList);

        Log.v("P_N", String.valueOf(PackageName));
        Log.v("P_F", PackagefileName);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        return v;
    }



//    private void fillExampleList() {
//
//        String url = "https://esstest-net.t4edu.com/SafetyDay";
//
//        packageList = new ArrayList<>();
//        try {
//            JSONArray jsonArray = new JSONArray(PackageName);
//            if (jsonArray != null) {
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject packgdata = jsonArray.getJSONObject(i);
//                    String PDF_Url = url + packgdata.getString("fileUrl");
//                    PackageData PD = new PackageData();
////                    packageList.add(new PackageItem(packgdata.getString("name"),PDF_Url));
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }



    private void GetPackageList(String accesstoken,String TrainingBagId ){


        String url = Constant.PACKAGE_LIST+TrainingBagId;

        packageList = new ArrayList<>();

        Log.v("url",url);
        final HashMap<String, String> headers = new HashMap<>();

        headers.put("Authorization","bearer "+ accesstoken);
        headers.put("Content-Type","application/x-www-form-urlencoded");

        StringRequest getUserStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                PackageItem packageitem;
                packageList = new ArrayList<>();

                try
                {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray PackageArray= responseObject.getJSONArray("content");
                    JSONObject Package_Data;

                    for (int i =0;i<PackageArray.length();i++){

                        Package_Data =   PackageArray.getJSONObject(i);

                        packageitem = new PackageItem();
                        packageitem.setTxtBtnPackage(Package_Data.getString("fileName"));
                        packageitem.setLink(Package_Data.getString("fileUrl"));
                        packageList.add(packageitem);


                    }
                    recyclerView = getView().findViewById(R.id.packages_recyclerview);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    adapter = new PackageAdapter(packageList);

                    recyclerView.setLayoutManager(layoutManager);

                    recyclerView.setAdapter(adapter);

//                    Log.v("LOC", "Map Retrieved Successfully");
//                    //save in shared prefs
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.putString("PckgName", packageitem.getTxtBtnPackage());
//                    editor.putString("Link",packageitem.getLink());ß
//
////                    editor.putString("Pckg_FileName", PD.getFileName());
////                    editor.putString("Packg_FileUrl", PD.getFileUrl());
////                    editor.putString("content",String.valueOf(PackageArray));
//                    editor.commit();


                }
                catch(Exception e)
                {
//                    Toast.makeText(getContext(), "Exception Error", Toast.LENGTH_SHORT).show();
                    Log.v("GETTTT_USER",e.toString());
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        //  Toast.makeText(getContext(), "Question Error", Toast.LENGTH_SHORT).show();
                        Log.v("GETT_USER", error.toString());
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

        RequestQueue getUserRequestQueue = Volley.newRequestQueue(getContext());
        getUserRequestQueue.add(getUserStringRequest);




    }


}
