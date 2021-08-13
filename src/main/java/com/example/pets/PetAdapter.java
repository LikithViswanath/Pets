package com.example.pets;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.pets.data.PetContract;

import java.util.zip.Inflater;

public class PetAdapter extends CursorAdapter {

    public PetAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.pets_data_list,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if(cursor!=null)
        {
            TextView breedTextView = (TextView)  view.findViewById(R.id.breed_text_view);
            TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);
            String breedString = cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_BREED));
            String nameString =  cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_NAME));
            breedTextView.setText(breedString);
            nameTextView.setText(nameString);
        }
    }
}
