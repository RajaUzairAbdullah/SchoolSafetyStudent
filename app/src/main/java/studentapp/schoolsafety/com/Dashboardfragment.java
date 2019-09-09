package studentapp.schoolsafety.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Dashboardfragment extends Fragment
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    //Meeting PLace Location
    GoogleMap map;
    TextView StartDate,EndDate;
    Double Longitude,Latitude;
    String accessToken,TrainingBagId;
    public List<PackageItem> packageList;
    SharedPreferences prefs;
    TextView HeaderText;
    NavigationView navigationView;
    Button bckbutton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dashboardfragment, container, false);
        prefs  = getContext().getSharedPreferences(Constant.USER_PREFS, MODE_PRIVATE);//object of shared pref
        accessToken = prefs.getString("access_token","");
        TrainingBagId = prefs.getString("TrainingBagId", "");
        HeaderText = (TextView) getActivity().findViewById(R.id.headertxt);
        Button button1= (Button) v.findViewById(R.id.viewpackages);
        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        bckbutton = (Button) getActivity().findViewById(R.id.bckheaderbtn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPackageList(accessToken,TrainingBagId);
                HeaderText.setText("");
                HeaderText.setText(R.string.packages_menu);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new Packages()).addToBackStack(null).commit();
               // NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                navigationView.getMenu().getItem(1).setChecked(true);
            }
        });

        Button surveybtn = (Button) v.findViewById(R.id.surveybutton);
        surveybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeaderText.setText("");
                HeaderText.setText(R.string.survery_heading);
                bckbutton.setVisibility(View.VISIBLE);
                navigationView.getMenu().getItem(0).setChecked(false);

                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new SurveryData()).addToBackStack(null).commit();

            }
        });



        TextView StartDate = v.findViewById(R.id.startdate);
        TextView EndDate = v.findViewById(R.id.enddate);
//        String access_token = prefs.getString("user_name","");

        //DATA form SharedPrefereces



        //Gettig PakcageList



        Log.v("Acc",accessToken);
        Log.v("TR_ID", String.valueOf(TrainingBagId));

        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");


        StartDate.setText(prefs.getString("Startdate",""));//called5
        EndDate.setText(prefs.getString("Startdate",""));//called


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.meetingmap);
        mapFragment.getMapAsync(this);




        return v;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        SharedPreferences prefs = getContext().getSharedPreferences(Constant.USER_PREFS, MODE_PRIVATE);//object of shared pref

        String lat = prefs.getString("Latitude","");
        String lon = prefs.getString("Longitude", "");

        Double latd = Double.parseDouble(lat);
        Double lond = Double.parseDouble(lon);


        LatLng Meetingplace = new LatLng(latd, lond);
//      Log.v("MEETINGPLACE", String.valueOf(Meetingplace));
        googleMap.addMarker(new MarkerOptions().position(Meetingplace));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Meetingplace));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latd,lond),12.0f));




    }

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
                PackageData PD = new PackageData();

                try
                {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray PackageArray= responseObject.getJSONArray("content");
                    Toast.makeText(getContext(), responseObject.toString(), Toast.LENGTH_SHORT).show();


                    //getting user data in strings

                    for (int i =0;i<PackageArray.length();i++){

                        JSONObject Package_Data =   PackageArray.getJSONObject(i);

                        PD.setName(Package_Data.getString("name"));
                        PD.setFileName(Package_Data.getString("fileName"));
                        PD.setFileUrl(Package_Data.getString("fileUrl"));

//                        packageList.addAll(PD);
                    }
                    Log.v("PCKG","PCKG Data Retrieved Successfully");

                    //save in shared prefs
                    prefs = getContext().getSharedPreferences(Constant.USER_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();


                    editor.putString("PckgName", PD.getName());
                    editor.putString("Pckg_FileName", PD.getFileName());
                    editor.putString("Packg_FileUrl", PD.getFileUrl());
                    editor.putString("List", String.valueOf(PackageArray));

                    editor.commit();


                }
                catch(Exception e)
                {
//                    Toast.makeText(, "Exception Occured in Get user", Toast.LENGTH_SHORT).show();
                    Log.v("GET_USER",e.toString());
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
//                        Toast.makeText(getActivity().getApplicationContext(), "Cannot Fetch User Data", Toast.LENGTH_SHORT).show();
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

        RequestQueue getUserRequestQueue = Volley.newRequestQueue(getContext());
        getUserRequestQueue.add(getUserStringRequest);




    }
}
