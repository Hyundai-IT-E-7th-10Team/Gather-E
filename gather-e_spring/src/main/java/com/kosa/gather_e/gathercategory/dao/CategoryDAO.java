package com.kosa.gather_e.gathercategory.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kosa.gather_e.gathercategory.vo.CategoryVO;

@Mapper
public interface CategoryDAO {
	
	List<CategoryVO> selectAllCategory();

}
