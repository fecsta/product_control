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
import com.app.crudapp.interfaces.IUser;
import com.app.crudapp.models.UsuarioModel;
import com.app.crudapp.utilities.ToastMessages;
import com.app.crudapp.webapi.ApiApp;
import com.app.crudapp.webapi.ApiBase;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.MyViewHolder> implements View.OnClickListener {
    private List<UsuarioModel> usuarioModelList;

    public UsuarioAdapter(List<UsuarioModel> usuarioModelList)
    {
        this.usuarioModelList = usuarioModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_usuario_all, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UsuarioModel usuarioModel = usuarioModelList.get(position);

        holder.txtUser.setText(usuarioModel.getUser());
        //holder.txtPassword.setText(usuarioModel.getPassword());

        holder.btnEdit.setTag(position);
        holder.btnDelete.setTag(position);

        holder.btnEdit.setOnClickListener(this);
        holder.btnDelete.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return usuarioModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtUser, txtPassword;
        public Button btnEdit, btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtUser = itemView.findViewById(R.id.txtUser);
            txtPassword = itemView.findViewById(R.id.txtPassword);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public void onClick(final View v) {
        final int position = Integer.parseInt(v.getTag().toString());
        final UsuarioModel usuarioModel = usuarioModelList.get(position);

        switch (v.getId())
        {
            case R.id.btnEdit:
                Bundle bundle = new Bundle();
                bundle.putSerializable("Model", usuarioModel);
                Navigation.findNavController(v).navigate(R.id.nav_usuarios_edit, bundle);

                break;

            case R.id.btnDelete:
                ApiBase apiBase = new ApiBase();
                ApiApp apiApp = new ApiApp(apiBase.getRetrofit());
                IUser iUser = apiApp.getiUser();

                iUser.Delete(usuarioModel.getID()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            usuarioModelList.remove(position);
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
