package com.example.pong;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import static android.content.Context.VIBRATOR_SERVICE;
import static com.example.pong.GameActivity.Gametv;
import static com.example.pong.GameActivity.Scoretv;
import static com.example.pong.GameActivity.cpuscoretv;
import static com.example.pong.GameActivity.messagetv;
import static com.example.pong.GameActivity.playerscoretv;
import static com.example.pong.GameActivity.restart;
import static com.example.pong.GameActivity.scorecard;
import static com.example.pong.MainActivity.Hard;
import static com.example.pong.MainActivity.VS_CPU;
import static com.example.pong.MainActivity.dark;
import static com.example.pong.MainActivity.soundfx;
import static com.example.pong.MainActivity.vibration;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.random;

public class GameLayout extends View {

    Paint color_fill,line_fill;
    public static SoundPool sounds;
    int circle_x, circle_y, Radius, x_dir, y_dir, Height, Width, topleft_x, bottomright_x, center_x, center_y, length, c = 0, bounce,gameover, width, speed, b = 0, topleft_cpu, bottomright_cpu,center_cpu,color;
    public static int Score_player = 0,Score_CPU=0,Highscore;
    public String[] Message={"This is good, but there's always better","Let me guess, A whole number?","Table tennis ,but online","A wall is just a wall,play with me","Hmm,training against wall helped","Congratulations, You've lost to a machine"};
    public static Intent intent;
    CountDownTimer timer;
    Boolean longbat=false,largeball=false,life=false,go=false;
    Vibrator vibrator;


    public GameLayout(Context context) {
        super(context);
        Radius = (30);
        length = 250;
        vibrator=(Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        width = 60;
        Score_player=0;
        Score_CPU=0;
        speed = 20;
        sounds = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        bounce = sounds.load(context, R.raw.bounce, 1);
        gameover = sounds.load(context, R.raw.gameover, 1);
        do {
            x_dir = randbw(-10, 10);
            y_dir = randbw(1, 10);
        } while (x_dir == 0 || y_dir == 0);
    }

    public GameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Radius = (30);
        length = 250;
        width = 60;
        vibrator=(Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        Score_player=0;
        Score_CPU=0;
        speed = 20;
        sounds = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        bounce = sounds.load(context, R.raw.bounce, 1);
        gameover = sounds.load(context, R.raw.gameover, 1);
        do {
            x_dir = randbw(-10, 10);
            y_dir = randbw(1, 10);
        } while (x_dir == 0 || y_dir == 0);
    }

    public GameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Radius=(30);
        length=250;
        vibrator=(Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        Score_player=0;
        Score_CPU=0;
        width=60;
        speed =20;
        sounds=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        bounce=sounds.load(context,R.raw.bounce,1);
        gameover = sounds.load(context, R.raw.gameover, 1);
        do {
            x_dir = randbw(-10, 10);
            y_dir = randbw(1,10);
        }while (x_dir==0 || y_dir==0);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        brushes();
        Height = getHeight()-150;
        Width = getWidth();
        if (c == 0) {
            go=false;
            Gametv.setText("Ready");
            timer=new CountDownTimer(3000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if(millisUntilFinished<=1000){
                        Gametv.setText("Go");
                    }
                }

                @Override
                public void onFinish() {
                    Gametv.setText(null);
                    go=true;
                }
            }.start();
            circle_x = (Width/2);
            circle_y = (Height/2);
            topleft_x = (Width / 2) - (length / 2);
            bottomright_x = (Width / 2) + (length / 2);
            center_x = (topleft_x + bottomright_x) / 2;
            center_y = 45;
            center_cpu=Width/2;
            if(VS_CPU && Score_CPU==0){
                cpuscoretv.setText("Score");
            }
        }
        topleft_cpu=center_cpu-(125);
        bottomright_cpu=center_cpu+(125);

        canvas.drawRoundRect(topleft_x, Height - width - 15, bottomright_x, Height - 15, 30, 30, color_fill);
        if(VS_CPU) {
            canvas.drawRoundRect(topleft_cpu, 15, bottomright_cpu, 15 + width, 30, 30, color_fill);
            moveCPU();
        }
        else{
            canvas.drawRect(0,0,Width,50,color_fill);
        }
        if(go){
            canvas.drawCircle(circle_x, circle_y, Radius, color_fill);
            moveCircle(speed);}
        Powerups();

        if(!VS_CPU && circle_y>=Height+150+Radius+30) {
            if(!life) {
                Vswall_endgame();
            }
            else {
                lifeUsed();
            }
        }
        else if(VS_CPU && (circle_y>=Height+150+Radius+30 || circle_y<=-30)){
            if(!life){
                VsCPU_endgame();
            }
            else{
                lifeUsed();
            }
        }
        else{
            c++;
            invalidate();}
        }

        public boolean onTouchEvent (MotionEvent motionEvent){
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                int touched_x = (int) motionEvent.getX();
                if (touched_x <= length / 2) {
                    topleft_x = 0;
                    bottomright_x = length;
                } else if (touched_x >= Width - (length / 2)) {
                    topleft_x = Width - length;
                    bottomright_x = Width;
                } else {
                    topleft_x = touched_x - (length / 2);
                    bottomright_x = touched_x + (length / 2);
                }

            }
            return true;
        }

        public void moveCircle ( int velocity){
            if (circle_x >= Width - Radius && circle_y<=Height && circle_y>=0) {
                x_dir = abs(x_dir) * -1;
                BounceSound();
            }
            if (circle_x <= Radius && circle_y<=Height && circle_y>=0) {
                x_dir = Math.abs(x_dir);
                BounceSound();
            }
            if(!VS_CPU){
                if(circle_y<=Radius+50) {
                    y_dir = abs(y_dir);
                    BounceSound();
                }
            }
            if (Player_Collision()) {
                y_dir = Math.abs(y_dir) * -1;
                Vibrate(false);
                BounceSound();
                Score_player++;
                playerscoretv.setText(String.valueOf(Score_player));
                b++;
                if (b % 3 == 0 && Hard) {
                    speed += 2;
                }
                CheckPowerups();
            }

            if (VS_CPU && CPU_Collision()) {
                y_dir = Math.abs(y_dir);
                Vibrate(false);
                BounceSound();
                Score_CPU++;
                cpuscoretv.setText(String.valueOf(Score_CPU));
            }
            circle_x += (x_dir / Math.sqrt((pow(x_dir, 2) + pow(y_dir, 2)))) * velocity;
            circle_y += (y_dir / Math.sqrt((pow(x_dir, 2) + pow(y_dir, 2)))) * velocity;
        }

        public void moveCPU() {
        int speedCPU;
        int gap=40;
        if(Hard){
            speedCPU=speed;
        }
        else {
            speedCPU=9;
        }
        if (circle_y <= Height / 2 && !(circle_x>topleft_cpu+gap && circle_x<bottomright_cpu-gap) && y_dir<0 ) {
            if(center_cpu<circle_x && circle_x<Width-Radius && circle_x>Radius){
                center_cpu+=speedCPU;
            }
            else if (center_cpu>circle_x && circle_x<Width-Radius && circle_x>Radius){
                center_cpu-=speedCPU;
            }
        }

    }

        public void CheckPowerups(){
            if(b%5==0){
                int select;
                int timeleft=Hard ? 5000:10000;
                int check=randbw(0,2);
                if(check==1){
                    if(!life){
                     select=randbw(1,6); }
                    else{
                        select=randbw(1,5);
                    }

                    switch (select){
                        case 1:
                        case 2:
                            longbat=true;
                            Gametv.setText("Large Bat");
                            animateGametv(true);
                            timer=new CountDownTimer(timeleft,100) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                }

                                @Override
                                public void onFinish() {
                                    longbat=false;
                                    animateGametv(false);
                                    Gametv.setText(null);
                                    }
                                }.start();
                            break;

                        case 3:
                        case 4:
                            largeball=true;
                            Gametv.setText("Large Ball");
                            animateGametv(true);
                            timer=new CountDownTimer(timeleft,100) {
                                @Override
                                public void onTick(long millisUntilFinished) { }
                                @Override
                                public void onFinish() {
                                    largeball=false;
                                    animateGametv(false);
                                    Gametv.setText(null);
                                }
                            }.start();
                            break;

                        case 5:
                                life = true;
                                animateGametv(true);
                                Gametv.setText("Extra life");
                                timer = new CountDownTimer(3000, 100) {
                                    @Override
                                    public void onTick(long millisUntilFinished) { }
                                    @Override
                                    public void onFinish() {
                                        animateGametv(false);
                                        Gametv.setText(null);
                                    }
                                }.start();
                            }


                    }
                }
            }

        public void Powerups(){
        if(longbat && length<400){
            length+=1;
        }
        if(!longbat && length>250){
            length-=1;
        }
        if(largeball && Radius<70){
            Radius+=1;
        }
        if(!largeball && Radius>30){
            Radius-=1;
        }
        }

        public void lifeUsed(){
            c=0;
            life=false;
            animateGametv(true);
            Gametv.setText("Life used");
            timer=new CountDownTimer(2000,100) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    animateGametv(false);
                    Gametv.setText(null);
                    invalidate();
                }
            }.start();

        }

        public boolean Player_Collision () {
            return (circle_y >= Height - 75 - Radius && circle_y<=Height-15 && circle_x >= topleft_x && circle_x <= bottomright_x && y_dir>0);
        }

        public boolean CPU_Collision () {
            return (circle_y <= 75 + Radius && circle_y>=0 && circle_x >= topleft_cpu && circle_x <= bottomright_cpu && y_dir<0);
        }

        public void Vswall_endgame(){
        Gametv.setText("Game Over");
        Vibrate(true);
        if(soundfx)
            sounds.play(gameover,5,5,1,0,1);
        animateGametv(true);
        timer=new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                scorecard.setVisibility(VISIBLE);
                Scoretv.setText(String.valueOf(Score_player));
                messagetv.setText(Message[randbw(0,4)]);
                UpdateHS();
                YoYo.with(Techniques.BounceIn).duration(700).playOn(scorecard);
            }
        }.start();
        restart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
            }
        });
    }

        public void VsCPU_endgame(){
        String winner = circle_y<0 ? "You Won" : "CPU Won";
        String message=circle_y<0 ? Message[4]: Message[5];
        Gametv.setText(winner);
        Vibrate(true);
        if(soundfx)
            sounds.play(gameover,1,1,1,0,1);
        animateGametv(true);
        timer=new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                scorecard.setVisibility(VISIBLE);
                Scoretv.setText(String.valueOf(Score_player));
                messagetv.setText(message);
                UpdateHS();
                YoYo.with(Techniques.BounceIn).duration(700).playOn(scorecard);
            }
        }.start();
        restart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
            }
        });
    }

        public void restart(){
        UpdateHS();
        c=0;
        Score_player=0;
        Score_CPU=0;
        speed=20;
        playerscoretv.setText("Score");
        String CPUscore = VS_CPU ? "Score" :null;
        cpuscoretv.setText(CPUscore);
        scorecard.setVisibility(INVISIBLE);
        Gametv.setText(null);
        invalidate();
    }

        public void UpdateHS(){
        if(Score_player>Highscore){
            Highscore=Score_player;
            SharedPreferences prefs= getContext().getSharedPreferences("Sharedprefs",Highscore);
            SharedPreferences.Editor highscoreeditor=prefs.edit();
            highscoreeditor.putInt("Highscore",Highscore);
            highscoreeditor.apply();
        }

    }

        public void animateGametv(Boolean in){
            if(in){
                YoYo.with(Techniques.FadeIn).duration(700).playOn(Gametv);
            }
            else{
                YoYo.with(Techniques.FadeOut).duration(700).playOn(Gametv);
            }
        }

        public void BounceSound () {
        if (soundfx)
            sounds.play(bounce, 1, 1, 1, 0, 1);
    }

        public void Vibrate(Boolean Long){
        if(vibration){
            int time=Long ? 500:40;
            vibrator.vibrate(time);}
    }

        public void brushes () {
        color=dark ? Color.parseColor("#DFDFDF"):Color.parseColor("#282828");
        color_fill = new Paint();
        color_fill.setColor(color);
        color_fill.setStyle(Paint.Style.FILL);

    }

        public int randbw ( int start, int end){
            return start + (int) (random() * (end - start));
        }


    }
