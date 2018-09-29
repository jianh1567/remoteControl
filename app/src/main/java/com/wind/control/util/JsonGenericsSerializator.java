package com.wind.control.util;

import com.google.gson.Gson;
import com.wind.control.okhttp.callback.IGenericsSerializator;

public class JsonGenericsSerializator implements IGenericsSerializator {

    Gson mGson = new Gson();
    @Override
    public <T> T transform(String response, Class<T> classOfT) {
        return mGson.fromJson(response, classOfT);
    }

}
