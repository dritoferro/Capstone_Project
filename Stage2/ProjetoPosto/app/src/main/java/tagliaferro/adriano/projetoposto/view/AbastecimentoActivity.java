package tagliaferro.adriano.projetoposto.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tagliaferro.adriano.projetoposto.R;
import tagliaferro.adriano.projetoposto.controller.Abastecimento;
import tagliaferro.adriano.projetoposto.controller.AbastecimentoController;
import tagliaferro.adriano.projetoposto.controller.Posto;
import tagliaferro.adriano.projetoposto.controller.PostoController;
import tagliaferro.adriano.projetoposto.controller.Veiculo;
import tagliaferro.adriano.projetoposto.controller.VeiculoController;

/**
 * Created by Adriano2 on 18/07/2017.
 */

public class AbastecimentoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Toolbar mToolbar;

    private EditText edtKmAtual;
    private EditText edtValorLitro;
    private Spinner spinnerVeiculo;
    private Spinner spinnerPosto;
    private Button btnData;
    private Button btnSalvar;
    private ImageView imgVeiculo;

    private ArrayAdapter<String> veiculoAdapter;
    private ArrayAdapter<String> postoAdapter;
    private List<Veiculo> veiculosList;
    private List<Posto> postosList;
    private List<String> veiculoNameList;
    private List<String> postoNameList;


    private AbastecimentoController AbastController;
    private VeiculoController veicController;
    private PostoController postoController;

    private static Abastecimento mAbastecimento;
    private Veiculo mVeiculo;
    private Posto mPosto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_abastecimento);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_abastecimento);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtKmAtual = (EditText) findViewById(R.id.edt_abastecimento_kmVeiculo);
        edtValorLitro = (EditText) findViewById(R.id.edt_abastecimento_precoLitro);
        spinnerVeiculo = (Spinner) findViewById(R.id.spinner_abastecimento_veiculo);
        spinnerPosto = (Spinner) findViewById(R.id.spinner_abastecimento_posto);
        btnData = (Button) findViewById(R.id.btn_abastecimento_data);
        btnSalvar = (Button) findViewById(R.id.btn_abastecimento_salvar);
        imgVeiculo = (ImageView) findViewById(R.id.img_abastecimento_veiculo);

        AbastController = new AbastecimentoController(this);
        veicController = new VeiculoController(this);
        postoController = new PostoController(this);
        loadExtra();
    }

    private void loadExtra() {
        btnSalvar.setOnClickListener(this);
        btnData.setOnClickListener(this);
        spinnerVeiculo.setOnItemSelectedListener(this);
        spinnerPosto.setOnItemSelectedListener(this);
        veiculoNameList = new ArrayList<>();
        postoNameList = new ArrayList<>();
        veiculosList = new ArrayList<>();
        postosList = new ArrayList<>();

        veiculosList = veicController.query();
        //Verifica se há veiculos cadastrados, caso não haja, redireciona para tela de cadastro.
        if (!veiculosList.isEmpty()) {
            veiculoNameList.add(getString(R.string.select));
            for (Veiculo v : veiculosList) {
                veiculoNameList.add(v.getVeiculo_nome());
            }
            veiculoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, veiculoNameList);
            spinnerVeiculo.setAdapter(veiculoAdapter);
        } else {
            Toast.makeText(this, R.string.zero_veiculo, Toast.LENGTH_SHORT).show();
            Intent mIntent = new Intent(this, VeiculoActivity.class);
            startActivity(mIntent);
        }

        postosList = postoController.query();
        //Verifica se há postos cadastrados, caso não haja, redireciona para tela de cadastro.
        if (!postosList.isEmpty()) {
            postoNameList.add(getString(R.string.select));
            for (Posto p : postosList) {
                postoNameList.add(p.getPosto_nome());
            }
            postoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, postoNameList);
            spinnerPosto.setAdapter(postoAdapter);
        } else {
            Toast.makeText(this, R.string.zero_posto, Toast.LENGTH_SHORT).show();
            Intent mIntent = new Intent(this, PostoActivity.class);
            startActivity(mIntent);
        }
        if (mAbastecimento != null) {
            //Preenche os campos, pois é uma alteração.
            btnData.setText(mAbastecimento.getAbastecimento_data());
            edtKmAtual.setText(mAbastecimento.getAbastecimento_km_atual());
            edtValorLitro.setText(mAbastecimento.getAbastecimento_valor_litro());
            //Verifica se o veículo que foi selecionado ainda existe.
            for (Veiculo v : veiculosList) {
                if (v.getVeiculo_id() == mAbastecimento.getAbastecimento_veiculo_id()) {
                    mVeiculo = v;
                    spinnerVeiculo.setSelection(veiculoAdapter.getPosition(mVeiculo.getVeiculo_nome()));
                }
            }
            //Verifica se o posto que foi selecionado ainda existe.
            for (Posto p : postosList) {
                if (p.getPosto_id() == mAbastecimento.getAbastecimento_posto_id()) {
                    mPosto = p;
                    spinnerPosto.setSelection(postoAdapter.getPosition(mPosto.getPosto_nome()));
                }
            }
            if (!mVeiculo.getVeiculo_imagem().isEmpty()) {
                Glide.with(this).load(mVeiculo.getVeiculo_imagem()).into(imgVeiculo);
            }
        }
    }

    @Override
    protected void onDestroy() {
        mAbastecimento = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_posto) {
            Intent postoActivity = new Intent(this, PostoActivity.class);
            startActivity(postoActivity);
        } else if (item.getItemId() == R.id.menu_item_veiculo) {
            Intent veiculoActivity = new Intent(this, VeiculoActivity.class);
            startActivity(veiculoActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        //Clique do botão data
        if (v.getId() == btnData.getId()) {

        }//Clique do botão salvar
        else if (v.getId() == btnSalvar.getId()) {

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Verifica se foi selecionado um veículo ou um posto respectivamente.
        //A position - 1 é o objeto verdadeiro, pois na lista de nomes, o primeiro objeto é o "selecione".
        if (id == spinnerVeiculo.getId()) {
            //Verifica se o veículo selecionado é diferente de "selecione", se sim, obtem o mesmo na lista.
            if (!spinnerVeiculo.getSelectedItem().toString().equals(getString(R.string.select))) {
                mVeiculo = veiculosList.get(position - 1);
            }
        } else if (id == spinnerPosto.getId()) {
            //Verifica se o posto selecionado é diferente de "selecione", se sim, obtem o mesmo na lista.
            if (!spinnerPosto.getSelectedItem().toString().equals(getString(R.string.select))) {
                mPosto = postosList.get(position - 1);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
