package com.app.crudapp.webapi;

import com.app.crudapp.interfaces.IProduto;
import com.app.crudapp.interfaces.IUser;

import retrofit2.Retrofit;

public class ApiApp {
    private Retrofit retrofit;
    private IUser iUser;
    private IProduto iProduto;

    public ApiApp(Retrofit retrofit)
    {
        this.retrofit = retrofit;
    }

    public IUser getiUser()
    {
        this.iUser = retrofit.create(IUser.class);

        return iUser;
    }

    public IProduto getiProduto()
    {
        this.iProduto = retrofit.create(IProduto.class);

        return iProduto;
    }
}
