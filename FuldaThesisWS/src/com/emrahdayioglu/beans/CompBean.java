package com.emrahdayioglu.beans;

public class CompBean implements Comparable{

	private long refreshTime;

	public int compareTo(Object o) {
		if(this.refreshTime >= ((CompBean)o).refreshTime)
			return 1;
		else
			return -1;
	}

	public boolean needsRefresh(long cacheTimeInMillis){
		if(System.currentTimeMillis() - refreshTime > cacheTimeInMillis){
			return true;
		}else{
			return false;
		}
	}

	public long getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
	}

}