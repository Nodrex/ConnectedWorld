package com.nodrex.connectedworld.ui;

import android.app.Activity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;

import com.nodrex.connectedworld.MainActivity;
import com.nodrex.connectedworld.R;
import com.nodrex.connectedworld.unit.Device;
import com.nodrex.connectedworld.unit.DeviceType;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnLongClickListener, View.OnClickListener, PopupWindow.OnDismissListener{

    private List<Device> data;
    private MainActivity activity;
    private LayoutInflater inflater;
    //private PopupWindow popupWindow;
    private PopupWindow popupWindow;
    private View moreTools;
    private View renameInput;
    private GridLayoutManager gridLayoutManager;
    private boolean inSelectinMode;
    private int selectedItemCount;

    public boolean isInSelectinMode() {
        return inSelectinMode;
    }

    public RecyclerViewAdapter(MainActivity activity, List<Device> data,GridLayoutManager gridLayoutManager) {
        this.activity = activity;
        this.data = data;
        this.gridLayoutManager = gridLayoutManager;
    }

    private void initPopupWindow(){
        if(popupWindow != null) return;
        inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        moreTools = inflater.inflate(R.layout.more_tools, null);
        int lParam = LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(moreTools, lParam,lParam);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        renameInput = moreTools.findViewById(R.id.renameInput);
        View v = moreTools.findViewById(R.id.rename);
        v.setOnClickListener(this);
        popupWindow.setOnDismissListener(this);
        //popupWindow.setInputMethodMode(PopupWindow.INPUT_);
        //popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
    }

    @Override
    public boolean onLongClick(final View v) {
        //initPopupWindow();
        //popupWindow.showAsDropDown(v, 100, -210);
        //activity.letBeDark();


        //TODO /////////////////////////////////////////////////////////
        //Helper.go();

        //Helper.goB();
        //TODO /////////////////////////////////////////////////////////


        selectedItemCount++;
        inSelectinMode = true;
        activity.activateDelete(true);
        if(selectedItemCount > 1) activity.activateRename(false);
        else activity.activateRename(true);
        AppCompatCheckBox checkBox = (AppCompatCheckBox) v.findViewById(R.id.selection);
        checkBox.setVisibility(View.VISIBLE);
        checkBox.setChecked(true);
        v.setOnClickListener(this);

        try {
           // v.findViewById(R.id.selectorIndicator).setVisibility(View.VISIBLE);
        }catch (Exception e){

        }



/*
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                //super.applyTransformation(interpolatedTime, t);
                GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) v.getLayoutParams();
                //params.topMargin = (int) (100 * interpolatedTime);
                //params.rightMargin = (int) (100 * interpolatedTime);

                params.setMargins(0, 0, (int) (100 * interpolatedTime), (int) (100 * interpolatedTime));

                v.setLayoutParams(params);
                //v.setPadding(0,(int)(1000 * interpolatedTime),(int)(1000 * interpolatedTime),0);
            }
        };
        a.setDuration(200);
        v.startAnimation(a);
*/


        /*int tmpx = v.getWidth()/9;
        int tmpy = v.getHeight()/10;

        ViewCompat.animate(v).scaleX(0.7F).translationX(-tmpx).scaleY(0.8F).translationY(tmpy).setInterpolator(new LinearInterpolator()).start();
*/
        int count = gridLayoutManager.getChildCount();
        for (int i=0; i< count; i++){
            View innerV  = gridLayoutManager.getChildAt(i);
            checkBox = (AppCompatCheckBox) innerV.findViewById(R.id.selection);
            checkBox.setVisibility(View.VISIBLE);
            innerV.setOnClickListener(this);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        //int visibility = renameInput.getVisibility();
        //if(visibility == View.GONE) renameInput.setVisibility(View.VISIBLE);
        //else renameInput.setVisibility(View.GONE);

        AppCompatCheckBox checkBox = (AppCompatCheckBox) v.findViewById(R.id.selection);
        checkBox.setChecked(!checkBox.isChecked());

        if(checkBox.isChecked())selectedItemCount++;
        else selectedItemCount--;
        if(selectedItemCount > 1) {
            activity.activateRename(false);
            activity.activateDelete(true);
        } else if(selectedItemCount <= 0){
          activity.activateRename(false);
          activity.activateDelete(false);
        } else {
            activity.activateRename(true);
            activity.activateDelete(true);
        }
    }

    @Override
    public void onDismiss() {
        renameInput.setVisibility(View.GONE);
        activity.letBeBright();
    }

    public void exitSelectinMode() {
        int count = gridLayoutManager.getChildCount();
        for (int i=0; i< count; i++){
            View innerV  = gridLayoutManager.getChildAt(i);
            AppCompatCheckBox checkBox = (AppCompatCheckBox) innerV.findViewById(R.id.selection);
            checkBox.setVisibility(View.GONE);
            checkBox.setChecked(false);
            innerV.setOnClickListener(null);
        }
        inSelectinMode = false;
        selectedItemCount = 0;
    }

    class GasSensor extends RecyclerView.ViewHolder {

        public GasSensor(View v) {
            super(v);
            v.setOnLongClickListener(RecyclerViewAdapter.this);
        }

    }

    class LightBulb extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LightBulb(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(RecyclerViewAdapter.this);
        }

        @Override
        public void onClick(View v) {
           /* Util.toast(activity, "ait");

            int index = (int) v.getTag(R.id.jarvis);

            com.nodrex.connectedworld.unit.LightBulb lb = (com.nodrex.connectedworld.unit.LightBulb) data.get(index);

            boolean isOn = lb.isOn();

            if(index == 0){
                Helper.go(isOn ? -1 : 1);
            }else if(index == 1){
                Helper.go(isOn ? -2 : 2);
            }else{
                Helper.go(isOn ? -3 : 3);
            }*/


        }

    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = super.getItemViewType(position);
        if(data == null || data.size() == 0) return itemViewType;
        Device d = data.get(position);
        if(d == null) return itemViewType;
        return d.getDeviceType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case DeviceType.LightBulb:{
                view = inflate(parent, R.layout.light_bulb);
                view.setTag(R.id.jarvis,0);
                return new LightBulb(view);
            }
            case DeviceType.GasSensor:{
                view = inflate(parent,R.layout.gas_sensor);
                return new GasSensor(view);
            }

            /*case DeviceType.Garaje:{
                view = inflate(parent, R.layout.garaje);
                view.setTag(R.id.jarvis,1);
                return new LightBulb(view);
            }

            case DeviceType.Dor:{
                view = inflate(parent, R.layout.dor);
                view.setTag(R.id.jarvis,2);
                return new LightBulb(view);
            }*/

            default: return null;
        }
    }

    private View inflate(ViewGroup parent,int layout){
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent,false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder ViewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

