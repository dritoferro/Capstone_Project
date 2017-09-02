package tagliaferro.adriano.projetoposto.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tagliaferro.adriano.projetoposto.R;
import tagliaferro.adriano.projetoposto.controller.Abastecimento;
import tagliaferro.adriano.projetoposto.controller.AbastecimentoController;
import tagliaferro.adriano.projetoposto.controller.AbastecimentosAdapter;
import tagliaferro.adriano.projetoposto.controller.OnDataSelected;

/**
 * Created by Adriano2 on 13/07/2017.
 */

public class FragmentListAbastecimentos extends Fragment implements OnDataSelected {

    //Criação dos atributos para lidar com a RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Abastecimento> mAbastecimentosList;
    private AbastecimentoController abastController;
    private AbastecimentosAdapter abastAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_abastecimentos, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_list_abastecimentos);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        int idVeiculo = getArguments() != null ? getArguments().getInt(getString(R.string.update_abast_key)) : -1;
        abastController = new AbastecimentoController(getActivity());

        if (idVeiculo != -1) {
            mAbastecimentosList = abastController.query(idVeiculo);
            if (!mAbastecimentosList.isEmpty()) {
                abastAdapter = new AbastecimentosAdapter(getActivity(), mAbastecimentosList, this);
                mRecyclerView.setAdapter(abastAdapter);
            }
        }
    }

    @Override
    public void onDataSelected(View view, int position) {

    }

    @Override
    public void onLongDataSelected(View view, int position) {

    }
}
