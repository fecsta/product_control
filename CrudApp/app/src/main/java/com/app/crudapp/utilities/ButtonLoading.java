package com.app.crudapp.utilities;

import android.view.View;

public class ButtonLoading {
    public static void ClickSave(boolean isResult, View btn, View progress)
    {
        if(isResult)
        {
            btn.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        }
        else {
            btn.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
    }
}
