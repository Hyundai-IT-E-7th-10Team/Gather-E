package com.kosa.gather.e.test.dao;

import com.kosa.gather.e.test.vo.TestVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestDAO {

    TestVO selectTestVO();
}
