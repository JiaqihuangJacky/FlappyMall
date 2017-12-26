package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * token the unique id string for computation
 */
public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    //prefix representation
    public static final String TOKEN_PREFIX = "token_";

    //LRU computtion
    //1000 means that the capacity is 1000 cache（缓存）
    //if more than 1000, then we will use LRU computation
    //10000 means that max size of cache
    //expire time is 24 hours
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //default data loading implementation, when we use the get methond; if there
                //is no key corresponding value, then you can use this methond for loading
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    //put the key and value intothe Cache, so that the outside can use it
    public static void setKey(String key,String value){
        localCache.put(key,value);
    }

    //obtain the current keys, use try...catching to avoid the error
    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            logger.error("localCache get error",e);
        }
        return null;
    }
}
