package com.catapultlearning.walkthrough.dao.base;

public interface BaseDAO<T, K> {
    
    public T create(T object);
    public T getById(K id);
    public T update(T object);
    public Boolean delete(K id);
}
