package com.example.pets;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pets.data.PetContract;

public class AddPetActivity extends AppCompatActivity {
    Spinner spinner;
    int gender=0,Weight=0;
    String NameString="",BreedString="",WeightString="";
    EditText NameEditText;
    EditText BreedEditText;
    EditText MeasurementEditText;
    boolean incompleteFields=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        spinner = findViewById(R.id.spinner_menu_category);
        String[] categories = {"Male","Female","Unknown"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,categories);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_pet_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.save:
                NameEditText = (EditText) findViewById(R.id.Overview1_edit_text_view);
                BreedEditText = (EditText) findViewById(R.id.Overview2_edit_text_view);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position){
                            case 0:
                                gender=1;
                                break;
                            case 1:
                                gender=2;
                                break;
                            case 2:
                                gender=0;
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });

                NameString = NameEditText.getText().toString();
                BreedString = BreedEditText.getText().toString();
                MeasurementEditText = (EditText) findViewById(R.id.Measurement_edit_text_view);
                WeightString = MeasurementEditText.getText().toString();
                try {
                    Weight = Integer.parseInt(WeightString);
                }
                catch ( Exception e)
                {

                }
                if(NameString.isEmpty()||BreedString.isEmpty()||WeightString.isEmpty())
                {
                    incompleteFields=true;
                    DialogInterface.OnClickListener discardButtonClickListener =
                            (dialogInterface, i) -> finish();
                    showNoDataDialog(discardButtonClickListener);
                }
                else {
                    incompleteFields=false;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PetContract.PetEntry.COLUMN_NAME, NameString);
                    contentValues.put(PetContract.PetEntry.COLUMN_BREED, BreedString);
                    contentValues.put(PetContract.PetEntry.COLUMN_GENDER, gender);
                    contentValues.put(PetContract.PetEntry.COLUMN_WEIGHT, Weight);
                    getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, contentValues);
                    Toast.makeText(getApplicationContext(), "Pet Saved", Toast.LENGTH_SHORT);
                }
                return true;
            case R.id.add_dummy_data:
                // do nothing for now
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (!incompleteFields)
            super.onBackPressed();

        DialogInterface.OnClickListener discardButtonClickListener =
                (dialogInterface, i) -> finish();
        showNoDataDialog(discardButtonClickListener);
        return false;
    }

    @Override
    public void onBackPressed() {
        Log.v("TAG","Back Button Pressed");
        if (!incompleteFields) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                (dialogInterface, i) -> finish();
        showNoDataDialog(discardButtonClickListener);
    }

    private void showNoDataDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.no_details);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.fill, (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}