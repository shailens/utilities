package org.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * user: shailendra
 * Date: 26/11/20
 * Time: 7:41 PM
 */
public class TimeoutMap<E,T> {

    Map<E,Long> timeoutKeyMap = new ConcurrentHashMap<E, Long>();
    Map<E,T> map = new ConcurrentHashMap<E, T>();
    Long expiryTimeInMillis;

    public TimeoutMap(Long expiryTimeInMillis) throws Exception{
        if(expiryTimeInMillis == null || expiryTimeInMillis <= 0L)
            throw new Exception("Invalid expiry time.");
        this.expiryTimeInMillis = expiryTimeInMillis;
    }

    public void put(E key,T value){
        map.put(key,value);
        timeoutKeyMap.put(key,System.currentTimeMillis() + expiryTimeInMillis);
    }

    public T get(E key){
        T value = map.get(key);
        if(System.currentTimeMillis() > timeoutKeyMap.get(key)){
            map.remove(key);
            timeoutKeyMap.remove(key);
            return null;
        }
        return value;
    }

    public T remove(E key){
        T value = map.remove(key);
        if(System.currentTimeMillis() > timeoutKeyMap.get(key)){
            return null;
        }
        return value;
    }

}
