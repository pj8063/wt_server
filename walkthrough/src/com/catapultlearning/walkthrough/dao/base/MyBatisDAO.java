package com.catapultlearning.walkthrough.dao.base;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;

public abstract class MyBatisDAO<T, K> extends SqlSessionDaoSupport {
    
    private static final String SQL_NAME_SPACE_SEPARATOR = ".";
    private static final String SQL_ID_CREATE = "create";
    private static final String SQL_ID_UPDATE = "update";
    private static final String SQL_ID_DELETE = "delete";
    private static final String SQL_ID_GET_BY_ID = "getById";

    
    @SuppressWarnings("unchecked")
    private Class<T> getActuallClassType(){
        Class<T> classType = null;
        ParameterizedType parameterizedType = (ParameterizedType)this.getClass().getGenericSuperclass();
        classType = (Class<T>)parameterizedType.getActualTypeArguments()[0];
        return classType;
    }
    
    protected String getSqlNameSpace(){
        String sqlNameSpace = StringUtils.EMPTY;
        Class<T> classType = this.getActuallClassType();
        if(classType!=null){
            sqlNameSpace= classType.getName();
        }
        return sqlNameSpace+ SQL_NAME_SPACE_SEPARATOR;
    }
    
    public T create(T object){
        this.getSqlSession().insert(this.getSqlNameSpace()+ SQL_ID_CREATE, object);
        return object;
    }
    
    @SuppressWarnings("unchecked")
    public T getById(K id){
        T result = null;
        result = (T)this.getSqlSession().selectOne(this.getSqlNameSpace()+ SQL_ID_GET_BY_ID, id);
        return result;
    }
    
    public T update(T object){
        this.getSqlSession().update(this.getSqlNameSpace() + SQL_ID_UPDATE, object);
        return object;
    }
    
    public Boolean delete(K id){
        Boolean result = Boolean.TRUE;
        this.getSqlSession().delete(this.getSqlNameSpace() + SQL_ID_DELETE, id);
        return result;
    }
    
    public List<Map<String, Object>> selectList(String queryName, Object parameter){
        List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
        result = this.getSqlSession().selectList(this.getSqlNameSpace()+ queryName, parameter);
        return result;
    }
}
