
package com.bs.bsims.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;

public class BSImgDialog extends Dialog {

    Context context;
    View view;
    ImageView mImageView;

    public BSImgDialog(Context context, View view ) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.view = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.img);
        mImageView =(ImageView) findViewById(R.id.dialog_id);
    }
 
 
}
