package tagliaferro.adriano.projetoposto.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tagliaferro.adriano.projetoposto.R;
import tagliaferro.adriano.projetoposto.controller.Veiculo;
import tagliaferro.adriano.projetoposto.controller.VeiculoController;
import tagliaferro.adriano.projetoposto.model.Contract.VeiculoContract;

/**
 * Created by Adriano2 on 13/07/2017.
 */

public class VeiculoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Toolbar mToolbar;
    private Button btnSalvar;
    private Button btnExcluir;
    private EditText edtNome;
    private Spinner spinnerVeiculos;
    private Spinner spinnerComb1;
    private Spinner spinnerComb2;

    private VeiculoController controller;
    private List<Veiculo> veiculos;
    private Veiculo veiculo;

    private ArrayAdapter<String> combs;
    private ArrayAdapter<String> veiculosAdapter;
    private List<String> veiculosName;

    private final int ALERT_TYPE_EXCLUIR = 1;
    private final int ALERT_TYPE_ERROR = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculo);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_veiculo);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtNome = (EditText) findViewById(R.id.edt_veiculo_nome);
        spinnerVeiculos = (Spinner) findViewById(R.id.spinner_veiculos);
        spinnerComb1 = (Spinner) findViewById(R.id.spinner_veiculo_comb1);
        spinnerComb2 = (Spinner) findViewById(R.id.spinner_veiculo_comb2);
        btnSalvar = (Button) findViewById(R.id.btn_veiculo_salvar);
        btnSalvar.setOnClickListener(this);
        btnExcluir = (Button) findViewById(R.id.btn_veiculo_excluir);
        btnExcluir.setOnClickListener(this);
        spinnerVeiculos.setOnItemSelectedListener(this);
        controller = new VeiculoController(this);

        loadExtra();
    }

    private void loadExtra() {
        try {
            combs = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.combs)));
            spinnerComb1.setAdapter(combs);
            spinnerComb2.setAdapter(combs);

            veiculos = controller.query();
            veiculosName = new ArrayList<>();
            String veicName;
            veiculosName.add(getString(R.string.select));
            for (int i = 0; i < veiculos.size(); i++) {
                veicName = veiculos.get(i).getVeiculo_nome();
                veiculosName.add(veicName);
            }
            veiculosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, veiculosName);
            spinnerVeiculos.setAdapter(veiculosAdapter);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == btnSalvar.getId()) {
                veiculo = new Veiculo();
                veiculo.setVeiculo_nome(edtNome.getText().toString());
                veiculo.setVeiculo_comb1(spinnerComb1.getSelectedItem().toString());
                veiculo.setVeiculo_comb2(spinnerComb2.getSelectedItem().toString());
                veiculo.setVeiculo_imagem("teste");
                //Aqui é para salvar as info do veículo.
                if (spinnerVeiculos.getSelectedItem().toString().equals(getString(R.string.select))) {
                    try {
                        controller.insert(veiculo);
                    } catch (Exception e){
                        if(e.getMessage().equals(getString(R.string.add_sucesso))) {
                            Toast.makeText(this, getString(R.string.add_sucesso), Toast.LENGTH_SHORT).show();
                            Intent principal = new Intent(this, MainActivity.class);
                            startActivity(principal);
                        } else {
                            buildAlerts(getString(R.string.warning), e.getMessage(), ALERT_TYPE_ERROR);
                        }
                    }
                //Aqui é para atualizar as info do veículo.
                } else {
                    int ret = controller.update(veiculo);
                    if (ret != 0) {
                        Toast.makeText(this, getString(R.string.up_sucesso), Toast.LENGTH_SHORT).show();
                        Intent principal = new Intent(this, MainActivity.class);
                        startActivity(principal);
                    } else {
                        buildAlerts(getString(R.string.warning), getString(R.string.erro_update), ALERT_TYPE_ERROR);
                    }
                }
        } else if (v.getId() == btnExcluir.getId()) {
            if (!spinnerVeiculos.getSelectedItem().toString().equals(getString(R.string.select))) {
                buildAlerts(getString(R.string.warning), getString(R.string.msg_delete_veiculo), ALERT_TYPE_EXCLUIR);

            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Foi necessário pegar a position -1 devido ao fato de que a primeira posição é a palavra "selecione" que não é um veículo.
        if (position != 0) {
            veiculo = veiculos.get(position - 1);
            edtNome.setText(veiculo.getVeiculo_nome());
            spinnerComb1.setSelection(combs.getPosition(veiculo.getVeiculo_comb1()));
            spinnerComb2.setSelection(combs.getPosition(veiculo.getVeiculo_comb2()));
            btnExcluir.setVisibility(View.VISIBLE);
        } else {
            btnExcluir.setVisibility(View.INVISIBLE);
            edtNome.setText("");
            spinnerComb1.setSelection(0);
            spinnerComb2.setSelection(0);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void buildAlerts(String title, String msg, final int alert_type) {
        AlertDialog.Builder ask = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg);

        if (alert_type == ALERT_TYPE_EXCLUIR) {
            ask.setNegativeButton(R.string.nao, null);
            ask.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        int ret = controller.delete(veiculo.getVeiculo_id());
                        if (ret != 0) {
                            Toast.makeText(getApplicationContext(), R.string.del_sucesso, Toast.LENGTH_SHORT).show();
                            Intent principal = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(principal);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (alert_type == ALERT_TYPE_ERROR) {
            ask.setNeutralButton(R.string.ok, null);
        }

        ask.show();
    }

}
