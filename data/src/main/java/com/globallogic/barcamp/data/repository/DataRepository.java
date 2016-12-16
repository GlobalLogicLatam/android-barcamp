package com.globallogic.barcamp.data.repository;

/**
 * Created by Gonzalo.Martin on 10/18/2016
 */

public interface DataRepository<T> {

    void create(T data);

    void update(T data);

    void delete(T data);

    void delete(String id);

}
