/*
 * Copyright 2015 chenupt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.chenupt.dragtoplayout.demo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.common.collect.Lists;

import java.util.List;

import de.greenrobot.event.EventBus;
import github.chenupt.dragtoplayout.DragTopLayout;
import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private DragTopLayout dragLayout;
    private ModelPagerAdapter adapter;
    private ViewPager viewPager;

    private ImageView menuImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dragLayout = (DragTopLayout) findViewById(R.id.drag_layout);
        menuImageView = (ImageView) findViewById(R.id.image_view);

        toolbar.setTitle("DragTopLayout");
        setSupportActionBar(toolbar);

        // init DragTopLayout
        DragTopLayout.from(this)
                .open()
                .setRefreshRadio(1.4f)
                .listener(new DragTopLayout.SimplePanelListener() {
                    @Override
                    public void onSliding(float radio) {
                        Log.d(TAG, "sliding: " + radio);
                    }
                }).setup(dragLayout);

        // init pager
        PagerModelManager factory = new PagerModelManager();
        factory.addCommonFragment(TestListFragment.class, getTitles(), getTitles());
        adapter = new ModelPagerAdapter(getSupportFragmentManager(), factory);
        viewPager.setAdapter(adapter);
    }

    private List<String> getTitles(){
        return Lists.newArrayList("TAB 1", "TAB 2", "TAB 3", "TAB 4");
    }

    // Handle scroll event from fragments
    public void onEvent(Boolean b){
        dragLayout.setTouchMode(b);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_menu_icon) {
            if(menuImageView.getVisibility() == View.GONE){
                menuImageView.setVisibility(View.VISIBLE);
            }else{
                menuImageView.setVisibility(View.GONE);
            }
            return true;
        } else if(id == R.id.action_toggle){
            dragLayout.toggleMenu();
            return true;
        } else if(id == R.id.action_about){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}