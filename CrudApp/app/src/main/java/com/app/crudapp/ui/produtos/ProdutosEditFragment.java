package com.app.crudapp.ui.produtos;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.app.crudapp.R;
import com.app.crudapp.interfaces.IProduto;
import com.app.crudapp.models.ProdutoModel;
import com.app.crudapp.utilities.ButtonLoading;
import com.app.crudapp.utilities.NavigationRoute;
import com.app.crudapp.utilities.ReturnSharedPreference;
import com.app.crudapp.utilities.ToastMessages;
import com.app.crudapp.webapi.ApiApp;
import com.app.crudapp.webapi.ApiBase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutosEditFragment extends Fragment implements View.OnClickListener {
    private EditText txtProductName, txtDescription, txtPrice;
    private Button btnSave;
    private ProgressBar progressBar;
    private ApiBase apiBase;
    private ApiApp apiApp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.produtos_edit_fragment, container, false);

        Bundle bundle = getArguments();

        InitializeViews(view, bundle);

        return view;
    }

    private void InitializeViews(View itemView, Bundle bundle)
    {
        txtProductName = itemView.findViewById(R.id.txtProductName);
        txtDescription = itemView.findViewById(R.id.txtDescription);
        txtPrice = itemView.findViewById(R.id.txtPrice);

        progressBar = itemView.findViewById(R.id.progressBar);

        btnSave = itemView.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(this);

        apiBase = new ApiBase();
        apiApp = new ApiApp(apiBase.getRetrofit());

        ProdutoModel produtoModel = (ProdutoModel) bundle.getSerializable("Model");
        txtProductName.setText(produtoModel.getProductName());
        txtDescription.setText(produtoModel.getDescription());
        txtPrice.setText(produtoModel.getPriceStr());

        btnSave.setTag(produtoModel.getId());
    }

    @Override
    public void onClick(View v) {
        final int id = Integer.parseInt(v.getTag().toString());

        switch (v.getId())
        {
            case R.id.btnSave:
                ProdutoModel produtoModel = new ProdutoModel(id, txtProductName.getText().toString(), txtDescription.getText().toString());
                produtoModel.setUserID(ReturnSharedPreference.GetUserID(v.getContext()));

                if(TextUtils.isEmpty(txtPrice.getText().toString()))
                {
                    produtoModel.setPrice(0);
                }
                else {
                    produtoModel.setPrice(Double.valueOf(txtPrice.getText().toString()));
                }

                boolean check = CheckFields(produtoModel, v);

                if(check)
                {
                    IProduto iProduto = apiApp.getiProduto();

                    ButtonLoading.ClickSave(true, btnSave, progressBar);

                    iProduto.Edit(produtoModel.getId(),produtoModel).enqueue(new Callback<ProdutoModel>() {
                        @Override
                        public void onResponse(Call<ProdutoModel> call, Response<ProdutoModel> response) {
                            if(response.isSuccessful())
                            {
                                ProdutoModel model = response.body();

                                if(model != null)
                                {
                                    NavController controller = Navigation.findNavController(btnSave);
                                    NavigationRoute.NavigateAndPopStack(controller, R.id.nav_produtos, R.id.nav_produtos, null);
                                }
                                else
                                {
                                    ToastMessages.ShowToastMessageShort(btnSave.getContext(), "Erro!");
                                }
                            }
                            else {
                                ToastMessages.ShowToastMessageShort(btnSave.getContext(), response.message());
                            }

                            ButtonLoading.ClickSave(false, btnSave, progressBar);
                        }

                        @Override
                        public void onFailure(Call<ProdutoModel> call, Throwable t) {
                            ToastMessages.ShowToastMessageShort(btnSave.getContext(), t.getMessage());
                            ButtonLoading.ClickSave(false, btnSave, progressBar);
                        }
                    });
                }

                break;
        }
    }

    private boolean CheckFields(ProdutoModel produtoModel, View view)
    {
        boolean result = true;
        String msgError = "";

        if(TextUtils.isEmpty(produtoModel.getProductName()))
        {
            msgError += "Product Name is blank!\n";
            result = false;
        }

        if(TextUtils.isEmpty(produtoModel.getDescription()))
        {
            msgError += "Description is blank!\n";
            result = false;
        }

        if(produtoModel.getPrice() == 0)
        {
            msgError += "Price is blank!\n";
            result = false;
        }

        if(!TextUtils.isEmpty(msgError))
        {
            ToastMessages.ShowToastMessageLong(view.getContext(), msgError.trim());
        }

        return result;
    }
}
