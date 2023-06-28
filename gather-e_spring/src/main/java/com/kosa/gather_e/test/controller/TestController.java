package com.kosa.gather_e.test.controller;

import com.kosa.gather_e.test.service.TestService;
import com.kosa.gather_e.test.vo.TestVO;
import com.kosa.gather_e.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/test")
    public ResponseEntity<TestVO> test() {
        System.out.println(SecurityUtil.getCurrentMemberSeq());
        return new ResponseEntity<>(testService.getTestVO(), HttpStatus.NOT_FOUND);
    }

}
