package com.example.pong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import static com.example.pong.GameLayout.Score_player;
import static com.example.pong.MainActivity.dark;
import static com.example.pong.MainActivity.soundfx;
import static com.example.pong.MainActivity.sounds;

public class GameActivity extends AppCompatActivity {

    public static TextView playerscoretv,cpuscoretv,Gametv,Scoretv,Scoretitle,messagetv;
    public static ConstraintLayout scorecard,scorelayout,background;
    Window window;
    public static Button back,restart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        playerscoretv =findViewById(R.id.Playerscoretv);
        background=findViewById(R.id.background);
        cpuscoretv=findViewById(R.id.CPUScoretv);
        Scoretv=findViewById(R.id.Scoretv);
        Scoretitle=findViewById(R.id.scoretitle);
        messagetv=findViewById(R.id.messagetv);
        Gametv =findViewById(R.id.Gametv);
        back=findViewById(R.id.back);
        restart=findViewById(R.id.restart);
        Gametv.setText(null);
        scorecard=findViewById(R.id.Scorecard);
        scorelayout=findViewById(R.id.scorelayout);
        window =getWindow();
        SoundPool sounds=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        int click=sounds.load(GameActivity.this,R.raw.buttonclick,1);
        GameLayout.intent = getIntent();
        cpuscoretv.setText(null);
        SetTheme();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(soundfx){
                    sounds.play(click,1,1,1,0,1);}
                Mainmenu();
            }
        });


    }

    public void SetTheme(){
        int lightcolour=Color.parseColor("#DFDFDF");
        int darkcolour=Color.parseColor("#282828");
        window.setStatusBarColor(darkcolour);
        if(dark) {
            background.setBackgroundResource(R.drawable.dark_background);
            playerscoretv.setTextColor(Color.parseColor("#80DFDFDF"));
            cpuscoretv.setTextColor(Color.parseColor("#80DFDFDF"));
            scorelayout.setBackgroundResource(R.drawable.cardviewdarkbg);
            Scoretitle.setTextColor(lightcolour);
            Scoretv.setTextColor(lightcolour);
            back.setBackgroundResource(R.drawable.backbtn_light);
            back.setTextColor(darkcolour);
            scorecard.setBackgroundResource(R.drawable.blurbg_dark);
            Gametv.setTextColor(Color.parseColor("#80DFDFDF"));
            restart.setBackgroundResource(R.drawable.backbtn_light);
            restart.setTextColor(darkcolour);
            messagetv.setTextColor(lightcolour);

        }
    }

    public void Mainmenu(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}