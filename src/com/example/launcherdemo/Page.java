package com.example.launcherdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import android.view.View;

public class Page{
  private List<RResolveInfo> items;
  private View view;
  private AppsAdapter adapter;

public View getView() {
	return view;
}

public void setView(View view) {
	this.view = view;
}

public AppsAdapter getAdapter() {
	return adapter;
}

public void setAdapter(AppsAdapter adapter) {
	this.adapter = adapter;
}

public List<RResolveInfo> getItems() {
	return items;
}

public void setItems(List<RResolveInfo> items) {
	this.items=Collections.synchronizedList(new ArrayList<RResolveInfo>());
	this.items.addAll(items);
}

public void removeItem(RResolveInfo rr)
{
	items.remove(rr);
	adapter.setApps(items);
}

public void notifyDataSetChanged()
{
	adapter.notifyDataSetChanged();
}

}
