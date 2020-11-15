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

import com.anzer.hospitalebook.Doctor_Dashboard;
import com.anzer.hospitalebook.MainActivity;
import com.anzer.hospitalebook.Package_FragmentDetail.FragmentDetail;
import com.anzer.hospitalebook.Package_SketchFragment.SketchFragment;
import com.anzer.hospitalebook.models.listeners.OnDrawChangedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class SketchView extends View implements View.OnTouchListener {

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

    public SketchView(Context context, AttributeSet attr) {
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
        if(FragmentDetail.sketch_background == -1){
            Paint rec_paint = new Paint();
            rec_paint.setColor(Color.BLACK);
            rec_paint.setStrokeWidth(3);
            canvas.drawRect(0,3,canvas.getWidth(),140, rec_paint);
            rec_paint.setStrokeWidth(0);
            rec_paint.setColor(Color.parseColor("#fafafa"));
            canvas.drawRect(3,6,canvas.getWidth()-3,137,rec_paint);

            Paint paint = new Paint();
            Typeface currentTypeFace =   paint.getTypeface();
            Typeface bold = Typeface.create(currentTypeFace, Typeface.BOLD);
            paint.setTypeface(bold);
            paint.setTextSize(40);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(SketchFragment.handwriting_title, canvas.getWidth()/ 2, canvas.getHeight()/35, paint);

            Paint patient_name = new Paint();
            Typeface patient =   paint.getTypeface();
            Typeface bold2 = Typeface.create(patient, Typeface.BOLD);
            patient_name.setTypeface(bold2);
            patient_name.setTextSize(25);
            canvas.drawText("Patient Name : ", 10,  90, patient_name);
            canvas.drawText(SketchFragment.patient_name, 200,  90, patient_name);

            Paint patient_cpi = new Paint();
            patient_cpi.setTypeface(bold2);
            patient_cpi.setTextSize(25);
            canvas.drawText("CPI : ", canvas.getWidth()-220,  90, patient_cpi);
            canvas.drawText(SketchFragment.patient_cpi, canvas.getWidth()-150,  90, patient_cpi);

            Paint doctor_name = new Paint();
            doctor_name.setTypeface(bold2);
            doctor_name.setTextSize(25);
            canvas.drawText("Doctor Name : ", 13,  120, doctor_name);
            canvas.drawText(Doctor_Dashboard.doctor_name, 200,  120, doctor_name);

            Paint patient_visit = new Paint();
            patient_visit.setTypeface(bold2);
            patient_visit.setTextSize(25);
            canvas.drawText("Reg # : ", canvas.getWidth()-244,  120, patient_visit);
            canvas.drawText(SketchFragment.patient_visit_no, canvas.getWidth()-150,  120, patient_visit);

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedTodayDate = df.format(c);

            Paint today_date = new Paint();
            today_date.setTypeface(bold2);
            today_date.setTextSize(25);
            today_date.setColor(Color.BLUE);
            canvas.drawText("Date : ", canvas.getWidth()-235,  170, today_date);
            canvas.drawText(formattedTodayDate, canvas.getWidth()-155,  170, today_date);
        }
        else if(FragmentDetail.sketch_background != -1){
            canvas.drawBitmap(MainActivity.image_array.get(FragmentDetail.sketch_background).getImage(), 0, 0, null);   /// edit***
        }

        for (Pair<Path, Paint> p : paths) {
            canvas.drawPath(p.first, p.second);
        }
        onDrawChangedListener.onDrawChanged();
    }


    private void touch_start(float x, float y) {
        // Clearing undone list
        undonePaths.clear();

        if(paths.size() == 0){
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(backgroundColor);
            Log.e("empty canvas >>> ", "empty canvas" );
        }

        if (mode == ERASER) {
            m_Paint.setColor(backgroundColor);
            m_Paint.setStrokeWidth(eraserSize);
        } else {
            m_Paint.setColor(strokeColor);
            m_Paint.setStrokeWidth(strokeSize);
        }

        // Avoids that a sketch with just erasures is saved
        if (!(paths.size() == 0  && mode == ERASER && bitmap == null)) {
            m_Path=new Path();
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
        if(FragmentDetail.sketch_background == -1){
            Paint rec_paint = new Paint();
            rec_paint.setColor(Color.BLACK);
            rec_paint.setStrokeWidth(3);
            canvas.drawRect(0,3,canvas.getWidth(),140, rec_paint);
            rec_paint.setStrokeWidth(0);
            rec_paint.setColor(Color.parseColor("#fafafa"));
            canvas.drawRect(3,6,canvas.getWidth()-3,137,rec_paint);

            Paint paint = new Paint();
            Typeface currentTypeFace =   paint.getTypeface();
            Typeface bold = Typeface.create(currentTypeFace, Typeface.BOLD);
            paint.setTypeface(bold);
            paint.setTextSize(40);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(SketchFragment.handwriting_title, canvas.getWidth()/ 2, canvas.getHeight()/35, paint);

            Paint patient_name = new Paint();
            Typeface patient =   paint.getTypeface();
            Typeface bold2 = Typeface.create(patient, Typeface.BOLD);
            patient_name.setTypeface(bold2);
            patient_name.setTextSize(25);
            canvas.drawText("Patient Name : ", 10,  90, patient_name);
            canvas.drawText(SketchFragment.patient_name, 200,  90, patient_name);

            Paint patient_cpi = new Paint();
            patient_cpi.setTypeface(bold2);
            patient_cpi.setTextSize(25);
            canvas.drawText("CPI : ", canvas.getWidth()-220,  90, patient_cpi);
            canvas.drawText(SketchFragment.patient_cpi, canvas.getWidth()-150,  90, patient_cpi);

            Paint doctor_name = new Paint();
            doctor_name.setTypeface(bold2);
            doctor_name.setTextSize(25);
            canvas.drawText("Doctor Name : ", 13,  120, doctor_name);
            canvas.drawText(Doctor_Dashboard.doctor_name, 200,  120, doctor_name);

            Paint patient_visit = new Paint();
            patient_visit.setTypeface(bold2);
            patient_visit.setTextSize(25);
            canvas.drawText("Reg # : ", canvas.getWidth()-244,  120, patient_visit);
            canvas.drawText(SketchFragment.patient_visit_no, canvas.getWidth()-150,  120, patient_visit);

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedTodayDate = df.format(c);

            Paint today_date = new Paint();
            today_date.setTypeface(bold2);
            today_date.setTextSize(25);
            today_date.setColor(Color.BLUE);
            canvas.drawText("Date : ", canvas.getWidth()-235,  170, today_date);
            canvas.drawText(formattedTodayDate, canvas.getWidth()-155,  170, today_date);
        }
        else if(FragmentDetail.sketch_background != -1){
            canvas.drawBitmap(MainActivity.image_array.get(FragmentDetail.sketch_background).getImage(), 0, 0, null);
        }

        for (Pair<Path, Paint> p : paths) {
            canvas.drawPath(p.first, p.second);
        }
//        Runtime.getRuntime().gc();                      /// testing for OOM error
        return bitmap;
    }

    public void RecycleBitmap(){
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

    public boolean checkCanvasIsEmpty(){
        Boolean isEmpty;
        if(paths.isEmpty()) {
            isEmpty = true;
            Log.e("check canvas >>> ", "is empty");
        }
        else {
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