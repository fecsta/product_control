package com.app.crudapp.utilities;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Response;

public class ToastMessages {
    public static void ShowToastMessageShort(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void ShowToastMessageLong(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void ShowToastError(Context context, Response response)
    {
        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
    }

    public static void ShowToastError2(Context context, Throwable response)
    {
        Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
