package com.example.sigit.master_data;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sigit.adapter.DBHelper;
import com.example.sigit.Global;
import com.example.sigit.R;
import com.example.sigit.adapter.AdaptDetailTransaksi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragFormTransaksi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragFormTransaksi extends Fragment {
    Context context;
    FloatingActionButton btnAddTransDetail;
    Button btnBayar;
    public EditText edJumlahBayar;
    public TextView txTotalbarang;
    public TextView txTotalHarga;
    public TextView txKembalian;

    public FragFormTransaksi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FormBukuF.
     */
    // TODO: Rename and change types and number of parameters
    public static FragFormTransaksi newInstance(String param1, String param2) {
        FragFormTransaksi fragment = new FragFormTransaksi();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form_trans_buku, container, false);
        DBHelper db = new DBHelper(this.getActivity());

        Global.rcViewTransBukuDetail = (RecyclerView) view.findViewById(R.id.rcViewTransBukuDetail);
        btnAddTransDetail = (FloatingActionButton) view.findViewById(R.id.btnAddTransDetail);
        txTotalHarga = (TextView) view.findViewById(R.id.txTotalHarga);
        txTotalbarang = (TextView) view.findViewById(R.id.txTotalbarang);
        txKembalian = (TextView) view.findViewById(R.id.txKembalian);
        btnBayar = (Button) view.findViewById(R.id.btnBayar);
        edJumlahBayar = (EditText) view.findViewById(R.id.edJumlahBayar);

        Global.rcViewTransBukuDetail.setLayoutManager(new LinearLayoutManager(context));
        Global.rcViewTransBukuDetail.setAdapter(new AdaptDetailTransaksi(this));

        txTotalHarga.setText(Global.SumTotalHarga());
        txTotalbarang.setText(Global.SumTotalBarang());
        txTotalHarga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String jumlahBayar = edJumlahBayar.getText().toString();
                if (jumlahBayar.isEmpty()) {
                    txKembalian.setText("0");
                }
                else {
                    txKembalian.setText(Global.CalcMinus(jumlahBayar, Global.SumTotalHarga()));
                }
            }

        });

        edJumlahBayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String jumlahBayar = edJumlahBayar.getText().toString();
                if (jumlahBayar.isEmpty()) {
                    txKembalian.setText("0");
                }
                else {
                    txKembalian.setText(Global.CalcMinus(jumlahBayar, Global.SumTotalHarga()));
                }
            }

        });

        btnAddTransDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.isTransaction = true;
                Global.PergiKe(new FragViewBuku());
            }
        });

        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Global.activity)
                        .setTitle("Pembayaran")
                        .setMessage("Selesaikan Transaksi?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String strDate = sdf.format(c.getTime());

                                ContentValues values = new ContentValues();
                                values.put(DBHelper.trans_buku_tanggal, strDate);
                                values.put(DBHelper.trans_buku_total, Global.SumTotalHarga());
                                values.put(DBHelper.trans_buku_jumlah_barang, Global.SumTotalBarang());
                                values.put(DBHelper.trans_buku_bayar, edJumlahBayar.getText().toString());
                                values.put(DBHelper.trans_buku_kembali, txKembalian.getText().toString());
                                db.insertData(DBHelper.table_transaction_buku, values);
                                String transId = db.GetMaxIdTransaction();
                                Global.listCurrentCart.forEach(item -> {
                                    ContentValues valuesDetail = new ContentValues();
                                    valuesDetail.put(DBHelper.trans_detail_buku_trans_id, transId);
                                    valuesDetail.put(DBHelper.trans_detail_buku_buku_id, item.BukuId);
                                    valuesDetail.put(DBHelper.trans_detail_buku_judul, item.Judul);
                                    valuesDetail.put(DBHelper.trans_detail_buku_harga, item.Harga);
                                    valuesDetail.put(DBHelper.trans_detail_buku_jumlah, item.Jumlah);
                                    valuesDetail.put(DBHelper.trans_detail_buku_subtotal, item.Subtotal);
                                    db.insertData(DBHelper.table_detail_transaction, valuesDetail);
                                });
                                Global.Toas("Transaksi Selesai");
                                Global.listCurrentCart.clear();
                                Global.PergiKe(new FragViewTransaksi());
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


        return view;
    }
}