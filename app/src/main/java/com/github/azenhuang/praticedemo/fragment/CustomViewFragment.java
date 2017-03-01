package com.github.azenhuang.praticedemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by huangyongzheng on 2/9/17.
 */

public class CustomViewFragment extends Fragment {
    public static final String ARG_VIEW_CLASS = "arg_view_class";

    public static CustomViewFragment newInstance(Class<? extends View> viewClass) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_VIEW_CLASS, viewClass);
        CustomViewFragment fragment = new CustomViewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Class viewClass = (Class) getArguments().getSerializable(ARG_VIEW_CLASS);
        if (viewClass != null) {
            try {
                Constructor<? extends View>  viewConstructor = viewClass.getDeclaredConstructor(Context.class);

                return viewConstructor.newInstance(getContext());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
