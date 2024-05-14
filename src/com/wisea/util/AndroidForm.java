package com.wisea.util;

import hd.utils.cn.ConverterUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AndroidForm {
    private Context context;
    private Class<?> clazz;
    private List<Object> formInputs = new ArrayList<Object>();

    public AndroidForm(Context context, Class<?> beanClazz) {
        this.context = context;
        this.clazz = beanClazz;
    }

    public void addInput(Object obj) {
        formInputs.add(obj);
    }

    public <T> T getValues() {
        T formBean = null;
        try {
            formBean = (T) clazz.newInstance();
            for (Object input : formInputs) {
                if (input instanceof TextView) {
//                    ((TextView) input).get
                    ConverterUtil.toString(((TextView) input).getText());                    
                    continue;
                }
                if (input instanceof EditText) {
                    continue;
                }
                if (input instanceof Spinner) {
                    continue;
                }
            }
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formBean;
    }
}
