package com.app.crudapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class ReturnSharedPreference {
    public static SharedPreferences get(Context context)
    {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("crud_app", context.MODE_PRIVATE);

        return preferences;
    }

    public static int GetUserID(Context context){
        SharedPreferences preferences = get(context);
        String id = preferences.getString("PK_User", "");
        int userId = 0;

        if(!TextUtils.isEmpty(id))
        {
            userId = Integer.parseInt(id);
        }

        return userId;
    }
}
