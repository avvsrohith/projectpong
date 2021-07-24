package com.example.pong;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import static com.example.pong.GameLayout.Highscore;

public class MainActivity extends AppCompatActivity {

    Button start,pong;
    ConstraintLayout BG,circlebg;
    int click,play,score;
    Vibrator vibrator;
    public static SoundPool sounds;
    public static Boolean VS_CPU=false,Hard=false,dark=false,soundfx=true,vibration=true;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch Switch_Difficulty,Switch_Mode,Switch_Hiscore,Switch_Theme,Switch_vib,Switch_Sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start=findViewById(R.id.startbtn);
        pong=findViewById(R.id.pong);
        BG=findViewById(R.id.mainbg);
        Switch_Difficulty=findViewById(R.id.Switch_difficulty);
        Switch_Mode=findViewById(R.id.Switch_Mode);
        Switch_Hiscore=findViewById(R.id.Switch_Highscore);
        Switch_Theme=findViewById(R.id.Switch_Theme);
        Switch_vib=findViewById(R.id.Switch_Vibraton);
        Switch_Sound=findViewById(R.id.Switch_Sound);
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        sounds=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        click=sounds.load(this,R.raw.buttonclick,1);
        play=sounds.load(this,R.raw.playfx,1);
        changetheme();
        updateButtons();
        SharedPreferences prefs=getSharedPreferences("Sharedprefs",MODE_PRIVATE);
        Highscore=prefs.getInt("Highscore",0);
        {
            start.setOnClickListener(v -> {
                YoYo.with(Techniques.Tada).duration(700).playOn(start);
                if (soundfx)
                    sounds.play(play, 1, 1, 1, 0, 1);
                if (vibration)
                    vibrator.vibrate(100);
                startgame();
            });
            pong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.Tada).duration(1000).playOn(pong);
                }
            });

            Switch_Mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ClickSound();
                    vibrateonclick();
                    Animate(Switch_Mode);
                    VS_CPU = isChecked;
                    updateButtons();
                }
            });
            Switch_Difficulty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ClickSound();
                    vibrateonclick();
                    Animate(Switch_Difficulty);
                    Hard = isChecked;
                    updateButtons();
                }
            });
            Switch_Theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ClickSound();
                    vibrateonclick();
                    Animate(Switch_Theme);
                    dark = isChecked;
                    changetheme();
                    updateButtons();
                }
            });
            Switch_Sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    vibrateonclick();
                    Animate(Switch_Sound);
                    if (isChecked) {
                        soundfx = false;
                    } else {
                        soundfx = true;
                        ClickSound();
                    }
                    updateButtons();
                }
            });
            Switch_vib.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ClickSound();
                    Animate(Switch_vib);
                    if (isChecked) {
                        vibration = false;
                    } else {
                        vibration = true;
                        vibrateonclick();
                    }
                    updateButtons();
                }
            });

            Switch_Hiscore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    vibrateonclick();
                    ClickSound();
                    Animate(Switch_Hiscore);
                    if (isChecked) {
                        Switch_Hiscore.setText(String.valueOf(Highscore));

                    } else {
                        Switch_Hiscore.setText("HI Score");
                    }
                }
            });

        }
    }

    public void startgame(){
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("CPU",VS_CPU);
        intent.putExtra("Hard",Hard);
        intent.putExtra("Dark", dark);
        intent.putExtra("Sound", soundfx);
        intent.putExtra("Vibration", vibration);
        CountDownTimer timer;
        timer=new CountDownTimer(900,100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }.start();

    }

    public void changetheme(){
        int darkcolour =Color.parseColor("#282828");
        Window window =getWindow();
        if(dark){
            BG.setBackgroundResource(R.drawable.dark_background);
            pong.setBackgroundResource(R.drawable.dark_buttonbg);
            pong.setTextColor(darkcolour);
            Switch_Theme.setBackgroundResource(R.drawable.dark_buttonbg);
            Switch_Mode.setBackgroundResource(R.drawable.dark_buttonbg);
            Switch_Difficulty.setBackgroundResource(R.drawable.dark_buttonbg);
            Switch_Hiscore.setBackgroundResource(R.drawable.dark_buttonbg);
            Switch_Sound.setBackgroundResource(R.drawable.dark_buttonbg);
            Switch_vib.setBackgroundResource(R.drawable.dark_buttonbg);
            Switch_Theme.setTextColor(darkcolour);
            Switch_Difficulty.setTextColor(darkcolour);
            Switch_Mode.setTextColor(darkcolour);
            Switch_Hiscore.setTextColor(darkcolour);
            Switch_Sound.setTextColor(darkcolour);
            Switch_vib.setTextColor(darkcolour);
            start.setBackgroundResource(R.drawable.dark_buttonbg);
            start.setTextColor(darkcolour);
            window.setStatusBarColor(darkcolour);
        }
        else{
            BG.setBackgroundResource(R.drawable.lightbg);
            pong.setBackgroundResource(R.drawable.buttonbglight);
            pong.setTextColor(Color.parseColor("#DFDFDF"));
            Switch_Theme.setBackgroundResource(R.drawable.buttonbglight);
            Switch_Mode.setBackgroundResource(R.drawable.buttonbglight);
            Switch_Difficulty.setBackgroundResource(R.drawable.buttonbglight);
            Switch_Hiscore.setBackgroundResource(R.drawable.buttonbglight);
            Switch_Sound.setBackgroundResource(R.drawable.buttonbglight);
            Switch_vib.setBackgroundResource(R.drawable.buttonbglight);
            start.setBackgroundResource(R.drawable.buttonbglight);
            Switch_Theme.setTextColor(Color.parseColor("#DFDFDF"));
            Switch_Difficulty.setTextColor(Color.parseColor("#DFDFDF"));
            Switch_Mode.setTextColor(Color.parseColor("#DFDFDF"));
            Switch_Hiscore.setTextColor(Color.parseColor("#DFDFDF"));
            Switch_Sound.setTextColor(Color.parseColor("#DFDFDF"));
            Switch_vib.setTextColor(Color.parseColor("#DFDFDF"));
            start.setTextColor(Color.parseColor("#DFDFDF"));
            window.setStatusBarColor(Color.parseColor("#DFDFDF"));
        }
    }

    public void updateButtons(){
        if(VS_CPU){
            Switch_Mode.setText("VS CPU");
        }
        else{
            Switch_Mode.setText("VS Wall");
        }

        if(Hard){
            Switch_Difficulty.setText("Hard");
        }
        else{
            Switch_Difficulty.setText("Easy");
        }

        if(dark){
            Switch_Theme.setText("Theme\nDark");
        }
        else{
            Switch_Theme.setText("Theme\nLight");
        }

        if(!soundfx){
            Switch_Sound.setText("Sound\nOff");
        }
        else{
            Switch_Sound.setText("Sound\nOn");
        }

        if(!vibration){
            Switch_vib.setText("Vibration\nOff");
        }
        else{
            Switch_vib.setText("Vibration\nOn");
        }

    }

    public void Animate(Switch selected){

        YoYo.with(Techniques.FlipInY).duration(1000).playOn(selected);
    }

    public void ClickSound(){
        if(soundfx)
        sounds.play(click,5,5,1,0,1);
    }

    public void vibrateonclick(){
        if(vibration)
        vibrator.vibrate(100);
    }



}