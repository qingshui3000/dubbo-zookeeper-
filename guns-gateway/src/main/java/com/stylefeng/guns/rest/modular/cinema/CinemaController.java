package com.stylefeng.guns.rest.modular.cinema;

import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.CinemaQueryVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cinema/")
public class CinemaController {
    @Reference(interfaceClass = CinemaServiceAPI.class)
    private CinemaServiceAPI cinemaServiceAPI;

    /**
     * 条件查询影院列表
     * @param cinemaQueryVO
     * @return
     */
    @GetMapping("getCinemas")
    public ResponseVO getCinemas(CinemaQueryVO cinemaQueryVO){
        return ResponseVO.success("");
    }

    /**
     * 获取条件列表
     *      品牌
     *      行政区域
     *      影厅类型
     * @param cinemaQueryVO
     * @return
     */
    @GetMapping("getCondition")
    public ResponseVO getCondition(CinemaQueryVO cinemaQueryVO){
        return ResponseVO.success("");
    }

    /**
     * 获取播放场次
     * @param cinemaId
     * @return
     */
    @GetMapping("getFields")
    public ResponseVO getFields(Integer cinemaId){
        return ResponseVO.success("");
    }

    /**
     * 获取播放场次的具体信息
     * @param cinemaId
     * @param fieldId
     * @return
     */
    @PostMapping("getFieldInfo")
    public ResponseVO getFieldInfo(Integer cinemaId,Integer fieldId){
        return ResponseVO.success("");
    }
}
