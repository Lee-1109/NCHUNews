package com.example.nchunews.dao;

import java.io.Serializable;
import java.util.List;

public interface IBaseDAO<M> {
    void insert(Serializable serializable);
    void delete(Serializable deleteObj);
    void update(Serializable oldObj,Serializable newObj);
    List<M> queryAll();
}
