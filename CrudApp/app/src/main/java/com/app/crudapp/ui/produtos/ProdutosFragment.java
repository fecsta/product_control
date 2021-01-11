package com.app.crudapp.ui.produtos;

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
import com.app.crudapp.adapters.ProdutoAdapter;
import com.app.crudapp.adapters.UsuarioAdapter;
import com.app.crudapp.interfaces.IProduto;
import com.app.crudapp.interfaces.IUser;
import com.app.crudapp.models.ProdutoModel;
import com.app.crudapp.models.UsuarioModel;
import com.app.crudapp.utilities.ReturnSharedPreference;
import com.app.crudapp.utilities.ToastMessages;
import com.app.crudapp.webapi.ApiApp;
import com.app.crudapp.webapi.ApiBase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutosFragment extends Fragment implements View.OnClickListener {
    private Button btnAdd;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ApiBase apiBase;
    private ApiApp apiApp;
    private IProduto iProduto;
    private SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.produtos_fragment, container, false);

        InitializeViews(view);

        return view;
    }

    public void InitializeViews(View view)
    {
        apiBase = new ApiBase();
        apiApp = new ApiApp(apiBase.getRetrofit());
        iProduto = apiApp.getiProduto();

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
                LoadProdutos();
            }
        });

        LoadProdutos();
    }

    private void LoadProdutos()
    {
        iProduto = apiApp.getiProduto();
        int userId = ReturnSharedPreference.GetUserID(getContext());

        swipe.setRefreshing(true);
        iProduto.GetAll(userId).enqueue(new Callback<List<ProdutoModel>>() {
            @Override
            public void onResponse(Call<List<ProdutoModel>> call, Response<List<ProdutoModel>> response) {
                if(response.isSuccessful())
                {
                    ProdutoAdapter produtoAdapter = new ProdutoAdapter(response.body());
                    recyclerView.setAdapter(produtoAdapter);
                }
                else
                {
                    ToastMessages.ShowToastMessageLong(recyclerView.getContext(), response.message());
                }

                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<ProdutoModel>> call, Throwable t) {
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
                Navigation.findNavController(v).navigate(R.id.nav_produtos_add);
                break;
        }
    }
}
