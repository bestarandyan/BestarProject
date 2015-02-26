package com.manyi.mall.user;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.manyi.mall.R;
import com.manyi.mall.Util.JsonData;
import com.manyi.mall.cachebean.GetProvinceRequest;
import com.manyi.mall.cachebean.GetProvinceResponse;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.service.UcService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

@EFragment(R.layout.fragment_register_next)
public class RegisterNextFragment extends ImageLoaderFragment {
    @ViewById(R.id.genderTv)
    TextView mGenderTv;

    @ViewById(R.id.youeryuanaddress)
    TextView mAddress;

    @ViewById(R.id.shenfenValue)
    TextView mShengFenTv;

    @ViewById(R.id.ProvinceList)
    ListView mProvinceList;

    @ViewById(R.id.CityList)
    ListView mCityList;

    @ViewById(R.id.CountyList)
    ListView mCountyList;

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    UcService mUcService;
    List<Map<String,String>> provinceList;


    @Click(R.id.genderTv)
    void selectGender(){
        showGenderPopmenu();
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void showGenderPopmenu(){
        PopupMenu popup = new PopupMenu(getActivity(), mGenderTv);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_gender_type1){
                    mGenderTv.setText("男");
                }else if (menuItem.getItemId() == R.id.menu_gender_type2){
                    mGenderTv.setText("女");
                }
                return false;
            }
        });
        inflater.inflate(R.menu.actions_gender, popup.getMenu());
        popup.show();
        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {
            }
        });
    }

    @Click(R.id.youeryuanaddress)
    void getAddress(){
        getProvince();
    }

    @Background
    void getProvince(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msg = request.getProvince();
        JsonData jsonData = new JsonData();
        provinceList = jsonData.jsonProvinceMsg(msg, null);
        notifyProvince();
    }

    @UiThread
    void notifyProvince(){
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),provinceList,R.layout.item_content,new String[]{"ProvinceName"},new int[]{R.id.contentTv});
        mProvinceList.setAdapter(adapter);
        mDrawerLayout.openDrawer(Gravity.RIGHT);
    }

    @Click(R.id.shenfenValue)
    void selectShenFen(){
        showShengFenPopmenu();
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void showShengFenPopmenu(){
        PopupMenu popup = new PopupMenu(getActivity(), mShengFenTv);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_shengFen_type1){
                    mShengFenTv.setText("园长");
                }else if (menuItem.getItemId() == R.id.menu_shengFen_type2){
                    mShengFenTv.setText("商家");
                }
                return false;
            }
        });
        inflater.inflate(R.menu.actions_shengfen, popup.getMenu());
        popup.show();
        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {
            }
        });
    }
}
