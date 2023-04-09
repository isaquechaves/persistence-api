package com.fatec.stacktec.persistenceapi.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fatec.stacktec.persistenceapi.model.util.BaseModel;

public interface CrudService<T extends BaseModel> {
	
	List<T> findAll();
	
	Page<T> findAll(Pageable pageable);
	
	List<T> findAllById(List<Long> IDS);
	
	T findById(Long ID);
	
	T findById(String ID);
	
	T findById(Object ID);
	
	T getOne(Long ID);
	
	T getOne(Object ID);
	
	T createElement(T t);
	
	List<T> createElements(List<T> o);
	
	T updateElement(Long ID, T t);
	
	boolean deleteElement(Long ID);
	
	boolean deleteElement(T element);
	
	void deleteAll();
	
	long count();

}
