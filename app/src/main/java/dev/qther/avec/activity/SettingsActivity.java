package dev.qther.avec.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import dev.qther.avec.R;
import dev.qther.avec.util.Globals;
import dev.qther.avec.util.Preferences;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_settings);

        Preferences p = Globals.pref;

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        // Right handed mode
        Switch rightHandedSwitch = findViewById(R.id.right_handed_switch);
        rightHandedSwitch.setChecked(p.isRightHanded());
        rightHandedSwitch.setOnClickListener(v -> p.setRightHanded(rightHandedSwitch.isChecked()));
    }
}
