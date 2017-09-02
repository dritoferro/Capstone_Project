package tagliaferro.adriano.projetoposto.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import tagliaferro.adriano.projetoposto.R;
import tagliaferro.adriano.projetoposto.controller.Updates;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Updates {

    //Criação dos atributos de Fragment e outros necessários para exibição da Activity
    private FragmentListVeiculos mFragmentListVeiculos;
    private FragmentListAbastecimentos mFragmentListAbastecimentos;
    private FragmentManager mFragmentManager;

    private Toolbar mToolbar;
    private FloatingActionButton fabAddAbast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentListVeiculos = new FragmentListVeiculos();
        mFragmentListAbastecimentos = new FragmentListAbastecimentos();
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.frameListVeiculos, mFragmentListVeiculos, getString(R.string.tagFrameVeiculos))
                .add(R.id.frameListAbastecimentos, mFragmentListAbastecimentos, getString(R.string.tagFrameAbastecimentos))
                .commit();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(mToolbar);

        fabAddAbast = (FloatingActionButton) findViewById(R.id.fab_add_abastecimento);
        fabAddAbast.setOnClickListener(this);

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
        Intent newAbast = new Intent(this, AbastecimentoActivity.class);
        startActivity(newAbast);
    }

    @Override
    public void updateListAbastecimentos(int id_veiculo) {
        Bundle args = new Bundle();
        args.putInt(getString(R.string.update_abast_key), id_veiculo);
        mFragmentListAbastecimentos = new FragmentListAbastecimentos();
        mFragmentListAbastecimentos.setArguments(args);
        mFragmentManager.beginTransaction()
                .replace(R.id.frameListAbastecimentos, mFragmentListAbastecimentos, getString(R.string.tagFrameAbastecimentos))
                .commit();
    }
}
