package link.app.byunm.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import link.app.byunm.Fragment.Fragment_settings_UI;
import link.app.byunm.R;

public class Settings_UIActivity extends AppCompatActivity {
    private static final String DB_CHANGE = "DB_CHANGE";
    private final boolean dbChange = false;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper_main.setTheme(this);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new Fragment_settings_UI()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra(DB_CHANGE, dbChange);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        Intent intent = new Intent();
        intent.putExtra(DB_CHANGE, dbChange);
        setResult(Activity.RESULT_OK, intent);
        finish();
        return true;
    }
}
