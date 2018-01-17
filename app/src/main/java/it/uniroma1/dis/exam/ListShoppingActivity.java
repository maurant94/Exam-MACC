package it.uniroma1.dis.exam;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;

public class ListShoppingActivity extends Activity {

    private Products p = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_shopping);
        this.setFinishOnTouchOutside(false);

        Intent i = getIntent();
        String extraString = getApplicationContext().getString(R.string.extra_product);
        p = (new Gson()).fromJson(i.getStringExtra(extraString), Products.class);
        if (p != null) {
            TextView text = (TextView) findViewById(R.id.editName);
            text.setText(p.getName() + "");
            text = (TextView) findViewById(R.id.editQuantity);
            text.setText(p.getQuantity() + "");
        }

        //see if is an EDIT or ADD


    }

    public void done_add(View v) {
        Intent returnIntent = new Intent();

        //setup Transaction
        String name = ((EditText) findViewById(R.id.editName)).getText().toString().trim();
        String quantity = ((EditText) findViewById(R.id.editQuantity)).getText().toString().trim();
        if (name.equals("") || quantity.equals(""))
            Toast.makeText(ListShoppingActivity.this, "COMPILA I CAMPI MANCANTI", Toast.LENGTH_SHORT).show();
        else {
            Products prod = new Products(name, quantity);
            if (p != null && p.getId() != null) prod.setId(p.getId());
            String extraString = getApplicationContext().getString(R.string.extra_product);
            returnIntent.putExtra(extraString, (new Gson()).toJson(prod));
            setResult(Activity.RESULT_OK, returnIntent); //or result_canceled
            finish();
        }
    }

}