package com.example.sigit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sigit.R;
import com.example.sigit.model.EntTransaksiBuku;

import java.util.List;

public class AdaptTransaksiBuku extends RecyclerView.Adapter<AdaptTransaksiBuku.ViewHolder> {
    private List<EntTransaksiBuku> list;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txTanggal;
        TextView txTotal;
        TextView txJumlahBarang;

        public ViewHolder(View view) {
            super(view);
            txTanggal = (TextView) view.findViewById(R.id.txTanggal);
            txTotal = (TextView) view.findViewById(R.id.txTotal);
            txJumlahBarang = (TextView) view.findViewById(R.id.txJumlahBarang);
        }
    }

    public AdaptTransaksiBuku(List<EntTransaksiBuku> listTransBuku) {
        list = listTransBuku;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rc_view_trans_buku, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txTanggal.setText(list.get(position).Tanggal);
        holder.txTotal.setText(list.get(position).Total.toString());
        holder.txJumlahBarang.setText(list.get(position).JumlahBarang.toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}