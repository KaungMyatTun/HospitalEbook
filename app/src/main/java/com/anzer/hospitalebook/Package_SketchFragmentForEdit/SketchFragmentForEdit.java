package com.anzer.hospitalebook.Package_SketchFragmentForEdit;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.anzer.hospitalebook.Package_FragmentDetail.FragmentDetail;
import com.anzer.hospitalebook.Package_Gallery_Activity.GalleryActivity;
import com.anzer.hospitalebook.Package_SketchFragment.SketchFragment;
import com.anzer.hospitalebook.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by David on 5/30/2018.
 */

public class SketchFragmentForEdit extends AppCompatActivity {
    SketchFragment sketchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sketch_fragment_for_edit);
        getIDs();
    }

    private void getIDs() {
        SketchFragment.patient_name = "tt";
        SketchFragment.patient_cpi  = "1";
        SketchFragment.patient_visit_no = "2334";
        SketchFragment.backfromGalleryEditPage = true;

        FragmentDetail.sketch_background = GalleryActivity.editFromFullImagesID;
        sketchFragment = (SketchFragment) this.getSupportFragmentManager().findFragmentById(R.id.sketch_fragment_for_edit);
    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new Dialog(SketchFragmentForEdit.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_with_one_button);

        TextView dialogBody = dialog.findViewById(R.id.dialog_body_txt);
        dialogBody.setText("Please complete before back !!! ");

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
}
