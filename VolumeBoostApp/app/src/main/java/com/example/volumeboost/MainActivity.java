package com.example.volumeboost;

import android.media.audiofx.Equalizer;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Equalizer equalizer;
    private boolean isBoosted = false;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getPreferences(MODE_PRIVATE);

        Button toggleButton = findViewById(R.id.toggle_boost);
        TextView faderLabel = findViewById(R.id.quietness_label);
        SeekBar fader = findViewById(R.id.quietness_fader);

        toggleButton.setText(isBoosted
                ? getString(R.string.disable_boost)
                : getString(R.string.enable_boost));

        int savedProgress = prefs.getInt("quietness_progress", 0);
        fader.setProgress(savedProgress);
        faderLabel.setText(getString(R.string.quietness_template, savedProgress));

        try {
            equalizer = new Equalizer(0, 0);
        } catch (Exception e) {
            equalizer = null;
        }
        if (equalizer != null) {
            equalizer.setEnabled(false);
            toggleButton.setOnClickListener(v -> {
                if (!isBoosted) {
                    short bands = equalizer.getNumberOfBands();
                    for (short i = 0; i < bands; i++) {
                        equalizer.setBandLevel(i, (short) equalizer.getBandLevelRange()[1]);
                    }
                    equalizer.setEnabled(true);
                    Toast.makeText(this, R.string.boost_on_toast, Toast.LENGTH_SHORT).show();
                    toggleButton.setText(R.string.disable_boost);
                    isBoosted = true;
                } else {
                    equalizer.setEnabled(false);
                    Toast.makeText(this, R.string.boost_off_toast, Toast.LENGTH_SHORT).show();
                    toggleButton.setText(R.string.enable_boost);
                    isBoosted = false;
                }
            });

            fader.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (isBoosted) {
                        equalizer.setEnabled(false);
                        isBoosted = false;
                        Toast.makeText(MainActivity.this, R.string.boost_off_toast, Toast.LENGTH_SHORT).show();
                        toggleButton.setText(R.string.enable_boost);
                    }
                    if (progress == 0) {
                        equalizer.setEnabled(false);
                    } else {
                        short minLevel = equalizer.getBandLevelRange()[0];
                        short bands = equalizer.getNumberOfBands();
                        short attenuation = (short) (minLevel * progress / 100);
                        for (short i = 0; i < bands; i++) {
                            equalizer.setBandLevel(i, attenuation);
                        }
                        equalizer.setEnabled(true);
                    }
                    faderLabel.setText(getString(R.string.quietness_template, progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    prefs.edit().putInt("quietness_progress", seekBar.getProgress()).apply();
                }
            });
        } else {
            toggleButton.setEnabled(false);
            Toast.makeText(this, R.string.equalizer_not_supported_toast, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (equalizer != null) {
            equalizer.release();
        }
    }
}