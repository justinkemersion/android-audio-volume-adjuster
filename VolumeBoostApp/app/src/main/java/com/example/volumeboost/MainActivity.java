package com.example.volumeboost;

import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Equalizer equalizer;
    private boolean isBoosted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        Button toggleButton = new Button(this);
        toggleButton.setText("Toggle Volume Boost");
        layout.addView(toggleButton);

        TextView faderLabel = new TextView(this);
        faderLabel.setText("Quietness: 0%");
        layout.addView(faderLabel);

        SeekBar fader = new SeekBar(this);
        fader.setMax(100);
        layout.addView(fader);

        setContentView(layout);

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
                    Toast.makeText(this, "Boost ON", Toast.LENGTH_SHORT).show();
                    isBoosted = true;
                } else {
                    equalizer.setEnabled(false);
                    Toast.makeText(this, "Boost OFF", Toast.LENGTH_SHORT).show();
                    isBoosted = false;
                }
            });

            fader.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (isBoosted) {
                        equalizer.setEnabled(false);
                        isBoosted = false;
                        Toast.makeText(MainActivity.this, "Boost OFF", Toast.LENGTH_SHORT).show();
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
                    faderLabel.setText("Quietness: " + progress + "%");
                }

                @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        } else {
            toggleButton.setEnabled(false);
            Toast.makeText(this, "Equalizer not supported on this device", Toast.LENGTH_LONG).show();
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