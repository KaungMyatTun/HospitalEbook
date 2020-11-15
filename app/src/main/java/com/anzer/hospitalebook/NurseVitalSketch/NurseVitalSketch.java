package com.anzer.hospitalebook.NurseVitalSketch;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.anzer.hospitalebook.NurseDashboard.NurseDashboard;
import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.api.ApiClient;
import com.anzer.hospitalebook.api.ApiInterface;
import com.anzer.hospitalebook.models.adapters.Item;
import com.anzer.hospitalebook.models.listeners.OnDrawChangedListener;
import com.anzer.hospitalebook.models.views.NurseVitalSketchView;
import com.anzer.hospitalebook.models.views.SketchView;
import com.anzer.hospitalebook.utils.AlphaManager;
import com.anzer.hospitalebook.utils.ConstantsBase;
import com.anzer.hospitalebook.utils.StorageHelper;
import com.anzer.hospitalebook.vo.HWImage;
import com.anzer.hospitalebook.vo.HWPtInfo;
import com.anzer.hospitalebook.vo.ImageCount;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NurseVitalSketch extends AppCompatActivity implements OnDrawChangedListener, View.OnClickListener {

    @BindView(R.id.drawing)
    NurseVitalSketchView mSketchView;
    @BindView(R.id.sketch_stroke)
    ImageView stroke;
    @BindView(R.id.sketch_eraser)
    ImageView eraser;
    @BindView(R.id.sketch_undo)
    ImageView undo;
    @BindView(R.id.sketch_redo)
    ImageView redo;
    @BindView(R.id.complete)
    ImageView complete;
    private int oldColor;
    private ColorPicker mColorPicker;
    private View popupLayout, popupEraserLayout;
    private int seekBarStrokeProgress, seekBarEraserProgress;
    private int size;
    private ImageView strokeImageView, eraserImageView;
    private Uri attachmentUri;
    private ArrayList<Uri> image_uri_array;
    private ArrayList<Item> image_array;
    ProgressDialog pd;
    int isSaved = 0;
    int isProcessed = 0;
    int urgent_flag_value;
    int isPatientForVital = 1; // 0 for patient of handwriting order, 1 for patient of vital order
    String currentDateandTime;
    Integer lastID;
    String imgDesc;
    String encodedImage;
    private byte[] byteArray;
    Integer insertedImgCount = 0;
    Boolean doneInsertPtInfo = false;
    Boolean doneInsertImg = false;

    public static String title;
    public static String patient_name;
    public static String patient_cpi;
    public static String patient_reg_number;
    public static String nurseCode, nurseName;
    public static String docCode, docName;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_vital_sketch);
        mSketchView = findViewById(R.id.drawing);
        mSketchView.setOnDrawChangedListener(this);

        stroke = findViewById(R.id.sketch_stroke);
        eraser = findViewById(R.id.sketch_eraser);
        undo = findViewById(R.id.sketch_undo);
        redo = findViewById(R.id.sketch_redo);
        complete = findViewById(R.id.complete);

        image_array = new ArrayList<>();
        image_uri_array = new ArrayList<>();

        title = "Vital Sign";
        patient_name = getIntent().getStringExtra("PATIENT_NAME");
        patient_cpi = getIntent().getStringExtra("PATIENT_CPI");
        patient_reg_number = getIntent().getStringExtra("PATIENT_VISIT_NUMBER");
        nurseCode = getIntent().getStringExtra("NURSE_CODE");
        nurseName = getIntent().getStringExtra("NURSE_NAME");
        docCode = getIntent().getStringExtra("ATTENDING_DOCTOR_CODE");
        docName = getIntent().getStringExtra("ATTENDING_DOCTOR_NAME");


        AlphaManager.setAlpha(eraser, 0.4f);
        complete.setOnClickListener(this);
        stroke.setOnClickListener(this);
        eraser.setOnClickListener(this);
        redo.setOnClickListener(this);
        undo.setOnClickListener(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(AppCompatActivity
                .LAYOUT_INFLATER_SERVICE);
        popupLayout = inflater.inflate(R.layout.popup_sketch_stroke, null);

        // And the one for eraser
        LayoutInflater inflaterEraser = (LayoutInflater) this.getSystemService(AppCompatActivity
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

        ApiInterface apiInterface;

    }

    @Override
    public void onBackPressed() {
        if (mSketchView.emptyPath()) {
            Intent myIntent = new Intent(this, NurseDashboard.class);
            startActivityForResult(myIntent, 0);
        } else {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_two_buttons);

            TextView dialogBody = dialog.findViewById(R.id.dialog_body_txt);
            dialogBody.setText("Are you sure to delete your previous handwriting inputs?");

            Button ok_btn = dialog.findViewById(R.id.ok_btn);
            Button cancel_btn = dialog.findViewById(R.id.cancel_btn);

            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(NurseVitalSketch.this, NurseDashboard.class);
                    startActivityForResult(myIntent, 0);
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
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sketch_redo:
                mSketchView.redo();
                break;
            case R.id.sketch_undo:
                mSketchView.undo();
                break;
            case R.id.sketch_eraser:
                if (mSketchView.getMode() == SketchView.ERASER) {
                    showPopup(view, SketchView.ERASER);
                } else {
                    mSketchView.setMode(SketchView.ERASER);
                    AlphaManager.setAlpha(stroke, 0.4f);
                    AlphaManager.setAlpha(eraser, 1f);
                }
                break;
            case R.id.sketch_stroke:
                if (mSketchView.getMode() == SketchView.STROKE) {
                    showPopup(view, SketchView.STROKE);
                } else {
                    mSketchView.setMode(SketchView.STROKE);
                    AlphaManager.setAlpha(eraser, 0.4f);
                    AlphaManager.setAlpha(stroke, 1f);
                }
                break;
            case R.id.complete: {
                saveImgToServer saveImgToServer = new saveImgToServer();
                saveImgToServer.execute();
            }
            break;
        }
    }

    private void showPopup(View anchor, final int eraserOrStroke) {
        boolean isErasing = eraserOrStroke == SketchView.ERASER;
        oldColor = mColorPicker.getColor();
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Creating the PopupWindow
        PopupWindow popup = new PopupWindow(this);
        popup.setContentView(isErasing ? popupEraserLayout : popupLayout);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(true);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mColorPicker.getColor() != oldColor)
                    mColorPicker.setOldCenterColor(oldColor);
            }
        });

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAtLocation(anchor, Gravity.BOTTOM, 0, 60);

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

    // save the vital template as image
    private void convertBitmapAsImage() {
        Bitmap bitmap = mSketchView.getBitmap();
        image_uri_array.clear();
        image_array.clear();
        if (bitmap != null) {
            try {
                File f = StorageHelper.createNewAttachmentFile(this, ConstantsBase.MIME_TYPE_SKETCH_EXT);
                Log.e("uri", f.toString());
                attachmentUri = Uri.fromFile(f);
                File bitmapFile = new File(attachmentUri.getPath());
                FileOutputStream out = new FileOutputStream(bitmapFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

                image_uri_array.add(attachmentUri);
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
                InputStream input = this.getContentResolver().openInputStream(attachmentUri);

                Bitmap bmp = BitmapFactory.decodeStream(input, null, bitmapOptions);
                image_array.add(new Item(bmp, "Vital"));
                out.close();
            } catch (Exception e) {
                Log.e("convertBitmapAsImage", e.getMessage());
            }
        }
    }

    // save image to server
    public class saveImgToServer extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(NurseVitalSketch.this);
            pd.setMessage("Uploading images ...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            convertBitmapAsImage();
            Log.e("Image array size ", String.valueOf(image_array.size()));
            UploadImg();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            pd.dismiss();
        }
    }

    // fun for upload image to server
    public void UploadImg(){
        urgent_flag_value = 1;
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
        HWPtInfo hwPtInfo = new HWPtInfo(docCode, patient_cpi, patient_reg_number, "AMM0000", formattedDateSaveDateTime, isSaved, urgent_flag_value, isProcessed, isPatientForVital );
        Call<Integer> call = apiInterface.insertPatientInfo(hwPtInfo);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("response code ", response.body().toString());
                if(response.isSuccessful()){
                    getLastRowOfPtInfo();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("error", call.toString());
                doneInsertPtInfo = false;
            }
        });
    }

    void getLastRowOfPtInfo(){
        Call<Integer> callForGetLastRow = apiInterface.getLastRowOfHWPtInfo();
        callForGetLastRow.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> callForGetLastRow, Response<Integer> response) {
                lastID = response.body();
                if(response.isSuccessful()){
                    insertVitalImage();
                }
                Log.e("last row", lastID.toString());
            }

            @Override
            public void onFailure(Call<Integer> callForGetLastRow, Throwable t) {

            }
        });
    }


    void insertVitalImage() {
        Log.e("msg", "do this function");

        for (int img_index = 0; img_index < image_array.size(); img_index++) {
            if (image_array.get(img_index).getCaption().equals("Vital")) {
                imgDesc = "Vital";
            }

            Bitmap image = Bitmap.createBitmap(image_array.get(img_index).getImage());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);

//            image.recycle();

            byteArray = byteArrayOutputStream.toByteArray();
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            HWImage hwImage = new HWImage(lastID, encodedImage, imgDesc);

            Call<String> callForInsertImg = apiInterface.insertHWImage(hwImage);
            callForInsertImg.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> callForInsertImg, Response<String> response) {
                    Log.e("insert image", response.body().toString());

                    if(response.isSuccessful()){
                        getInsertedImage();
                    }
                }

                @Override
                public void onFailure(Call<String> callForInsertImg, Throwable t) {
                    Log.e("insert image error", callForInsertImg.toString());
                    doneInsertImg = false;
                }
            });
        }
    }

    void getInsertedImage(){
        Call<ArrayList<ImageCount>> callForImgCount = apiInterface.getImgCount(lastID);
        callForImgCount.enqueue(new Callback<ArrayList<ImageCount>>() {
            @Override
            public void onResponse(Call<ArrayList<ImageCount>> callForImgCount, Response<ArrayList<ImageCount>> response) {
                insertedImgCount = Integer.parseInt(response.body().get(0).getImgCount());
                Log.e("img count", insertedImgCount.toString());
                if (response.isSuccessful() && insertedImgCount == image_array.size()) {
                    pd.dismiss();
                    image_array.clear();
                    // delete file from image_uri_array
                    for (int i = 0; i < image_uri_array.size(); i++) {
                        File deleteFile = new File(image_uri_array.get(i).getPath().toString());
                        deleteFile.delete();
                    }

                    image_uri_array.clear();
                    Intent myIntent = new Intent(getApplicationContext(), NurseDashboard.class);
                    startActivity(myIntent);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ImageCount>> callForImgCount, Throwable t) {
                Log.e("inserted image count", callForImgCount.toString());
            }
        });
    }
}