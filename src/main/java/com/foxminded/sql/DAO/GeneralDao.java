package com.foxminded.sql.DAO;


import java.util.List;

public interface GeneralDao<E, ID> {

    void save(List<E> entities);

    List<E> findAll(Integer offset, Integer limit);

    List<E> findById(ID id);

    void deleteById(ID id);
}
