package com.app.crudapp.ui.usuarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.crudapp.R;
import com.app.crudapp.adapters.UsuarioAdapter;
import com.app.crudapp.interfaces.IUser;
import com.app.crudapp.models.UsuarioModel;
import com.app.crudapp.utilities.ToastMessages;
import com.app.crudapp.webapi.ApiApp;
import com.app.crudapp.webapi.ApiBase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsusariosFragment extends Fragment implements View.OnClickListener {
    private Button btnAdd;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ApiBase apiBase;
    private ApiApp apiApp;
    private IUser iUser;
    private SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.usuarios_fragment, container, false);

        InitializeViews(view);

        return view;
    }

    public void InitializeViews(View view)
    {
        apiBase = new ApiBase();
        apiApp = new ApiApp(apiBase.getRetrofit());
        iUser = apiApp.getiUser();

        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recycleview);
        swipe = view.findViewById(R.id.swipe);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadUsuarios();
            }
        });

        LoadUsuarios();
    }

    private void LoadUsuarios()
    {
        iUser = apiApp.getiUser();

        swipe.setRefreshing(true);
        iUser.GetAll().enqueue(new Callback<List<UsuarioModel>>() {
            @Override
            public void onResponse(Call<List<UsuarioModel>> call, Response<List<UsuarioModel>> response) {
                if(response.isSuccessful())
                {
                    UsuarioAdapter userAdapter = new UsuarioAdapter(response.body());
                    recyclerView.setAdapter(userAdapter);
                }
                else
                {
                    ToastMessages.ShowToastMessageLong(recyclerView.getContext(), response.message());
                }

                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<UsuarioModel>> call, Throwable t) {
                ToastMessages.ShowToastMessageLong(recyclerView.getContext(), t.getMessage());
                swipe.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnAdd:
                Navigation.findNavController(v).navigate(R.id.nav_usuarios_add);
                break;
        }
    }
}
