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
import com.example.sigit.Global;
import com.example.sigit.R;
import com.example.sigit.adapter.AdaptBuku;
import com.example.sigit.model.EntBuku;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragViewBuku#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragViewBuku extends Fragment {
    FloatingActionButton btnAddBuku;
    TextView jumlahBuku;
    Context context;
    public FragViewBuku() {
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
    public static FragViewBuku newInstance(String param1, String param2) {
        FragViewBuku fragment = new FragViewBuku();
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
        View view = inflater.inflate(R.layout.fragment_view_buku, container, false);
        DBHelper db = new DBHelper(context);

        btnAddBuku = (FloatingActionButton) view.findViewById(R.id.btnAddBuku);
        Global.rcViewBuku = (RecyclerView) view.findViewById(R.id.rcViewBuku);
        jumlahBuku = (TextView) view.findViewById(R.id.jumlahBuku);

        btnAddBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.isEditBuku = false;
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.mainFragment, new FragFormBuku()).addToBackStack(null).commit();
            }
        });

        List<EntBuku> listBuku = db.getAllBuku();
        Global.rcViewBuku.setLayoutManager(new LinearLayoutManager(context));
        Global.rcViewBuku.setAdapter(new AdaptBuku(listBuku));
        jumlahBuku.setText("Jumlah Buku: " + listBuku.size());



        return view;
    }
}