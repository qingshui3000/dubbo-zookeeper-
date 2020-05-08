package com.stylefeng.guns.api.cinema;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.vo.*;

import java.util.List;

public interface CinemaServiceAPI {
    //1.根据CinemaQueryVO查询影院列表
    Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO);
    //2.根据品牌id获取品牌列表
    List<BrandVO> getBrands(int brandId);
    //3.根据区域id获取区域列表
    List<AreaVO> getAreas(int areaId);
    //4.获取影厅类型列表
    List<HallTypeVO> getHallTypes(int hallTypes);
    //5.根据影院id获取影院信息
    CinemaInfoVO getCinemaInfoById(int cinemaId);
    //6.根据影院id获取所有电影的信息和对应的放映场次的信息
    FilmInfoVO getFilmInfoByCinemaId(int cinemaId);
    //7.根据放映场次的id获取放映信息
    FilmFieldVO getFilmFieldInfo(int fieldId);
    //8.根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
    FilmInfoVO getFilmInfoByFieldId(int fieldId);
}
