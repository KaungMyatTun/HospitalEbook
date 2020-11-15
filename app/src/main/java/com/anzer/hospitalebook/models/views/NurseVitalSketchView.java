package com.anzer.hospitalebook.models.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.anzer.hospitalebook.NurseVitalSketch.NurseVitalSketch;
import com.anzer.hospitalebook.models.listeners.OnDrawChangedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NurseVitalSketchView extends View implements View.OnTouchListener {

    private static final float TOUCH_TOLERANCE = 4;
    public static final int STROKE = 0;
    public static final int ERASER = 1;
    public static final int DEFAULT_STROKE_SIZE = 8;
    public static final int DEFAULT_ERASER_SIZE = 100;
    private float strokeSize = DEFAULT_STROKE_SIZE;
    private int strokeColor = Color.BLACK;
    private float eraserSize = DEFAULT_ERASER_SIZE;
    private int backgroundColor = Color.parseColor("#fafafa");
    //  private int backgroundColor = Color.parseColor("#000000");
    private Path m_Path;
    private Paint m_Paint, rec_paint;
    private float mX, mY;
    private int width, height;
    private ArrayList<Pair<Path, Paint>> paths = new ArrayList<>();
    private ArrayList<Pair<Path, Paint>> undonePaths = new ArrayList<>();
    private Context mContext;
    private Bitmap bitmap;
    private int mode = STROKE;
    private OnDrawChangedListener onDrawChangedListener;
    public int i = 2;
    private SparseArray<PointF> mActivePointers;

    public NurseVitalSketchView(Context context, AttributeSet attr) {
        super(context, attr);
        this.mContext = context;

        setFocusable(true);
        setFocusableInTouchMode(true);
        setBackgroundColor(backgroundColor);

        this.setOnTouchListener(this);

        m_Paint = new Paint();
        m_Paint.setAntiAlias(true);
        m_Paint.setDither(true);
        m_Paint.setColor(strokeColor);
        m_Paint.setStyle(Paint.Style.STROKE);
        m_Paint.setStrokeJoin(Paint.Join.ROUND);
        m_Paint.setStrokeCap(Paint.Cap.ROUND);
        m_Paint.setStrokeWidth(strokeSize);

        m_Path = new Path();

        invalidate();

        mActivePointers = new SparseArray<PointF>();
    }

    public void setMode(int mode) {
        if (mode == STROKE || mode == ERASER)
            this.mode = mode;
    }

    public int getMode() {
        return this.mode;
    }


    /**
     * Change canvass background and force redraw
     *
     * @param bitmap saved sketch
     */
    public void setBackgroundBitmap(Activity mActivity, Bitmap bitmap) {
        if (!bitmap.isMutable()) {
            Bitmap.Config bitmapConfig = bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }
            bitmap = bitmap.copy(bitmapConfig, true);
        }
        this.bitmap = bitmap;
//      this.bitmap = getScaledBitmap(mActivity, bitmap);
    }


//    private Bitmap getScaledBitmap(Activity mActivity, Bitmap bitmap) {
//        DisplayMetrics display = new DisplayMetrics();
//        mActivity.getWindowManager().getDefaultDisplay().getMetrics(display);
//        int screenWidth = display.widthPixels;
//        int screenHeight = display.heightPixels;
//        float scale = bitmap.getWidth() / screenWidth > bitmap.getHeight() / screenHeight ? bitmap.getWidth() /
//                screenWidth : bitmap.getHeight() / screenHeight;
//        int scaledWidth = (int) (bitmap.getWidth() / scale);
//        int scaledHeight = (int) (bitmap.getHeight() / scale);
//        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }


    public boolean onTouch(View arg0, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
            default:
                Log.e("kdk", "Wrong element choosen: " + event.getAction());
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 140, m_Paint);
        }
        Paint rec_paint = new Paint();
        rec_paint.setColor(Color.BLACK);
        rec_paint.setStrokeWidth(3);
        canvas.drawRect(0, 3, canvas.getWidth(), 140, rec_paint);
        rec_paint.setStrokeWidth(0);
        rec_paint.setColor(Color.parseColor("#fafafa"));
        canvas.drawRect(3, 6, canvas.getWidth() - 3, 137, rec_paint);

        Paint paint = new Paint();
        Typeface currentTypeFace = paint.getTypeface();
        Typeface bold = Typeface.create(currentTypeFace, Typeface.BOLD);
        paint.setTypeface(bold);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(NurseVitalSketch.title, canvas.getWidth() / 2, canvas.getHeight() / 35, paint);

        Paint patient_name = new Paint();
        Typeface patient = paint.getTypeface();
        Typeface bold2 = Typeface.create(patient, Typeface.BOLD);
        patient_name.setTypeface(bold2);
        patient_name.setTextSize(25);
        canvas.drawText("Patient Name : ", 10, 90, patient_name);
        canvas.drawText(NurseVitalSketch.patient_name, 200, 90, patient_name);

        Paint patient_cpi = new Paint();
        patient_cpi.setTypeface(bold2);
        patient_cpi.setTextSize(25);
        canvas.drawText("CPI : ", canvas.getWidth() - 220, 90, patient_cpi);
        canvas.drawText(NurseVitalSketch.patient_cpi, canvas.getWidth() - 150, 90, patient_cpi);

        Paint doctor_name = new Paint();
        doctor_name.setTypeface(bold2);
        doctor_name.setTextSize(25);
        canvas.drawText("Doctor Name : ", 13, 120, doctor_name);
        canvas.drawText(NurseVitalSketch.docName, 200, 120, doctor_name);

        Paint patient_visit = new Paint();
        patient_visit.setTypeface(bold2);
        patient_visit.setTextSize(25);
        canvas.drawText("Reg # : ", canvas.getWidth() - 244, 120, patient_visit);
        canvas.drawText(NurseVitalSketch.patient_reg_number, canvas.getWidth() - 150, 120, patient_visit);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedTodayDate = df.format(c);

        Paint today_date = new Paint();
        today_date.setTypeface(bold2);
        today_date.setTextSize(25);
        today_date.setColor(Color.BLUE);
        canvas.drawText("Date : ", canvas.getWidth() - 235, 170, today_date);
        canvas.drawText(formattedTodayDate, canvas.getWidth() - 155, 170, today_date);

        _buildVitalTemplate(canvas);

//        else if(FragmentDetail.sketch_background != -1){
//            canvas.drawBitmap(MainActivity.image_array.get(FragmentDetail.sketch_background).getImage(), 0, 0, null);   /// edit***
//        }

        for (Pair<Path, Paint> p : paths) {
            canvas.drawPath(p.first, p.second);
        }
        onDrawChangedListener.onDrawChanged();
    }

    // build vital template
    private void _buildVitalTemplate(Canvas _canvas){

        int _fontSize = 40;
        int _helperTextFontSize = 14;
        // temp lbl
        Paint paint = new Paint();
        Typeface currentTypeFace = paint.getTypeface();
        Typeface bold2 = Typeface.create(currentTypeFace, Typeface.BOLD);
        Paint temp_lbl = new Paint();
        temp_lbl.setTypeface(bold2);
        temp_lbl.setTextSize(_fontSize);
        _canvas.drawText("Temp", 50, 250, temp_lbl);

        // build underline
        _buildUnderLine(_canvas, 200, 250);

        // build f temp lbl
        Paint FTemp_lbl = new Paint();
        FTemp_lbl.setTypeface(bold2);
        FTemp_lbl.setTextSize(_fontSize);
        _canvas.drawText("°F", 470, 250, FTemp_lbl);

        // build underline
        _buildUnderLine(_canvas, 520, 250);

        // build C temp lbl
        Paint CTemp_lbl = new Paint();
        CTemp_lbl.setTypeface(bold2);
        CTemp_lbl.setTextSize(_fontSize);
        _canvas.drawText("°C", 790, 250, CTemp_lbl);

        // build temp helper lbl
        Paint temp_helper = new Paint();
        temp_helper.setTypeface(bold2);
        temp_helper.setTextSize(_helperTextFontSize);
        _canvas.drawText("(Temperature)", 50, 270, temp_helper);

        // build pr lbl
        Paint PR_lbl = new Paint();
        PR_lbl.setTypeface(bold2);
        PR_lbl.setTextSize(_fontSize);
        _canvas.drawText("PR", 80, 370, PR_lbl);

        // build underline
        _buildUnderLine(_canvas, 200, 370);

        // build pr unit lbl
        Paint PR_unit_lbl = new Paint();
        PR_unit_lbl.setTypeface(bold2);
        PR_unit_lbl.setTextSize(_fontSize);
        _canvas.drawText("/min", 470, 370, PR_unit_lbl);

        // build pr helper lbl
        Paint pr_helper = new Paint();
        pr_helper.setTypeface(bold2);
        pr_helper.setTextSize(_helperTextFontSize);
        _canvas.drawText("(Pulse Rate)", 50, 390, pr_helper);

        // build rr lbl
        Paint RR_lbl = new Paint();
        RR_lbl.setTypeface(bold2);
        RR_lbl.setTextSize(_fontSize);
        _canvas.drawText("RR", 80, 490, RR_lbl);

        // build underline
        _buildUnderLine(_canvas, 200, 490);

        // build pr unit lbl
        Paint RR_unit_lbl = new Paint();
        RR_unit_lbl.setTypeface(bold2);
        RR_unit_lbl.setTextSize(_fontSize);
        _canvas.drawText("/min", 470, 490, RR_unit_lbl);

        // build rr helper lbl
        Paint rr_helper = new Paint();
        rr_helper.setTypeface(bold2);
        rr_helper.setTextSize(_helperTextFontSize);
        _canvas.drawText("(Respiration Rate)", 50, 510, rr_helper);

        // build rr lbl
        Paint BP_lbl = new Paint();
        BP_lbl.setTypeface(bold2);
        BP_lbl.setTextSize(_fontSize);
        _canvas.drawText("BP", 80, 610, BP_lbl);

        // build underline
        _buildUnderLine(_canvas, 200, 610);

        // build pr unit lbl
        Paint bp_divider = new Paint();
        bp_divider.setTypeface(bold2);
        bp_divider.setTextSize(_fontSize);
        _canvas.drawText("/", 470, 610, bp_divider);

        // build underline
        _buildUnderLine(_canvas, 490, 610);

        // build pr unit lbl
        Paint BP_unit_lbl = new Paint();
        BP_unit_lbl.setTypeface(bold2);
        BP_unit_lbl.setTextSize(_fontSize);
        _canvas.drawText("mm/Hg", 750, 610, BP_unit_lbl);

        // build rr helper lbl
        Paint bp_helper = new Paint();
        bp_helper.setTypeface(bold2);
        bp_helper.setTextSize(_helperTextFontSize);
        _canvas.drawText("(Blood Pressure)", 50, 630, bp_helper);

        // build spo2 lbl
        Paint SPO2_lbl = new Paint();
        SPO2_lbl.setTypeface(bold2);
        SPO2_lbl.setTextSize(_fontSize);
        _canvas.drawText("SPO2", 30, 730, SPO2_lbl);

        // build underline
        _buildUnderLine(_canvas, 200, 730);

        // build spo2 unit lbl
        Paint spo2_unit_lbl = new Paint();
        spo2_unit_lbl.setTypeface(bold2);
        spo2_unit_lbl.setTextSize(_fontSize);
        _canvas.drawText("%", 470, 730, spo2_unit_lbl);

        // build spo2 sup lbl
        Paint SPO2_supp_lbl = new Paint();
        SPO2_supp_lbl.setTypeface(bold2);
        SPO2_supp_lbl.setTextSize(_fontSize);
        _canvas.drawText("O2 Supplemental", 50, 850, SPO2_supp_lbl);

        // build check box
        _buildCheckBox(_canvas, 450, 800, 520, 870);

        // build yes for spo2 sup lbl
        Paint yes_spo2_lbl = new Paint();
        yes_spo2_lbl.setTypeface(bold2);
        yes_spo2_lbl.setTextSize(_fontSize);
        _canvas.drawText("Yes", 540, 850, yes_spo2_lbl);

        // build no check box for spo2 sup
        _buildCheckBox(_canvas,620, 800, 690, 870);

        // build yes for spo2 sup lbl
        Paint no_spo2_lbl = new Paint();
        no_spo2_lbl.setTypeface(bold2);
        no_spo2_lbl.setTextSize(_fontSize);
        _canvas.drawText("No", 710, 850, yes_spo2_lbl);

        // build LOC lbl
        Paint LOC_lbl = new Paint();
        LOC_lbl.setTypeface(bold2);
        LOC_lbl.setTextSize(_fontSize);
        _canvas.drawText("LOC", 60, 970, LOC_lbl);

        _buildCheckBox(_canvas, 200, 920, 270, 990);

        Paint LOC_A_lbl = new Paint();
        LOC_A_lbl.setTypeface(bold2);
        LOC_A_lbl.setTextSize(_fontSize);
        _canvas.drawText("A", 290, 970, LOC_A_lbl);

        _buildCheckBox(_canvas, 350, 920, 420, 990);

        Paint LOC_V_lbl = new Paint();
        LOC_V_lbl.setTypeface(bold2);
        LOC_V_lbl.setTextSize(_fontSize);
        _canvas.drawText("V", 440, 970, LOC_V_lbl);

        _buildCheckBox(_canvas, 500, 920, 570, 990);

        Paint LOC_P_lbl = new Paint();
        LOC_P_lbl.setTypeface(bold2);
        LOC_P_lbl.setTextSize(_fontSize);
        _canvas.drawText("P", 590, 970, LOC_P_lbl);

        _buildCheckBox(_canvas, 650, 920, 720, 990);

        Paint LOC_U_lbl = new Paint();
        LOC_U_lbl.setTypeface(bold2);
        LOC_U_lbl.setTextSize(_fontSize);
        _canvas.drawText("U", 740, 970, LOC_U_lbl);

        // build weight label
        Paint weight_lbl = new Paint();
        weight_lbl.setTypeface(bold2);
        weight_lbl.setTextSize(_fontSize);
        _canvas.drawText("Weight", 30, 1120, weight_lbl);

        _buildUnderLine(_canvas, 200, 1120);

        // build weight kg unit label
        Paint weight_kg_unit_lbl = new Paint();
        weight_kg_unit_lbl.setTypeface(bold2);
        weight_kg_unit_lbl.setTextSize(_fontSize);
        _canvas.drawText("kg / ", 470, 1120, weight_kg_unit_lbl);

        _buildUnderLine(_canvas, 570, 1120);

        // build weight lb unit label
        Paint weight_lb_unit_lbl = new Paint();
        weight_lb_unit_lbl.setTypeface(bold2);
        weight_lb_unit_lbl.setTextSize(_fontSize);
        _canvas.drawText("lbs ", 840, 1120, weight_lb_unit_lbl);

        // build height label
        Paint height_lbl = new Paint();
        height_lbl.setTypeface(bold2);
        height_lbl.setTextSize(_fontSize);
        _canvas.drawText("Height", 30, 1240, height_lbl);

        _buildUnderLine(_canvas, 200, 1240);

        // build height kg unit label
        Paint height_cm_unit_lbl = new Paint();
        height_cm_unit_lbl.setTypeface(bold2);
        height_cm_unit_lbl.setTextSize(_fontSize);
        _canvas.drawText("cm / ", 470, 1240, height_cm_unit_lbl);

//        _buildUnderLine(_canvas, 570, 1240);
        Paint linePaint = new Paint();
        linePaint.setTypeface(bold2);
        linePaint.setStrokeWidth(3);
        _canvas.drawLine(570, 1240, 670, 1240, linePaint);

        // build height lb unit label
        Paint height_ft_unit_lbl = new Paint();
        height_ft_unit_lbl.setTypeface(bold2);
        height_ft_unit_lbl.setTextSize(_fontSize);
        _canvas.drawText("ft ", 700, 1240, height_ft_unit_lbl);

//        _buildUnderLine(_canvas, 890, 1240);

        linePaint.setTypeface(bold2);
        linePaint.setStrokeWidth(3);
        _canvas.drawLine(790, 1240, 890, 1240, linePaint);

        // build height inch unit label
        Paint height_inch_unit_lbl = new Paint();
        height_inch_unit_lbl.setTypeface(bold2);
        height_inch_unit_lbl.setTextSize(_fontSize);
        _canvas.drawText("inches", 920, 1240, height_inch_unit_lbl);

        // build taken inch unit label
        Paint takenBy_lbl = new Paint();
        takenBy_lbl.setTypeface(bold2);
        takenBy_lbl.setTextSize(_fontSize + 10);
        Log.e("canvas height", String.valueOf(_canvas.getHeight()));
        _canvas.drawText("Taken By", _canvas.getWidth() - 500, _canvas.getHeight() - 200, takenBy_lbl);

        // build taken user
        Paint takenUserName_lbl = new Paint();
//        takenUser_lbl.setTypeface(bold2);
        takenUserName_lbl.setTextSize(_fontSize);
        Log.e("canvas height", String.valueOf(_canvas.getHeight()));
        _canvas.drawText(NurseVitalSketch.nurseName, _canvas.getWidth() - 500, _canvas.getHeight() - 120, takenUserName_lbl);

        Paint takenUserID_lbl = new Paint();
//        takenUser_lbl.setTypeface(bold2);
        takenUserID_lbl.setTextSize(_fontSize);
        Log.e("canvas height", String.valueOf(_canvas.getHeight()));
        _canvas.drawText(NurseVitalSketch.nurseCode, _canvas.getWidth() - 500, _canvas.getHeight() - 60, takenUserID_lbl);
    }

    // build short line
    private void _buildUnderLine(Canvas _canvas, int _startX, int _startY){
        Paint linePaint = new Paint();
        Typeface currentTypeFace = linePaint.getTypeface();
        Typeface bold2 = Typeface.create(currentTypeFace, Typeface.BOLD);
        linePaint.setTypeface(bold2);
        linePaint.setStrokeWidth(3);
        _canvas.drawLine(_startX, _startY, _startX + 250, _startY, linePaint);
    }

    // build check box
    private void _buildCheckBox(Canvas _canvas, int left, int top, int right, int bottom){
        Paint rec_paint = new Paint();
        rec_paint.setColor(Color.BLACK);
        rec_paint.setStrokeWidth(3);
        _canvas.drawRect(left, top, right, bottom, rec_paint);
        rec_paint.setStrokeWidth(0);
        rec_paint.setColor(Color.parseColor("#fafafa"));
        _canvas.drawRect(left + 3, top + 3, right - 3, bottom -3, rec_paint);
    }


    private void touch_start(float x, float y) {
        // Clearing undone list
        undonePaths.clear();

        if (paths.size() == 0) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(backgroundColor);
            Log.e("empty canvas >>> ", "empty canvas");
        }

        if (mode == ERASER) {
            m_Paint.setColor(backgroundColor);
            m_Paint.setStrokeWidth(eraserSize);
        } else {
            m_Paint.setColor(strokeColor);
            m_Paint.setStrokeWidth(strokeSize);
        }

        // Avoids that a sketch with just erasures is saved
        if (!(paths.size() == 0 && mode == ERASER && bitmap == null)) {
            m_Path = new Path();
            paths.add(new Pair<>(m_Path, new Paint(m_Paint)));
        }

        m_Path.moveTo(x, y);
        m_Path.lineTo(++x, y); // for draw a one touch path
        mX = x;
        mY = y;
    }


    private void touch_move(float x, float y) {
        m_Path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
        mX = x;
        mY = y;
    }

    public Boolean emptyPath(){
        if(paths.size() == 0)
            return true;
        else return false;
    }

    /**
     * Returns a new bitmap associated with a drawn canvas
     *
     * @return background bitmap with a paths drawn on it
     */
    public Bitmap getBitmap() {
        if (paths.size() == 0)
            return null;

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(backgroundColor);
        }
        Canvas canvas = new Canvas(bitmap);
        Paint rec_paint = new Paint();
        rec_paint.setColor(Color.BLACK);
        rec_paint.setStrokeWidth(3);
        canvas.drawRect(0, 3, canvas.getWidth(), 140, rec_paint);
        rec_paint.setStrokeWidth(0);
        rec_paint.setColor(Color.parseColor("#fafafa"));
        canvas.drawRect(3, 6, canvas.getWidth() - 3, 137, rec_paint);

        Paint paint = new Paint();
        Typeface currentTypeFace = paint.getTypeface();
        Typeface bold = Typeface.create(currentTypeFace, Typeface.BOLD);
        paint.setTypeface(bold);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(NurseVitalSketch.title, canvas.getWidth() / 2, canvas.getHeight() / 35, paint);

        Paint patient_name = new Paint();
        Typeface patient = paint.getTypeface();
        Typeface bold2 = Typeface.create(patient, Typeface.BOLD);
        patient_name.setTypeface(bold2);
        patient_name.setTextSize(25);
        canvas.drawText("Patient Name : ", 10, 90, patient_name);
        canvas.drawText(NurseVitalSketch.patient_name, 200, 90, patient_name);

        Paint patient_cpi = new Paint();
        patient_cpi.setTypeface(bold2);
        patient_cpi.setTextSize(25);
        canvas.drawText("CPI : ", canvas.getWidth() - 220, 90, patient_cpi);
        canvas.drawText(NurseVitalSketch.patient_cpi, canvas.getWidth() - 150, 90, patient_cpi);

        Paint doctor_name = new Paint();
        doctor_name.setTypeface(bold2);
        doctor_name.setTextSize(25);
        canvas.drawText("Doctor Name : ", 13, 120, doctor_name);
        canvas.drawText(NurseVitalSketch.docName, 200, 120, doctor_name);

        Paint patient_visit = new Paint();
        patient_visit.setTypeface(bold2);
        patient_visit.setTextSize(25);
        canvas.drawText("Reg # : ", canvas.getWidth() - 244, 120, patient_visit);
        canvas.drawText(NurseVitalSketch.patient_reg_number, canvas.getWidth() - 150, 120, patient_visit);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedTodayDate = df.format(c);

        Paint today_date = new Paint();
        today_date.setTypeface(bold2);
        today_date.setTextSize(25);
        today_date.setColor(Color.BLUE);
        canvas.drawText("Date : ", canvas.getWidth() - 235, 170, today_date);
        canvas.drawText(formattedTodayDate, canvas.getWidth() - 155, 170, today_date);

//        else if(FragmentDetail.sketch_background != -1){
//            canvas.drawBitmap(MainActivity.image_array.get(FragmentDetail.sketch_background).getImage(), 0, 0, null);
//        }

        _buildVitalTemplate(canvas);

        for (Pair<Path, Paint> p : paths) {
            canvas.drawPath(p.first, p.second);
        }
//        Runtime.getRuntime().gc();                      /// testing for OOM error
        return bitmap;
    }

    public void RecycleBitmap() {
        bitmap.recycle();
        bitmap = null;
    }


    public void undo() {
        if (!paths.isEmpty()) {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        }
    }


    public void redo() {
        if (!undonePaths.isEmpty()) {
            paths.add(undonePaths.remove(undonePaths.size() - 1));
            invalidate();
        }
    }

    public boolean checkCanvasIsEmpty() {
        Boolean isEmpty;
        if (paths.isEmpty()) {
            isEmpty = true;
            Log.e("check canvas >>> ", "is empty");
        } else {
            isEmpty = false;
            Log.e("check canvas >>> ", "isn't empty");
        }
        return isEmpty;
    }

    public int getUndoneCount() {
        return undonePaths.size();
    }

    public ArrayList<Pair<Path, Paint>> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Pair<Path, Paint>> paths) {
        this.paths = paths;
    }

    public ArrayList<Pair<Path, Paint>> getUndonePaths() {
        return undonePaths;
    }

    public void setUndonePaths(ArrayList<Pair<Path, Paint>> undonePaths) {
        this.undonePaths = undonePaths;
    }

    public int getStrokeSize() {
        return Math.round(this.strokeSize);
    }

    public void setSize(int size, int eraserOrStroke) {
        switch (eraserOrStroke) {
            case STROKE:
                strokeSize = size;
                break;
            case ERASER:
                eraserSize = size;
                break;
            default:
                Log.e("kdk", "Wrong element choosen: " + eraserOrStroke);
        }
    }


    public int getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeColor(int color) {
        strokeColor = color;
    }

    public void erase() {
        paths.clear();
        undonePaths.clear();
        invalidate();
    }

    public void setOnDrawChangedListener(OnDrawChangedListener listener) {
        this.onDrawChangedListener = listener;
    }
}
