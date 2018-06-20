package com.omagrahari.hackernews.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.omagrahari.hackernews.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private Context context;
    private ArrayList<String> titleList;

    public ViewPagerAdapter(FragmentManager manager, Context context, ArrayList<String> titleList) {
        super(manager);
        this.context = context;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    public View getTabView(int position) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.custom_tab_view, null);

        TextView textView = (TextView) view.findViewById(R.id.text_view_title);
        if (titleList != null) {
            if (titleList.size() > 0) {
                textView.setText(titleList.get(position));
            }
        }
        return view;
    }
}
