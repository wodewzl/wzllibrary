
package com.bs.bsims.chatutils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;

public class DialogManager {

    /**
     * 浠ヤ笅涓篸ialog鐨勫垵濮嬪寲鎺т欢锛屽寘鎷叾涓殑甯冨眬鏂囦欢
     */

    private Dialog mDialog;

    private ImageView mIcon;
    private ImageView mVoice;

    private TextView mLable;

    private Context mContext;

    public DialogManager(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    public void showRecordingDialog() {
        // TODO Auto-generated method stub

        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        mDialog = new Dialog(mContext, R.style.Theme_audioDialog);
        // 鐢╨ayoutinflater鏉ュ紩鐢ㄥ竷灞�
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.im_dialog_manager, null);
        mDialog.setContentView(view);

        mIcon = (ImageView) mDialog.findViewById(R.id.dialog_icon);
        mVoice = (ImageView) mDialog.findViewById(R.id.dialog_voice);
        mLable = (TextView) mDialog.findViewById(R.id.recorder_dialogtext);
        mDialog.show();

    }

    /**
     * 璁剧疆姝ｅ湪褰曢煶鏃剁殑dialog鐣岄潰
     */
    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.recorder);
            mLable.setText(R.string.shouzhishanghua);
        }
    }

    /**
     * 鍙栨秷鐣岄潰
     */
    public void wantToCancel() {
        // TODO Auto-generated method stub
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.cancel);
            mLable.setText(R.string.want_to_cancle);
        }

    }

    // 鏃堕棿杩囩煭
    public void tooShort() {
        // TODO Auto-generated method stub
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.voice_to_short);
            mLable.setText("说话时间过短");
        }

    }

    // 鏃堕棿杩囩煭
    public void NoTimeRe(String time) {
        // TODO Auto-generated method stub
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.voice_to_short);
            mLable.setText(time);
        }

    }

    // 闅愯棌dialog
    public void dimissDialog() {
        // TODO Auto-generated method stub

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }

    }

    public void updateVoiceLevel(int level) {
        // TODO Auto-generated method stub

        if (mDialog != null && mDialog.isShowing()) {

            // 鍏堜笉鏀瑰彉瀹冪殑榛樿鐘舵�
            // mIcon.setVisibility(View.VISIBLE);
            // mVoice.setVisibility(View.VISIBLE);
            // mLable.setVisibility(View.VISIBLE);

            // 閫氳繃level鏉ユ壘鍒板浘鐗囩殑id锛屼篃鍙互鐢╯witch鏉ュ鍧�紝浣嗘槸浠ｇ爜鍙兘浼氭瘮杈冮暱
            int resId = mContext.getResources().getIdentifier("v" + level,
                    "drawable", mContext.getPackageName());

            mVoice.setImageResource(resId);
        }

    }

}
