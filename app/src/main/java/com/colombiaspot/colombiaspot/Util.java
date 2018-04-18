package com.colombiaspot.colombiaspot;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Equipo on 1/06/2017.
 */

public class Util {

    public static SharedPreferences sp;
    /*
        Método que devuelve el bufferedreader asociado a una URL.
     */

    public static BufferedReader Url2BufferedReader(String url) throws IOException {

        //return new BufferedReader(new InputStreamReader((new URL(url)).openStream()));
        URL Url = new URL(url);

        InputStream is = Url.openStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        return br;

    }


    /*
        Método que devuelve el contenido de una URL como una cadena de caracteres
     */

    public static String getStringContentFromUrl(String url) throws IOException {

        StringBuilder sb = new StringBuilder();

        BufferedReader br =  Url2BufferedReader(url);

        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();

        return sb.toString();

    }



    public static String obtieneId(Context mContext){
        sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        String id = sp.getString("ID", null);
        return id;
    }


}
