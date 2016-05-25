package com.example.nut.speechtyping.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.nut.speechtyping.R;
import com.example.nut.speechtyping.adapter.MaterialSimpleListAdapter;
import com.example.nut.speechtyping.adapter.MaterialSimpleListItem;
import com.example.nut.speechtyping.fragment.FirstTabFragment;
import com.example.nut.speechtyping.fragment.MainFragment;
import com.example.nut.speechtyping.fragment.SecondTabFragment;

public class MainActivity extends AppCompatActivity implements SecondTabFragment.FragmentListener {

    Toolbar toolbar;
    private MaterialSimpleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initInstance();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, MainFragment.newInstance())
                    .commit();
        }
    }

    private void init() {

    }

    private void initInstance() {
        // Set ToolBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_help:
                showHelpDialog();
                return true;
            case R.id.action_language:
                showLanguageMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showHelpDialog() {
        String content = "Software version : 1.0\nThis software is part of my subject 'Wireless Device Programing' " +
                "for develop an application to record voice and transform into text for use later as memo of message or evidence later\n" +
                "if you found any problem or bug please report  me.\n\n" +
                "Nopanut Noojit";

        MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                .title("Information")
                .content(content)
                .positiveText("OK")
                .positiveColor(getResources().getColor(R.color.colorPrimarySoft))
                .show();

    }

    public void showLanguageMenu() {
        adapter = new MaterialSimpleListAdapter(MainActivity.this);
        adapter.add(new MaterialSimpleListItem.Builder(MainActivity.this)
                .content("Thai")
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(MainActivity.this)
                .content("English")
                .backgroundColor(Color.WHITE)
                .build());

        setupLanguage();
    }

    private void setupLanguage() {
        new MaterialDialog.Builder(MainActivity.this)
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        MaterialSimpleListItem item = adapter.getItem(which);

                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                        FirstTabFragment firstTabFragment = new FirstTabFragment();
                        switch (which) {
                            case 0:
                                firstTabFragment.setMyLanguage("th");
                                //Log.d("lang", "th");
                                break;
                            case 1:
                                firstTabFragment.setMyLanguage("en");
                                //Log.d("lang", "en");
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    public void onItemListClicked(Bundle bundle) {
        Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra("listData", bundle);
        startActivity(intent);
    }
}
