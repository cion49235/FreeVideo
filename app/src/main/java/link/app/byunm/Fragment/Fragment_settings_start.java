package link.app.byunm.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import link.app.byunm.Activity.Whitelist_AdBlock;
import link.app.byunm.Activity.Whitelist_Cookie;
import link.app.byunm.Activity.Whitelist_Javascript;
import link.app.byunm.R;
import link.app.byunm.Task.ExportWhitelistAdBlockTask;
import link.app.byunm.Task.ExportWhitelistCookieTask;
import link.app.byunm.Task.ExportWhitelistJSTask;
import link.app.byunm.Task.ImportWhitelistAdBlockTask;
import link.app.byunm.Task.ImportWhitelistCookieTask;
import link.app.byunm.Task.ImportWhitelistJSTask;

public class Fragment_settings_start extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private boolean spChange = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_start);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        sp.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        switch (preference.getTitleRes()) {
            case R.string.setting_title_whitelist:
                Intent toWhitelist = new Intent(getActivity(), Whitelist_AdBlock.class);
                getActivity().startActivity(toWhitelist);
                break;
            case R.string.setting_title_whitelistJS:
                Intent toJavascript = new Intent(getActivity(), Whitelist_Javascript.class);
                getActivity().startActivity(toJavascript);
                break;
            case R.string.setting_title_whitelistCookie:
                Intent toCookie = new Intent(getActivity(), Whitelist_Cookie.class);
                getActivity().startActivity(toCookie);
                break;
            case R.string.setting_title_export_whitelist:
                new ExportWhitelistAdBlockTask(getActivity()).execute();
                break;
            case R.string.setting_title_import_whitelist:
                new ImportWhitelistAdBlockTask(getActivity()).execute();
                break;

            case R.string.setting_title_export_whitelistJS:
                new ExportWhitelistJSTask(getActivity()).execute();
                break;
            case R.string.setting_title_import_whitelistJS:
                new ImportWhitelistJSTask(getActivity()).execute();
                break;
            case R.string.setting_title_export_whitelistCookie:
                new ExportWhitelistCookieTask(getActivity()).execute();
                break;
            case R.string.setting_title_import_whitelistCookie:
                new ImportWhitelistCookieTask(getActivity()).execute();
                break;

            default:
                break;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        spChange = true;
    }
}
