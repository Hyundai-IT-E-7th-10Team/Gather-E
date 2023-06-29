package com.kosa.gather_e.gathercategory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kosa.gather_e.gathercategory.dao.CategoryDAO;
import com.kosa.gather_e.gathercategory.vo.CategoryVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceImpl {

	private final CategoryDAO categoryDAO;

	@Override
	public List<CategoryVO> getAllCategory() {
		return categoryDAO.selectAllCategory();
		
	}
	

}
