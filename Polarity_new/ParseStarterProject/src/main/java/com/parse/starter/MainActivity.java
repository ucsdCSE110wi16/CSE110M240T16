/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.provider.Settings.Secure;


public class MainActivity extends PolarityActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    // show loading screen
    setContentView(R.layout.activity_main);

    initialize();
    android_id = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);

    // debug
    if(android_id.compareTo("50012c6b71ca00fa") == 0) {
      com_userID = "GMso6fh0Bb";
      com_user = "lucas";
      com_previousActivity = HubActivity.class.getSimpleName();
      toActivity_AddFriends();
    }
    else {
      toActivity_Login();
    }
  }

  //region Auto-Generated Stuff

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  //endregion
}
