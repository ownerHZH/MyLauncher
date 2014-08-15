package com.example.launcherdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Page{
  private List<RResolveInfo> items;

	public List<RResolveInfo> getItems() {
		return items;
	}
	
	public void setItems(List<RResolveInfo> items) {
		this.items=items;
	}
	
	public void addItem(RResolveInfo item) {
		items.add(item);
	}
	
	public void swapItems(int itemA, int itemB) {
		Collections.swap(items, itemA, itemB);
	}
	
	public RResolveInfo removeItem(int itemIndex) {
		RResolveInfo item = items.get(itemIndex);
		items.remove(itemIndex);
		return item;
	}
	
	public void deleteItem(int itemIndex) {
		items.remove(itemIndex);
	}

}
