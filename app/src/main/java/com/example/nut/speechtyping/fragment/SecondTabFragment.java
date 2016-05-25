package com.example.nut.speechtyping.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.nut.speechtyping.R;
import com.example.nut.speechtyping.adapter.ListCursorAdapter;
import com.example.nut.speechtyping.adapter.MaterialSimpleListAdapter;
import com.example.nut.speechtyping.adapter.MaterialSimpleListItem;
import com.example.nut.speechtyping.sql.DatabaseManager;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

@SuppressWarnings("unused")
public class SecondTabFragment extends Fragment {

    private DatabaseManager dbManager;
    private ListView listView;
    private ListCursorAdapter listCursorAdapter;
    private Cursor cursor;
    private MaterialSimpleListAdapter adapter;


    public interface FragmentListener {
        void onItemListClicked(Bundle bundle);
    }

    public SecondTabFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static SecondTabFragment newInstance() {
        SecondTabFragment fragment = new SecondTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_second, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        dbManager = new DatabaseManager();
        dbManager.open();
        cursor = dbManager.fetchData();
    }


    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here

        listView = (ListView) rootView.findViewById(R.id.listView);

        listCursorAdapter = new ListCursorAdapter(getContext(), cursor, 0, SecondTabFragment.this);
        listCursorAdapter.notifyDataSetChanged();
        listView.invalidate();
        listView.setAdapter(listCursorAdapter);

        listView.setOnItemClickListener(listViewItemClickListener);
    }

    @Override
    public void onResume() {
        Log.d("OnRe", "onResume");
        super.onResume();
        dbManager.open();

    }

    @Override
    public void onPause() {
        Log.d("OnPu", "onPuase");
        super.onPause();
        dbManager.close();

    }

    @Override
    public void onStart() {
        super.onStart();
        dbManager.open();

    }

    @Override
    public void onStop() {
        super.onStop();
        dbManager.close();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

    final AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {

            TextView idTextView = (TextView) view.findViewById(R.id.tvId);
            TextView titleTextView = (TextView) view.findViewById(R.id.tvTitle);
            TextView msgTextView = (TextView) view.findViewById(R.id.tvMessage);

            String title = titleTextView.getText().toString();
            String msg = msgTextView.getText().toString();
            int id = 0;
            try {
                id = Integer.parseInt(idTextView.getText().toString());
            } catch (NumberFormatException e) {
                System.out.println("Could not parse " + e);
            }

            //showToast("Position " + position + "\nListView ID = " + String.valueOf(id));

            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("msg", msg);
            bundle.putInt("id", id);

            FragmentListener listener = (FragmentListener) getActivity();
            listener.onItemListClicked(bundle);
        }
    };

    private void showToast(String text) {
        Toast.makeText(Contextor.getInstance().getContext(),
                text,
                Toast.LENGTH_SHORT).show();

    }

    public void showMoreMenu(Integer id, String title, String msg) {

        adapter = new MaterialSimpleListAdapter(getContext());
        adapter.add(new MaterialSimpleListItem.Builder(getContext())
                .content("Rename")
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(getContext())
                .content("Delete")
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(getContext())
                .content("Share")
                .backgroundColor(Color.WHITE)
                .build());

        showDialogMoreSetting(id, title, msg);
    }

    private void showDialogMoreSetting(final Integer id, final String title, final String msg) {
        new MaterialDialog.Builder(getContext())
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        MaterialSimpleListItem item = adapter.getItem(which);

                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                        switch (which) {
                            case 0:
                                //rename
                                showDialogRename(id, title);
                                break;
                            case 1:
                                showDialogDelete(id);
                                break;
                            case 2:
                                shareMessage(title, msg);
                                break;

                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }

      private void showDialogRename(final int id, String title) {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext())
                .title("Rename")
                .inputRangeRes(2, 15, R.color.colorPrimary)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("untitle", title, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        dbManager.updateName(id, input.toString());

                        Cursor reCursor = dbManager.reFetchData();
                        listCursorAdapter.swapCursor(reCursor);

                        listView.setAdapter(listCursorAdapter);

                    }
                })
                .positiveColorRes(R.color.colorPrimary)
                .buttonRippleColorRes(R.color.colorPrimaryDark);
        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    private void showDialogDelete(int id) {
        dbManager.deleteData(id);

        Cursor reCursor = dbManager.reFetchData();
        listCursorAdapter.swapCursor(reCursor);

        listView.setAdapter(listCursorAdapter);
    }

    private void shareMessage(String title, String msg) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}
