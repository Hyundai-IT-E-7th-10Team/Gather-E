package com.kosa.gather_e.user_gather.dao;

import com.kosa.gather_e.auth.vo.UserVO;
import com.kosa.gather_e.config.DBConfig;
import com.kosa.gather_e.user_gather.vo.UserGatherVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.util.List;

@Mapper
@ImportAutoConfiguration(DBConfig.class)
public interface UserGatherDAO {

    List<UserVO> selectAllUserByGatherSeq(long gatherSeq);

    void insertUserGather(UserGatherVO userGatherVO);

    void deleteUserGather(UserGatherVO userGatherVO);
}
