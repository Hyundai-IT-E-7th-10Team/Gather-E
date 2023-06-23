package com.kosa.gather_e.test.service;

import com.kosa.gather_e.test.dao.TestDAO;
import com.kosa.gather_e.test.vo.TestVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestDAO testDAO;

    public TestVO getTestVO() {
        return testDAO.selectTestVO();
    };
}
