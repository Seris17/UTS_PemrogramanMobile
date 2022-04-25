package com.example.sigit.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sigit.Global;
import com.example.sigit.R;
import com.example.sigit.model.EntTransaksiDetailBuku;
import com.example.sigit.master_data.FragFormTransaksi;

import java.util.List;

public class AdaptDetailTransaksi extends RecyclerView.Adapter<AdaptDetailTransaksi.ViewHolder> {
    private List<EntTransaksiDetailBuku> list;
    private FragFormTransaksi fragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText edJudul, edHarga, edJumlah, edSubTotal;
        Button btnDeleteTransDetail;

        public ViewHolder(View view) {
            super(view);
            edJudul = (EditText) view.findViewById(R.id.edJudul);
            edHarga = (EditText) view.findViewById(R.id.edHarga);
            edJumlah = (EditText) view.findViewById(R.id.edJumlah);
            edSubTotal = (EditText) view.findViewById(R.id.edSubTotal);
            btnDeleteTransDetail = (Button) view.findViewById(R.id.btnDeleteTransDetail);
        }
    }

    public AdaptDetailTransaksi(FragFormTransaksi paramFragment) {
        list = Global.listCurrentCart;
        fragment = paramFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rc_view_trans_buku_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.edJudul.setText(Global.EmptyIfNull(list.get(position).Judul));
        holder.edHarga.setText(Global.EmptyIfNull(list.get(position).Harga));
        holder.edJumlah.setText(Global.EmptyIfNull(list.get(position).Jumlah));
        holder.edSubTotal.setText(Global.EmptyIfNull(list.get(position).Subtotal));

        holder.edHarga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                holder.edSubTotal.setText(Global.CalcMultiply(holder.edHarga.getText().toString(), holder.edJumlah.getText().toString()));
                Global.listCurrentCart.get(position).Harga = Double.parseDouble(holder.edHarga.getText().toString());
                Global.listCurrentCart.get(position).Subtotal = Double.parseDouble(holder.edSubTotal.getText().toString());
                fragment.txTotalbarang.setText(Global.SumTotalBarang());
                fragment.txTotalHarga.setText(Global.SumTotalHarga());
            }

        });

        holder.edJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                holder.edSubTotal.setText(Global.CalcMultiply(holder.edHarga.getText().toString(), holder.edJumlah.getText().toString()));
                Global.listCurrentCart.get(position).Jumlah = Double.parseDouble(holder.edJumlah.getText().toString());
                Global.listCurrentCart.get(position).Subtotal = Double.parseDouble(holder.edSubTotal.getText().toString());
                fragment.txTotalbarang.setText(Global.SumTotalBarang());
                fragment.txTotalHarga.setText(Global.SumTotalHarga());
            }

        });

        holder.btnDeleteTransDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Global.activity)
                        .setTitle("Hapus Transaksi")
                        .setMessage("Apakah kamu yakin akan menghapus buku " + list.get(position).Judul +"?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Global.listCurrentCart.removeIf(x -> x._id == list.get(position)._id);
                                Global.PergiKe(new FragFormTransaksi());
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        holder.edJudul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.Toas("hmn");

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}