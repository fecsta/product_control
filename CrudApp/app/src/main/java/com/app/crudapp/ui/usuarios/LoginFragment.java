package com.app.crudapp.ui.usuarios;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.app.crudapp.MainActivity;
import com.app.crudapp.R;
import com.app.crudapp.interfaces.IUser;
import com.app.crudapp.models.UsuarioModel;
import com.app.crudapp.utilities.ButtonLoading;
import com.app.crudapp.utilities.ReturnSharedPreference;
import com.app.crudapp.utilities.ToastMessages;
import com.app.crudapp.webapi.ApiApp;
import com.app.crudapp.webapi.ApiBase;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener {
    private Button btnLogin, btnNewUser;
    EditText txtUser, txtPassword;
    private ProgressBar progressBar;

    private ApiBase apiBase;
    private ApiApp apiApp;
    private IUser iUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        ((MainActivity) requireActivity()).getSupportActionBar().hide();

        InitializeViews(view);

        return view;
    }

    private void InitializeViews(View view)
    {
        txtUser = view.findViewById(R.id.txtUser);
        txtPassword = view.findViewById(R.id.txtPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnNewUser = view.findViewById(R.id.btnNewUser);
        progressBar = view.findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(this);
        btnNewUser.setOnClickListener(this);

        apiBase = new ApiBase();
        apiApp = new ApiApp(apiBase.getRetrofit());
        iUser = apiApp.getiUser();

        CheckUserLogged(btnLogin);
    }

    private void CheckUserLogged(final View v)
    {
        SharedPreferences preferences = ReturnSharedPreference.get(v.getContext());
        String id = preferences.getString("PK_User", "");

        if(!TextUtils.isEmpty(id))
        {
            if(id.equals("0"))
            {
                UsuarioModel model = new UsuarioModel();
                model.setUser("ADMIN");
                model.setID(0);
                SendToHomeActivity(model);
            }
            else {
                ButtonLoading.ClickSave(true, btnLogin, progressBar);
                iUser.GetById(id).enqueue(new Callback<UsuarioModel>() {
                    @Override
                    public void onResponse(Call<UsuarioModel> call, Response<UsuarioModel> response) {
                        try {
                            if(response.isSuccessful())
                            {
                                UsuarioModel model = response.body();

                                if(model != null)
                                {
                                    SendToHomeActivity(model);
                                }
                                else {
                                    ToastMessages.ShowToastMessageLong(v.getContext(), "User not found!");
                                }
                            }
                            else {
                                ToastMessages.ShowToastError(btnLogin.getContext(), response);
                            }

                            ButtonLoading.ClickSave(false, btnLogin, progressBar);
                        }catch (Exception ex)
                        {
                            ToastMessages.ShowToastMessageShort(v.getContext(), ex.getMessage().toString());
                            ButtonLoading.ClickSave(false, btnLogin, progressBar);
                        }
                    }

                    @Override
                    public void onFailure(Call<UsuarioModel> call, Throwable t) {
                        ToastMessages.ShowToastError2(btnLogin.getContext(), t);
                        ButtonLoading.ClickSave(false, btnLogin, progressBar);
                    }
                });
            }

        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId())
        {
            case R.id.btnLogin:
                try{
                    UsuarioModel userModel = new UsuarioModel(txtUser.getText().toString(), txtPassword.getText().toString());

                    if(CheckFields(userModel, v))
                    {
                        ButtonLoading.ClickSave(true, btnLogin, progressBar);

                        if(userModel.getUser().toUpperCase().equals("ADMIN") && userModel.getPassword().toUpperCase().equals("ADMIN"))
                        {
                            SendToHomeActivity(userModel);
                        }
                        else {
                            iUser.CheckUser(userModel.getUser(), userModel.getPassword()).enqueue(new Callback<List<UsuarioModel>>() {
                                @Override
                                public void onResponse(Call<List<UsuarioModel>> call, Response<List<UsuarioModel>> response) {
                                    try {
                                        if(response.isSuccessful())
                                        {
                                            List<UsuarioModel> models = response.body();

                                            if(models.size() > 0)
                                            {
                                                SendToHomeActivity(models.get(0));
                                            }
                                            else {
                                                ToastMessages.ShowToastMessageLong(v.getContext(), "User not found!");
                                            }
                                        }
                                        else {
                                            ToastMessages.ShowToastError(btnLogin.getContext(), response);
                                        }

                                        ButtonLoading.ClickSave(false, btnLogin, progressBar);
                                    }catch (Exception ex)
                                    {
                                        ToastMessages.ShowToastMessageShort(v.getContext(), ex.getMessage().toString());
                                        ButtonLoading.ClickSave(false, btnLogin, progressBar);
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<UsuarioModel>> call, Throwable t) {
                                    ToastMessages.ShowToastError2(btnLogin.getContext(), t);
                                    ButtonLoading.ClickSave(false, btnLogin, progressBar);
                                }
                            });
                        }


                    }
                }catch (Exception ex)
                {
                    ToastMessages.ShowToastMessageShort(v.getContext(), ex.getMessage().toString());
                }

                break;

            case R.id.btnNewUser:
                Bundle bundle = new Bundle();
                bundle.putBoolean("NewUser", true);

                Navigation.findNavController(v).navigate(R.id.nav_usuarios_add, bundle);

                break;
        }
    }

    private boolean CheckFields(UsuarioModel usuarioModel, View view)
    {
        boolean result = true;
        String msgError = "";

        if(TextUtils.isEmpty(usuarioModel.getUser()))
        {
            msgError += "Username is blank!\n";
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

    private void SendToHomeActivity(UsuarioModel model)
    {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        SetTextMenu(navigationView, model);
        SaveLoginShared(model);
        ((MainActivity) requireActivity()).getSupportActionBar().show();

        Navigation.findNavController(btnLogin).navigate(R.id.nav_home);
    }

    private void SetTextMenu(NavigationView navigationView, UsuarioModel usuarioModel)
    {
        View navHeader = navigationView.getHeaderView(0);
        TextView txtNome = navHeader.findViewById(R.id.txtUser);

        txtNome.setText(usuarioModel.getUser().toUpperCase());
    }

    private void SaveLoginShared(UsuarioModel userModel)
    {
        SharedPreferences preferences = ReturnSharedPreference.get(getContext());
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("PK_User", userModel.getID());

        editor.commit();
    }
}
