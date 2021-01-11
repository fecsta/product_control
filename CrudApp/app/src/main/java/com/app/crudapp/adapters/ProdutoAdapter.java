package com.app.crudapp.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.app.crudapp.R;
import com.app.crudapp.interfaces.IProduto;
import com.app.crudapp.interfaces.IUser;
import com.app.crudapp.models.ProdutoModel;
import com.app.crudapp.models.UsuarioModel;
import com.app.crudapp.utilities.ToastMessages;
import com.app.crudapp.webapi.ApiApp;
import com.app.crudapp.webapi.ApiBase;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.MyViewHolder> implements View.OnClickListener {

    private List<ProdutoModel> produtoModels;

    public ProdutoAdapter(List<ProdutoModel> produtoModels)
    {
        this.produtoModels = produtoModels;
    }

    @NonNull
    @Override
    public ProdutoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_produto_all, parent, false);

        return new ProdutoAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoAdapter.MyViewHolder holder, int position) {
        ProdutoModel produtoModel = produtoModels.get(position);

        holder.txtProductName.setText(produtoModel.getProductName());
        holder.txtDescription.setText(produtoModel.getDescription());
        holder.txtPrice.setText(String.valueOf(produtoModel.getPrice()));

        holder.btnEdit.setTag(position);
        holder.btnDelete.setTag(position);

        holder.btnEdit.setOnClickListener(this);
        holder.btnDelete.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return produtoModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProductName, txtDescription, txtPrice;
        public Button btnEdit, btnDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public void onClick(final View v) {
        final int position = Integer.parseInt(v.getTag().toString());
        final ProdutoModel produtoModel = produtoModels.get(position);

        switch (v.getId())
        {
            case R.id.btnEdit:
                Bundle bundle = new Bundle();
                bundle.putSerializable("Model", produtoModel);
                Navigation.findNavController(v).navigate(R.id.nav_produtos_edit, bundle);

                break;

            case R.id.btnDelete:
                ApiBase apiBase = new ApiBase();
                ApiApp apiApp = new ApiApp(apiBase.getRetrofit());
                IProduto iProduto = apiApp.getiProduto();

                iProduto.Delete(produtoModel.getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            produtoModels.remove(position);
                            notifyItemRemoved(position);
                        }
                        else {
                            ToastMessages.ShowToastMessageShort(v.getContext(), response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        ToastMessages.ShowToastMessageShort(v.getContext(), t.getMessage());
                    }
                });

                break;
        }
    }
}
