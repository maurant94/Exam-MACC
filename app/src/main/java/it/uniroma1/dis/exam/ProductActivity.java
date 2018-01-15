package it.uniroma1.dis.exam;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductActivity extends AppCompatActivity {

    private Products p = null;

    private static TextView mDateExpireDisplay = null;
    private static TextView mDateBuyDisplay = null;

    // The callback received when the user "sets" the date in the Dialog

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        //date pickers
        mDateExpireDisplay = (TextView) findViewById(R.id.textExpireDate);
        mDateBuyDisplay = (TextView) findViewById(R.id.textBuyDate);
        //initial date
        Calendar c = Calendar.getInstance();
        mDateBuyDisplay.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(c.get(Calendar.DAY_OF_MONTH)).append("/").append(c.get(Calendar.MONTH) + 1).append("/")
                .append(c.get(Calendar.YEAR)).append(" "));
        c.add(Calendar.DATE, 3);
        mDateExpireDisplay.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(c.get(Calendar.DAY_OF_MONTH)).append("/").append(c.get(Calendar.MONTH) + 1).append("/")
                .append(c.get(Calendar.YEAR)).append(" "));
        final FragmentManager frag = this.getFragmentManager();
        mDateExpireDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragmentExpire();
                newFragment.show(frag, "datePicker");
            }
        });
        mDateBuyDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragmentBuy();
                newFragment.show(frag, "datePicker");
            }
        });
        Intent i = getIntent();
        String extraString = getApplicationContext().getString(R.string.extra_product);
        p = (new Gson()).fromJson(i.getStringExtra(extraString), Products.class);
        if (p != null) {
            TextView text = (TextView) findViewById(R.id.editName);
            text.setText(p.getName() + "");
            text = (TextView) findViewById(R.id.editQuantity);
            text.setText(p.getQuantity() + "");
            text = (TextView) findViewById(R.id.textBuyDate);
            text.setText(p.getBuyDate() + "");
            text = (TextView) findViewById(R.id.textExpireDate);
            text.setText(p.getExpDate() + "");
        }

        //see if is an EDIT or ADD


    }

    public void done_add(View v) {
        Intent returnIntent = new Intent();

        //setup Transaction
        String name = ((EditText) findViewById(R.id.editName)).getText().toString().trim();
        String quantity = ((EditText) findViewById(R.id.editQuantity)).getText().toString().trim();
        String buyDate = ((TextView) findViewById(R.id.textBuyDate)).getText().toString().trim();
        String expDate = ((TextView) findViewById(R.id.textExpireDate)).getText().toString().trim();
        if (name.equals("") || quantity.equals("") || buyDate.equals("") || expDate.equals(""))
            Toast.makeText(ProductActivity.this, "COMPILA I CAMPI MANCANTI", Toast.LENGTH_SHORT).show();
        else {
            Products p = new Products(name, quantity, buyDate, expDate);
            String extraString = getApplicationContext().getString(R.string.extra_product);
            returnIntent.putExtra(extraString, (new Gson()).toJson(p));
            setResult(Activity.RESULT_OK, returnIntent); //or result_canceled
            finish();
        }
    }

    public static class DatePickerFragmentBuy extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            mDateBuyDisplay.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(day).append("/").append(month + 1).append("/")
                    .append(year).append(" "));
        }
    }

    public static class DatePickerFragmentExpire extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            mDateExpireDisplay.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(day).append("/").append(month + 1).append("/")
                    .append(year).append(" "));
        }
    }
}
