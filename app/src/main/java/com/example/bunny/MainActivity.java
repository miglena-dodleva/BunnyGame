package com.example.bunny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // elements
    private TextView scoreLabel, startLabel;
    private ImageView bunny, carrot, wolf, cabbage;

    // size
    private int screenWidth;
    private int frameHeight;
    private int bunnySize;

    //position
    private float bunnyY;
    private float carrotX, carrotY;
    private float cabbageX, cabbageY;
    private float wolfX, wolfY;

    //speed
    private int bunnySpeed, carrotSpeed, cabbageSpeed, wolfSpeed;

    // score
    private int score;

    // timer
    private Timer  timer = new Timer();
    private Handler handler = new Handler();

    // status
    private boolean action_flg = false;
    private boolean start_flg = false;

    // soundPlayer
    private SoundPlayer soundPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundPlayer = new SoundPlayer(this);

        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        bunny = findViewById(R.id.bunny);
        carrot = findViewById(R.id.carrot);
        cabbage = findViewById(R.id.cabbage);
        wolf = findViewById(R.id.wolf);

        // screen size
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        int screenHeight = size.y;



        // initial positions
        carrot.setX(-80.0f);
        carrot.setY(-80.0f);
        cabbage.setX(-80.0f);
        cabbage.setY(-80.0f);
        wolf.setX(-80.0f);
        wolf.setY(-80.0f);

        //scoreLabel.setText("Score : " + score);
        scoreLabel.setText(getString(R.string.score, score));
    }

    public void changePos(){

        hitCheck();

        // carrot
        carrotX -= 12;
        if (carrotX < 0 ){
            carrotX = screenWidth + 20;
            carrotY = (float) Math.floor(Math.random()* (frameHeight - carrot.getHeight()));
        }
        carrot.setX(carrotX);
        carrot.setY(carrotY);

        // wolf
        wolfX -= 16;
        if (wolfX < 0){
            wolfX = screenWidth + 10;
            wolfY = (float) Math.floor(Math.random()* (frameHeight - wolf.getHeight()));
        }
        wolf.setX(wolfX);
        wolf.setY(wolfY);

        // cabbage
        cabbageX -= 20;
        if (cabbageX < 0) {
            cabbageX = screenWidth + 2000;
            cabbageY = (float) Math.floor(Math.random()* (frameHeight - cabbage.getHeight()));
        }
        cabbage.setX(cabbageX);
        cabbage.setY(cabbageY);


        // bunny
        if (action_flg){
            // touching
            bunnyY -= 18;
        }else {
            // releasing
            bunnyY += 18;
        }

        if (bunnyY < 0 ) bunnyY = 0;
        if (bunnyY > frameHeight - bunnySize ) bunnyY = frameHeight - bunnySize;

        bunny.setY(bunnyY);

        //scoreLabel.setText("Score : " + score);
        scoreLabel.setText(getString(R.string.score, score));
    }

    public void hitCheck(){

        // carrot
        float carrotCenterX = carrotX + carrot.getWidth() / 2.0f;
        float carrotCenterY = carrotY + carrot.getHeight() / 2.0f;

        if ( 0 <= carrotCenterX && carrotCenterX <= bunnySize &&
                bunnyY <= carrotCenterY && carrotCenterY <= bunnyY + bunnySize) {
            carrotX = -100.0f;
            score += 10;
            soundPlayer.playCollectSound();
        }

        // cabbage
        float cabbageCenterX = cabbageX + cabbage.getWidth() / 2.0f;
        float cabbageCenterY = cabbageY + cabbage.getHeight() / 2.0f;

        if ( 0 <= cabbageCenterX && cabbageCenterX <= bunnySize &&
                bunnyY <= cabbageCenterY && cabbageCenterY <= bunnyY + bunnySize) {
            cabbageX = -100.0f;
            score += 30;
            soundPlayer.playCollectSound();
        }

        //  wolf
        float wolfCenterX = wolfX + wolf.getWidth() / 2.0f;
        float wolfCenterY = wolfY + wolf.getHeight() / 2.0f;

        if ( 0 <= wolfCenterX && wolfCenterX <= bunnySize &&
                bunnyY <= wolfCenterY && wolfCenterY <= bunnyY + bunnySize) {

            soundPlayer.playOverSound();

            // GAME OVER !!!
            if (timer != null){
                timer.cancel();
                timer = null;
            }

            // show ResultActivity
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);

        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if (!start_flg){
            start_flg = true;

            // frameHeight
            FrameLayout frameLayout = findViewById(R.id.frame);
            frameHeight = frameLayout.getHeight();

            //bunny
            bunnyY = bunny.getY();
            bunnySize = bunny.getHeight();

            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> changePos());
                }
            }, 0, 20);

        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;

            } else if (event.getAction() == MotionEvent.ACTION_UP){
                action_flg = false;
            }


        }


        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {}
}