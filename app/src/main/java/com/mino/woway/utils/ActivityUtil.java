package com.mino.woway.utils;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ActivityUtil {

    public static void pushFragment(@NonNull Fragment fragment, @NonNull FragmentManager fragmentManager,
                                    @IdRes int resId, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction().remove(fragment);
        if (addToBackStack) {
            transaction.add(resId, fragment, fragment.getClass().getSimpleName());
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        } else {
            transaction.replace(resId, fragment, fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }
}
