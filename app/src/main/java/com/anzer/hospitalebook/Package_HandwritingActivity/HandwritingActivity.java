package com.anzer.hospitalebook.Package_HandwritingActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.anzer.hospitalebook.Doctor_Dashboard;
import com.anzer.hospitalebook.LabAndDI.Lab_And_DI_Main_Fragment;
import com.anzer.hospitalebook.MainActivity;
import com.anzer.hospitalebook.Package_FragmentDetail.FragmentDetail;
import com.anzer.hospitalebook.Package_Gallery_Activity.GalleryActivity;
import com.anzer.hospitalebook.Package_SketchFragment.SketchFragment;
import com.anzer.hospitalebook.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HandwritingActivity extends AppCompatActivity {
    FragmentDetail fragmentParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_fragment_activity);
        getIDs();

        // Show the Up button in the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getIDs() {
        fragmentParent = (FragmentDetail) this.getSupportFragmentManager().findFragmentById(R.id.fragmentParent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Fragment checkFragmentInstance(int id, Object instanceClass) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        Fragment result = null;
        if (mFragmentManager != null) {
            Fragment fragment = mFragmentManager.findFragmentById(id);
            if (instanceClass.equals(fragment.getClass())) {
                result = fragment;
            }
        }
        return result;
    }

    public  void back_to_fragmentDetail(){
        FragmentDetail fdetail  = new FragmentDetail();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentParent, fdetail);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public  void back_to_GalleryActivity(){
        Intent back_to_gallery = new Intent(this, GalleryActivity.class);
        FragmentDetail.clickedImage = GalleryActivity.editFromFullImagesID;
        startActivity(back_to_gallery);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        // fragment detail
        Fragment fragDetail = checkFragmentInstance(R.id.fragmentParent, FragmentDetail.class);
        if(fragDetail != null){
            // testing
            MainActivity.LAB_OrderedTest.clear();
            MainActivity.DI_OrderedTest.clear();
            if(MainActivity.image_array.size() != 0 ){                                    ///// Need to redo
                Dialog dialog = new Dialog(HandwritingActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_with_one_button);

                TextView dialogBody = dialog.findViewById(R.id.dialog_body_txt);
                dialogBody.setText("Please save images first !!! ");

                Button ok_btn = dialog.findViewById(R.id.ok_btn);

                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

            }
            else{
                Intent myIntent = new Intent(this, Doctor_Dashboard.class);
                startActivityForResult(myIntent, 0);
            }
        }

        // SketchFragment
        Fragment f = checkFragmentInstance(R.id.fragmentParent, SketchFragment.class);
        if(f != null){
            Dialog dialog = new Dialog( HandwritingActivity.this );
            dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
            dialog.setContentView( R.layout.dialog_with_two_buttons );

            TextView dialogBody = dialog.findViewById( R.id.dialog_body_txt );
            dialogBody.setText( "Do you want to save current page ?" );

            Button ok_btn = dialog.findViewById( R.id.ok_btn );
            Button cancel_btn = dialog.findViewById( R.id.cancel_btn );

            ok_btn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SketchFragment) f).save();
                    if (!SketchFragment.backfromGalleryEditPage) {
                        back_to_fragmentDetail();
                    } else {
                        back_to_GalleryActivity();
                        SketchFragment.backfromGalleryEditPage = false;
                    }
                    dialog.dismiss();
                }
            } );

            cancel_btn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!SketchFragment.backfromGalleryEditPage)
                        back_to_fragmentDetail();
                    else {
                        back_to_GalleryActivity();
                        SketchFragment.backfromGalleryEditPage = false;
                    }
                    dialog.dismiss();
                }
            } );

            dialog.setCanceledOnTouchOutside( false );
            dialog.show();
        }

        // lab and di Fragment
        Fragment frag_DInLAB = checkFragmentInstance(R.id.fragmentParent, Lab_And_DI_Main_Fragment.class);
        if(frag_DInLAB != null){
            getSupportFragmentManager().beginTransaction().remove(frag_DInLAB).commit();
//            back_to_fragmentDetail();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
