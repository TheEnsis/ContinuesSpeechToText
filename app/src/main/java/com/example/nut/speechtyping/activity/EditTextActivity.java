package com.example.nut.speechtyping.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nut.speechtyping.R;
import com.example.nut.speechtyping.fragment.AddPhotoFragment;
import com.example.nut.speechtyping.fragment.EditTextFragment;

public class EditTextActivity extends AppCompatActivity implements EditTextFragment.FragmentListener {

    Bundle bundle;
    EditTextFragment editTextFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        bundle = getIntent().getBundleExtra("listData");

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, EditTextFragment.newInstance(bundle))
                    .commit();
        }

        initInstance();

    }

    private void initInstance() {
        // Set ToolBar
        getSupportActionBar().setTitle(bundle.getString("title"));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_copy) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(bundle.getString("title"), bundle.getString("msg"));
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "copied to clipboard", Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddPicClicked() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.from_right, R.anim.to_left, R.anim.from_left, R.anim.to_right)
                .replace(R.id.contentContainer, AddPhotoFragment.newInstance(bundle))
                .addToBackStack(null)
                .commit();
    }
}
