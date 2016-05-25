package com.example.nut.speechtyping.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.nut.speechtyping.R;
import com.example.nut.speechtyping.adapter.MaterialSimpleListAdapter;
import com.example.nut.speechtyping.adapter.MaterialSimpleListItem;
import com.example.nut.speechtyping.sql.DatabaseManager;

import java.io.FileNotFoundException;
import java.io.InputStream;

import uk.co.deanwild.flowtextview.FlowTextView;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class AddPhotoFragment extends Fragment implements View.OnClickListener {

    private Bundle bundle;
    private ImageView photoAdded;
    private ImageButton btnAddPic;
    private ImageButton btnDelPic;
    private ImageButton btnSetPic;
    private ImageButton btnShare;
    private int imageID;

    private MaterialSimpleListAdapter adapter;
    private RelativeLayout relativeLayout;
    private DatabaseManager dbManager;

    private Bitmap yourSelectedImage;

    private static int RESULT_LOAD_IMAGE = 111;

    public AddPhotoFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static AddPhotoFragment newInstance(Bundle bundle) {
        AddPhotoFragment fragment = new AddPhotoFragment();
        Bundle args = new Bundle();
        args.putBundle("listData", bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        View rootView = inflater.inflate(R.layout.fragment_add_photo, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        dbManager = new DatabaseManager();
        dbManager.open();
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        String m = bundle.getString("msg");
        imageID = bundle.getInt("id");

        FlowTextView flowTextView = (FlowTextView) rootView.findViewById(R.id.flowTextView);
        flowTextView.setText(m);
        flowTextView.invalidate();

        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.imageLayout);

        photoAdded = (ImageView) rootView.findViewById(R.id.photoAdded);
        if (dbManager.fetchImage(imageID) != null) {
            yourSelectedImage = dbManager.fetchImage(imageID);
            photoAdded.setImageBitmap(Bitmap.createScaledBitmap(yourSelectedImage,
                    getImageWidth(yourSelectedImage), getImageHeight(yourSelectedImage), false));
            photoAdded.setVisibility(View.VISIBLE);
            //Log.d("dddd", "set image");
        }

        btnAddPic = (ImageButton) rootView.findViewById(R.id.btnAddPicture);
        btnDelPic = (ImageButton) rootView.findViewById(R.id.btnDelPicture);
        btnSetPic = (ImageButton) rootView.findViewById(R.id.btnSetPicLoc);

        btnAddPic.setOnClickListener(this);
        btnDelPic.setOnClickListener(this);
        btnSetPic.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @TargetApi(17)
    @Override
    public void onClick(View v) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        switch (v.getId()) {
            case R.id.btnAddPicture:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                break;
            case R.id.btnDelPicture:
                photoAdded.setVisibility(View.GONE);
                photoAdded.setImageResource(R.drawable.ic_launcher);
                params.addRule(RelativeLayout.ALIGN_START);
                relativeLayout.setLayoutParams(params);
                break;
            case R.id.btnSetPicLoc:
                setPictureLocation();
                break;
            default:
                break;
        }
    }

    private void setPictureLocation() {

        adapter = new MaterialSimpleListAdapter(getContext());
        adapter.add(new MaterialSimpleListItem.Builder(getContext())
                .content("Top + Left")
                .icon(R.drawable.ic_smartphone_black_18dp_tl)
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(getContext())
                .content("Top")
                .icon(R.drawable.ic_smartphone_black_18dp_t)
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(getContext())
                .content("Top + Right")
                .icon(R.drawable.ic_smartphone_black_18dp_tr)
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(getContext())
                .content("Center + Left")
                .icon(R.drawable.ic_smartphone_black_18dp_cl)
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(getContext())
                .content("Center")
                .icon(R.drawable.ic_smartphone_black_18dp_c)
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(getContext())
                .content("Center + Right")
                .icon(R.drawable.ic_smartphone_black_18dp_cr)
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(getContext())
                .content("Bottom + Left")
                .icon(R.drawable.ic_smartphone_black_18dp_bl)
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(getContext())
                .content("Bottom")
                .icon(R.drawable.ic_smartphone_black_18dp_b)
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(getContext())
                .content("Bottom + Right")
                .icon(R.drawable.ic_smartphone_black_18dp_br)
                .backgroundColor(Color.WHITE)
                .build());

        showDialogSetLocation();
    }

    private void showDialogSetLocation() {
        new MaterialDialog.Builder(getContext())
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        MaterialSimpleListItem item = adapter.getItem(which);

                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                        switch (which) {
                            case 0:
                                // TOP + LEFT
                                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                relativeLayout.setLayoutParams(params);
                                break;
                            case 1:
                                // TOP
                                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                                relativeLayout.setLayoutParams(params);
                                break;
                            case 2:
                                // TOP + RIGHT
                                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                relativeLayout.setLayoutParams(params);
                                break;
                            case 3:
                                // CENTER + LEFT
                                params.addRule(RelativeLayout.CENTER_VERTICAL);
                                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                relativeLayout.setLayoutParams(params);
                                break;
                            case 4:
                                // CENTER
                                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                                relativeLayout.setLayoutParams(params);
                                break;
                            case 5:
                                // CENTER + RIGHT
                                params.addRule(RelativeLayout.CENTER_VERTICAL);
                                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                relativeLayout.setLayoutParams(params);
                                break;
                            case 6:
                                // BOTTOM + LEFT
                                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                relativeLayout.setLayoutParams(params);
                                break;
                            case 7:
                                // BOTTOM
                                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                                relativeLayout.setLayoutParams(params);
                                break;
                            case 8:
                                // BOTTOM + RIGHT
                                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                relativeLayout.setLayoutParams(params);
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && intent != null) {
            Uri selectedImage = intent.getData();
            InputStream imageStream = null;

            try {
                imageStream = getContext().getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            photoAdded.setImageBitmap(Bitmap.createScaledBitmap(yourSelectedImage,
                    getImageWidth(yourSelectedImage), getImageHeight(yourSelectedImage), false));
            photoAdded.setVisibility(View.VISIBLE);

            dbManager.updateImage(imageID, getResizedBitmap(yourSelectedImage, getImageWidth(yourSelectedImage), getImageHeight(yourSelectedImage)));
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    private int getImageWidth(Bitmap bitmap) {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        return width / 3;
    }

    private int getImageHeight(Bitmap bitmap) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        return height / 4;
    }

}
