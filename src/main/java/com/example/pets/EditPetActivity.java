package com.example.pets;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.pets.data.PetContract;

public class EditPetActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    EditText NameEditText,BreedEditText,MeasurementEditText;
    Spinner spinner;
    int gender=0,Weight=0;
    long pet_id;
    String NameString,BreedString;
    private boolean mPetHasChanged = false;

    @SuppressLint("ClickableViewAccessibility")
    private final View.OnTouchListener mTouchListener = (view, motionEvent) -> {
        mPetHasChanged = true;
        return false;
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);

        NameEditText = findViewById(R.id.Overview1_edit_text_view);
        BreedEditText = findViewById(R.id.Overview2_edit_text_view);
        MeasurementEditText = findViewById(R.id.Measurement_edit_text_view);
        spinner = findViewById(R.id.spinner_menu_category);
        String[] categories = {"Male","Female","Unknown"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,categories);
        spinner.setAdapter(arrayAdapter);

        NameEditText.setOnTouchListener(mTouchListener);
        BreedEditText.setOnTouchListener(mTouchListener);
        MeasurementEditText.setOnTouchListener(mTouchListener);
        spinner.setOnTouchListener(mTouchListener);
        getLoaderManager().initLoader(0,null,this);

    }

   @Override
    public boolean onSupportNavigateUp() {
        if (!mPetHasChanged) {
            finish();
            return false;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                (dialogInterface, i) -> finish();
        showUnsavedChangesDialog(discardButtonClickListener);
        return false;
    }

    @Override
    public void onBackPressed() {
        Log.v("TAG","Back Button Pressed");
        if (!mPetHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                (dialogInterface, i) -> finish();
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                NameEditText = findViewById(R.id.Overview1_edit_text_view);
                BreedEditText = findViewById(R.id.Overview2_edit_text_view);

                NameString = NameEditText.getText().toString();
                BreedString = BreedEditText.getText().toString();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                gender = 1;
                                break;
                            case 1:
                                gender = 2;
                                break;
                            case 2:
                                gender = 0;
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                String WeightString = MeasurementEditText.getText().toString();
                MeasurementEditText = findViewById(R.id.Measurement_edit_text_view);
                try {
                    Weight = Integer.parseInt(WeightString);
                } catch (Exception ignored) {

                }
                if(NameString.isEmpty()||BreedString.isEmpty()||WeightString.isEmpty())
                {
                    DialogInterface.OnClickListener discardButtonClickListener =
                            (dialogInterface, i) -> finish();
                    showNoDataDialog(discardButtonClickListener);
                }
                else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PetContract.PetEntry.COLUMN_NAME, NameString);
                    contentValues.put(PetContract.PetEntry.COLUMN_BREED, BreedString);
                    contentValues.put(PetContract.PetEntry.COLUMN_GENDER, gender);
                    contentValues.put(PetContract.PetEntry.COLUMN_WEIGHT, Weight);

                    String selectionClause = PetContract.PetEntry._ID +  " = ?";
                    String[] selectionArgs = new String[]{String.valueOf(pet_id)};
                    getContentResolver().update(PetContract.PetEntry.CONTENT_URI,contentValues,selectionClause,selectionArgs);
                    finish();
                }
                return true;
            case R.id.delete_data:
                MeasurementEditText.setText("");
                NameEditText.setText("");
                BreedEditText.setText("");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Intent intent = getIntent();
        pet_id = intent.getLongExtra("dataURI",0);

        String[] projectionFields = new String[] {
                PetContract.PetEntry._ID,//***This column is the primary_key and this show be included in the projection String***//
                PetContract.PetEntry.COLUMN_NAME,
                PetContract.PetEntry.COLUMN_BREED,
                PetContract.PetEntry.COLUMN_WEIGHT,
                PetContract.PetEntry.COLUMN_GENDER
        };

        Uri pet_uri = ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI,pet_id);
        return new CursorLoader(this,
                pet_uri, projectionFields, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        NameEditText = findViewById(R.id.Overview1_edit_text_view);
        BreedEditText = findViewById(R.id.Overview2_edit_text_view);
        MeasurementEditText = findViewById(R.id.Measurement_edit_text_view);
        spinner = findViewById(R.id.spinner_menu_category);
        data.moveToNext();
        NameString = data.getString(data.getColumnIndex(PetContract.PetEntry.COLUMN_NAME));
        BreedString = data.getString(data.getColumnIndex(PetContract.PetEntry.COLUMN_BREED));
        gender = data.getInt(data.getColumnIndex(PetContract.PetEntry.COLUMN_GENDER));
        Weight = data.getInt(data.getColumnIndex(PetContract.PetEntry.COLUMN_WEIGHT));

        MeasurementEditText.setText(String.valueOf(Weight));
        NameEditText.setText(NameString);
        BreedEditText.setText(BreedString);
        int[] set_gender = new int[3];
        set_gender[2]=1;
        set_gender[0]=2;
        spinner.setSelection(set_gender[gender]);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        MeasurementEditText.setText("");
        NameEditText.setText("");
        BreedEditText.setText("");
    }
}