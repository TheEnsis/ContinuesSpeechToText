package com.example.nut.speechtyping.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.nut.speechtyping.R;
import com.example.nut.speechtyping.fragment.SecondTabFragment;
import com.example.nut.speechtyping.sql.DatabaseHelper;
import com.example.nut.speechtyping.sql.DatabaseManager;

/**
 * Created by Nut on 5/2/2016.
 */
public class ListCursorAdapter extends CursorAdapter {

    private Button btnMore;
    private Context context;
    private SecondTabFragment secondTabFragment;

    public ListCursorAdapter(Context context, Cursor c, int flags, SecondTabFragment fragment) {
        super(context, c, flags);
        this.context = context;
        this.secondTabFragment = fragment;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_text, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find fields to populate in inflated template
        final TextView tvID = (TextView) view.findViewById(R.id.tvId);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvMsg = (TextView) view.findViewById(R.id.tvMessage);
        // Extract properties from cursor
        final Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.MEMBER_ID));
        final String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MEMBER_TITLE));
        final String msg = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MEMBER_MESSAGE));
        // Populate fields with extracted properties
        tvTitle.setText(title);
        tvMsg.setText(msg);
        tvID.setText(id.toString());

        Button btn = (Button) view.findViewById(R.id.btn_moreSetting);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "More Setting of position " + tvID.getText(), Toast.LENGTH_SHORT).show();

                secondTabFragment.showMoreMenu(id, title, msg);
            }
        });
    }
}
