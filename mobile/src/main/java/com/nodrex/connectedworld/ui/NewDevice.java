package com.nodrex.connectedworld.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.nodrex.android.tools.Util;
import com.nodrex.connectedworld.MainActivity;
import com.nodrex.connectedworld.R;

import java.util.ArrayList;
import java.util.List;

public class NewDevice extends FragmentPagerAdapter{

    private MainActivity activity;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabTitles = new ArrayList<>();

    public void addFragment(Fragment f,String title){
        fragments.add(f);
        tabTitles.add(title);
    }

    public NewDevice(final MainActivity activity,View newDeviceView,FragmentManager fm){
        super(fm);
        this.activity = activity;
        if(newDeviceView == null) return;
        tabLayout = (TabLayout) newDeviceView.findViewById(R.id.tabLayout);
        tabLayout.setSelectedTabIndicatorColor(Util.getColorFromRes(activity, android.R.color.white));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        newDeviceView.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.log("clicked");
            }
        });

        viewPager = (ViewPager) newDeviceView.findViewById(R.id.viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int oldPosition;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Util.log("olt position: " + oldPosition);
                Util.log("curr position: " + position);
                boolean back = oldPosition > position;
                if(position == 0){

                    ValueAnimator anim = new ValueAnimator();
                    anim.setIntValues(Util.getColorFromRes(activity,R.color.gasSensor),Util.getColorFromRes(activity,R.color.lightBulb));
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            tabLayout.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
                        }
                    });

                    anim.setDuration(200);
                    anim.start();

                }else if (position == 1){

                    ValueAnimator anim = new ValueAnimator();
                    if(back){
                        anim.setIntValues(Util.getColorFromRes(activity,R.color.garage),Util.getColorFromRes(activity,R.color.gasSensor));
                    }else anim.setIntValues(Util.getColorFromRes(activity,R.color.lightBulb),Util.getColorFromRes(activity,R.color.gasSensor));
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            tabLayout.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
                        }
                    });

                    anim.setDuration(200);
                    anim.start();

                }
                else if (position == 2){

                    ValueAnimator anim = new ValueAnimator();
                    if(back){
                        anim.setIntValues(Util.getColorFromRes(activity,R.color.WaterTemperatureHot),Util.getColorFromRes(activity,R.color.garage));
                    }
                    else anim.setIntValues(Util.getColorFromRes(activity,R.color.gasSensor),Util.getColorFromRes(activity,R.color.garage));
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            tabLayout.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
                        }
                    });

                    anim.setDuration(200);
                    anim.start();

                }else if(position == 3){
                    ValueAnimator anim = new ValueAnimator();
                    anim.setIntValues(Util.getColorFromRes(activity,R.color.garage),Util.getColorFromRes(activity,R.color.WaterTemperatureHot));
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            tabLayout.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
                        }
                    });

                    anim.setDuration(200);
                    anim.start();

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(ViewPager.SCROLL_STATE_DRAGGING == state ){
                    oldPosition = viewPager.getCurrentItem();
                }
            }
        });

        LightBalbFragment lightBulbFragment = new LightBalbFragment();
        lightBulbFragment.setActivity(activity);
        this.addFragment(lightBulbFragment, "ნათურა");
        lightBulbFragment = new LightBalbFragment();
        lightBulbFragment.setActivity(activity);
        this.addFragment(lightBulbFragment, "გაზის სენსორი");
        lightBulbFragment = new LightBalbFragment();
        lightBulbFragment.setActivity(activity);
        this.addFragment(lightBulbFragment, "ავტოსადგომი");
        lightBulbFragment = new LightBalbFragment();
        lightBulbFragment.setActivity(activity);
        this.addFragment(lightBulbFragment, "წყლის ტემპერატურის სენსორი     ");

        viewPager.setAdapter(this);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
