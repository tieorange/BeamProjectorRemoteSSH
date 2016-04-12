package tieorange.edu.beamprojectorrunner;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;
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
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //region Fields

    private final static String TAG = "MainActivity";

    @Bind(R.id.ip1_et1)
    public EditText mUiIp1Et1;
    @Bind(R.id.ip1_et2)
    public EditText mUiIp1Et2;
    @Bind(R.id.ip1_et3)
    public EditText mUiIp1Et3;
    @Bind(R.id.ip1_et4)
    public EditText mUiIp1Et4;

    @Bind(R.id.ip2_et1)
    public EditText mUiIp2Et1;
    @Bind(R.id.ip2_et2)
    public EditText mUiIp2Et2;
    @Bind(R.id.ip2_et3)
    public EditText mUiIp2Et3;
    @Bind(R.id.ip2_et4)
    public EditText mUiIp2Et4;

    private String ip_address1 = "";
    private String ip_address2 = "";
    private SharedPreferences mSharedPreferences;

    //endregion Fields

    //region Activity methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mSharedPreferences = getPreferences(MODE_PRIVATE);

        ip_address1 = mSharedPreferences.getString(string(R.string.ip_address1), "127.0.0.1");
        ip_address2 = mSharedPreferences.getString(string(R.string.ip_address2), "127.0.0.1");


        initializeIpAddress1EditTexts(ip_address1);
        initializeIpAddress2EditTexts(ip_address2);

        final MorphingButton btnMorphSimple = (MorphingButton) findViewById(R.id.btnMorphSimple);
        btnMorphSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MorphButton.onMorphButton1ClickedSimple(MainActivity.this, btnMorphSimple);
            }
        });

        MorphButton.morphToSquare(btnMorphSimple, 1, this);

        linkEditTexts(mUiIp1Et1, mUiIp1Et2);
        linkEditTexts(mUiIp1Et2, mUiIp1Et3);
        linkEditTexts(mUiIp1Et3, mUiIp1Et4);
        linkEditTexts(mUiIp1Et4, mUiIp2Et1);

        linkEditTexts(mUiIp2Et1, mUiIp2Et2);
        linkEditTexts(mUiIp2Et2, mUiIp2Et3);
        linkEditTexts(mUiIp2Et3, mUiIp2Et4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called with");
        saveValue();
    }

    //endregion Activity methods

    private void saveValue() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(getString(R.string.ip_address1), getIpAddress1FromEditTexts());
        editor.putString(getString(R.string.ip_address2), getIpAddress2FromEditTexts());
        editor.apply();
    }

    //region IpAddresses Edit Text Methods

    private void initializeIpAddress1EditTexts(String ipAddress) {
        String[] splitIdAddress = ipAddress.split(Pattern.quote("."));
        mUiIp1Et1.setText(splitIdAddress[0]);
        mUiIp1Et2.setText(splitIdAddress[1]);
        mUiIp1Et3.setText(splitIdAddress[2]);
        mUiIp1Et4.setText(splitIdAddress[3]);
    }

    private void initializeIpAddress2EditTexts(String ipAddress) {
        String[] splitIdAddress = ipAddress.split(Pattern.quote("."));
        mUiIp2Et1.setText(splitIdAddress[0]);
        mUiIp2Et2.setText(splitIdAddress[1]);
        mUiIp2Et3.setText(splitIdAddress[2]);
        mUiIp2Et4.setText(splitIdAddress[3]);
    }

    private String getIpAddress1FromEditTexts() {
        StringBuilder sb = new StringBuilder();

        sb.append(mUiIp1Et1.getText().toString() + ".");
        sb.append(mUiIp1Et2.getText().toString() + ".");
        sb.append(mUiIp1Et3.getText().toString() + ".");
        sb.append(mUiIp1Et4.getText().toString());

        return sb.toString();
    }

    private String getIpAddress2FromEditTexts() {
        StringBuilder sb = new StringBuilder();

        sb.append(mUiIp2Et1.getText().toString() + ".");
        sb.append(mUiIp2Et2.getText().toString() + ".");
        sb.append(mUiIp2Et3.getText().toString() + ".");
        sb.append(mUiIp2Et4.getText().toString());

        return sb.toString();
    }

    //endregion IpAddresses Edit Text Methods

    //region IP TextWatcher Listeners

    private void linkEditTexts(final EditText et1, final EditText et2) {

        ShortTextWatcher fromEt1ToEt2 = new ShortTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    et2.requestFocus();
                }
            }
        };

        ShortTextWatcher fromEt2BackToEt1 = new ShortTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && s.length() == 0) {
                    et1.requestFocus();
                }
            }
        };

        et1.addTextChangedListener(fromEt1ToEt2);
        et2.addTextChangedListener(fromEt2BackToEt1);
    }

    private abstract class ShortTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //  empty by design
        }

        @Override
        public void afterTextChanged(Editable s) {
            //  empty by design
        }
    }

    //endregion IP TextWatcher Listeners

    //region SSH methods

    public void runSSHCommand() {
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    executeRemoteCommand("root", "admin", getIpAddress1FromEditTexts(), 22);
                    Log.d(TAG, "after executeRemoteCommand()");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    executeRemoteCommand("root", "admin", getIpAddress2FromEditTexts(), 22);
                    Log.d(TAG, "after executeRemoteCommand()");

                } catch (JSchException jsce) {
                    try {
                        executeRemoteCommand("root", "admin", getIpAddress2FromEditTexts(), 2222);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(1);
        Log.d(TAG, "after }.execute(1);");
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

    //endregion SSH methods

    //region Utility methods

    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
    }

    public String string(@StringRes int resId) {
        return getString(resId);
    }

    //endregion Utility methods

}
