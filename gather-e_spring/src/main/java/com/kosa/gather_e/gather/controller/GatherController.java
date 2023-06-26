package com.kosa.gather_e.gather.controller;


import com.kosa.gather_e.gather.service.GatherService;
import com.kosa.gather_e.gather.vo.GatherVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GatherController {

    private final GatherService gatherService;

    @PostMapping("/gather")
    public ResponseEntity<GatherVO> createGather(@RequestBody GatherVO gatherVO){
        return new ResponseEntity<>(gatherService.createGather(gatherVO), HttpStatus.CREATED);
    }

    @GetMapping("/gather")
    public ResponseEntity<List<GatherVO>> getAllGather(){
        return new ResponseEntity<>(gatherService.getAllGather(), HttpStatus.OK);
    }

    @GetMapping("/gather/{gatherSeq}")
    public ResponseEntity<GatherVO> getAllGather(@PathVariable int gatherSeq){
        return new ResponseEntity<>(gatherService.getOneGather(gatherSeq), HttpStatus.OK);
    }

    @DeleteMapping("/gather/{gatherSeq}")
    public ResponseEntity<String> deleteGather(@PathVariable int gatherSeq){
        return new ResponseEntity<>(gatherService.deleteGather(gatherSeq),HttpStatus.OK);
    }

    @PutMapping("/gather/{gatherSeq}")
    public ResponseEntity<GatherVO> updateOneGather(@RequestBody GatherVO gatherVO,  @PathVariable int gatherSeq){
        gatherVO.setGatherSeq(gatherSeq);
        System.out.println("updateVO" + gatherVO);
        return new ResponseEntity<>(gatherService.updateGather(gatherVO), HttpStatus.OK);
    }

}
