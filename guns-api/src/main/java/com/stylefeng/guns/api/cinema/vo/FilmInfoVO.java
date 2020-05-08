package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 影片详细信息，和film模块的详细信息对象有所区别，对应的数据库表也不一样
 */
@Data
public class FilmInfoVO implements Serializable {
    private String filmId;
    private String filmName;
    private String filmLength;
    private String filmType;
    private String filmCats;
    private String actors;
    private String imgAddress;
    private List<FilmFieldVO> filmFields;
}
