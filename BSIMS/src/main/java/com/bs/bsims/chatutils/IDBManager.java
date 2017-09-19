/**
 * 
 */
package com.bs.bsims.chatutils;

import java.util.List;

/**

 *          BS北盛最帅程序员

 *         Copyright (c) 2016

 *        湖北北盛科技有限公司

 *        @author 梁骚侠
 
 *        @date 2016-7-15

 *        @version 2.0

 */
public interface IDBManager<T> {
    List<T> getAll();
    
    T getById(String id);
    
    int deleteById(String id);
    
    int insert(T t);
    
    int update(T t);
}