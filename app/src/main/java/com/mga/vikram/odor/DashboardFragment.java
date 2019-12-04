package com.mga.vikram.odor;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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

        String htmlText = displayName==null? "<h2>Not Signed In yet</h2>" : "<h2>"+displayName+"</h2><br><p>Welcome!</p>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTextMessage.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT));
        } else {
            mTextMessage.setText(Html.fromHtml(htmlText));
        }
        return view;
    }
}
