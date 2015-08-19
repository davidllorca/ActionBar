package com.davidllorca.actionbar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;


public class ActionBarTestActivity extends Activity {

    private ShareActionProvider mShareActioProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar_test);

        // Show/hide Actionbar dynamically
        final ToggleButton showHideButton = (ToggleButton) findViewById(R.id.toggleButton);
        showHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionBar actionBar = getActionBar();
                if (showHideButton.isChecked()) {
                    actionBar.hide();
                } else {
                    actionBar.show();
                }
            }
        });

        // Set ActionBar for tab navigation
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Add first fragment
        Fragment firstFragment = new Fragment();
        actionBar.addTab(actionBar.newTab().setText(R.string.edit)
                .setIcon(android.R.drawable.ic_menu_edit)
                .setTabListener(new TabListener(firstFragment)));
        // Add second fragment
        Fragment secondFragment = new Fragment();
        actionBar.addTab(actionBar.newTab().setText(R.string.delete)
                .setIcon(android.R.drawable.ic_menu_delete)
                .setTabListener(new TabListener(secondFragment)));

        // Get intent action and process data
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                processText(intent.getStringExtra(Intent.EXTRA_TEXT));
            } else if (type.startsWith("image/")) {
                processImage(intent.getParcelableExtra(Intent.EXTRA_STREAM));
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            processMultipleImages(intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM));
        } else {
            // Other actions
        }
    }

    private void processText(String stringExtra) {

    }

    private void processImage(Parcelable parcelableExtra) {

    }

    private void processMultipleImages(ArrayList<Parcelable> parcelableArrayListExtra) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar_test, menu);
        // Manage items dynamically
        MenuItem itemDelete = menu.findItem(R.id.delete);
        itemDelete.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        // Add provider
        MenuItem itemProvider = menu.findItem(R.id.share);
        mShareActioProvider = (ShareActionProvider) itemProvider.getActionProvider();
        setShareIntent();
        return true;
    }

    private void setShareIntent() {
        if (mShareActioProvider != null) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String text = ((EditText) findViewById(R.id.editText)).getText().toString();
            intent.putExtra(Intent.EXTRA_TEXT, text);
            mShareActioProvider.setShareIntent(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Toast.makeText(this, "Icon launcher clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ActionBarTestActivity.class);
                // If Activity is in stack, Intent clear all of activities over that
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class TabListener implements ActionBar.TabListener {

        public TabListener(Fragment fragment) {
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }

    public void sendText(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String text = ((EditText) findViewById(R.id.editText)).getText().toString();
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, "Choose option"));
    }

    public void sendImage(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        Uri path = Uri.parse("android.resource://com.davidllorca.actionbar/" + R.drawable.image);
        intent.putExtra(Intent.EXTRA_STREAM, path);
        // In case choose email action
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of mail");
        startActivity(Intent.createChooser(intent, "Choose option"));
    }
}
