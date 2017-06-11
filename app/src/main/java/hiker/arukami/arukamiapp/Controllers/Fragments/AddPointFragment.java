package hiker.arukami.arukamiapp.Controllers.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.widgets.RecordButton;

import hiker.arukami.arukamiapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPointFragment extends Fragment {


    private View mainView;
    private static final int PERMISSIONS_REQUEST_CAMERA = 1;

    public AddPointFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_add_point, container, false);
        // Inflate the layout for this fragment
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_CAMERA);
        }
        CameraFragment cameraFragment = CameraFragment.newInstance(new Configuration.Builder().build());

        getFragmentManager().beginTransaction()
                .replace(R.id.container_add_point, cameraFragment, null)
                .commit();



        return mainView;
    }



}
