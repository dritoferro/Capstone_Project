package tagliaferro.adriano.projetoposto.view;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import tagliaferro.adriano.projetoposto.R;

public class MainActivity extends AppCompatActivity {

    //Criação dos atributos de Fragment e outros necessários para exibição da Activity
    private FragmentListVeiculos mFragmentListVeiculos;
    private FragmentListAbastecimentos mFragmentListAbastecimentos;
    private FragmentManager mFragmentManager;


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

    }
}
