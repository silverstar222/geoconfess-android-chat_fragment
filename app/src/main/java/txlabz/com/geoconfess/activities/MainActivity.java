package txlabz.com.geoconfess.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.view.ActionMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.adapters.NavDrawerAdapter;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.CreatedDynamicSpotEvent;
import txlabz.com.geoconfess.events.DeletedDynamicSpotEvent;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.LogoutEvent;
import txlabz.com.geoconfess.events.ServerNotificationEvent;
import txlabz.com.geoconfess.fragments.ChatFragment;
import txlabz.com.geoconfess.fragments.HomeFragment;
import txlabz.com.geoconfess.fragments.MessageFragment;
import txlabz.com.geoconfess.fragments.NotificationFragment;
import txlabz.com.geoconfess.fragments.ProfileFragment;
import txlabz.com.geoconfess.fragments.QuestConfessionFragment;
import txlabz.com.geoconfess.fragments.SpotCreationStep1Fragment;
import txlabz.com.geoconfess.models.response.ServerNotificationModel;
import txlabz.com.geoconfess.network.requests.NotificationsRequest;
import txlabz.com.geoconfess.service.MyLocationTracker;
import txlabz.com.geoconfess.utils.DialogUtility;
import txlabz.com.geoconfess.utils.GeneralUtility;
import txlabz.com.geoconfess.utils.NotificationUtils;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;
import txlabz.com.geoconfess.utils.Utils;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int START_FOREGROUND_SERVICE = 123;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.left_drawer)
    ListView left_drawer;
    @BindView(R.id.icon_back)
    ImageView icon_back;
    @BindView(R.id.un_available)
    ImageView unAvailable;
    @BindView(R.id.available)
    ImageView available;
    @BindView(R.id.notification_count)
    TextView notificationCount;
    @BindView(R.id.bottom_bar_center_button)
    RelativeLayout mBottomBarCenterButton;
    @BindView(R.id.fl_warning)
    FrameLayout fl_warning;
    @BindView(R.id.priest_warning_log_off)
    TextView priest_warning_log_off;
    @BindView(R.id.priest_login_warning)
    TextView priest_login_warning;
    @BindView(R.id.bottom_bar)
    RelativeLayout footer;
    private Boolean backStatus = false;
    private String[] navItemList;
    private View navHeader;
    private boolean mIsBound;
    private MyLocationTracker mBoundService;
    private Intent serviceIntent;
    private Fragment currentFragment;
    private LatLng myLocation;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            mBoundService = ((MyLocationTracker.LocationBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.i(TAG, "onServiceDisconnected");
            mBoundService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doBindService();
        ButterKnife.bind(this);
        updateView();
        Log.i(TAG, "push token: " + SharedPreferenceUtils.getPushToken(this));
        fetchNotifications();
        callWarningLogout();

    }


    private void callWarningLogout() {
        priest_warning_log_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
//        checkUserData();
    }



    public LatLng getMyLocation() {
        return myLocation;
    }




    public void setMyLocation(Double latitude, Double longitude) {
        if (latitude != null && longitude != null) {
            myLocation = new LatLng(latitude, longitude);
        } else {
            myLocation = null;
        }
    }


    /*Set the notification badge count on the icon, if the count is 0 , Red icon will be removed*/
    public void setNotificationCount(int count) {

        if (count > 0) {
            notificationCount.setVisibility(View.VISIBLE);
            notificationCount.setText(count + "");

            ShortcutBadger.applyCount(this, count);
            SharedPreferenceUtils.setBadgeCount(this, count);

        } else {
            notificationCount.setVisibility(View.GONE);

            ShortcutBadger.removeCount(this);
            SharedPreferenceUtils.setBadgeCount(this, 0);
        }

    }



    private void updateView() {
        mDrawerLayout.setDrawerShadow(null,
                GravityCompat.START);
        icon_back.setImageResource(R.drawable.ic_menu);
        icon_back.setVisibility(View.VISIBLE);
        prepareNavigationDrawer();
        initHomeFragment();
        checkIfServiceRunning();
    }



    private void prepareNavigationDrawer() {
        navItemList = getResources().getStringArray(R.array.naw_drawer_items);
        NavDrawerAdapter navAdapter = new NavDrawerAdapter(this, navItemList);
        setNavDrawerHeader();
        left_drawer.setAdapter(navAdapter);
        left_drawer.setOnItemClickListener(this);
    }



    public void setNavDrawerHeader() {
        navHeader = getLayoutInflater().inflate(R.layout.nav_drawer_header, null);
        setHeaderName();
        left_drawer.addHeaderView(navHeader);
    }

    //setHeaderName
    public void setHeaderName() {
        TextView headerUserName = (TextView) navHeader.findViewById(R.id.header_user_name);
        headerUserName.setText(SharedPreferenceUtils.getUserNameSurname(this));
        Log.i(TAG, "user role: " + SharedPreferenceUtils.getLogInRole(this));
        if (TextUtils.equals(SharedPreferenceUtils.getLogInRole(this), AppConstants.PRIEST_ROLE)) {
            mBottomBarCenterButton.setVisibility(View.VISIBLE);
        } else {
            mBottomBarCenterButton.setVisibility(View.GONE);
        }
    }



    @OnClick(R.id.un_available)
    public void onUnUnavailableClick() {
        if (!(currentFragment instanceof SpotCreationStep1Fragment)) {
            SpotCreationStep1Fragment fragment = new SpotCreationStep1Fragment();
            loadFragment(fragment, true, true);
        }
    }



    @OnClick(R.id.available)
    public void onAvailableClick() {
        tryToStopService();
    }

    @OnClick(R.id.imageView)
    public void onNotificationClick() {
        if (!(currentFragment instanceof NotificationFragment)) {
            NotificationFragment fragment = new NotificationFragment();
            loadFragment(fragment, true, true);
        }
    }



    @OnClick(R.id.icon_back)
    public void onToolbarBackClick() {
        if (backStatus) {
            onBackPressed();
        } else {
            if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
                mDrawerLayout.setScrimColor(Color.TRANSPARENT);

            } else {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                mDrawerLayout.setScrimColor(Color.TRANSPARENT);
            }
        }
    }




    private void initHomeFragment() {
        HomeFragment myf = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content_frame, myf);
        transaction.commit();
    }



    public void loadFragment(final Fragment fragment, boolean backButtonShown, boolean isFooterShown) {
        currentFragment = fragment;
        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
        onFragmentChange(backButtonShown, isFooterShown);
    }


    public void closeCurrentFragment() {
        getSupportFragmentManager().popBackStack();

    }



    public void clearBackStack(Fragment fragment) {
        getSupportFragmentManager().popBackStack(fragment.getClass().getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }



    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);
    }



    private void onFragmentChange(boolean backButtonShown, boolean isFooterShown) {
        int image = backButtonShown ? R.drawable.ic_back : R.drawable.ic_menu;
        backStatus = backButtonShown;
        icon_back.setImageResource(image);
        footer.setVisibility(isFooterShown ? View.VISIBLE : View.GONE);
    }



    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() == 1) {
            icon_back.setImageResource(R.drawable.ic_menu);
            backStatus = false;
//            footer.setVisibility(View.GONE);
        }
        if (currentFragment instanceof ChatFragment) {
            footer.setVisibility(View.VISIBLE);
        }
        /*if (currentFragment instanceof HomeFragment){
            footer.setVisibility(View.VISIBLE);
        }*/


        GeneralUtility.hideKeyBoard(this);
        super.onBackPressed();
        getActiveFragment();
    }

    private void getActiveFragment() {
        FragmentManager manager = getSupportFragmentManager();
        if (!manager.getFragments().isEmpty()) {
            currentFragment = manager.getFragments().get(manager.getFragments().size() - 1);
        } else {
            currentFragment = null;
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case AppConstants.NAV_DRAWER_CONFESSION: {
                QuestConfessionFragment f = new QuestConfessionFragment();
                Bundle args = new Bundle();
                args.putString(AppConstants.FOR_QUESTCONFESSION, AppConstants.FOR_QUESTONE);
                f.setArguments(args);
                loadFragment(f, true, true);
                break;
            }
            case AppConstants.NAV_DRAWER_POURCONFESSOR: {
                QuestConfessionFragment f = new QuestConfessionFragment();
                Bundle args = new Bundle();
                args.putString(AppConstants.FOR_QUESTCONFESSION, AppConstants.FOR_QUESTTWO);
                f.setArguments(args);
                loadFragment(f, true, true);
                break;
            }
            case AppConstants.NAV_DRAWER_PREPARCONFESSION: {
                QuestConfessionFragment f = new QuestConfessionFragment();
                Bundle args = new Bundle();
                args.putString(AppConstants.FOR_QUESTCONFESSION, AppConstants.FOR_QUESTTHREE);
                f.setArguments(args);
                loadFragment(f, true, true);
                break;
            }
            case AppConstants.NAV_DRAWER_PARTAGER: {
                // NotesFragment f = new NotesFragment();
                //loadFragment(f, true, true);
                MessageFragment f = new MessageFragment();
                loadFragment(f, true, true);
                break;
            }
            case AppConstants.NAV_DRAWER_MODIFICATIONPROFILE: {
                ProfileFragment f = new ProfileFragment();
                loadFragment(f, true, true);
                break;
            }
           /* case AppConstants.NAV_DRAWER_AIDE: {
                break;
            }*/
            case AppConstants.NAV_DRAWER_FAIRUNDON: {
                Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.ktotv.com/aider-kto"));
                startActivity(browserIntent);
                break;

            }
            case AppConstants.NAV_DRAWER_LOGOUT: {
                logout();
                break;
            }
            default: {
            }
        }

        // Toast.makeText(MainActivity.this, "Clicked item: " + (position - 1), Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }



    public void logout() {
        BusProvider.getInstance().post(new LogoutEvent());

        SharedPreferenceUtils.setUserId(this, -1);
        SharedPreferenceUtils.setUserNameSurname(this, "");
        SharedPreferenceUtils.setLogInRole(this, "");

        SharedPreferenceUtils.setAccessToken(this, "");
        Utils.savePreferencesString(this,AppConstants.PASSWORD,"");
        SharedPreferenceUtils.setTrackServiceRunning(this, false);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



    public void tryToStartService() {
        startService(generateServiceIntent());
        Intent intent = new Intent(this, MainActivity.class);
        mBoundService.startForeground(START_FOREGROUND_SERVICE, NotificationUtils.getAppNotification(getString(R.string.tracking_location), intent, this));
    }


    @Nullable
    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }

    private Intent generateServiceIntent() {
        if (serviceIntent == null) {
            serviceIntent = new Intent(this, MyLocationTracker.class);
        }
        return serviceIntent;
    }

    public void tryToStopService() {
        if (mBoundService != null) {
            mBoundService.stopForeground(true);
            mBoundService.stopService(generateServiceIntent());
//            stopService(serviceIntent);
        } else {
            updateTrackingUI(false);
        }
    }

    void doBindService() {
        Intent mServiceIntent = new Intent(this, MyLocationTracker.class);
        bindService(mServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
//            stopService(new Intent(this, TestTrackLocation.class));
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    public void updateTrackingUI(boolean enabled) {
        if (enabled) {
            unAvailable.setVisibility(View.GONE);
            available.setVisibility(View.VISIBLE);
        } else {
            unAvailable.setVisibility(View.VISIBLE);
            available.setVisibility(View.GONE);
        }
    }

    public void checkIfServiceRunning() {
        updateTrackingUI(SharedPreferenceUtils.isTrackServiceRunning(this));
    }

    @Subscribe
    public void onEvent(CreatedDynamicSpotEvent event) {
        hideDialog();
        DialogUtility.showDialog(this, getString(R.string.geo_location), getString(R.string.service_start_toast_message));
        updateTrackingUI(true);
        onBackPressed();
    }

    @Subscribe
    public void onEvent(DeletedDynamicSpotEvent event) {
        hideDialog();
        updateTrackingUI(false);
//        SpotCreationStep1Fragment fragment = new SpotCreationStep1Fragment();
//        loadFragment(fragment, true, true);
    }

    @Subscribe
    public void onEvent(ErrorResponseEvent event) {
        hideDialog();
    }

    public void fetchNotifications() {

        NotificationsRequest.getNotifications(SharedPreferenceUtils.getAccessToken(this));
    }

    @Subscribe
    public void OnEvent(ServerNotificationEvent event) {

        getNotificationBadgeCount(removeDuplicateNotification(event));

    }

    /*
    Get the unread notification count from the Notification api result and update the badge count in the notification icon
     */
    public void getNotificationBadgeCount(List<ServerNotificationModel> notificationList) {

        int notificationCount = 0;
        if (!notificationList.isEmpty()) {
            for (int i = 0; i < notificationList.size(); i++) {

                //Checking is there any unread status is true, if there, Incrementing count to display in the Badge.
                if (notificationList.get(i).isUnread()) {
                    notificationCount = notificationCount + 1;
                }
            }

            setNotificationCount(notificationCount);
        }
    }

    public List<ServerNotificationModel> removeDuplicateNotification(ServerNotificationEvent event) {
        List<ServerNotificationModel> itemsData = new ArrayList<>();

        List<ServerNotificationModel> programs = Arrays.asList(event.getResponse());

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        // Remove duplicate notification for same Priest and same message sender Id
        for (ServerNotificationModel model : programs) {
            if (TextUtils.equals(SharedPreferenceUtils.getLogInRole(this), AppConstants.PRIEST_ROLE)) {
                if ((model.getModel().equalsIgnoreCase(AppConstants.MEET_REQUEST))) {
                    if (!map.containsKey("Priest:" + model.getMeetRequestModel().getPenitentModel().getId())) {
                        itemsData.add(model);
                    }
                }
                if ((model.getModel().equalsIgnoreCase(AppConstants.MESSAGE))) {
                    if (!map.containsKey("Message:" + model.getMessage().getSenderId())) {
                        itemsData.add(model);
                    }
                }
            } else {

                if ((model.getModel().equalsIgnoreCase(AppConstants.MEET_REQUEST))) {
                    if (!map.containsKey("Priest:" + model.getMeetRequestModel().getPriestModel().getId())) {
                        map.put("Priest:" + model.getMeetRequestModel().getPriestModel().getId(), 0);
                        itemsData.add(model);
                    }
                }
                if ((model.getModel().equalsIgnoreCase(AppConstants.MESSAGE))) {
                    if (!map.containsKey("Message:" + model.getMessage().getSenderId())) {
                        map.put("Message:" + model.getMessage().getSenderId(), 0);
                        itemsData.add(model);
                    }
                }
            }
        }

        return itemsData;
    }

    public void showInactiveView() {
        fl_warning.setVisibility(View.VISIBLE);
    }


}