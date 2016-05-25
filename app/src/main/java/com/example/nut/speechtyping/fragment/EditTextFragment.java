package com.example.nut.speechtyping.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.nut.speechtyping.R;
import com.example.nut.speechtyping.sql.DatabaseManager;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class EditTextFragment extends Fragment implements View.OnClickListener {

    private Bundle bundle;
    private EditText edtMessage;
    private ImageButton btnAddPic, btnDel, btnShare, btnSave;
    private int listID;

    public EditText getEdtMessage() {
        return edtMessage;
    }

    public interface FragmentListener {
        void onAddPicClicked();
    }

    public EditTextFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static EditTextFragment newInstance(Bundle bundle) {
        EditTextFragment fragment = new EditTextFragment();
        Bundle args = new Bundle();
        args.putBundle("listData", bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        bundle = new Bundle();
        bundle = getArguments().getBundle("listData");

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_text, container, false);
        initInstances(rootView, savedInstanceState);

        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        String m = bundle.getString("msg");
        listID = bundle.getInt("id");

        edtMessage = (EditText) rootView.findViewById(R.id.edtMessage);
        edtMessage.setText(m);

        btnSave = (ImageButton) rootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        btnAddPic = (ImageButton) rootView.findViewById(R.id.btnAddPic);
        btnAddPic.setOnClickListener(this);

        btnDel = (ImageButton) rootView.findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);

        btnShare = (ImageButton) rootView.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            showDialogUpdate(listID, edtMessage.getText().toString());
        } else if (v == btnAddPic) {
            FragmentListener listener = (FragmentListener) getActivity();
            listener.onAddPicClicked();
        } else if (v == btnDel) {
            showDialogDelete(listID);
        } else if (v == btnShare) {
            shareMessage(bundle.getString("title"), edtMessage.getText().toString());
        }
    }

    private void showDialogUpdate(int id, String msg) {

        DatabaseManager dbManager = new DatabaseManager();
        dbManager.open();

        dbManager.updateData(id, msg);
        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();

    }

    private void showDialogDelete(int id) {
        DatabaseManager dbManager = new DatabaseManager();
        dbManager.open();
        dbManager.deleteData(id);
        dbManager.close();
        getActivity().finish();
    }

    private void shareMessage(String title, String msg) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

}
