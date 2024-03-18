package com.example.calc_new;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.calc_new.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ArrayList<Button> calculator_buttons = new ArrayList();

    private ArrayList<String> pervious_calculations_list = new ArrayList();
    private Button plus;
    private Button minus;
    private Button multiply;
    private Button divide;
    private Button equals;
    private Button clear;
    private Button history;
    private Button decimal;

    private TextView calc_monitor;


    private void clear_monitor(){
        calc_monitor.setText("");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        calc_monitor = (TextView) findViewById(R.id.calc_monitor);
        clear_monitor();
//        setSupportActionBar(binding.toolbar);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        Resources res = getResources();
//        int id = res.getIdentifier("titleText", "id", this.getApplicationContext().getPackageName());
        for(int i = 0; i < 10; i++){
            //number button
//            Resources res = getResources();
            int id = res.getIdentifier("button_" + i, "id",  this.getApplicationContext().getPackageName());
            Button newb = (Button) findViewById( id);
            newb.setOnClickListener(this);
            calculator_buttons.add(newb);
//            private Button plus;
//            private Button minus;
//            private Button multiply;
//            private Button divide;
//            private Button equals;
//            private Button clear;
//            private Button decimal;
             plus = (Button) findViewById( R.id.button_plus);

             minus = (Button) findViewById( R.id.button_minus);
             multiply = (Button) findViewById( R.id.button_multiply);
             divide = (Button) findViewById( R.id.button_divide);
             equals = (Button) findViewById( R.id.button_equals);
             clear = (Button) findViewById( R.id.button_clear);
             history = (Button) findViewById( R.id.button_history);
//             decimal = (Button) findViewById( R.id.button_decimal);

            plus.setOnClickListener(this);
            minus.setOnClickListener(this);
            multiply.setOnClickListener(this);
            divide.setOnClickListener(this);
            equals.setOnClickListener(this);
            clear.setOnClickListener(this);
            history.setOnClickListener(this);
//            decimal.setOnClickListener(this);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
//        Snackbar.make(view,((Button)view).getText().toString() , Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
        calc_monitor.setTextColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
        switch (((Button)view).getText().toString()) {
            case "C":
                calc_monitor.setText("");
                return;
            case "=":
                try {
                    float answer = eval(calc_monitor.getText().toString());
                    if ((answer * 10) % 10 == 0) {
                        pervious_calculations_list.add(calc_monitor.getText().toString() + "=" + String.valueOf((int) answer));
                        calc_monitor.setText(String.valueOf((int) answer));
                    } else {
                        pervious_calculations_list.add(calc_monitor.getText().toString() + "=" + String.valueOf(answer));
                        calc_monitor.setText(String.valueOf(answer));
                    }
                    calc_monitor.setTextColor(getResources().getColor(com.google.android.material.R.color.design_default_color_secondary));

                }catch (Exception ex){
                    calc_monitor.setText(String.valueOf(ex.getMessage() ));
                    calc_monitor.setTextColor(getResources().getColor(com.google.android.material.R.color.design_default_color_error));
                }
                return;
            case "H":
                //if history in empty show no history
                if(pervious_calculations_list.size() == 0){
                    //show no history toast
                    Snackbar.make(view, "No History", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    return;
                }
                //show all previous calculations in a dialog
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("History");

                builder.setItems(pervious_calculations_list.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get text of which clicked
                        calc_monitor.setText(pervious_calculations_list.get(which).split("=")[0]);
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();

                return;
        }
        calc_monitor.setText(calc_monitor.getText().toString() + ((Button)view).getText().toString());
    }

    private static final String OPERATOR_MATCHER = "(\\+|\\-|\\*|\\/)";

    public float eval(String formula) {

        String[] tokens = formula.split(OPERATOR_MATCHER);

        String operator = "";
        Matcher m = Pattern.compile(OPERATOR_MATCHER).matcher(formula);
        while (m.find()) {
            operator = m.group();
        }

        Float[] operands = new Float[tokens.length];
        for (int i = 0; i < tokens.length; ++i) {
            operands[i] = Float.parseFloat(tokens[i]);
        }

        return doOp(operator, operands);
    }

    private float doOp(String operator, Float[] operands) {
        if(operator.equals("+") ){
            return operands[0] + operands[1];
        }else if (operator.equals("-"))  {
            return operands[0] - operands[1];
        }else if (operator.equals("*") )  {
            return operands[0] * operands[1];
        }else if (operator.equals("/"))  {
            return operands[0] / operands[1];
        }
        return operands[0];
    }
}