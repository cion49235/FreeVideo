package link.app.byunm.Util;

import android.app.Activity;
import android.widget.Toast;

import link.app.byunm.R;


public class BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;
    private boolean rtf;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public boolean onBackPressed() {
        rtf = false;

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            rtf = false;
            return rtf;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {

//            activity.finish();
            toast.cancel();
            rtf = true;
        }
        return rtf;
    }

    public void showGuide() {
        toast = Toast.makeText(activity, activity.getString(R.string.backpress), Toast.LENGTH_SHORT);
        toast.show();
    }

}
