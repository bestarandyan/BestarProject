package com.manyi.mall.mine;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.ManyiUtils;
import com.huoqiu.widget.PhoneEditTextView;
import com.manyi.mall.R;
import com.manyi.mall.service.UcService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by jason on 2014/12/12.
 */
@EFragment(R.layout.fragment_new_phone_number)
public class ChangeNewPhoneNumberFragment extends SuperFragment<Object> {

    @ViewById(R.id.change_new_phone_number)
    PhoneEditTextView mChangeNewPhoneNumber;//新电话号码

    @ViewById(R.id.input_phone_verification)
    EditText mGetPhoneVerification;//输入验证码

    @ViewById(R.id.get_verification)
    Button mGetVerification; //获取验证码

    private UcService mUcService;
    private boolean isFirstEnter = true;


    @AfterViews
    public void showKeyBoard() {
        addAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isFirstEnter) {
                    ManyiUtils.showKeyBoard(getActivity(), mChangeNewPhoneNumber);
                    isFirstEnter = false ;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }



    @Click(R.id.get_verification)
    @Background
    public void getVerification(){

        if (CheckDoubleClick.isFastDoubleClick())
            return;
        if (!phonenumValidate()) {
            return;
        }
        onSendSMSSuccess();
    }



    @UiThread
    public void onSendSMSSuccess() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        mGetVerification.setEnabled(false);
        DialogBuilder.showSimpleDialog("验证码已发送!", getActivity(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timer.start();
                int left = mGetVerification.getPaddingLeft();
                int right = mGetVerification.getPaddingRight();
                mGetVerification.setPadding(left, 0, right, 0);
            }
        });
    }

    CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long arg0) {
            mGetVerification.setText("再次获取(" + arg0 / 1000 + ")秒");
        }

        @Override
        public void onFinish() {
            setTimerViewCanceledState();
        }
    };


    @UiThread
    public void onSendSMSError(String e) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        mGetVerification.setEnabled(true);
        DialogBuilder.showSimpleDialog(e, getActivity());
    }

    private boolean phonenumValidate() {
        if (mChangeNewPhoneNumber.getText().toString().length() == 0) {
            onSendSMSError("凑哦了");
            return false;
        } else if (mChangeNewPhoneNumber.getText().toString().length() < 11) {
            onSendSMSError("手机号码错误");
            return false;
        } else
            return true;
    }




    /**
     * 关闭倒计时，改变时间控件状态
     *
     * @author bestar
     */
    private void setTimerViewCanceledState() {
        mGetVerification.setText(R.string.register_get_captcha);
        mGetVerification.setEnabled(true);
        int left = mGetVerification.getPaddingLeft();
        int right = mGetVerification.getPaddingRight();
        mGetVerification.setBackgroundResource(R.drawable.start_selector);
        mGetVerification.setPadding(left, 0, right, 0);
    }

    @UiThread
    public void releaseSuccess(String message) {
        if (this.getActivity() == null || this.getActivity().isFinishing()) {
            return;
        }
        DialogBuilder.showSimpleDialog(message, getBackOpActivity(),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CheckDoubleClick.isFastDoubleClick()) {
                    return;
                }

                notifySelected(null);
            }
        });
    }

    @Click(R.id.new_phone_back)
    public void onBack(){
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        remove();
    }
}
