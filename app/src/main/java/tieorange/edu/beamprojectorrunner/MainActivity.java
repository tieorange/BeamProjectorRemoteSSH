package tieorange.edu.beamprojectorrunner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.dd.morphingbutton.MorphingButton;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MY_TAG";
    private int mMorphCounter1 = 1;
    private String ip_address = "192.168.0.21";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final MorphingButton btnMorphSimple = (MorphingButton) findViewById(R.id.btnMorphSimple);
        btnMorphSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMorphButton1ClickedSimple(btnMorphSimple);
            }

        });

        morphToSquare(btnMorphSimple, 1);
    }

    private void onMorphButton1ClickedSimple(MorphingButton btnMorphSimple) {
        if (mMorphCounter1 == 0) {
            mMorphCounter1++;
            morphToSquare(btnMorphSimple, integer(R.integer.mb_animation));
        } else if (mMorphCounter1 == 1) {
            mMorphCounter1 = 0;
            morphToSuccessSimple(btnMorphSimple);

            runSSHCommand();
        }

        morphToSuccessSimple(btnMorphSimple);
    }

    private void morphToSuccessSimple(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer(R.integer.mb_animation))
                .cornerRadius(dimen(R.dimen.mb_height_56) * 2)
                .width(dimen(R.dimen.mb_height_56) * 3)
                .height(dimen(R.dimen.mb_height_56) * 3)
                .color(color(R.color.mb_green))
                .colorPressed(color(R.color.mb_green_dark))
                .icon(R.drawable.ic_projector);
        btnMorph.morph(circle);
    }

    private void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_width_square))
                .height(dimen(R.dimen.mb_height_square))
                .color(color(R.color.mb_blue))
                .colorPressed(color(R.color.mb_blue_dark))
                .text(getString(R.string.mb_button));
        btnMorph.morph(square);
    }

    private void runSSHCommand() {
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

        String commandAndrewAndAndrii =
                "input keyevent 82\n" +
                "sleep 1\n" +
                "am start -n com.spac.projectorgalaxybeamtoggle/.MainActivity\n" +
                "sleep 1\n" +
                "input keyevent 4\n" +
                "sleep 1\n" +
                "am start -n  com.google.chromeremotedesktop/org.chromium.chromoting.Chromoting\n" +
                "sleep 1\n" +
                "input tap 150 153\n" +
                "sleep 1\n" +
                "input text \"000000\"\n" +
                "sleep 1\n" +
                "input keyevent 66\n" +
                "sleep 1\n" +
                "input tap 768 86\n" +
                "sleep 1\n" +
                "sh /sdcard/sendevent_input4.sh\n";


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
