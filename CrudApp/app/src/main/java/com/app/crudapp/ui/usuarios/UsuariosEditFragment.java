package com.app.crudapp.ui.usuarios;

import android.content.Intent;
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
import com.app.crudapp.interfaces.IUser;
import com.app.crudapp.models.UsuarioModel;
import com.app.crudapp.utilities.ButtonLoading;
import com.app.crudapp.utilities.NavigationRoute;
import com.app.crudapp.utilities.ToastMessages;
import com.app.crudapp.webapi.ApiApp;
import com.app.crudapp.webapi.ApiBase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuariosEditFragment extends Fragment implements View.OnClickListener {
    private EditText
            txtUser,txtPassword;

    private Button btnSave;
    private ProgressBar progressBar;
    private ApiBase apiBase;
    private ApiApp apiApp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.usuarios_edit_fragment, container, false);

        Bundle bundle = getArguments();

        InitializeViews(view, bundle);

        return view;
    }

    private void InitializeViews(View itemView, Bundle bundle)
    {
        txtUser = itemView.findViewById(R.id.txtUser);
        txtPassword = itemView.findViewById(R.id.txtPassword);
        progressBar = itemView.findViewById(R.id.progressBar);

        btnSave = itemView.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(this);

        apiBase = new ApiBase();
        apiApp = new ApiApp(apiBase.getRetrofit());

        UsuarioModel usuarioModel = (UsuarioModel) bundle.getSerializable("Model");
        txtUser.setText(usuarioModel.getUser());
        txtPassword.setText(usuarioModel.getPassword());

        btnSave.setTag(usuarioModel.getID());
    }

    @Override
    public void onClick(View v) {
        final int id = Integer.parseInt(v.getTag().toString());

        switch (v.getId())
        {
            case R.id.btnSave:
                UsuarioModel usuarioModel = new UsuarioModel(id, txtUser.getText().toString(), txtPassword.getText().toString());

                boolean check = CheckFields(usuarioModel, v);

                if(check)
                {
                    IUser iUser = apiApp.getiUser();

                    ButtonLoading.ClickSave(true, btnSave, progressBar);

                    iUser.Edit(usuarioModel.getID(),usuarioModel).enqueue(new Callback<UsuarioModel>() {
                        @Override
                        public void onResponse(Call<UsuarioModel> call, Response<UsuarioModel> response) {
                            if(response.isSuccessful())
                            {
                                UsuarioModel user = response.body();

                                if(user != null)
                                {
                                    NavController controller = Navigation.findNavController(btnSave);
                                    NavigationRoute.NavigateAndPopStack(controller, R.id.nav_usuarios, R.id.nav_usuarios, null);
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
                        public void onFailure(Call<UsuarioModel> call, Throwable t) {
                            ToastMessages.ShowToastMessageShort(btnSave.getContext(), t.getMessage());
                            ButtonLoading.ClickSave(false, btnSave, progressBar);
                        }
                    });
                }

                break;
        }
    }

    private boolean CheckFields(UsuarioModel usuarioModel, View view)
    {
        boolean result = true;
        String msgError = "";

        if(TextUtils.isEmpty(usuarioModel.getUser()))
        {
            msgError += "User is blank!\n";
            result = false;
        }

        if(TextUtils.isEmpty(usuarioModel.getPassword()))
        {
            msgError += "Password is blank!\n";
            result = false;
        }

        if(!TextUtils.isEmpty(msgError))
        {
            ToastMessages.ShowToastMessageLong(view.getContext(), msgError.trim());
        }

        return result;
    }
}
