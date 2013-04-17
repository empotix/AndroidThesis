package com.emrahdayioglu;
/**
* This class for caching ArrayList for better performance
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/
import java.util.ArrayList;

public class CachedArrayList<T> extends ArrayList<T> implements Comparable {

    private static final long serialVersionUID = 1L;
    private long refreshTime;

    public int compareTo(Object o) {
        if (this.refreshTime >= ((CachedArrayList) o).refreshTime) {
            return 1;
        } else {
            return -1;
        }
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long accessTime) {
        this.refreshTime = accessTime;
    }

    public boolean needsRefresh(long cacheTimeInMillis) {
        if (System.currentTimeMillis() - refreshTime > cacheTimeInMillis) {
            return true;
        } else {
            return false;
        }
    }
}
