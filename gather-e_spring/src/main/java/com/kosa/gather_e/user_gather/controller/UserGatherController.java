package com.kosa.gather_e.user_gather.controller;

import com.kosa.gather_e.auth.vo.UserVO;
import com.kosa.gather_e.user_gather.service.UserGatherService;
import com.kosa.gather_e.user_gather.vo.UserGatherVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user_gather")
@RequiredArgsConstructor
@Slf4j
public class UserGatherController {

    private final UserGatherService userGatherService;

    @GetMapping("/{gatherSeq}")
    public ResponseEntity<List<UserVO>> userGather(@PathVariable long gatherSeq) {
        log.debug("전체 유저 API 호출" + gatherSeq);
        return new ResponseEntity<>(userGatherService.getUsersInGather(gatherSeq), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserGatherVO> join(@RequestBody UserGatherVO vo) {
        userGatherService.joinGather(vo);
        return new ResponseEntity<>(vo, HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity<UserGatherVO> leave(@RequestParam long gatherSeq) {
        UserGatherVO vo = new UserGatherVO();
        vo.setGatherSeq(gatherSeq);
        userGatherService.leaveGather(vo);
        return new ResponseEntity<>(vo, HttpStatus.ACCEPTED);
    }
}
