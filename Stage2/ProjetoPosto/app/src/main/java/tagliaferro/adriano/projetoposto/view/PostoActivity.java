package tagliaferro.adriano.projetoposto.view;

import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.List;

import tagliaferro.adriano.projetoposto.R;
import tagliaferro.adriano.projetoposto.controller.Posto;
import tagliaferro.adriano.projetoposto.controller.PostoController;

/**
 * Created by Adriano2 on 18/07/2017.
 */

public class PostoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Toolbar mToolbar;

    private EditText edtNome;
    private EditText edtPrecoComb1;
    private EditText edtPrecoComb2;
    private Spinner spinnerPostos;
    private Spinner spinnerComb1;
    private Spinner spinnerComb2;
    private Button btnSalvar;
    private Button btnGetLocation;

    private PostoController controller;

    private List<Posto> postos;
    private Posto posto;

    private List<String> postosName;
    private ArrayAdapter<String> combAdapter;
    private ArrayAdapter<String> postosAdapter;
    private String location;
    private boolean isLocationChanged = false;
    private final int ALERT_TYPE_EXCLUIR = 1;
    private final int ALERT_TYPE_ERROR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_posto);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_posto);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtNome = (EditText) findViewById(R.id.edt_posto_nome);
        edtPrecoComb1 = (EditText) findViewById(R.id.edt_posto_preco_comb1);
        edtPrecoComb2 = (EditText) findViewById(R.id.edt_posto_preco_comb2);
        spinnerPostos = (Spinner) findViewById(R.id.spinner_postos);
        spinnerComb1 = (Spinner) findViewById(R.id.spinner_posto_comb1);
        spinnerComb2 = (Spinner) findViewById(R.id.spinner_posto_comb2);
        btnSalvar = (Button) findViewById(R.id.btn_posto_salvar);
        btnGetLocation = (Button) findViewById(R.id.btn_posto_get_location);

        loadExtra();

    }

    private void loadExtra() {
        btnSalvar.setOnClickListener(this);
        btnGetLocation.setOnClickListener(this);
        spinnerPostos.setOnItemSelectedListener(this);

        combAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.combs));
        spinnerComb1.setAdapter(combAdapter);
        spinnerComb2.setAdapter(combAdapter);

        controller = new PostoController(this);
        postos = controller.query();

        postosName = new ArrayList<>();
        postosName.add(getString(R.string.select));
        if (postos.size() > 0) {
            for (Posto p : postos) {
                postosName.add(p.getPosto_nome());
            }
        }
        postosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, postosName);
        spinnerPostos.setAdapter(postosAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnSalvar.getId()) {
            if (spinnerPostos.getSelectedItem().toString().equals(getString(R.string.select))) {
                posto = new Posto();
            }
            posto.setPosto_nome(edtNome.getText().toString());
            posto.setPosto_comb1(spinnerComb1.getSelectedItem().toString());
            posto.setPosto_comb2(spinnerComb2.getSelectedItem().toString());
            posto.setPosto_valor_comb1(edtPrecoComb1.getText().toString());
            posto.setPosto_valor_comb2(edtPrecoComb2.getText().toString());
            if (isLocationChanged) {
                posto.setPosto_localizacao(location);
            }
            if (spinnerPostos.getSelectedItem().toString().equals(getString(R.string.select))) {
                //Verifica se o posto não existe para ser cadastrado.
                try {
                    controller.insert(posto);
                } catch (Exception e) {
                    buildAlerts(getString(R.string.warning), e.getMessage(), ALERT_TYPE_ERROR);
                }
            } else {
                //O posto já existe então será realizado um update no mesmo.
                try {
                    controller.update(posto);
                } catch (Exception e) {
                    buildAlerts(getString(R.string.warning), e.getMessage(), ALERT_TYPE_ERROR);
                }
            }

        } else if (v.getId() == btnGetLocation.getId()) {
            //TODO pegar a localização do posto
            isLocationChanged = true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Foi necessário pegar a position -1 devido ao fato de que a primeira posição é a palavra "selecione" que não é um veículo.
        if (position != 0) {
            posto = postos.get(position - 1);
            edtNome.setText(posto.getPosto_nome());
            spinnerComb1.setSelection(combAdapter.getPosition(posto.getPosto_comb1()));
            spinnerComb2.setSelection(combAdapter.getPosition(posto.getPosto_comb2()));
            edtPrecoComb1.setText(posto.getPosto_valor_comb1());
            edtPrecoComb2.setText(posto.getPosto_valor_comb2());
            location = posto.getPosto_localizacao();
            //TODO falta fazer o botão de excluir posto.
        } else {
            edtNome.setText("");
            edtPrecoComb1.setText("");
            edtPrecoComb2.setText("");
            spinnerComb1.setSelection(0);
            spinnerComb2.setSelection(0);
            //TODO fazer o botão de excluir ficar invisivel.
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
                        int ret = controller.delete(posto);
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
