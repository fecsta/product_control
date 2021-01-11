package com.app.crudapp.utilities;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class NavigationRoute {
    public static void NavigateAndPopStack(NavController navController, int remove, int to, Bundle bundle)
    {
        navController.popBackStack(remove, true);
        navController.navigate(to, bundle);
    }
}
