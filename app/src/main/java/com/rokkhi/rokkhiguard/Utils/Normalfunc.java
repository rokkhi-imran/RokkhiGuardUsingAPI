package com.rokkhi.rokkhiguard.Utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.rokkhi.rokkhiguard.Model.UDetails;

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


   /* public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }*/


    public void addUser(String utoken, String ulogin,String userid,String gender,String mailid,String name) {
        // Create the arguments to the callable function.
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.YEAR, 4000);
        Date date = calendar.getTime();

        List<String> ll= new ArrayList<>();
        List<String> tokens= new ArrayList<>();
        tokens.add(utoken);
        // Log.d(TAG, "onSuccess: tokenxx "+ useertoken +"xx"+ utoken);

        UDetails uDetails= new UDetails(userid,name,"","",date,gender,mailid,true,ulogin,"","","","","",tokens,new ArrayList<String>()
                ,false,Calendar.getInstance().getTime(),Calendar.getInstance().getTime(),false,ll);


        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();

        WriteBatch batch= firebaseFirestore.batch();

        DocumentReference userset1 = firebaseFirestore.collection("userdetails").document(userid);
        batch.set(userset1, uDetails);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete( Task<Void> task) {
                if(task.isSuccessful()){

                }
            }
        });


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


    public boolean isvalidphone14(String phoneno){
        if(phoneno.isEmpty())return false;

        if(phoneno.charAt(0)!='+'){
            return  false;

        }

        if(phoneno.length()!=14){
            return  false;

        }
        if(phoneno.charAt(0)=='+'){
            phoneno=phoneno.substring(3);
        }
        if(phoneno.charAt(0)!='0')return false;
        phoneno=phoneno.replace("+88","");

        Log.d(TAG, "isvalidphone: bb "+ phoneno +" "+phoneno.length());
        if(phoneno.length()!=11)return false;
        for(int i=0;i<11;i++){
            char xx= phoneno.charAt(i);
            if(xx< '0' || xx>'9')return false;
        }

        return true;
    }

    public String makephone14(String phoneno){
        if(isvalidphone14(phoneno))return phoneno;
        if(isvalidphone11(phoneno))return "+88"+phoneno;
        else return "error";
    }
    public String makephone11(String phoneno){
        if(isvalidphone14(phoneno)){
            phoneno=phoneno.replace("+88","");
            return  phoneno;
        }
        if(isvalidphone11(phoneno))return phoneno;
        else return "error";
    }

    public boolean isvalidphone11(String phoneno){
        if(phoneno.isEmpty())return false;
        if(phoneno.charAt(0)!='0')return false;

        Log.d(TAG, "isvalidphone: bb "+ phoneno +" "+phoneno.length());
        if(phoneno.length()!=11)return false;
        for(int i=0;i<11;i++){
            char xx= phoneno.charAt(i);
            if(xx< '0' || xx>'9')return false;
        }

        return true;
    }


   /* public  boolean isValidEmail(String target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }*/
/*

    public  String getRandomNumberString4() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        return String.format("%04d", number);
    }
*/

    public  String getRandomNumberString5() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        return String.format("%05d", number);
    }

  /*  public  String getPassForGuards5(String phoneno) {
        String number = phoneno.substring(6);
        return number;
    }
*/
    public boolean isvalidphone(String phoneno){


        if(phoneno.isEmpty())return  false;
        if(phoneno.charAt(0)!='0')return false;

        if(phoneno.length()!=11)return false;
        for(int i=1;i<11;i++){
            char xx= phoneno.charAt(i);
            if(xx< '0' || xx>'9')return false;
        }
        return true;
    }

    public boolean checkcontainsspaceonly(String text){
        if(text.isEmpty())return true;
        else{
            for(int i=0;i<text.length();i++){
                if(text.charAt(i) !=' ')return false;
            }
            return  true;
        }
    }
/*
    public String getvalidphone(String phoneno){

        if(phoneno.charAt(0)=='8'){
            phoneno=phoneno.substring(2);
        }
        phoneno=phoneno.replace("-","");
        phoneno=phoneno.replace("+88","");
        phoneno = phoneno.replace(" ","");
        return phoneno;
    }*/



    //get Number Without Country Code
    public String getNumberWithoutCountryCode(String st) {
        String number = "";
        if (st.charAt(0) == '+' && st.charAt(1) == '8' && st.charAt(2) == '8') {

            for (int i = 3; i < st.length(); i++) {
                number += st.charAt(i);
            }
        } else {
            number = st;
        }


        return number;
    }


}
