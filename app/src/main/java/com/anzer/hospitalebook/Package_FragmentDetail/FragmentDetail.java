package com.anzer.hospitalebook.Package_FragmentDetail;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anzer.hospitalebook.Doctor_Dashboard;
import com.anzer.hospitalebook.LabAndDI.Lab_n_DI;
import com.anzer.hospitalebook.MainActivity;
import com.anzer.hospitalebook.Objects.Handwriting_title;
import com.anzer.hospitalebook.Package_Gallery_Activity.GalleryActivity;
import com.anzer.hospitalebook.Package_SketchFragment.SketchFragment;
import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.SQLiteOpenHelper.SQLiteHelper;
import com.anzer.hospitalebook.VoiceProgressNote.VoiceProgressNote;
import com.anzer.hospitalebook.api.ApiClient;
import com.anzer.hospitalebook.api.ApiInterface;
import com.anzer.hospitalebook.models.adapters.ImageGridAdapter;
import com.anzer.hospitalebook.models.adapters.Item;
import com.anzer.hospitalebook.models.views.ExpandableHeightGridView;
import com.anzer.hospitalebook.utils.ConstantsBase;
import com.anzer.hospitalebook.utils.FabSpeedDial;
import com.anzer.hospitalebook.utils.StorageHelper;
import com.anzer.hospitalebook.vo.HWImage;
import com.anzer.hospitalebook.vo.HWPtInfo;
import com.anzer.hospitalebook.vo.ImageCount;
import com.google.android.material.internal.NavigationMenu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;

public class FragmentDetail extends Fragment implements View.OnClickListener {
    @BindView(R.id.detail_root)
    ViewGroup root;
    @BindView(R.id.gridview)
    ExpandableHeightGridView mGridView;
    public static TextView patient_info;
    private ImageGridAdapter imageGridAdapter;
    private Button save_images;
    public static int sketch_background = -1;
    public static int clickedImage = 0;
    public static String patient_name, patient_cpi, patient_visit_no, patient_reg_date, doc_code;
    public static TextView pt_name, pt_cpi, pt_visitno, pt_regDate;
    public static boolean shouldAllowNewPage = true;
    private Boolean catch_fragment;
    public static SQLiteHelper sqLiteHelper;
    private ArrayList<Item> notSave_images;
    public static Uri attachemntUri;
    private static final int TAKE_PHOTO = 1;
    public final String FRAGMENT_DETAIL_TAG = "FragmentDetail";
    private RadioGroup selection_filter;
    private RadioButton check_radio_btn;
    private ArrayList<Item> temp_img_array;
    private byte[] byteArray;
    public static ArrayList<Integer> real_IndexFromMainArray;
    private static final int CAMERA_PERMISSION_REQUEST = 0xabc;
    private boolean mPermissionGranted;
    public String LatestID = null;

    int check_radio_flag;
    int countOfResultSetForVerification;
    Uri photo_Uri, before_photo_Uri;
    File f;
    ViewStub attachmentsBelow;
    String photo_type;
    String msg;
    int isSaved = 1;
    int isProcessed = 0;
    int isPatientVital = 0; // 0 for patient of handwriting order, 1 for patient of vital order
    String encodedImage;
    String imgDesc;
    String currentDateandTime;
    ProgressDialog pd;
    FabSpeedDial fabSpeedDial;
    Boolean Urgent_Flag;
    ApiInterface apiInterface;
    int laID;
    Integer insertedImgCount = 0;
    LinearLayout ordered_test_panel, voice_progress_note_panel;
    TextView ordered_test_count;
    StringBuffer test_to_show_on_ordered_test_panel;
    Button seeDetailBtn, seeVoiceProgressNoteBtn;


    @Override
    public void onResume() {
        super.onResume();
        Log.e("OnResume", "Fragment is resumed");
        // ordered test panel
        test_to_show_on_ordered_test_panel.delete(0, test_to_show_on_ordered_test_panel.length());
        if (MainActivity.DI_OrderedTest.size() > 0) {
            test_to_show_on_ordered_test_panel.append("DI [" + MainActivity.DI_OrderedTest.size() + " Tests]");
        }
        if (MainActivity.LAB_OrderedTest.size() > 0) {
            test_to_show_on_ordered_test_panel.append(", LAB [" + MainActivity.LAB_OrderedTest.size() + " Tests]");
        }
        if (test_to_show_on_ordered_test_panel.length() == 0) {
            ordered_test_panel.setVisibility(View.GONE);
        } else {
            ordered_test_panel.setVisibility(View.VISIBLE);
            ordered_test_count.setText(test_to_show_on_ordered_test_panel);
        }

        // voice progress note panel
        if(MainActivity.voiceProgressNote != ""){
            voice_progress_note_panel.setVisibility(View.VISIBLE);
        }else{
            voice_progress_note_panel.setVisibility(View.GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        notSave_images = new ArrayList<Item>();

        getIDs(view);
        setEvents();

        pt_name = view.findViewById(R.id.pt_name);
        pt_cpi = view.findViewById(R.id.pt_cpi);
        pt_visitno = view.findViewById(R.id.pt_visit);
        pt_regDate = view.findViewById(R.id.reg_date);


        catch_fragment = getActivity().getIntent().getBooleanExtra("From_Dashboard_To_FragDetail", false);
        if (catch_fragment) {
            patient_name = getActivity().getIntent().getStringExtra("PATIENT_NAME");
            patient_cpi = getActivity().getIntent().getStringExtra("PATIENT_CPI");
            patient_visit_no = getActivity().getIntent().getStringExtra("PATIENT_VISIT_NUMBER");
            patient_reg_date = getActivity().getIntent().getStringExtra("PATIENT_REG_DATE");

        } else {
            sqLiteHelper = new SQLiteHelper(getActivity(), "temp_db", null, 1);
            SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM PATIENT_INFO_TABLE WHERE AD_CODE = '" + Doctor_Dashboard.doctor_code + "'", null);

            while (cursor.moveToNext()) {
                patient_name = cursor.getString(2);
                patient_cpi = cursor.getString(3);
                patient_visit_no = cursor.getString(4);
                patient_reg_date = cursor.getString(5);
            }
            save_images.setVisibility(View.VISIBLE);
        }

        pt_name.setText(patient_name);
        pt_cpi.setText(patient_cpi);
        pt_visitno.setText(patient_visit_no);
        pt_regDate.setText(patient_reg_date);

        // ordered test panel
        ordered_test_panel = view.findViewById(R.id.panel_ordered_tests);
        ordered_test_count = view.findViewById(R.id.test_count);
        test_to_show_on_ordered_test_panel = new StringBuffer();
        seeDetailBtn = view.findViewById(R.id.see_test_detail);
        seeDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LabAndDIFragment();
            }
        });

        if (MainActivity.DI_OrderedTest.size() > 0) {
            test_to_show_on_ordered_test_panel.append("DI [" + MainActivity.DI_OrderedTest.size() + " Tests]");
        }
        if (MainActivity.LAB_OrderedTest.size() > 0) {
            test_to_show_on_ordered_test_panel.append(", LAB [" + MainActivity.LAB_OrderedTest.size() + " Tests]");
        }
        if (test_to_show_on_ordered_test_panel.length() == 0) {
            ordered_test_panel.setVisibility(View.GONE);
        } else {
            ordered_test_panel.setVisibility(View.VISIBLE);
            ordered_test_count.setText(test_to_show_on_ordered_test_panel);
        }

        // voice progress note panel
        voice_progress_note_panel = view.findViewById(R.id.panel_voiceProgressNote);
        seeVoiceProgressNoteBtn = view.findViewById(R.id.voice_progress_note_detail);
        seeVoiceProgressNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voidProgressNote();
            }
        });
        if(MainActivity.voiceProgressNote != ""){
            voice_progress_note_panel.setVisibility(View.VISIBLE);
        }else{
            voice_progress_note_panel.setVisibility(View.GONE);
        }


        return view;
    }

    private void getIDs(View view) {
        fabSpeedDial = view.findViewById(R.id.fabSpeedDial);
        save_images = view.findViewById(R.id.save_images);
        selection_filter = view.findViewById(R.id.selection_filter);

        root = view.findViewById(R.id.detail_root);
        attachmentsBelow = view.findViewById(R.id.detail_attachments_below);

        attachmentsBelow.inflate();
        mGridView = (ExpandableHeightGridView) root.findViewById(R.id.gridview);
        temp_img_array = new ArrayList<>();
        real_IndexFromMainArray = new ArrayList<>();
    }

    private void setEvents() {
//        multiple floating button
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {

            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true; // false  - don't show menu
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                String title = "";
                if (menuItem.getTitle().equals("Progress Note")) {
                    sketch_background = -1;
                    FragmentDetail.shouldAllowNewPage = true;
                    title = "Progress Note";
                    nextFragment();
                } else if (menuItem.getTitle().equals("Take Photo")) {
                    title = "Camera";
                    takePhoto();
                } else if (menuItem.getTitle().equals("Handwriting Order")) {
                    sketch_background = -1;
                    FragmentDetail.shouldAllowNewPage = true;
                    title = "Handwriting Order";
                    nextFragment();
                } else if (menuItem.getTitle().equals("LAB And DI")) {
                    LabAndDIFragment();
                } else if (menuItem.getTitle().equals("Voice Progress Note")) {
                    voidProgressNote();
                }
                Handwriting_title handwritingTitle = new Handwriting_title(title);
//                Toast.makeText(getContext(),handwritingTitle.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public void onMenuClosed() {
            }

        });

        if (MainActivity.image_array.size() != 0) {
            save_images.setVisibility(View.VISIBLE);
        }

        save_images.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        check_radio_flag = 0;
        MainActivity.image_array_clone.clear();
        MainActivity.image_array_clone_uri.clear();

        real_IndexFromMainArray.clear();
        MainActivity.image_array_clone.addAll(MainActivity.image_array);
        MainActivity.image_array_clone_uri.addAll(MainActivity.image_uri_array);              /// copy image array

        for (int i = 0; i < MainActivity.image_array_clone.size(); i++) {
            real_IndexFromMainArray.add(i);
        }
        if (MainActivity.image_array_clone.size() != 0)
            initViewAttachments();

        selection_filter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                check_radio_btn = getActivity().findViewById(checkedId);
                if (check_radio_btn.getText().equals("All")) {
                    check_radio_flag = 0;
                    Log.e("check >>>>> ", String.valueOf(checkedId));
                    real_IndexFromMainArray.clear();
                    MainActivity.image_array_clone.clear();
                    MainActivity.image_array_clone_uri.clear();

                    MainActivity.image_array_clone.addAll(MainActivity.image_array);
                    MainActivity.image_array_clone_uri.addAll(MainActivity.image_uri_array);

                    for (int i = 0; i < MainActivity.image_array_clone.size(); i++) {
                        real_IndexFromMainArray.add(i);
                    }

                    imageGridAdapter = new ImageGridAdapter(getActivity(), MainActivity.image_array_clone, mGridView);
                    mGridView.setAdapter(imageGridAdapter);
                    imageGridAdapter.notifyDataSetChanged();

                    mGridView.autoresize();
                    mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            List<String> items = Arrays.asList(getResources().getStringArray(R.array.attachments_actions));
                            if (MainActivity.image_array.get(position).getCaption() == "Photo") {
                                items = items.subList(0, items.size() - 1);
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Page " + (position + 1))
                                    .setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int item) {
                                            performAttachmentAction(position, item);
                                        }
                                    }).show();
                            return true;
                        }
                    });

                    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            clickedImage = position;

                            Intent attachmentIntent = new Intent(getActivity(), GalleryActivity.class);
                            startActivity(attachmentIntent);
                            shouldAllowNewPage = false;
                        }
                    });
                }
                // Handwriting Orders
                else if (check_radio_btn.getText().equals("Handwriting Orders")) {
                    check_radio_flag = 1;
                    MainActivity.image_array_clone.clear();
                    MainActivity.image_array_clone_uri.clear();
                    real_IndexFromMainArray.clear();
                    for (int img_index = 0; img_index < MainActivity.image_array.size(); img_index++) {
                        if (MainActivity.image_array.get(img_index).getCaption().equals("Handwriting Order")) {
                            MainActivity.image_array_clone.add(MainActivity.image_array.get(img_index));
                            MainActivity.image_array_clone_uri.add(MainActivity.image_uri_array.get(img_index));
                            real_IndexFromMainArray.add(img_index);
                        }
                    }

                    imageGridAdapter = new ImageGridAdapter(getActivity(), MainActivity.image_array_clone, mGridView);
                    mGridView.setAdapter(imageGridAdapter);
                    imageGridAdapter.notifyDataSetChanged();

                    mGridView.autoresize();
                    mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            List<String> items = Arrays.asList(getResources().getStringArray(R.array.attachments_actions));
                            if (MainActivity.image_array_clone.get(position).getCaption() == "Photo") {
                                items = items.subList(0, items.size() - 1);
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Page " + (position + 1))
                                    .setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int item) {
                                            performAttachmentAction(position, item);
                                        }
                                    }).show();
                            return true;
                        }
                    });


                    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            clickedImage = position;
                            Intent attachmentIntent = new Intent(getActivity(), GalleryActivity.class);
                            startActivity(attachmentIntent);
                            shouldAllowNewPage = false;
                        }
                    });
                }
                // Photos Radio
                else if (check_radio_btn.getText().equals("Photos")) {
                    check_radio_flag = 2;
                    MainActivity.image_array_clone.clear();
                    MainActivity.image_array_clone_uri.clear();

                    real_IndexFromMainArray.clear();
                    for (int img_index = 0; img_index < MainActivity.image_array.size(); img_index++) {
                        if (MainActivity.image_array.get(img_index).getCaption().equals("Photo")) {
                            MainActivity.image_array_clone.add(MainActivity.image_array.get(img_index));
                            MainActivity.image_array_clone_uri.add(MainActivity.image_uri_array.get(img_index));
                            real_IndexFromMainArray.add(img_index);
                        }
                    }

                    imageGridAdapter = new ImageGridAdapter(getActivity(), MainActivity.image_array_clone, mGridView);
                    mGridView.setAdapter(imageGridAdapter);
                    imageGridAdapter.notifyDataSetChanged();

                    mGridView.autoresize();
                    mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            List<String> items = Arrays.asList(getResources().getStringArray(R.array.attachments_actions));
                            if (MainActivity.image_array_clone.get(position).getCaption() == "Photo") {
                                items = items.subList(0, items.size() - 1);
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Page " + (position + 1))
                                    .setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int item) {
                                            performAttachmentAction(position, item);
                                        }
                                    }).show();
                            return true;
                        }
                    });


                    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            clickedImage = position;

                            Intent attachmentIntent = new Intent(getActivity(), GalleryActivity.class);
                            startActivity(attachmentIntent);
                            shouldAllowNewPage = false;

                        }
                    });
                }
                // Progress Note Radio
                else {
                    check_radio_flag = 3;
                    MainActivity.image_array_clone.clear();
                    MainActivity.image_array_clone_uri.clear();
                    real_IndexFromMainArray.clear();
                    for (int img_index = 0; img_index < MainActivity.image_array.size(); img_index++) {
                        if (MainActivity.image_array.get(img_index).getCaption().equals("Progress Note")) {
                            Log.e("image index", String.valueOf(img_index));
                            MainActivity.image_array_clone.add(MainActivity.image_array.get(img_index));
                            MainActivity.image_array_clone_uri.add(MainActivity.image_uri_array.get(img_index));
                            real_IndexFromMainArray.add(img_index);
                        }
                    }

                    imageGridAdapter = new ImageGridAdapter(getActivity(), MainActivity.image_array_clone, mGridView);
                    mGridView.setAdapter(imageGridAdapter);
                    imageGridAdapter.notifyDataSetChanged();

                    mGridView.autoresize();
                    mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            List<String> items = Arrays.asList(getResources().getStringArray(R.array.attachments_actions));
                            if (MainActivity.image_array_clone.get(position).getCaption() == "Photo") {
                                items = items.subList(0, items.size() - 1);
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Page " + (position + 1))
                                    .setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int item) {
                                            performAttachmentAction(position, item);
                                        }
                                    }).show();

                            return true;
                        }
                    });

                    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            clickedImage = position;
                            Intent attachmentIntent = new Intent(getActivity(), GalleryActivity.class);
                            startActivity(attachmentIntent);
                            shouldAllowNewPage = false;
                        }
                    });
                }
            }
        });
    }

    // to route to handwriting canvas
    public void nextFragment() {
        SketchFragment sketchFragment = new SketchFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentParent, sketchFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // to route to lab and di fragment
    public void LabAndDIFragment() {
        Intent myIntent = new Intent(getActivity(), Lab_n_DI.class);
        startActivity(myIntent);
    }

    // to route to voice progress note
    public void voidProgressNote() {
        Intent voiceProgressNote = new Intent(getActivity(), VoiceProgressNote.class);
        startActivity(voiceProgressNote);
    }

    private void initViewAttachments() {
        real_IndexFromMainArray.clear();
        MainActivity.image_array_clone.clear();
        MainActivity.image_array_clone.addAll(MainActivity.image_array);
        for (int i = 0; i < MainActivity.image_array_clone.size(); i++) {
            real_IndexFromMainArray.add(i);
        }

        imageGridAdapter = new ImageGridAdapter(getActivity(), MainActivity.image_array_clone, mGridView);
        mGridView.setAdapter(imageGridAdapter);
        imageGridAdapter.notifyDataSetChanged();

        mGridView.autoresize();
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                List<String> items = Arrays.asList(getResources().getStringArray(R.array.attachments_actions));
                if (MainActivity.image_array_clone.get(position).getCaption() == "Photo") {
                    items = items.subList(0, items.size() - 1);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Page " + (position + 1))
                        .setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                performAttachmentAction(position, item);
                            }
                        }).show();

                return true;
            }
        });


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickedImage = position;
                Intent attachmentIntent = new Intent(getActivity(), GalleryActivity.class);
                startActivity(attachmentIntent);
                shouldAllowNewPage = false;
            }
        });
    }

    private void performAttachmentAction(int attachmentPosition, int i) {
        switch (getResources().getStringArray(R.array.attachments_actions_values)[i]) {
            case "delete all pages":
                Dialog dialog = new Dialog(this.getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_with_two_buttons);

                TextView dialogBody = dialog.findViewById(R.id.dialog_body_txt);
                dialogBody.setText("Are you sure want to delete all images ?");

                Button ok_btn = dialog.findViewById(R.id.ok_btn);
                Button cancel_btn = dialog.findViewById(R.id.cancel_btn);

                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.image_array_clone.clear();
                        // ALL FILTER TYPE
                        if (check_radio_flag == 0) {
                            MainActivity.image_array.clear();
                            for (int i = 0; i < MainActivity.image_array_clone_uri.size(); i++) {
                                File deleteFile = new File(MainActivity.image_array_clone_uri.get(i).getPath().toString());
                                deleteFile.delete();
                            }
                        }
                        // Handwriting Order FILTER TYPE
                        else if (check_radio_flag == 1) {
                            temp_img_array.clear();
                            temp_img_array.addAll(MainActivity.image_array);
                            MainActivity.image_array.clear();
                            for (int i = 0; i < temp_img_array.size(); i++) {
                                if (!(temp_img_array.get(i).getCaption().equals("Handwriting Order"))) {
                                    MainActivity.image_array.add(temp_img_array.get(i));
                                }
                            }

                            MainActivity.image_uri_array.removeAll(MainActivity.image_array_clone_uri);
                            for (int i = 0; i < MainActivity.image_array_clone_uri.size(); i++) {
                                File deleteFile = new File(MainActivity.image_array_clone_uri.get(i).getPath().toString());
                                deleteFile.delete();
                            }
                        }
                        // Photo FILTER TYPE
                        else if (check_radio_flag == 2) {
                            temp_img_array.clear();
                            temp_img_array.addAll(MainActivity.image_array);
                            MainActivity.image_array.clear();
                            for (int i = 0; i < temp_img_array.size(); i++) {
                                if (!(temp_img_array.get(i).getCaption().equals("Photo"))) {
                                    MainActivity.image_array.add(temp_img_array.get(i));
                                }
                            }

                            MainActivity.image_uri_array.removeAll(MainActivity.image_array_clone_uri);

                            for (int i = 0; i < MainActivity.image_array_clone_uri.size(); i++) {
                                File deleteFile = new File(MainActivity.image_array_clone_uri.get(i).getPath().toString());
                                deleteFile.delete();
                            }
                        } else {
                            temp_img_array.clear();
                            temp_img_array.addAll(MainActivity.image_array);
                            MainActivity.image_array.clear();
                            for (int i = 0; i < temp_img_array.size(); i++) {
                                if (!(temp_img_array.get(i).getCaption().equals("Progress Note"))) {
                                    MainActivity.image_array.add(temp_img_array.get(i));
                                }
                            }

                            MainActivity.image_uri_array.removeAll(MainActivity.image_array_clone_uri);

                            for (int i = 0; i < MainActivity.image_array_clone_uri.size(); i++) {
                                File deleteFile = new File(MainActivity.image_array_clone_uri.get(i).getPath().toString());
                                deleteFile.delete();
                            }
                        }
                        MainActivity.image_array_clone_uri.clear();
                        mGridView.invalidateViews();
                        Runtime.getRuntime().gc();                            ///testing for OOM error

                        if (MainActivity.image_array.size() == 0)
                            save_images.setVisibility(View.INVISIBLE);
                        dialog.dismiss();
                    }
                });

                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;

            case "edit":
                takeSketch(real_IndexFromMainArray.get(attachmentPosition));        /// change code for edit feature
                shouldAllowNewPage = false;
                break;
        }

    }

    public void takeSketch(int attachment_number) {
        File f = StorageHelper.createNewAttachmentFile(getContext(), ConstantsBase.MIME_TYPE_SKETCH_EXT);
        Log.e("file", f.toString());

        attachemntUri = Uri.fromFile(f);
        Log.e("uri", attachemntUri.toString());
        sketch_background = attachment_number;
        SketchFragment sketchFragment = new SketchFragment();

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentParent, sketchFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_images:
                if (isConnected()) {
                    String alertMessage;
                    if (MainActivity.image_array.size() == 1) {
                        alertMessage = "Are you sure to save image ? ";
                    } else {
                        alertMessage = "Are you sure to save images ? ";
                    }
                    Dialog dialog = new Dialog(this.getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_with_three_buttons);

                    TextView dialogBody = dialog.findViewById(R.id.dialog_body_txt);
                    dialogBody.setText(alertMessage);
                    Button normal_save_btn = dialog.findViewById(R.id.normal_save_btn);
                    Button urgent_save_btn = dialog.findViewById(R.id.urgent_save_btn);
                    Button cancel_btn = dialog.findViewById(R.id.cancel_btn);

                    normal_save_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Urgent_Flag = false;
                            saveImgToServer saveImgToServer = new saveImgToServer();
                            saveImgToServer.execute();
                            dialog.dismiss();
                        }
                    });

                    urgent_save_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Urgent_Flag = true;
                            saveImgToServer saveImgToServer = new saveImgToServer();
                            saveImgToServer.execute();
                            dialog.dismiss();
                        }
                    });

                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } else {
                    if (Doctor_Dashboard.fromNotification == false) {
                        Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_with_two_buttons);

                        TextView dialogBody = dialog.findViewById(R.id.dialog_body_txt);
                        dialogBody.setText("Your Network is not available! Do you want to save images in device ? ");

                        Button ok_btn = dialog.findViewById(R.id.ok_btn);
                        Button cancel_btn = dialog.findViewById(R.id.cancel_btn);

                        ok_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //define db name and version
                                sqLiteHelper = new SQLiteHelper(getActivity(), "HW_APP", null, 1);
                                sqLiteHelper.copyDatabase(getActivity());

                                ///create table for doctor table
                                sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS DOCTOR_TABLE (Id INTEGER PRIMARY KEY AUTOINCREMENT, adCODE VARCHAR, adNAME VARCHAR)");

                                ///create table for patient info table
                                sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS PATIENT_INFO_TABLE (Id INTEGER PRIMARY KEY AUTOINCREMENT, AD_CODE VARCHAR, PATIENT_NAME VARCHAR, PATIENT_CPI VARCHAR, PATIENT_VISIT_NO VARCHAR, PATIENT_REG_DATE VARCHAR)");

                                ///create table for handwriting image table
                                sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS HANDWRITING_IMG_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, AD_CODE VARCHAR, PATIENT_NAME VARCHAR, PATIENT_CPI VARCHAR, PATIENT_VISIT_NO VARCHAR, PATIENT_REG_DATE VARCHAR, HANDWRITING_IMG VARCHAR, PHOTO_TYPE VARCHAR)");

                                try {
                                    ///insert data into doctor table
                                    sqLiteHelper.insertDoctorTable(Doctor_Dashboard.doctor_code, Doctor_Dashboard.doctor_name);

                                    ///insert data into patient info table
                                    sqLiteHelper.insertPatientInfo(Doctor_Dashboard.doctor_code, patient_name, patient_cpi, patient_visit_no, patient_reg_date);

                                    ///insert data into handwriting image table
                                    for (int imageURI = 0; imageURI < MainActivity.image_uri_array.size(); imageURI++) {
//                                        Log.e("something wrong !!!!!!!",  String.valueOf(MainActivity.image_uri_array.size()) );
                                        if (MainActivity.image_array.get(imageURI).getCaption().equals("Photo")) {
                                            photo_type = String.valueOf(2);
                                            sqLiteHelper.insertHandwritingImg(Doctor_Dashboard.doctor_code, patient_name, patient_cpi, patient_visit_no, patient_reg_date, MainActivity.image_uri_array.get(imageURI).toString(), photo_type);
                                        } else if (MainActivity.image_array.get(imageURI).getCaption().equals("Progress Note")) {
                                            photo_type = String.valueOf(1);
                                            sqLiteHelper.insertHandwritingImg(Doctor_Dashboard.doctor_code, patient_name, patient_cpi, patient_visit_no, patient_reg_date, MainActivity.image_uri_array.get(imageURI).toString(), photo_type);
                                        } else {
                                            photo_type = String.valueOf(0);
                                            sqLiteHelper.insertHandwritingImg(Doctor_Dashboard.doctor_code, patient_name, patient_cpi, patient_visit_no, patient_reg_date, MainActivity.image_uri_array.get(imageURI).toString(), photo_type);
                                        }
                                    }

                                    ///select and count from doctor table
                                    Cursor cursor = sqLiteHelper.getData("SELECT * FROM DOCTOR_TABLE");
                                    cursor.getCount();

                                    ///select and count from patient info
                                    Cursor cursor2 = sqLiteHelper.getData("SELECT * FROM PATIENT_INFO_TABLE");
                                    cursor2.getCount();

                                    ///select and count from handwriting images
                                    Cursor cursor1 = sqLiteHelper.getData("SELECT * FROM HANDWRITING_IMG_TABLE");
                                    cursor1.getCount();
//                                    Log.e("count of handwriting images table", String.valueOf(cursor1.getCount()) );

                                    if (cursor.getCount() != 0 && cursor1.getCount() != 0 && cursor2.getCount() != 0)
                                        Toast.makeText(getActivity(), "Added Successfully", Toast.LENGTH_LONG).show();
                                } catch (Exception ex) {
                                    Log.e("error here  3", ex.getMessage());
                                }

                                MainActivity.image_array.clear();
                                MainActivity.image_uri_array.clear();
                                Intent myIntent = new Intent(getContext(), MainActivity.class);
                                startActivityForResult(myIntent, 0);
                                System.exit(1);
                            }
                        });

                        cancel_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        Doctor_Dashboard.fromNotification = false;
                    } else {
                        Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_with_two_buttons);

                        TextView dialogBody = dialog.findViewById(R.id.dialog_body_txt);
                        dialogBody.setText("Your Network is not available !!! Images are already saved. Do you want to update ? ");

                        Button ok_btn = dialog.findViewById(R.id.ok_btn);
                        Button cancel_btn = dialog.findViewById(R.id.cancel_btn);

                        ok_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sqLiteHelper = new SQLiteHelper(getActivity(), "temp_db", null, 1);
                                SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
                                Cursor cursor = db.rawQuery("DELETE FROM HANDWRITING_IMG_TABLE WHERE AD_CODE = '" + Doctor_Dashboard.doctor_code + "' AND PATIENT_NAME = '" + patient_name + "' AND PATIENT_CPI = '" + patient_cpi + "' AND PATIENT_VISIT_NO = '" + patient_visit_no + "' AND PATIENT_REG_DATE = '" + patient_reg_date + "'", null);
                                for (int imageURI = 0; imageURI < MainActivity.image_uri_array.size(); imageURI++) {
                                    if (MainActivity.image_array.get(imageURI).getCaption().equals("Photo")) {
                                        photo_type = String.valueOf(2);
                                        sqLiteHelper.insertHandwritingImg(Doctor_Dashboard.doctor_code, patient_name, patient_cpi, patient_visit_no, patient_reg_date, MainActivity.image_uri_array.get(imageURI).toString(), photo_type);
                                    } else if (MainActivity.image_array.get(imageURI).getCaption().equals("Progress Note")) {
                                        photo_type = String.valueOf(1);
                                        sqLiteHelper.insertHandwritingImg(Doctor_Dashboard.doctor_code, patient_name, patient_cpi, patient_visit_no, patient_reg_date, MainActivity.image_uri_array.get(imageURI).toString(), photo_type);
                                    } else {
                                        photo_type = String.valueOf(0);
                                        sqLiteHelper.insertHandwritingImg(Doctor_Dashboard.doctor_code, patient_name, patient_cpi, patient_visit_no, patient_reg_date, MainActivity.image_uri_array.get(imageURI).toString(), photo_type);
                                    }
                                }
                                MainActivity.image_array.clear();
                                MainActivity.image_uri_array.clear();
                                Intent myIntent = new Intent(getContext(), MainActivity.class);
                                startActivityForResult(myIntent, 0);
                                System.exit(1);
                            }
                        });

                        cancel_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                        Doctor_Dashboard.fromNotification = false;
                    }
                }
        }
    }

    public boolean isConnected() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private void takePhoto() {
        // Checks for camera app available
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        int cameraPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        mPermissionGranted = cameraPermission == PackageManager.PERMISSION_GRANTED;
        if (!mPermissionGranted) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        }
        if (mPermissionGranted) {
            // Checks for created file validity
            f = StorageHelper.createNewAttachmentFile(getContext(), ConstantsBase.MIME_TYPE_IMAGE_EXT);
            if (f == null) {
                Toast.makeText(getContext(), "Something wrong !!! Please contact to developer team. ", Toast.LENGTH_SHORT).show();
                return;
            }
//          Launches intent
            before_photo_Uri = Uri.fromFile(f);
            photo_Uri = FileProvider.getUriForFile(getContext().getApplicationContext(), "com.anzer.hospitalebook.fileprovider", f);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photo_Uri);
            startActivityForResult(intent, TAKE_PHOTO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST && grantResults.length > 0) {
            mPermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == TAKE_PHOTO) {
                Bitmap bmp = null;
                if (data != null) {
                    Bundle extras = data.getExtras();
                    bmp = (Bitmap) extras.get("data");
                    File bitmapFile2 = new File(photo_Uri.getPath());
                    try {
                        FileOutputStream outt = new FileOutputStream(bitmapFile2);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 90, outt);
                        bmp.recycle();                       /// recycle the original bitmap
                        outt.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                MainActivity.image_uri_array.add(before_photo_Uri);
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

                try {
                    InputStream input = getActivity().getContentResolver().openInputStream(photo_Uri);
                    Bitmap bmp2 = BitmapFactory.decodeStream(input, null, bitmapOptions);
                    Bitmap scalebitmap = Bitmap.createScaledBitmap(bmp2, 1110, 800, true);
                    if (scalebitmap != bmp2) {
                        bmp2.recycle();
                    }
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(scalebitmap, 0, 0, scalebitmap.getWidth(), scalebitmap.getHeight(), matrix, true);
                    if (rotatedBitmap != scalebitmap) {
                        scalebitmap.recycle();
                    }
                    MainActivity.image_array.add(new Item(rotatedBitmap, "Photo"));
                } catch (FileNotFoundException ee) {
                    ee.printStackTrace();
                }
                FragmentDetail fdetail = new FragmentDetail();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentParent, fdetail);
                fragmentTransaction.addToBackStack(FRAGMENT_DETAIL_TAG);
                fragmentTransaction.commit();
            }
        }
    }

    public class saveImgToServer extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            pd = new ProgressDialog(getContext());
            pd.setMessage("Uploading images ...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            UploadImg();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    public void UploadImg() {
        int urgent_flag_value;
        if (Urgent_Flag) {
            urgent_flag_value = 1;
        } else {
            urgent_flag_value = 0;
        }

        // get current upload images date time
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:sss.mmm");
        currentDateandTime = df.format(Calendar.getInstance().getTime());

        // parse the String saveDateAndTime to a java.util.Date object
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy HH:mm:sss.mmm").parse(currentDateandTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // format the java.util.Date object to the desired format
        String formattedDateSaveDateTime = new SimpleDateFormat("dd/MMM/yyyy HH:mm:sss.mmm").format(date);

        // get data using retrofit
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        HWPtInfo hwPtInfo = new HWPtInfo(MainActivity.copy_doctor_code, patient_cpi, patient_visit_no, MainActivity.insti_code, formattedDateSaveDateTime, isSaved, urgent_flag_value, isProcessed, isPatientVital);
        Call<Integer> call = apiInterface.insertPatientInfo(hwPtInfo);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("response code ", response.body().toString());
                if (response.isSuccessful()) {
                    getLastRowOfPtInfo();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("error", call.toString());
            }
        });
    }

    void getLastRowOfPtInfo() {
        Call<Integer> callForGetLastRow = apiInterface.getLastRowOfHWPtInfo();
        callForGetLastRow.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> callForGetLastRow, Response<Integer> response) {
                laID = response.body();
                if (response.isSuccessful()) {
                    insertImage();
                    createProgressNote();
                }
                Log.e("last row", String.valueOf(laID));
            }

            @Override
            public void onFailure(Call<Integer> callForGetLastRow, Throwable t) {

            }
        });
    }

    void insertImage() {
        for (int img_index = 0; img_index < MainActivity.image_array.size(); img_index++) {
            if (MainActivity.image_array.get(img_index).getCaption().equals("Photo")) {
                imgDesc = "HWPhoto";
            } else if (MainActivity.image_array.get(img_index).getCaption().equals("Progress Note")) {
                imgDesc = "HWProgressNote";
            } else {
                imgDesc = "HWSketchImage";
            }

            Bitmap image = Bitmap.createBitmap(MainActivity.image_array.get(img_index).getImage());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);

            byteArray = byteArrayOutputStream.toByteArray();
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            HWImage hwImage = new HWImage(laID, encodedImage, imgDesc);

            Call<String> callForInsertImg = apiInterface.insertHWImage(hwImage);
            callForInsertImg.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> callForInsertImg, Response<String> response) {
                    Log.e("insert image", response.body().toString());

                    if (response.isSuccessful()) {
                        getInsertedImage();
                    }
                }

                @Override
                public void onFailure(Call<String> callForInsertImg, Throwable t) {
                    Log.e("insert image error", callForInsertImg.toString());
                }
            });
        }
    }

    void createProgressNote() {
        Call<String> crateProgressNote = apiInterface.CreateProgressNote(laID, MainActivity.copy_doctor_code, patient_visit_no, MainActivity.insti_code);
        crateProgressNote.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("crate progress note", response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("create progress error", call.toString());
            }
        });
    }

    void getInsertedImage() {
        Call<ArrayList<ImageCount>> callForImgCount = apiInterface.getImgCount(laID);
        callForImgCount.enqueue(new Callback<ArrayList<ImageCount>>() {
            @Override
            public void onResponse(Call<ArrayList<ImageCount>> callForImgCount, Response<ArrayList<ImageCount>> response) {
                insertedImgCount = Integer.parseInt(response.body().get(0).getImgCount());
                Log.e("img count", insertedImgCount.toString());
                if (response.isSuccessful() && insertedImgCount == MainActivity.image_array.size()) {
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_with_one_button);

                    TextView dialogBody = dialog.findViewById(R.id.dialog_body_txt);
                    dialogBody.setText(" You have successfully saved " + insertedImgCount + " images !");

                    Button ok_btn = dialog.findViewById(R.id.ok_btn);

                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sqLiteHelper = new SQLiteHelper(getContext(), "temp_db", null, 1);
                            SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
                            if (Doctor_Dashboard.circleView.getMessageNum() == 1) {
                                Cursor cursor = db.rawQuery("DELETE FROM DOCTOR_TABLE WHERE adCODE = '" + Doctor_Dashboard.doctor_code + "'", null);
                                Cursor cursor1 = db.rawQuery("DELETE FROM PATIENT_INFO_TABLE WHERE AD_CODE = '" + Doctor_Dashboard.doctor_code + "' AND PATIENT_NAME = '" + patient_name + "' AND PATIENT_CPI = '" + patient_cpi + "' AND PATIENT_VISIT_NO = '" + patient_visit_no + "' AND PATIENT_REG_DATE = '" + patient_reg_date + "'", null);
                                Cursor cursor2 = db.rawQuery("DELETE FROM HANDWRITING_IMG_TABLE WHERE AD_CODE = '" + Doctor_Dashboard.doctor_code + "' AND PATIENT_NAME = '" + patient_name + "' AND PATIENT_CPI = '" + patient_cpi + "' AND PATIENT_VISIT_NO = '" + patient_visit_no + "' AND PATIENT_REG_DATE = '" + patient_reg_date + "'", null);

                                int cursorCount = cursor.getCount();
                                int cursorOneCount = cursor1.getCount();
                                int cursorThreeCount = cursor2.getCount();
                            }
                            MainActivity.image_array.clear();

                            // delete file from image_uri_array
                            for (int i = 0; i < MainActivity.image_uri_array.size(); i++) {
                                File deleteFile = new File(MainActivity.image_uri_array.get(i).getPath().toString());
                                deleteFile.delete();
                            }

                            MainActivity.image_uri_array.clear();
                            Intent myIntent = new Intent(getContext(), Doctor_Dashboard.class);
                            startActivity(myIntent);
                            getActivity().finish();
                        }
                    });
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ImageCount>> callForImgCount, Throwable t) {
                Log.e("inserted image count", callForImgCount.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }

        Runtime.getRuntime().gc();                      /// testing for OOM error
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
