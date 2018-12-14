package link.app.byunm.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import link.app.byunm.R;


public class DialogCloseMain extends Dialog implements View.OnClickListener {
    Context context;
    private ImageView bt_close;
    private ImageButton ib_close;
    private ImageView bt_finish;
    Activity activity;
    CircleViewFlipper vfPop;
    View[] page;
    JsonClient client;
    String[] apUrl;
    int m_nPreTouchPosX = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_close_main);
        client = JsonClient.getInstance(context);
        setLayoutDialog();
        init_ui();
        do_close_banner();

    }

    private void do_close_banner(){
        try{
            // 팝업 정보 가져오기
            client.init(R.string.close_popup, "");
            client.post(new BaseResponse<ClosePopupList>(context) {
                public void onResponse(ClosePopupList response) {
                    LogHelper.debug(this, response.toString());
                    if (!response.code.equals("000")) {
                        Toast.makeText(context, response.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    apUrl = new String[response.list.size()];
                    page = new View[response.list.size()];

                    LayoutInflater inflater = getLayoutInflater();

                    //page[1] = inflater.inflate(R.layout.view_close_popup01, null);

                    for (int i=0; i<response.list.size(); i++) {

                        ClosePopupItem closePopupItem = response.list.get(i);

                        apUrl[i] = closePopupItem.ap_url_link;

                        //UtilHelper.getInstance().loadImage(ClosePopupActivity.this, ivPop[i],closePopupItem.ap_image_link);
                        //Glide.with(ClosePopupActivity.this).load("http://modoostar.com"+closePopupItem.ap_image_link).priority(Priority.HIGH).into(ivPop[i]);
                        ImageView imageView = new ImageView(context);

                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    /*imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    Glide.with(ClosePopupActivity.this).load("http://modoostar.com"+closePopupItem.ap_image_link).priority(Priority.HIGH).into(imageView);
                    vfPop.addView(imageView);*/

                        page[i] = inflater.inflate(R.layout.view_close_popup01, null);
                        vfPop.addView(page[i]);
                        ImageButton ibPop = (ImageButton) page[i].findViewById(R.id.imageButton);
                        ibPop.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if ( event.getAction() == MotionEvent.ACTION_DOWN ) {
                                    m_nPreTouchPosX = (int) event.getX();
                                }

                                if ( event.getAction() == MotionEvent.ACTION_UP ) {
                                    int nTouchPosX = (int) event.getX();

                                    if ( nTouchPosX + 200 < m_nPreTouchPosX ) {
                                        moveNextView();
                                    } else if ( nTouchPosX - 200 > m_nPreTouchPosX ) {
                                        movePreviousView();
                                    } else {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(apUrl[vfPop.indexOfChild(vfPop.getCurrentView())]));
                                        context.startActivity(intent);
                                    }

                                    m_nPreTouchPosX = nTouchPosX;
                                }
                                return true;
                            }
                        });
                        Glide.with(context).load(closePopupItem.ap_image_link).priority(Priority.HIGH).into(ibPop);
                    }
                }
            });
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    vfPop.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.appear_from_right));
                    vfPop.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.disappear_to_left));
                }
            }, 500);
            vfPop.setFlipInterval(10000);
            vfPop.startFlipping();
        }catch (IllegalArgumentException e){
        }catch (Exception e){
        }
    }

    void moveNextView() {
        vfPop.stopFlipping();
        vfPop.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.appear_from_right));
        vfPop.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.disappear_to_left));
        vfPop.showNext();
        vfPop.startFlipping();
    }

    void movePreviousView() {
        vfPop.stopFlipping();
        vfPop.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.appear_from_left));
        vfPop.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.disappear_to_right));
        vfPop.showPrevious();
        vfPop.startFlipping();
    }

    private void setLayoutDialog() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.6f;
        params.width = (int) (UIUtil.getScreenWidth(context) * 0.8);
        params.height = (int) (UIUtil.getScreenHeight(context) * 0.8);
        getWindow().setAttributes(params);
    }


    private void init_ui(){
        vfPop = (CircleViewFlipper)findViewById(R.id.vfPop);
        vfPop.setHeightRate(0.95);

        bt_close = (ImageView)findViewById(R.id.bt_close);
        bt_close.setOnClickListener(this);
        bt_finish = (ImageView)findViewById(R.id.bt_finish);
        bt_finish.setOnClickListener(this);
        ib_close = (ImageButton)findViewById(R.id.ib_close);
        ib_close.setOnClickListener(this);
//        dialog_close_animation();
    }

    public DialogCloseMain(@NonNull Context context, Activity activity) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        if(view == bt_close){
            dismiss();
        }else if(view == ib_close){
            dismiss();
        }else if(view == bt_finish){
            Intent intent = new Intent();
            activity.setResult(Activity.RESULT_OK,intent);
            activity.finish();
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
        }
    }
}