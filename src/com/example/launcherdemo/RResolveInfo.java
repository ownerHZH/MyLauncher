package com.example.launcherdemo;

import android.content.pm.ResolveInfo;

public class RResolveInfo{
	private boolean longclicked;
	private boolean clicked;

	private ResolveInfo resolveInfo;
	public boolean getLongclicked() {
		return longclicked;
	}

	public void setLongclicked(boolean longclicked) {
		this.longclicked = longclicked;
	}

	public ResolveInfo getResolveInfo() {
		return resolveInfo;
	}

	public void setResolveInfo(ResolveInfo resolveInfo) {
		this.resolveInfo = resolveInfo;
	}

	public boolean getClicked() {
		return clicked;
	}

	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}

}
