package lico.example.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import lico.example.R;
import lico.example.fragment.ImageFragment;
import lico.example.helper.EventHelper;

/**
 * Created by zzk on 15/11/26.
 */
public class MainView extends ViewImpl {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.container)
    FrameLayout container;

    private ImageView mHeadBgImg, mAvatorImg;
    private ImageFragment mImageFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void bindEvent() {
        super.bindEvent();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                (Activity) mRootView.getContext(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        View headerView = navView.inflateHeaderView(R.layout.nav_header_main);
        mHeadBgImg = (ImageView) headerView.findViewById(R.id.head_image);
        mAvatorImg = (ImageView) headerView.findViewById(R.id.imageView);

//        此方法为官网的 还是23.1.1出来的、发现会有各种各样的问题，比如你必须要触发addOnLayout...方法后才能得到里面控件。在此之前使用控件都会报NullPointerException
//        navView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                navView.removeOnLayoutChangeListener(this);
//                mAvatorImg = (ImageView) navView.findViewById(R.id.imageView);
//                EventHelper.click(mPresenter, mAvatorImg);
//            }
//        });

        EventHelper.click(mPresenter, fab, mAvatorImg);
        EventHelper.setNavigationItemSelected(mPresenter, navView);
    }

    public void setAvator(Bitmap bitmap) {
        mHeadBgImg.setImageBitmap(bitmap);
    }

    public boolean setNavigationItemSelected(FragmentManager manager, int menuId) {
        boolean isConsume = false;
        setNewRootFragment(manager, menuId);
        drawerLayout.closeDrawer(GravityCompat.START);
        return isConsume;
    }

    public boolean setOptionsItemSelected(int itemId) {
        boolean isConsume = false;
        switch (itemId) {
            case R.id.action_settings:
                Snackbar.make(mRootView.getRootView(), "点击了Setting", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                isConsume = true;
                break;
        }
        return isConsume;
    }

    public boolean closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else return false;
    }

    public void setFabClick(String string) {
        Snackbar.make(mRootView.getRootView(), string, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
    }

    public int getOptionsMenuId() {
        return R.menu.main;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void destroy() {
        ButterKnife.unbind(mRootView.getRootView());
    }

    private void setNewRootFragment(FragmentManager manager, int menuId) {
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (menuId) {
            case R.id.nav_camera:
                    mImageFragment = (ImageFragment) manager.findFragmentByTag("images");
                if (mImageFragment == null) {
                    mImageFragment = new ImageFragment();
                    transaction.add(R.id.container, mImageFragment, "images");
                } else {
                    transaction.show(mImageFragment);
                }
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
        }
        transaction.commit();
        drawerLayout.closeDrawers();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mImageFragment != null) {
            transaction.hide(mImageFragment);
        }
    }

}
