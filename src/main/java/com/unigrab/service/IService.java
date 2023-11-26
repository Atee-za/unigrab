package com.unigrab.service;

import java.util.List;

public interface IService <T, t> {
    T save(T t);
    T find(String id);
    List<T> findAll();
    T update(T t);
    boolean delete(String id);
}
