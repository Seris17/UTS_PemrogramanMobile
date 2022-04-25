package com.example.sigit.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sigit.Global;
import com.example.sigit.R;
import com.example.sigit.master_data.FragFormBuku;
import com.example.sigit.master_data.FragViewBuku;
import com.example.sigit.model.EntBuku;
import com.example.sigit.model.EntTransaksiDetailBuku;
import com.example.sigit.master_data.FragFormTransaksi;

import java.util.ArrayList;
import java.util.List;

public class AdaptBuku extends RecyclerView.Adapter<AdaptBuku.ViewHolder> {
    private List<EntBuku> list = new ArrayList<EntBuku>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txJudul;
        TextView txHarga;
        TextView txDesk;
        Button btnEdit;
        Button btnDelete;
        LinearLayout llBuku;

        public ViewHolder(View view) {
            super(view);
            txJudul = (TextView) view.findViewById(R.id.txJudul);
            txHarga = (TextView) view.findViewById(R.id.txHarga);
            txDesk = (TextView) view.findViewById(R.id.txDesk);
            btnEdit = (Button) view.findViewById(R.id.btnEdit);
            btnDelete = (Button) view.findViewById(R.id.btnDelete);
            llBuku = (LinearLayout) view.findViewById(R.id.llBuku);
        }
    }

    public AdaptBuku(List<EntBuku> listBuku) {
        list = listBuku;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rc_view_buku, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DBHelper db = new DBHelper(Global.activity);

        EntBuku buku = new EntBuku();
        buku._id = list.get(position)._id;
        buku.Judul = list.get(position).Judul;
        buku.Harga = list.get(position).Harga;
        buku.Deskripsi = list.get(position).Deskripsi;

        holder.txJudul.setText(list.get(position).Judul);
        holder.txHarga.setText(list.get(position).Harga.toString());
        holder.txDesk.setText(list.get(position).Deskripsi);

        if(Global.isTransaction){
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.llBuku.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EntTransaksiDetailBuku entTransaksiDetailBuku = new EntTransaksiDetailBuku();
                    entTransaksiDetailBuku._id = Global.FindMaxIdTransDetail();
                    entTransaksiDetailBuku.BukuId = buku._id;
                    entTransaksiDetailBuku.Judul = buku.Judul;
                    entTransaksiDetailBuku.Harga = buku.Harga;
                    Global.listCurrentCart.add(entTransaksiDetailBuku);
                    Global.PergiKe(new FragFormTransaksi());
                }
            });
        }
        else{
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
        }

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.bukuEdit = buku;
                Global.isEditBuku = true;
                Global.activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.mainFragment, new FragFormBuku()).addToBackStack(null).commit();

            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Global.activity)
                        .setTitle("Hapus Buku")
                        .setMessage("Apakah kamu yakin akan menghapus buku " + buku.Judul +"?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteData(DBHelper.table_master_buku, DBHelper.master_buku_id, buku._id);
                                Global.Toas("Buku " + buku.Judul + " telah dihapus");
                                Global.activity.getSupportFragmentManager().beginTransaction().
                                        replace(R.id.mainFragment, new FragViewBuku()).addToBackStack(null).commit();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}