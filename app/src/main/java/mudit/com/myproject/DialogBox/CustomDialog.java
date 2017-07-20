package mudit.com.myproject.DialogBox;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import mudit.com.myproject.R;

import static mudit.com.myproject.R.id.imgFriend;

public class CustomDialog extends Dialog implements android.view.View.OnClickListener{
    Activity a;
    Context context;
    public Dialog d;
    ImageView imgRotate;
    public CustomDialog(Activity a,Context context) {
        super(a);
        this.a=a;
        this.context=context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case R.id.imgFriend:
            dismiss();
        default:
            break;}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_custom_dialog);
                imgRotate=(ImageView)findViewById(R.id.imgRotate);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate_around_center_point);
        imgRotate.startAnimation(animation);
//        imgRotate=(ImageView)findViewById(R.id.imgRotate);
//        imgRotate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
    }
}
