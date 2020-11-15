package com.anzer.hospitalebook.Package_SketchFragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anzer.hospitalebook.MainActivity;
import com.anzer.hospitalebook.Objects.Handwriting_title;
import com.anzer.hospitalebook.Objects.Pt_Info_Data;
import com.anzer.hospitalebook.Objects.Visit_History_Data;
import com.anzer.hospitalebook.Package_FragmentDetail.FragmentDetail;
import com.anzer.hospitalebook.Package_Gallery_Activity.GalleryActivity;
import com.anzer.hospitalebook.Package_ViewOrder.ViewOrderItemActivity;
import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.api.ApiClient;
import com.anzer.hospitalebook.api.ApiInterface;
import com.anzer.hospitalebook.models.adapters.Item;
import com.anzer.hospitalebook.models.adapters.RecyclerAdapterForVisitHistory;
import com.anzer.hospitalebook.models.listeners.OnDrawChangedListener;
import com.anzer.hospitalebook.models.views.SketchView;
import com.anzer.hospitalebook.utils.AlphaManager;
import com.anzer.hospitalebook.utils.ConstantsBase;
import com.anzer.hospitalebook.utils.StorageHelper;
import com.anzer.hospitalebook.vo.PtInfoDetail;
import com.anzer.hospitalebook.vo.PtVisitHistory;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SketchFragment extends Fragment implements OnDrawChangedListener, View.OnClickListener {
    @BindView(R.id.sketch_stroke)
    ImageView stroke;
    @BindView(R.id.sketch_eraser)
    ImageView eraser;
    @BindView(R.id.drawing)
    SketchView mSketchView;
    @BindView(R.id.sketch_undo)
    ImageView undo;
    @BindView(R.id.sketch_redo)
    ImageView redo;
    @BindView(R.id.next_page)
    ImageView next_page;
    //    @BindView(R.id.preview_images)ImageView preview_image;
    @BindView(R.id.complete)
    ImageView complete;
    @BindView(R.id.patientInfo)
    ImageView patientInfo;
    @BindView(R.id.visitHistory)
    ImageView visitHistory;
    @BindView(R.id.viewOrderHistory)
    ImageView viewOrderHistory;

    private int oldColor;
    private ColorPicker mColorPicker;
    private View popupLayout, popupEraserLayout;
    private int seekBarStrokeProgress, seekBarEraserProgress;
    private int size;
    private ImageView strokeImageView, eraserImageView;
    public static String patient_name, patient_cpi, patient_visit_no, handwriting_title;
    public Uri sketchUri;
    public static boolean backfromGalleryEditPage = false;
    private Uri attachmentUri, attachmentUri2;
    public final String FRAGMENT_SKETCH_TAG = "sketch_fragment";
    public final String FRAGMENT_DETAIL_TAG = "FragmentDetail";
    SketchFragment sketchFragment;
    Dialog pt_Info;
    CircleProgressBar visitProgressBar, PtInfoProgressBar;
    public static ArrayList<Pt_Info_Data> patientInfoData;
    public static ArrayList<Visit_History_Data> visitHistoryData;
    public static RecyclerView recyclerViewForVisitHistoryItem;
    RecyclerView.LayoutManager layoutManager;
    public static RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sketch_fragment, container, false);

        stroke = view.findViewById(R.id.sketch_stroke);
        eraser = view.findViewById(R.id.sketch_eraser);
        undo = view.findViewById(R.id.sketch_undo);
        redo = view.findViewById(R.id.sketch_redo);
        mSketchView = view.findViewById(R.id.drawing);
        next_page = view.findViewById(R.id.next_page);
        complete = view.findViewById(R.id.complete);
        patientInfo = view.findViewById(R.id.patientInfo);
        visitHistory = view.findViewById(R.id.visitHistory);
        viewOrderHistory = view.findViewById(R.id.viewOrderHistory);

        if (FragmentDetail.shouldAllowNewPage == false) {
            AlphaManager.setAlpha(next_page, 0.4f);
            next_page.setEnabled(false);
        }

        ButterKnife.bind(this, view);
        patient_name = FragmentDetail.patient_name;
        patient_cpi = FragmentDetail.patient_cpi;
        patient_visit_no = FragmentDetail.patient_visit_no;
        handwriting_title = Handwriting_title.getTitle();

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSketchView.setOnDrawChangedListener(this);
        patientInfoData = new ArrayList<>();
        visitHistoryData = new ArrayList<>();

        AlphaManager.setAlpha(eraser, 0.4f);

        next_page.setOnClickListener(this);
        complete.setOnClickListener(this);
        stroke.setOnClickListener(this);
        eraser.setOnClickListener(this);
        redo.setOnClickListener(this);
        undo.setOnClickListener(this);
        patientInfo.setOnClickListener(this);
        visitHistory.setOnClickListener(this);
        viewOrderHistory.setOnClickListener(this);

        ///// ***************erase All function********************
//            erase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                askForErase();
//            }
//            private void askForErase() {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Confirmation")
//                        .setMessage("Are you sure want to delete all ? ")
//                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                mSketchView.erase();
//                            }
//                        })
//                        .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                        .setIcon(R.drawable.alert_icon)
//                        .show();
//            }
//        });
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(AppCompatActivity
                .LAYOUT_INFLATER_SERVICE);
        popupLayout = inflater.inflate(R.layout.popup_sketch_stroke, null);

        // And the one for eraser
        LayoutInflater inflaterEraser = (LayoutInflater) getActivity().getSystemService(AppCompatActivity
                .LAYOUT_INFLATER_SERVICE);
        popupEraserLayout = inflaterEraser.inflate(R.layout.popup_sketch_eraser, null);

        // Actual stroke shape size is retrieved
        strokeImageView = (ImageView) popupLayout.findViewById(R.id.stroke_circle);
        final Drawable circleDrawable = getResources().getDrawable(R.drawable.circle);
        size = circleDrawable.getIntrinsicWidth();

        // Actual eraser shape size is retrieved
        eraserImageView = (ImageView) popupEraserLayout.findViewById(R.id.stroke_circle);
        size = circleDrawable.getIntrinsicWidth();

        setSeekbarProgress(SketchView.DEFAULT_STROKE_SIZE, SketchView.STROKE);
        setSeekbarProgress(SketchView.DEFAULT_ERASER_SIZE, SketchView.ERASER);

        // Stroke color picker initialization and event managing
        mColorPicker = (ColorPicker) popupLayout.findViewById(R.id.stroke_color_picker);
        mColorPicker.addSVBar((SVBar) popupLayout.findViewById(R.id.svbar));
        mColorPicker.addOpacityBar((OpacityBar) popupLayout.findViewById(R.id.opacitybar));
        mColorPicker.setOnColorChangedListener(mSketchView::setStrokeColor);
        mColorPicker.setColor(mSketchView.getStrokeColor());
        mColorPicker.setOldCenterColor(mSketchView.getStrokeColor());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.patientInfo:
                patientInfoDialog();
                break;
            case R.id.visitHistory:
                visitHistoryDialog();
                break;
            case R.id.sketch_redo:
                mSketchView.redo();
                break;
            case R.id.sketch_undo:
                mSketchView.undo();
                break;
            case R.id.sketch_eraser:
                if (mSketchView.getMode() == SketchView.ERASER) {
                    showPopup(v, SketchView.ERASER);
                } else {
                    mSketchView.setMode(SketchView.ERASER);
                    AlphaManager.setAlpha(stroke, 0.4f);
                    AlphaManager.setAlpha(eraser, 1f);
                }
                break;
            case R.id.sketch_stroke:
                if (mSketchView.getMode() == SketchView.STROKE) {
                    showPopup(v, SketchView.STROKE);
                } else {
                    mSketchView.setMode(SketchView.STROKE);
                    AlphaManager.setAlpha(eraser, 0.4f);
                    AlphaManager.setAlpha(stroke, 1f);
                }
                break;
            case R.id.complete:
                // save();
                if (mSketchView.checkCanvasIsEmpty() == false) {
                    save();
//                    mSketchView.RecycleBitmap();
                    mSketchView.getBitmap().recycle();
                }
                if (backfromGalleryEditPage == false)
                    back_to_fragmentDetail();
                else {
                    back_to_GalleryActivity();
                    backfromGalleryEditPage = false;
                }
                Runtime.getRuntime().gc();                           // testing for OOM error
                break;

            case R.id.next_page:
                if (mSketchView.checkCanvasIsEmpty() == false) {
                    save();
//                    mSketchView.RecycleBitmap();
                    mSketchView.getBitmap().recycle();
                }
                sketchFragment = new SketchFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentParent, sketchFragment);
                fragmentTransaction.addToBackStack(FRAGMENT_SKETCH_TAG);
                fragmentTransaction.commit();
//                  Runtime.getRuntime().gc();                      // testing for OOM error
                break;

            case R.id.viewOrderHistory:
                Intent myIntent = new Intent(SketchFragment.this.getActivity(), ViewOrderItemActivity.class);
                startActivityForResult(myIntent, 0);
                break;
        }
    }

    public void save() {
        Bitmap bitmap = mSketchView.getBitmap();
        if (bitmap != null) {
            if (FragmentDetail.sketch_background == -1) {
                try {
//                  String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File f = StorageHelper.createNewAttachmentFile(getContext(), ConstantsBase.MIME_TYPE_SKETCH_EXT);
                    Log.e("uri", f.toString());
                    attachmentUri = Uri.fromFile(f);
                    File bitmapFile = new File(attachmentUri.getPath());

                    FileOutputStream out = new FileOutputStream(bitmapFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

                    MainActivity.image_uri_array.add(attachmentUri);
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;                    /// bitmap option (change)
                    InputStream input = getActivity().getContentResolver().openInputStream(attachmentUri);

                    Bitmap bmp = BitmapFactory.decodeStream(input, null, bitmapOptions);
                    MainActivity.image_array.add(new Item(bmp, handwriting_title));
                    out.close();

                } catch (Exception e) {
                    Log.e("Error", "Error writing sketch image data", e);
                }
            } else if (FragmentDetail.sketch_background != -1) {
                int edit_img_position = FragmentDetail.sketch_background;
                String caption = MainActivity.image_array.get(edit_img_position).getCaption();
                Log.e("Caption :", caption);
                MainActivity.image_array.remove(edit_img_position);               /// edit ***
                MainActivity.image_uri_array.remove(edit_img_position);           /// edit ***

                File f2 = StorageHelper.createNewAttachmentFile(getContext(), ConstantsBase.MIME_TYPE_SKETCH_EXT);
                Log.e("edit uri", f2.toString());
                attachmentUri2 = Uri.fromFile(f2);
                File bitmapFile2 = new File(attachmentUri2.getPath());
                try {
                    FileOutputStream outt = new FileOutputStream(bitmapFile2);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, outt);
//                    bitmap.recycle();                        ///change
                    outt.close();
                    MainActivity.image_uri_array.add(edit_img_position, attachmentUri2);             /// edit ***
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
                    InputStream input = getActivity().getContentResolver().openInputStream(attachmentUri2);
                    Bitmap bmp = BitmapFactory.decodeStream(input, null, bitmapOptions);
                    MainActivity.image_array.add(edit_img_position, new Item(bmp, caption));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showPopup(View anchor, final int eraserOrStroke) {
        boolean isErasing = eraserOrStroke == SketchView.ERASER;
        oldColor = mColorPicker.getColor();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Creating the PopupWindow
        PopupWindow popup = new PopupWindow(getActivity());
        popup.setContentView(isErasing ? popupEraserLayout : popupLayout);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(true);
        popup.setOnDismissListener(() -> {
            if (mColorPicker.getColor() != oldColor)
                mColorPicker.setOldCenterColor(oldColor);
        });

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAtLocation(anchor, Gravity.BOTTOM, 0, 60);

        // Displaying the popup at the specified location, + offsets (transformed
        // dp to pixel to support multiple screen sizes)
        popup.showAsDropDown(anchor);

        // Stroke size seekbar initialization and event managing
        SeekBar mSeekBar;
        mSeekBar = (SeekBar) (isErasing ? popupEraserLayout
                .findViewById(R.id.stroke_seekbar) : popupLayout
                .findViewById(R.id.stroke_seekbar));
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // When the seekbar is moved a new size is calculated and the new shape
                // is positioned centrally into the ImageView
                setSeekbarProgress(progress, eraserOrStroke);
            }
        });
        int progress = isErasing ? seekBarEraserProgress : seekBarStrokeProgress;
        mSeekBar.setProgress(progress);
    }

    protected void setSeekbarProgress(int progress, int eraserOrStroke) {
        int calcProgress = progress > 1 ? progress : 1;
        int newSize = Math.round((size / 100f) * calcProgress);
        int offset = (size - newSize) / 2;
        Log.v("Stroke size", "Stroke size " + newSize + " (" + calcProgress + "%)");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(newSize, newSize);
        lp.setMargins(offset, offset, offset, offset);
        if (eraserOrStroke == SketchView.STROKE) {
            strokeImageView.setLayoutParams(lp);
            seekBarStrokeProgress = progress;
        } else {
            eraserImageView.setLayoutParams(lp);
            seekBarEraserProgress = progress;
        }
        mSketchView.setSize(newSize, eraserOrStroke);
    }

    @Override
    public void onDrawChanged() {
        // Undo
        if (mSketchView.getPaths().size() > 0)
            AlphaManager.setAlpha(undo, 1f);
        else
            AlphaManager.setAlpha(undo, 0.4f);
        // Redo
        if (mSketchView.getUndoneCount() > 0)
            AlphaManager.setAlpha(redo, 1f);
        else
            AlphaManager.setAlpha(redo, 0.4f);
    }

    public void back_to_fragmentDetail() {
        FragmentDetail fdetail = new FragmentDetail();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentParent, fdetail);
        fragmentTransaction.addToBackStack(FRAGMENT_DETAIL_TAG);
        fragmentTransaction.commit();
    }

    public void back_to_GalleryActivity() {
        Intent back_to_gallery = new Intent(getActivity(), GalleryActivity.class);
        FragmentDetail.clickedImage = GalleryActivity.editFromFullImagesID;
        startActivity(back_to_gallery);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sketchFragment = null;
        Runtime.getRuntime().gc();
    }

    // patient info dialog class
    public void patientInfoDialog() {
        pt_Info = new Dialog(getActivity());
        pt_Info.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pt_Info.setContentView(R.layout.patient_info_dialog);
        pt_Info.setCancelable(false);
        pt_Info.setCanceledOnTouchOutside(false);

        PtInfoProgressBar = pt_Info.findViewById(R.id.progress_bar);
        PtInfoProgressBar.setColorSchemeResources(android.R.color.holo_blue_bright);
        PtInfoProgressBar.setClickable(false);

        pt_Info.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pt_Info.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        pt_Info.getWindow().setGravity(Gravity.CENTER);
        startLoadPtInfoData();

        ImageView close = pt_Info.findViewById(R.id.close_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pt_Info.dismiss();
            }
        });
        pt_Info.show();
    }

    //  Load patient detail information with progress bar
    public void startLoadPtInfoData() {
        PtInfoProgressBar.setVisibility(View.VISIBLE);
        SketchFragment.this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        // get data using retrofit
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<PtInfoDetail>> call = apiInterface.GetPtInfoDetail(patient_cpi, MainActivity.insti_code, patient_visit_no);
        call.enqueue(new Callback<ArrayList<PtInfoDetail>>() {
            @Override
            public void onResponse(Call<ArrayList<PtInfoDetail>> call, Response<ArrayList<PtInfoDetail>> response) {
                if (response.isSuccessful()) {
                    Pt_Info_Data getPtInfoData = new Pt_Info_Data(
                            response.body().get(0).getSurName(),
                            response.body().get(0).getGivenName(),
                            response.body().get(0).getSalutName(),
                            response.body().get(0).getSex(),
                            response.body().get(0).getMaritalStatus(),
                            response.body().get(0).getDob(),
                            response.body().get(0).getTob(),
                            response.body().get(0).getAge(),
                            response.body().get(0).getAlive(),
                            response.body().get(0).getDod(),
                            response.body().get(0).getLang(),
                            response.body().get(0).getAddress1(),
                            response.body().get(0).getTownship(),
                            response.body().get(0).getCity(),
                            response.body().get(0).getProvince(),
                            response.body().get(0).getPcode(),
                            response.body().get(0).getHomePhone(),
                            response.body().get(0).getCellPhone(),
                            response.body().get(0).getAttDoctor(),
                            response.body().get(0).getRefDoctor(),
                            response.body().get(0).getAllergies()
                    );
                    patientInfoData.add(getPtInfoData);
                    new LoadPtInfoDataTask().execute(0);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PtInfoDetail>> call, Throwable t) {
                Toast.makeText(getContext(), call.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //  load visit data on dashboard with asynctask
    class LoadPtInfoDataTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String s) {
            PtInfoProgressBar.setVisibility(View.INVISIBLE);
            Load_PtInfo_Data();
            SketchFragment.this.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    //  Load Patient detail information
    public void Load_PtInfo_Data() {
        TextView pt_surname = pt_Info.findViewById(R.id.surname);
        TextView pt_givenname = pt_Info.findViewById(R.id.givenname);
        TextView pt_salutation = pt_Info.findViewById(R.id.salutation);
        TextView pt_sex = pt_Info.findViewById(R.id.sex);
        TextView pt_marital_status = pt_Info.findViewById(R.id.marital_status);
        TextView pt_dob = pt_Info.findViewById(R.id.dob);
        TextView pt_tob = pt_Info.findViewById(R.id.tob);
        TextView pt_age = pt_Info.findViewById(R.id.age);
        TextView pt_alive_or_dead = pt_Info.findViewById(R.id.alive_or_dead);
        TextView pt_dod = pt_Info.findViewById(R.id.dod);
        TextView pt_language = pt_Info.findViewById(R.id.language);
        TextView pt_address = pt_Info.findViewById(R.id.address);
        TextView pt_township = pt_Info.findViewById(R.id.township);
        TextView pt_city = pt_Info.findViewById(R.id.city);
        TextView pt_province = pt_Info.findViewById(R.id.province);
        TextView pt_postal_code = pt_Info.findViewById(R.id.postal_code);
        TextView pt_home_ph = pt_Info.findViewById(R.id.home_ph);
        TextView pt_cell_ph = pt_Info.findViewById(R.id.cell_ph);
        TextView pt_family_doctor = pt_Info.findViewById(R.id.family_doctor);
        TextView pt_secondary_doctor = pt_Info.findViewById(R.id.secondary_doctor);
        TextView pt_allergy = pt_Info.findViewById(R.id.allergy);

        for (int i = 0; i < patientInfoData.size(); i++) {
            pt_surname.setText(patientInfoData.get(i).getSurName());
            pt_givenname.setText(patientInfoData.get(i).getGivenName());
            pt_salutation.setText(patientInfoData.get(i).getSalutation());
            pt_sex.setText(patientInfoData.get(i).getSex());
            pt_marital_status.setText(patientInfoData.get(i).getMaritalStatus());
            pt_dob.setText(patientInfoData.get(i).getDOB());
            pt_tob.setText(patientInfoData.get(i).getTOB());
            pt_age.setText(patientInfoData.get(i).getAge());
            pt_alive_or_dead.setText(patientInfoData.get(i).getAlive());
            pt_dod.setText(patientInfoData.get(i).getDOD());
            pt_language.setText(patientInfoData.get(i).getLanguage());
            pt_address.setText(patientInfoData.get(i).getAddress());
            pt_township.setText(patientInfoData.get(i).getTownship());
            pt_city.setText(patientInfoData.get(i).getCity());
            pt_province.setText(patientInfoData.get(i).getProvince());
            pt_postal_code.setText(patientInfoData.get(i).getPostalCode());
            pt_home_ph.setText(patientInfoData.get(i).getHomePh());
            pt_cell_ph.setText(patientInfoData.get(i).getCellPh());
            pt_family_doctor.setText(patientInfoData.get(i).getFamilyDoctor());
            pt_secondary_doctor.setText(patientInfoData.get(i).getRefDoctor());
            pt_allergy.setText(patientInfoData.get(i).getAllergy());
        }
    }

    // visit history dialog class
    public void visitHistoryDialog() {
        final Dialog visit_History = new Dialog(getActivity());
        visit_History.requestWindowFeature(Window.FEATURE_NO_TITLE);
        visit_History.setContentView(R.layout.visit_history_dialog);
        visit_History.setCanceledOnTouchOutside(false);
        visit_History.setCancelable(false);

        visitProgressBar = visit_History.findViewById(R.id.progress_bar);
        visitProgressBar.setColorSchemeResources(android.R.color.holo_blue_bright);
        visitProgressBar.setClickable(false);

        recyclerViewForVisitHistoryItem = visit_History.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewForVisitHistoryItem.setLayoutManager(layoutManager);
        recyclerViewForVisitHistoryItem.setHasFixedSize(true);

        visit_History.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        startLoadVisitData();

        visit_History.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        visit_History.getWindow().setGravity(Gravity.CENTER);

        ImageView close = visit_History.findViewById(R.id.close_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visit_History.dismiss();
            }
        });
        visit_History.show();
    }

    // load visit data on dashboard with progress bar     *****Visit History Data on Visit History Dialog*****
    public void startLoadVisitData() {
        visitProgressBar.setVisibility(View.VISIBLE);
        visitHistoryData.clear();
        SketchFragment.this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        // get data using retrofit
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<PtVisitHistory>> call = apiInterface.GetPtVisitHistory(patient_cpi, MainActivity.insti_code);
        call.enqueue(new Callback<ArrayList<PtVisitHistory>>() {
            @Override
            public void onResponse(Call<ArrayList<PtVisitHistory>> call, Response<ArrayList<PtVisitHistory>> response) {
                if (response.isSuccessful()) {
                    for (int index = 0; index < response.body().size(); index++) {
                        Visit_History_Data getVisitHistoryData = new Visit_History_Data(
                                String.valueOf(index + 1),
                                response.body().get(index).getRegNum(),
                                response.body().get(index).getRegDate(),
                                response.body().get(index).getAttDoc(),
                                response.body().get(index).getQueueNumber(),
                                response.body().get(index).getDischargeDate(),
                                response.body().get(index).getCancelReason()
                        );
                        visitHistoryData.add(getVisitHistoryData);
                        new LoadVisitHistoryDataTask().execute(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PtVisitHistory>> call, Throwable t) {
                Toast.makeText(getContext(), call.toString(), Toast.LENGTH_SHORT).show();
            }
        });

//        // download visit history data
//        try {
//            Connection con = DBConnection.getConnectionClass(MainActivity.DB_username, MainActivity.DB_password, MainActivity.DB_NAME, MainActivity.Server_ip);
//            CallableStatement stmt = con.prepareCall("{call dbo.usp_APP_OE_GetPatientVisit(?, ?)}");
//            stmt.setString(1, patient_cpi);
//            stmt.setString(2, MainActivity.insti_code);
//            ResultSet rs = stmt.executeQuery();
//            int count = 0;
//
//            while (rs.next()) {
//                Visit_History_Data getvisitHistoryData = new Visit_History_Data(String.valueOf(count + 1), rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
//                visitHistoryData.add(getvisitHistoryData);
//                count++;
//            }
//        } catch (SQLException se) {
//            Log.e("error here 1", se.getMessage());
//        } catch (Exception ex) {
//            Log.e("error here  3", ex.getMessage());
//        }
//        new LoadVisitHistoryDataTask().execute(0);
    }

    // load visit data on dashboard with asynctask    ******Visit History Data on Visit History Dialog*****
    class LoadVisitHistoryDataTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String s) {
            Load_VisitHistory_Data();
            visitProgressBar.setVisibility(View.INVISIBLE);
            SketchFragment.this.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    // load visit History data
    public void Load_VisitHistory_Data() {
        adapter = new RecyclerAdapterForVisitHistory(visitHistoryData, new RecyclerAdapterForVisitHistory.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        recyclerViewForVisitHistoryItem.setAdapter(adapter);
    }

    // view order history dialog class
    public void viewOrderHistoryDialog() {
        final Dialog order_History = new Dialog(getActivity());
        order_History.requestWindowFeature(Window.FEATURE_NO_TITLE);
        order_History.setContentView(R.layout.view_order_history_dialog);
        order_History.setCanceledOnTouchOutside(false);
        order_History.setCancelable(false);
        order_History.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        order_History.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        order_History.getWindow().setGravity(Gravity.CENTER);
        ImageView close = order_History.findViewById(R.id.close_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_History.dismiss();
            }
        });
    }
}