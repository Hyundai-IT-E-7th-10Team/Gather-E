package com.kosa.gather_e.gathercategory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosa.gather_e.gathercategory.service.CategoryService;
import com.kosa.gather_e.gathercategory.vo.CategoryVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {
	
	private final CategoryService categoryService;
	
    /**
     * 카테고리 조회 api (모임 모집 글)
     * @return ResponseEntity<List<CategoryVO>>
     */
	@GetMapping("/category")
	public ResponseEntity<List<CategoryVO>> getAllCategory() {
		return new ResponseEntity<>(categoryService.getAllCategory(), HttpStatus.OK);
	}
	

}
