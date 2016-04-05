package tieorange.edu.beamprojectorrunner;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.dd.morphingbutton.MorphingButton;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private EditText mUiIP1;
    private EditText mUiIP2;

    private EditText mUiEtIP;
    private static String TAG = "MY_TAG";
    private String ip_address = "192.168.0.28";
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSharedPreferences = getPreferences(MODE_PRIVATE);

        // restore ip address 1
        ip_address = mSharedPreferences.getString(getString(R.string.ip_address1), "127.0.0.1");

        mUiEtIP = (EditText) findViewById(R.id.etIpAddress1);
        mUiEtIP.setText(ip_address);

        final MorphingButton btnMorphSimple = (MorphingButton) findViewById(R.id.btnMorphSimple);
        btnMorphSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MorphButton.onMorphButton1ClickedSimple(MainActivity.this, btnMorphSimple);
            }

        });

        MorphButton.morphToSquare(btnMorphSimple, 1, this);

        // setup ip edit texts:
        // setup edit text (ip)
        mUiIP1 = (EditText) findViewById(R.id.main_ip1);
        mUiIP2 = (EditText) findViewById(R.id.main_ip2);

        mUiIP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >= 3){
                    // TODO: focus on the next edit text
                    mUiIP2.requestFocus();
                }
            }
        });
    }

    public void runSSHCommand() {
        ip_address = mUiEtIP.getText().toString();
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    ip_address =
                            executeRemoteCommand("root", "admin", ip_address, 22);
                    Log.d(TAG, "after executeRemoteCommand()");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(1);
        Log.d(TAG, "after }.execute(1);");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called with");
        saveValue();
    }

    public void saveValue() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(getString(R.string.ip_address1), mUiEtIP.getText().toString());
        editor.apply();
    }

    public static String executeRemoteCommand(String username, String password, String hostname, int port)
            throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        // SSH Channel
        ChannelExec channelssh = (ChannelExec)
                session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        String command = "input keyevent 82\n" +
                "sleep 1\n" +
                "am start -n com.spac.projectorgalaxybeamtoggle/.MainActivity\n" +
                "sleep 1\n" +
                "input keyevent 4\n" +
                "sleep 1\n" +
                "am start -a android.intent.action.VIEW -d https://web.facebook.com/Sieci-Urządzeń-Mobilnych-211004225604000/\n" +
                "\n";

        String commandVibration =
                "input keyevent 4\n" +
                        "am start -n tieorange.edu.vibrator/.MainActivity\n" +
                        "sleep 2\n" +
                        "input keyevent 4\n";

        String commandMacbookProSecondRaw = "input tap 421 267\n";
        String commandMacbookProFirstRaw = "input tap 150 153\n";


        String commandAndrewAndAndrii =
                "input keyevent 82\n" +
                        "sleep 1\n" +
                        "am start -n com.spac.projectorgalaxybeamtoggle/.MainActivity\n" + // start pojector
                        "sleep 1\n" +
                        "input keyevent 4\n" +
                        "sleep 1\n" +
                        "am start -n  com.google.chromeremotedesktop/org.chromium.chromoting.Chromoting\n" + // start Chrome Remote
                        "sleep 1\n" +
                        "content insert --uri content://settings/system --bind name:s:accelerometer_rotation --bind value:i:0\n" + // turn of auto rotate
                        "sleep 1\n" +
                        "content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:1\n" + // force landscape
                        "sleep 1\n" +
                        commandMacbookProSecondRaw + // MacbookPro
                        "sleep 2\n" +
                        "input text \"000000\"\n" + // password
                        "sleep 1\n" +
                        "input keyevent 66\n" + // enter
                        "sleep 2\n" +
                        "input tap 700 90\n" // full screen
//                        "sleep 1\n" +
//                        "sh /sdcard/sendevent_input4.sh\n"
                ;


        // Execute command
        channelssh.setCommand(commandAndrewAndAndrii);
        Log.d(TAG, "after setCommand()");
        channelssh.connect();
        Log.d(TAG, "after .connect()");

        channelssh.disconnect();
        Log.d(TAG, "after .disconnect()");


        return baos.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
    }


}
