package tagliaferro.adriano.projetoposto.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import tagliaferro.adriano.projetoposto.R;

/**
 * Created by Adriano2 on 18/07/2017.
 */

public class PostoActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_posto);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_posto);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
