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
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.*;

public class MainActivity extends AppCompatActivity {
  //private Button btnLogin, btnNewUser;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

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

  /* Handle Button Clicks */

  public void onLoginClick(View view) {

    // TODO:
    // check login credentials.. if pass then process the fallowing code.
    // Otherwise stay on this page

    Intent intentObject = new Intent(this, HubActivity.class);
    startActivity(intentObject);
  } // btnLogin Click

  public void onCreateUser(View view) {
    Intent intentObject = new Intent(this, SignUpActivity.class);
    startActivity(intentObject);
  } // btnCreateUser

}
