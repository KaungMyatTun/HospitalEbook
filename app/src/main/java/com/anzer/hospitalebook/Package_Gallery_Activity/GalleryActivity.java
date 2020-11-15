package com.anzer.hospitalebook.Package_Gallery_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.anzer.hospitalebook.Doctor_Dashboard;
import com.anzer.hospitalebook.MainActivity;
import com.anzer.hospitalebook.Package_FragmentDetail.FragmentDetail;
import com.anzer.hospitalebook.Package_HandwritingActivity.HandwritingActivity;
import com.anzer.hospitalebook.Package_SketchFragmentForEdit.SketchFragmentForEdit;
import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.models.adapters.SlidingImage_Adapter;
import com.anzer.hospitalebook.models.views.InterceptorFrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 5/29/2018.
 */

public class GalleryActivity extends AppCompatActivity {
    @BindView( R.id.gallery_root)
    InterceptorFrameLayout galleryRootView;
    @BindView( R.id.fullscreen_content)
    ViewPager mViewPager;
    public static int editFromFullImagesID;
    private ImageView[]dots;
    private int dotscount;
    private int real_position;

    String get_check_btn;
    LinearLayout sliderDotspanel;
    SlidingImage_Adapter slidingImage_adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gallery, menu);
        if(MainActivity.image_array_clone.get(real_position).getCaption().equals("Photo")){
            menu.findItem(R.id.edit_images).setVisible(false);
        }
        return true;
    }

    private void initViews() {
        // Show the Up button in the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        get_check_btn = this.getIntent().getStringExtra("Check Radio btn") ;
        mViewPager = findViewById(R.id.fullscreen_content);
        slidingImage_adapter = new SlidingImage_Adapter(this, MainActivity.image_array_clone);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setSubtitle("(" + (position + 1) + "/" + MainActivity.image_array_clone.size() + ")");      ///// Need to redo
                real_position = position;

                for(int i = 0; i< dotscount; i++){
                    if(i == real_position){
                        dots[real_position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                    }
                    else
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                if(MainActivity.image_array_clone.get(position).getCaption().equals("Photo")){
                    invalidateOptionsMenu();
                }
                else
                    invalidateOptionsMenu();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


        mViewPager.setAdapter(slidingImage_adapter);
        mViewPager.getAdapter().notifyDataSetChanged();
        mViewPager.setOffscreenPageLimit(100);
        mViewPager.setCurrentItem(FragmentDetail.clickedImage);
        getSupportActionBar().setSubtitle("(" + (FragmentDetail.clickedImage + 1 ) + "/" + MainActivity.image_array_clone.size() + ")");           ///// Need to redo

        sliderDotspanel = findViewById(R.id.SliderDots);
        dotscount = slidingImage_adapter.getCount();
        dots = new ImageView[dotscount];
        for(int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);
        }
        dots[real_position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        if(MainActivity.image_array_clone.get(real_position).getCaption().equals("Photo")){
            invalidateOptionsMenu();
        }
        else
            invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.edit_images:
                Intent attachmentIntent = new Intent(this, SketchFragmentForEdit.class);
                startActivity(attachmentIntent);
                editFromFullImagesID = FragmentDetail.real_IndexFromMainArray.get(mViewPager.getCurrentItem());
            default:
                Log.e("Wrong", "Wrong element choosen: " + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    public int editFromFullImages(int attPosition){
        return attPosition;
    }

    @Override
    public void onBackPressed() {
        slidingImage_adapter.notifyDataSetChanged();
        mViewPager.getAdapter().notifyDataSetChanged();
//        FragmentDetail.patient_name = FragmentDetail.patient_name;
//        FragmentDetail.patient_cpi = FragmentDetail.patient_cpi;
//        FragmentDetail.patient_visit_no = FragmentDetail.patient_visit_no;
//        FragmentDetail.patient_reg_date = FragmentDetail.patient_reg_date;

        Intent go_back = new Intent(this, HandwritingActivity.class);
        go_back.putExtra("PATIENT_CPI", FragmentDetail.patient_cpi);
        go_back.putExtra("PATIENT_NAME", FragmentDetail.patient_name);
        go_back.putExtra("PATIENT_VISIT_NUMBER", FragmentDetail.patient_visit_no);
        go_back.putExtra("PATIENT_REG_DATE", FragmentDetail.patient_reg_date);
        Doctor_Dashboard.fromDashboardToFragmentDetail = true;
        go_back.putExtra("From_Dashboard_To_FragDetail", Doctor_Dashboard.fromDashboardToFragmentDetail);
        go_back.putExtra("check_btn", get_check_btn);
        startActivity(go_back);
    }
}

