package com.rokkhi.rokkhiguard.Utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Normalfunc {

    private static final String TAG = "Normalfunc";

    public Normalfunc(){}

    public List<String> splitstring(String ss){



        String[] array=ss.trim().split(" +");

        List<String> xx=new ArrayList<>();

        for(int i=0;i<array.length;i++){
            Log.d(TAG, "splitstring: ooo1 "+ array[i]);
            if(i>0)xx.addAll(splitchar(array[i].toLowerCase()));
        }

        xx.addAll(splitchar(ss.toLowerCase()));

        Log.d(TAG, "splitstring: ooo "+ ss );

        return xx;
    }

    public List<String> splitchar(String ss){
        Log.d(TAG, "splitchar: ppp "+ ss);

        List<String> xx=new ArrayList<>();
        String yy="";

        if(ss.length()>0 && ss.charAt(0)== '+'){
            xx.add(ss);
            ss=ss.substring(3);
        }

        for(int j=0;j<ss.length();j++){
            yy=yy + ss.charAt(j);
            xx.add(yy.toLowerCase());
        }

        return xx;
    }

    public Date futuredate(){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.YEAR, 4000);
        Date date = calendar.getTime();
        return date;
    }


    public  boolean isValidEmail(String target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public  String getRandomNumberString4() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        return String.format("%04d", number);
    }

    public  String getRandomNumberString5() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        return String.format("%05d", number);
    }

    public boolean isvalidphone(String phoneno){
        if(phoneno.isEmpty())return false;

        if(phoneno.charAt(0)=='8'){
            phoneno=phoneno.substring(2);
        }
        if(phoneno.charAt(0)=='+'){
            phoneno=phoneno.substring(3);
        }
        if(phoneno.charAt(0)!='0')return false;
        phoneno=phoneno.replace("-","");
        phoneno=phoneno.replace("+88","");
        phoneno = phoneno.replace(" ","");

        Log.d(TAG, "isvalidphone: bb "+ phoneno +" "+phoneno.length());
        if(phoneno.length()!=11)return false;
        for(int i=0;i<11;i++){
            char xx= phoneno.charAt(i);
            if(xx< '0' || xx>'9')return false;
        }

        return true;
    }

}
