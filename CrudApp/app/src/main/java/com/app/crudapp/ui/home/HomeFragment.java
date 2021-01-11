package com.app.crudapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.app.crudapp.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private CardView cardUsers, cardProducts, cardAbout, cardFinish;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        cardUsers = root.findViewById(R.id.cardUsers);
        cardProducts = root.findViewById(R.id.cardProducts);
        cardAbout = root.findViewById(R.id.cardAbout);
        cardFinish = root.findViewById(R.id.cardFinish);

        cardUsers.setOnClickListener(this);
        cardProducts.setOnClickListener(this);
        cardAbout.setOnClickListener(this);
        cardFinish.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.cardUsers:
                Navigation.findNavController(v).navigate(R.id.nav_usuarios);
                break;

            case R.id.cardProducts:
                Navigation.findNavController(v).navigate(R.id.nav_produtos);
                break;

            case R.id.cardAbout:
                Navigation.findNavController(v).navigate(R.id.nav_sobre);
                break;

            case R.id.cardFinish:
                getActivity().finish();
                break;
        }
    }
}
