package com.nodrex.connectedworld;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;

import com.nodrex.android.tools.Util;
import com.nodrex.connectedworld.helper.Constants;
import com.nodrex.connectedworld.helper.FPoint;
import com.nodrex.connectedworld.helper.Helper;
import com.nodrex.connectedworld.ui.FABBehavior;
import com.nodrex.connectedworld.ui.NewDevice;
import com.nodrex.connectedworld.ui.RecyclerViewAdapter;
import com.nodrex.connectedworld.unit.Device;
import com.nodrex.connectedworld.unit.GasSensor;
import com.nodrex.connectedworld.unit.LightBulb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean isRenameOpen;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Butler.REQUEST_CODE:
                Butler.handleResult(this,resultCode,data);
                break;
        }
    }

    private int fabState = FABBehavior.FabState.DEFAULT; //0 init condition , 1 searching device , 2 new device
    private Point display;//width and height of display
    private FPoint searchDeviceTranslation;//x and y for fab device search translation animation.
    private FPoint newDeviceTranslation;//x and y for fab new device translation animation.

    private RecyclerView recyclerView;//Main view for displaying devices such as light bulb , or gas sensor.
    private GridLayoutManager gridLayoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    private FloatingActionButton fab;//Floating action button: starts search and cancels, also cancels view for adding newly searched device(s).
    private View searchDeviceLocation;//Fake view for search dialog location to calculate fab animation transition as cancel button.
    private View newDeviceFabLocation;//Fake view for new device fab location to calculate fab animation transition as cancel button.
    private View searchDevice;//Dialog for searching new Device.
    private View fabBackColor;//Makes color darker when searching new device, or adding new device.
    private View newDevice;//View for adding newly searched device(s).
    private View renameLayout;
    private MenuItem delete;
    private MenuItem rename;
    private EditText renameDevice;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Util.log("onNewIntent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if(intent == null) return;
        if(!Butler.isFromLauncher()) return;
        Bundle bundle = intent.getExtras();
        if(bundle == null) return;
        int butlerType = bundle.getInt(Butler.KEY);
        Butler.start(this,butlerType);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent(getIntent());

        Util.log(savedInstanceState  + "");

        Util.initLang(this, Helper.GEORGIAN);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.drawable.cast_ic_notification_on);

        Util.keepScreenOn(this, true);

        display = Util.getDisplayPoint(MainActivity.this);

        calcFabAnimationForDeviceSearch();
        calcFabAnimationFornewDevice();

        //newDeviceFabLocation = findViewById(R.id.newDeviceFabLocation);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnDragListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fabState == FABBehavior.FabState.SEARCHING_DEVICE) {
                    cancelDeviceSearch(true);
                }else if(fabState == FABBehavior.FabState.NEW_DEVICE){
                    cancelNewDevice();
                }else{
                    searchDevice();
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        int columnNumber = Util.isLandscapeMode(this) ? 4 : 3;
        gridLayoutManager = new GridLayoutManager(this,columnNumber);

        recyclerView.setLayoutManager(gridLayoutManager);

        List<Device> data = new ArrayList<>();
        data.add(new LightBulb(0, Constants.IP));

        /*for(int i=0; i<10; i++)
            data.add(new LightBulb(0, Constants.IP_PORT));

        for(int i=0; i<10; i++)
            data.add(new GasSensor(0,Constants.IP_PORT));

        Collections.shuffle(data);*/

        recyclerViewAdapter = new RecyclerViewAdapter(this, data,gridLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    private void inflateNewDeviceView(){
        if(newDevice == null){
            Util.log("newDevice is null");
            View viewStab = ((ViewStub) findViewById(R.id.newDeviceViewStab)).inflate();
            if(viewStab == null) {
                //show problem to user.
                return;
            }
            newDevice = viewStab.findViewById(R.id.newDevice);

            new NewDevice(this,newDevice,getSupportFragmentManager());
        }

        if(Util.isLandscapeMode(this)){
            Point p = Util.getDisplayPoint(this);
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) newDevice.getLayoutParams();
            params.width = p.y;
            params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            newDevice.setLayoutParams(params);
        }

        newDevice.setTranslationY(Util.dpToPixel(this, 250));
        newDevice.setVisibility(View.VISIBLE);
    }

    private void calcFabAnimationForDeviceSearch(){
        searchDeviceLocation = findViewById(R.id.searchDeviceLocation);
        searchDeviceLocation.post(new Runnable() {
            @Override
            public void run() {
                float x = (searchDeviceLocation.getX() - display.x );
                float y = (searchDeviceLocation.getY() - display.y );
                searchDeviceTranslation = new FPoint(x,y);

                float fabW = (display.x - fab.getX());
                float fabH = (display.y -fab.getY());

                searchDeviceTranslation.offset(fabW, fabH);
            }
        });
    }

    private void calcFabAnimationFornewDevice(){
        newDeviceFabLocation = findViewById(R.id.newDeviceFabLocation);
        newDeviceFabLocation.post(new Runnable() {
            @Override
            public void run() {
                float x = (newDeviceFabLocation.getX() - display.x);
                float y = (newDeviceFabLocation.getY() - display.y);
                newDeviceTranslation = new FPoint(x, y);

                float fabW = (display.x - fab.getX());
                float fabH = (display.y - fab.getY());

                newDeviceTranslation.offset(fabW, fabH);
            }
        });
    }

    Runnable r= null;


    public void letBeDark(){
        ValueAnimator anim = new ValueAnimator();

        anim.setIntValues(Util.getColorFromRes(this, android.R.color.transparent), Util.getColorFromRes(this, R.color.soSoTransparent));
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if(fabBackColor == null){
                    View viewStab = ((ViewStub) findViewById(R.id.fabBackColorViewStab)).inflate();
                    if(viewStab == null) {
                        //show problem to user.
                        return;
                    }
                    fabBackColor = viewStab.findViewById(R.id.fabBackColor);
                }
                fabBackColor.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });

        anim.setDuration(300);
        anim.start();
    }

    public void letBeBright(){
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(Util.getColorFromRes(this, R.color.soSoTransparent), Util.getColorFromRes(this, android.R.color.transparent));
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fabBackColor.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        anim.setDuration(300);
        anim.start();
    }

    private void inflateSearchDevice(){
        if(searchDevice == null){
            View viewStab = ((ViewStub) findViewById(R.id.searchDeviceViewStab)).inflate();
            if(viewStab == null) {
                //show problem to user.
                return;
            }
            searchDevice = viewStab.findViewById(R.id.searchDevice);
        }
    }

    private void searchDevice(){
        letBeDark();

        fab.animate().translationY(searchDeviceTranslation.getY()).setInterpolator(new LinearInterpolator()).start();//-500
        fab.animate().translationX(searchDeviceTranslation.getX()).setInterpolator(new LinearInterpolator()).start();//-100
        fab.animate().rotation(45).setInterpolator(new LinearInterpolator()).start();

        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setBehavior(null);
        fab.setLayoutParams(p);

        fabState = 1;

        inflateSearchDevice();
        searchDevice.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.search_device_show));
        searchDevice.setVisibility(View.VISIBLE);

        inflateNewDeviceView();//TODO

        r = new Runnable() {
            @Override
            public void run() {
                Util.log("newDevice :" + newDevice);

                newDevice.animate().translationY(0).setInterpolator(new LinearInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        Util.log("onAnimationStart");
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Util.log("onAnimationEnd");
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        Util.log("onAnimationCancel");
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        Util.log("onAnimationRepeat");
                    }
                }).start();

                fab.animate().translationY(newDeviceTranslation.getY()).setInterpolator(new LinearInterpolator()).start();
                fab.animate().translationX(newDeviceTranslation.getX()).setInterpolator(new LinearInterpolator()).start();

                //TODO
               /* CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                p.setAnchorId(R.id.newDevice);
                p.gravity = Gravity.NO_GRAVITY;
                p.anchorGravity = Gravity.END;
                fab.setLayoutParams(p);*/

                cancelDeviceSearch(false);

            }
        };

        fab.postDelayed(r, 3000);
    }

    private void cancelNewDevice(){
        letBeBright();

       // CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        //p.setBehavior(new FABBehavior(this, null));
       // p.setAnchorId(View.NO_ID);
        //p.gravity = Gravity.BOTTOM|Gravity.END;
        //p.anchorGravity = Gravity.NO_GRAVITY;
        //fab.setLayoutParams(p);

        //TODO
        fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
        fab.animate().translationX(0).setInterpolator(new LinearInterpolator()).start();

        fab.animate().rotation(0).setInterpolator(new LinearInterpolator()).start();

        newDevice.animate().translationY(Util.dpToPixel(this, 250)).setInterpolator(new LinearInterpolator()).start();

        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setBehavior(new FABBehavior(this, null));
        fab.setLayoutParams(p);
        fabState = 0;

    }

    private void cancelDeviceSearch(boolean brigtBackground){

        fab.removeCallbacks(r);

        if(brigtBackground){
            fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
            fab.animate().translationX(0).setInterpolator(new LinearInterpolator()).start();
            fab.animate().rotation(0).setInterpolator(new LinearInterpolator()).start();
        }else{
            //TODO
            //fab.setTranslationY(0);
            //fab.setTranslationX(0);
        }

        if(brigtBackground){
            ValueAnimator anim = new ValueAnimator();
            anim.setIntValues(Util.getColorFromRes(this,R.color.soSoTransparent),Util.getColorFromRes(this,android.R.color.transparent));
            anim.setEvaluator(new ArgbEvaluator());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    fabBackColor.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
                }
            });
            anim.setDuration(300);
            anim.start();
        }

        if(brigtBackground){
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            p.setBehavior(new FABBehavior(this, null));
            fab.setLayoutParams(p);
            fabState = 0;
        }else fabState = 2;

        searchDevice.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.search_device_hide));
        searchDevice.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

        if(isRenameOpen){
            View v = postRenameAction(false);
            if( v!= null) v.performClick();
            return;
        }

        if(recyclerViewAdapter.isInSelectinMode()){
            recyclerViewAdapter.exitSelectinMode();
            activateDelete(false);
            activateRename(false);
            return;
        }

        switch (fabState){
            case 1: cancelDeviceSearch(true);
                return;
            case 2:
                cancelNewDevice();
                return;
        }
        //super.onBackPressed();
        finish();
    }

    public void activateDelete(boolean activate){
        delete.setVisible(activate);
        //delete.getActionView().animate().translationX(-100).setInterpolator(new LinearInterpolator()).start();//-100
    }

    public void activateRename(boolean activate) {
        rename.setVisible(activate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        delete = menu.findItem(R.id.delete);
        delete.setVisible(false);

        {
            ImageView image = new ImageView(this);
            image.setImageResource(R.drawable.ic_delete_forever_white_24dp);
            delete.setActionView(image);
        }

        rename = menu.findItem(R.id.rename);
        rename.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.more) {
            //TODO change lang of app.
            return true;
        }else if(id == R.id.rename /*&& renameLayout.getVisibility() == View.GONE*/ ){

            if(renameLayout == null){
                View viewStab = ((ViewStub) findViewById(R.id.renameViewStub)).inflate();
                if(viewStab == null) {
                    //show problem to user.
                    return super.onOptionsItemSelected(item);
                }
                renameLayout = viewStab.findViewById(R.id.renameLayout);
            }else if(renameLayout.getVisibility() == View.VISIBLE) return super.onOptionsItemSelected(item);

            renameLayout.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rename_show));
            renameLayout.setVisibility(View.VISIBLE);
            isRenameOpen = true;

            renameLayout.post(new Runnable() {
                @Override
                public void run() {
                    EditText rename = (EditText) renameLayout.findViewById(R.id.renameInput);
                    if(rename != null) rename.requestFocus();
                    Util.showKeyboard(MainActivity.this,rename);
                }
            });

            postRenameAction(true);
        }else if(id == R.id.jarvis){
            Butler.start(this,Butler.Types.Jarvis);
        }
        return super.onOptionsItemSelected(item);
    }

    private View postRenameAction(final boolean showSnackbar) {
        View RenameDone = findViewById(R.id.RenameDone);
        RenameDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renameLayout.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rename_hide));
                renameLayout.setVisibility(View.GONE);
                isRenameOpen = false;
                renameDevice = (EditText) findViewById(R.id.renameInput);
                Util.hideKeyboard(MainActivity.this, renameDevice.getWindowToken());

                if(showSnackbar){
                    Snackbar snackbar = Snackbar
                            .make(recyclerView, "Device renamed", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });

                    snackbar.show();
                }

            }
        });
        return RenameDone;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.hideKeyboard(getWindow());
    }
}
