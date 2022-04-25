package com.example.sigit;

import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sigit.model.EntBuku;
import com.example.sigit.model.EntTransaksiDetailBuku;

import java.util.ArrayList;
import java.util.List;

public class Global {
    public static MainActivity activity;
    public static boolean isTransaction;
    public static boolean isEditBuku;
    public static EntBuku bukuEdit;
    public static EntBuku bukuCart;
    public static RecyclerView rcViewBuku;
    public static RecyclerView rcViewTransBukuDetail;
    public static List<EntTransaksiDetailBuku> listCurrentCart = new ArrayList<EntTransaksiDetailBuku>();

    public static String EmptyIfNull(String value){
        if(value == null) return "";
        return value;
    }
    public static String EmptyIfNull(Double value){
        if(value == null) return "0";
        return value.toString();
    }
    public static void PergiKe(Fragment fragment){
        activity.getSupportFragmentManager().beginTransaction().
                replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
    }
    public static void Toas(String tulisan){
        Toast.makeText(activity, tulisan, Toast.LENGTH_LONG).show();
    }

    public static String CalcPlus(String strDouble1, String strDouble2){
        Double double1 = Double.parseDouble(strDouble1);
        Double double2 = Double.parseDouble(strDouble2);
        return String.valueOf(double1 + double2);
    }

    public static String CalcMinus(String strDouble1, String strDouble2){
        Double double1 = Double.parseDouble(strDouble1);
        Double double2 = Double.parseDouble(strDouble2);
        return String.valueOf(double1 - double2);
    }

    public static String CalcMultiply(String strDouble1, String strDouble2){
        Double double1 = Double.parseDouble(strDouble1);
        Double double2 = Double.parseDouble(strDouble2);
        return String.valueOf(double1 * double2);
    }

    public static String SumTotalBarang(){
        listCurrentCart.forEach(item ->
        {
            if(item.Jumlah == null) item.Jumlah = 0.0;
        });
        return String.valueOf(listCurrentCart.stream()
                .mapToDouble(x -> x.Jumlah)
                .sum());
    }
    public static String SumTotalHarga(){
        listCurrentCart.forEach(item ->
        {
            if(item.Subtotal == null) item.Subtotal = 0.0;
        });
        return String.valueOf(listCurrentCart.stream()
                .mapToDouble(x -> x.Subtotal)
                .sum());
    }


    public static String FindMaxIdTransDetail(){
        if(listCurrentCart.size() == 0) return "1";
        List<Integer> listInt = new ArrayList<Integer>();
        listCurrentCart.forEach(item -> {
            listInt.add(Integer.parseInt(item._id));
        });
        return String.valueOf(listInt.stream().mapToInt(x -> x).max().getAsInt() + 1);
    }
}