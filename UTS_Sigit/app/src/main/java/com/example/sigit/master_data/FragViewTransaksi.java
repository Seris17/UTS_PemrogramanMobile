package com.example.sigit.master_data;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sigit.adapter.DBHelper;
import com.example.sigit.R;
import com.example.sigit.adapter.AdaptTransaksiBuku;
import com.example.sigit.model.EntTransaksiBuku;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragViewTransaksi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragViewTransaksi extends Fragment {
    FloatingActionButton btnTransBuku;
    RecyclerView rcViewTransBuku;
    TextView jumlahTrans;

    Context context;
    public FragViewTransaksi() {
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
    public static FragViewTransaksi newInstance(String param1, String param2) {
        FragViewTransaksi fragment = new FragViewTransaksi();
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
        View view = inflater.inflate(R.layout.fragment_view_trans_buku, container, false);
        DBHelper db = new DBHelper(context);

        btnTransBuku = (FloatingActionButton) view.findViewById(R.id.btnTransBuku);
        rcViewTransBuku = (RecyclerView) view.findViewById(R.id.rcViewTransBuku);
        jumlahTrans = (TextView) view.findViewById(R.id.jumlahTrans);

        btnTransBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.mainFragment, new FragFormTransaksi()).addToBackStack(null).commit();

            }

        });

        List<EntTransaksiBuku> listTransBuku = db.getAllTransBuku();
        rcViewTransBuku.setLayoutManager(new LinearLayoutManager(context));
        rcViewTransBuku.setAdapter(new AdaptTransaksiBuku(listTransBuku));
        jumlahTrans.setText("Jumlah Trans: " + listTransBuku.size());

        return view;
    }
}