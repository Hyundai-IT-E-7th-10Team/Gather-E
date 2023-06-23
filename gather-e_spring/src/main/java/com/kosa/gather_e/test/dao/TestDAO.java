package com.kosa.gather_e.test.dao;

import com.kosa.gather_e.test.vo.TestVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestDAO {

    TestVO selectTestVO();
}
