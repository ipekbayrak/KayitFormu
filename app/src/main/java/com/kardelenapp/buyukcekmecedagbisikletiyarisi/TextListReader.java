package com.kardelenapp.buyukcekmecedagbisikletiyarisi;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mustafa on 2/21/2018.
 */

public class TextListReader {

    BufferedReader reader;
    InputStream file;
    String line;
    List<String> lines =  new ArrayList<String>();

    public TextListReader(Context context, String filename){

        try{
            file = context.getAssets().open(filename);
            reader = new BufferedReader(new InputStreamReader(file));


            line = reader.readLine();
            while(line != null){
                lines.add(line) ;
                line = reader.readLine();
            }

        } catch(IOException ioe){
            ioe.printStackTrace();
        }

    }


    public  List<String> getList(){
        return lines;
    }
}
