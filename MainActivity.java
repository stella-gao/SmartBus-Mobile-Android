package edu.njit.stella.smartbus;
/**
 * Created by stella on 3/21/16.
 */


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;


import edu.njit.stella.smartbus.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_simple).setOnClickListener(this);
        //findViewById(R.id.btn_transit).setOnClickListener(this);
        //findViewById(R.id.btn_alternative).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_simple) {
            goToSimpleDirection();
        } /*
        else if (id == R.id.btn_transit) {
            goToTransitDirection();
        }
        else if (id == R.id.btn_alternative) {
            goToAlternativeDirection();
        }*/
    }

    public void goToSimpleDirection() {
        openActivity(Login.class);
    }
/*
    public void goToTransitDirection() {
        openActivity(TransitDirectionActivity.class);
    }

    public void goToAlternativeDirection() {
        openActivity(AlternativeDirectionActivity.class);
    }*/

    public void openActivity(Class<?> cs) {
        startActivity(new Intent(this, cs));
    }
}
