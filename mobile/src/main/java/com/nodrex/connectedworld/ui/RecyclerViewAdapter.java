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
import com.nodrex.connectedworld.helper.Helper;
import com.nodrex.connectedworld.unit.Device;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnLongClickListener, View.OnClickListener, PopupWindow.OnDismissListener{

    private List<Device> data;
    private MainActivity activity;
    private LayoutInflater inflater;
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

    }

    @Override
    public boolean onLongClick(final View v) {
        //TODO /////////////////////////////////////////////////////////
        Helper.go();
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


        }

    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = super.getItemViewType(position);
        if(data == null || data.size() == 0) return itemViewType;
        Device d = data.get(position);
        if(d == null) return itemViewType;
        return d.getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case Device.Types.LightBulb:{
                view = inflate(parent, R.layout.light_bulb);
                view.setTag(R.id.jarvis,0);
                return new LightBulb(view);
            }
            case Device.Types.GasSensor:{
                view = inflate(parent,R.layout.gas_sensor);
                return new GasSensor(view);
            }

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
