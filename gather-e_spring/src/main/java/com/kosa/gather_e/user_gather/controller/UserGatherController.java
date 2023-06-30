package com.kosa.gather_e.user_gather.controller;

import com.kosa.gather_e.auth.vo.UserVO;
import com.kosa.gather_e.user_gather.service.UserGatherService;
import com.kosa.gather_e.user_gather.vo.UserGatherVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user_gather")
@RequiredArgsConstructor
public class UserGatherController {

    private final UserGatherService userGatherService;

    @GetMapping("/{gatherSeq}")
    public ResponseEntity<List<UserVO>> userGather(@PathVariable long gatherSeq) {
        return new ResponseEntity<>(userGatherService.getUsersInGather(gatherSeq), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> join(UserGatherVO vo) {
        userGatherService.joinGather(vo);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity<String> leave(@RequestParam long gatherSeq) {
        UserGatherVO vo = new UserGatherVO();
        vo.setGatherSeq(gatherSeq);
        userGatherService.leaveGather(vo);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
