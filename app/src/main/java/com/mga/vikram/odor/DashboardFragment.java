package com.mga.vikram.odor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class DashboardFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard,null);

        Reporter reporter = Verifier.getInstance();
        String displayName = reporter.getDisplayName();
        TextView mTextMessage;
        mTextMessage = view.findViewById(R.id.message);
        /*if ( reporter.isLocationAvailable() ){
            displayName += " : Location : " + reporter.getLat() + " : " + reporter.getLng();
        }*/
        mTextMessage.setText(displayName==null? "Not yet signed in!": displayName);

        return view;
    }
}
