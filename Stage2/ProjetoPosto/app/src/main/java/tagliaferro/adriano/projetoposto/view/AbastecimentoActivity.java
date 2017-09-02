package tagliaferro.adriano.projetoposto.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private EditText edtValor;
    private Spinner spinnerVeiculo;
    private Spinner spinnerPosto;
    private Spinner spinnerVeicComb;
    private static Button btnData;
    private Button btnSalvar;
    private ImageView imgVeiculo;

    private ArrayAdapter<String> veiculoAdapter;
    private ArrayAdapter<String> postoAdapter;
    private ArrayAdapter<String> veicCombAdapter;
    private List<Veiculo> veiculosList;
    private List<Posto> postosList;
    private List<String> veiculoNameList;
    private List<String> postoNameList;
    private List<String> veicCombList;
    private static String dataAbastecimento;

    private AbastecimentoController AbastController;
    private VeiculoController veicController;
    private PostoController postoController;

    private static Abastecimento mAbastecimento;
    private Veiculo mVeiculo;
    private Posto mPosto;

    private boolean hasVeiculo = true;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_abastecimento);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_abastecimento);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtKmAtual = (EditText) findViewById(R.id.edt_abastecimento_kmVeiculo);
        edtValorLitro = (EditText) findViewById(R.id.edt_abastecimento_precoLitro);
        edtValor = (EditText) findViewById(R.id.edt_abastecimento_valor);
        spinnerVeiculo = (Spinner) findViewById(R.id.spinner_abastecimento_veiculo);
        spinnerPosto = (Spinner) findViewById(R.id.spinner_abastecimento_posto);
        spinnerVeicComb = (Spinner) findViewById(R.id.spinner_abastecimento_veicComb);
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
        spinnerVeicComb.setOnItemSelectedListener(this);
        veiculoNameList = new ArrayList<>();
        postoNameList = new ArrayList<>();
        veiculosList = new ArrayList<>();
        postosList = new ArrayList<>();
        veicCombList = new ArrayList<>();

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
            hasVeiculo = false;
            Toast.makeText(this, R.string.zero_veiculo, Toast.LENGTH_SHORT).show();
            Intent mIntent = new Intent(this, VeiculoActivity.class);
            startActivity(mIntent);
        }

        if (hasVeiculo) {
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
        }
        veicCombList.add(getString(R.string.select));
        veicCombAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, veicCombList);
        spinnerVeicComb.setAdapter(veicCombAdapter);
        if (mAbastecimento != null) {
            //Preenche os campos, pois é uma alteração.
            isUpdate = true;
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
        } else {
            //Caso seja um novo cadastro, coloque a data atual no botão de data.
            dataAbastecimento = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault()).format(new Date());
            btnData.setText(dataAbastecimento);
        }

    }

    @Override
    protected void onDestroy() {
        mAbastecimento = null;
        dataAbastecimento = null;
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
            new SimpleCalendarDialogFragment().show(getSupportFragmentManager(), getString(R.string.calendar_dialog_tag));
        }//Clique do botão salvar
        else if (v.getId() == btnSalvar.getId()) {
            try {
                //if mAbastecimento igual a null, então é um novo abastecimento.
                if (mAbastecimento == null) {
                    mAbastecimento = new Abastecimento();
                    isUpdate = false;
                    buildAbastecimento();
                    AbastController.insert(mAbastecimento);
                } else {
                    isUpdate = true;
                    //Aqui é uma alteração de um abastecimento, cuidado para não perder o id.
                    buildAbastecimento();
                    int ret = AbastController.update(mAbastecimento);
                    if (ret != 0) {
                        Toast.makeText(this, getString(R.string.up_sucesso), Toast.LENGTH_SHORT).show();
                        Intent principal = new Intent(this, MainActivity.class);
                        startActivity(principal);
                    } else {
                        buildAlerts(getString(R.string.warning), getString(R.string.erro_update));
                    }
                }
            } catch (Exception e) {
                if (e.getMessage().equals(getString(R.string.add_sucesso))) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent principal = new Intent(this, MainActivity.class);
                    startActivity(principal);
                } else {
                    buildAlerts(getString(R.string.warning), e.getMessage());
                }
            }
        }
    }

    private void buildAbastecimento() {
        mAbastecimento.setAbastecimento_veiculo_id(mVeiculo != null ? mVeiculo.getVeiculo_id() : null);
        mAbastecimento.setAbastecimento_comb(spinnerVeicComb.getSelectedItem().toString());
        mAbastecimento.setAbastecimento_km_atual(edtKmAtual.getText().toString());
        mAbastecimento.setAbastecimento_posto_id(mPosto != null ? mPosto.getPosto_id() : null);
        mAbastecimento.setAbastecimento_valor_litro(edtValorLitro.getText().toString());
        mAbastecimento.setAbastecimento_data(btnData.getText().toString());
        mAbastecimento.setAbastecimento_valor(edtValor.getText().toString());
        if(!isUpdate){
            mAbastecimento.setAbastecimento_id(-1);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Verifica se foi selecionado um veículo ou um posto respectivamente.
        //A position - 1 é o objeto verdadeiro, pois na lista de nomes, o primeiro objeto é o "selecione".
        if (parent.getId() == spinnerVeiculo.getId()) {
            //Verifica se o veículo selecionado é diferente de "selecione", se sim, obtem o mesmo na lista.
            if (!spinnerVeiculo.getSelectedItem().toString().equals(getString(R.string.select))) {
                mVeiculo = veiculosList.get(position - 1);
                //Adiciona os combustíveis do veículo ao spinner
                veicCombList.clear();
                if (!mVeiculo.getVeiculo_comb1().equals(getString(R.string.select))) {
                    veicCombList.add(mVeiculo.getVeiculo_comb1());
                }
                if (!mVeiculo.getVeiculo_comb2().equals(getString(R.string.select))) {
                    veicCombList.add(mVeiculo.getVeiculo_comb2());
                }
                veicCombAdapter.notifyDataSetChanged();

            } else {
                mVeiculo = null;
                veicCombList.clear();
                veicCombList.add(getString(R.string.select));
                veicCombAdapter.notifyDataSetChanged();
            }
        } else if (parent.getId() == spinnerPosto.getId()) {
            //Verifica se o posto selecionado é diferente de "selecione", se sim, obtem o mesmo na lista.
            if (!spinnerPosto.getSelectedItem().toString().equals(getString(R.string.select))) {
                mPosto = postosList.get(position - 1);
                if (spinnerVeicComb.getSelectedItem().equals(mPosto.getPosto_comb1())) {
                    edtValorLitro.setText(mPosto.getPosto_valor_comb1());
                } else if (spinnerVeicComb.getSelectedItem().equals(mPosto.getPosto_comb2())) {
                    edtValorLitro.setText(mPosto.getPosto_valor_comb2());
                } else {
                    edtValorLitro.setText("");
                }
            } else {
                mPosto = null;
                edtValorLitro.setText("");
            }
        } else if (parent.getId() == spinnerVeicComb.getId()) {
            if (mPosto != null && mVeiculo != null) {
                if (spinnerVeicComb.getSelectedItem().equals(mPosto.getPosto_comb1())) {
                    edtValorLitro.setText(mPosto.getPosto_valor_comb1());
                } else if (spinnerVeicComb.getSelectedItem().equals(mPosto.getPosto_comb2())) {
                    edtValorLitro.setText(mPosto.getPosto_valor_comb2());
                } else {
                    edtValorLitro.setText("");
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static class SimpleCalendarDialogFragment extends AppCompatDialogFragment implements OnDateSelectedListener {

        private DateFormat format = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater inflater = getActivity().getLayoutInflater();

            //inflate custom layout and get views
            //pass null as parent view because will be in dialog layout
            View view = inflater.inflate(R.layout.calendar_dialog, null);

            MaterialCalendarView widget = (MaterialCalendarView) view.findViewById(R.id.my_calendar);

            widget.setOnDateChangedListener(this);
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.selecione_data)
                    .setView(view)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (dataAbastecimento != null) {
                                btnData.setText(dataAbastecimento);
                            }
                        }
                    })
                    .create();
        }

        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
            dataAbastecimento = format.format(date.getDate());
        }
    }

    public void buildAlerts(String title, String msg) {
        AlertDialog.Builder ask = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setNeutralButton(R.string.ok, null);

        ask.show();

    }
}
