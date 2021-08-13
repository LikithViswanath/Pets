package com.example.pets;


import android.app.AlertDialog;
import android.app.LoaderManager;
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
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;

import com.example.pets.data.PetContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    PetAdapter adapter;
    long pet_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new PetAdapter(this,null);
        ListView listView = (ListView) findViewById(R.id.data_list_view);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(0,null,this);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,AddPetActivity.class);
            startActivity(intent);
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,EditPetActivity.class);
                intent.putExtra("dataURI",id);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               pet_id=id;
                showDeleteConfirmationDialog();
                return false;
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, (dialog, id) -> deletePet());
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePet() {
        String selectionClause = PetContract.PetEntry._ID +  " = ?";
        String[] selectionArgs = new String[]{String.valueOf(pet_id)};
        getContentResolver().delete(PetContract.PetEntry.CONTENT_URI,selectionClause,selectionArgs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        MenuCompat. setGroupDividerEnabled(menu, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add_dummy_data:
                    insertDummyPet();
                return true;
            case R.id.delete_all:
                    deleteAllPets();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void insertDummyPet()
    {
          ContentValues contentValues = new ContentValues();
          contentValues.put(PetContract.PetEntry.COLUMN_NAME,"Tommy");
          contentValues.put(PetContract.PetEntry.COLUMN_BREED,"pomeranian");
          contentValues.put(PetContract.PetEntry.COLUMN_GENDER,2);
          contentValues.put(PetContract.PetEntry.COLUMN_WEIGHT,12);
        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI,contentValues);

        contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.COLUMN_NAME,"Garfield");
        contentValues.put(PetContract.PetEntry.COLUMN_BREED,"Tabby");
        contentValues.put(PetContract.PetEntry.COLUMN_GENDER,2);
        contentValues.put(PetContract.PetEntry.COLUMN_WEIGHT,12);
        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI,contentValues);

        contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.COLUMN_NAME,"Binx");
        contentValues.put(PetContract.PetEntry.COLUMN_BREED,"Bombay");
        contentValues.put(PetContract.PetEntry.COLUMN_GENDER,2);
        contentValues.put(PetContract.PetEntry.COLUMN_WEIGHT,12);
        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI,contentValues);

        contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.COLUMN_NAME,"Cat");
        contentValues.put(PetContract.PetEntry.COLUMN_BREED,"Tabby");
        contentValues.put(PetContract.PetEntry.COLUMN_GENDER,2);
        contentValues.put(PetContract.PetEntry.COLUMN_WEIGHT,12);
        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI,contentValues);

        contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.COLUMN_NAME,"Baxter");
        contentValues.put(PetContract.PetEntry.COLUMN_BREED,"Border terrier");
        contentValues.put(PetContract.PetEntry.COLUMN_GENDER,2);
        contentValues.put(PetContract.PetEntry.COLUMN_WEIGHT,12);

          getContentResolver().insert(PetContract.PetEntry.CONTENT_URI,contentValues);
    }

    void deleteAllPets()
    {
        getContentResolver().delete(PetContract.PetEntry.CONTENT_URI,null,null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projectionFields = new String[] {
                PetContract.PetEntry._ID,// ***This column is the primary_key and this show be included in the projection String****
                PetContract.PetEntry.COLUMN_NAME,
                PetContract.PetEntry.COLUMN_BREED};
        CursorLoader cursorLoader = new CursorLoader(this,
                PetContract.PetEntry.CONTENT_URI, projectionFields, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);
        Log.v("MainActivity","data");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}