package studentapp.schoolsafety.com;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ContactUs extends Fragment implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contactus, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.contact_location);
        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng Meetingplace = new LatLng(24.690911, 46.716340);
        googleMap.addMarker(new MarkerOptions().position(Meetingplace));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Meetingplace));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Meetingplace,12.0f));

    }
}
