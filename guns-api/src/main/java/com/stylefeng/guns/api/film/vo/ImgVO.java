package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 电影图片VO
 * 1张主图、4张小图
 */
@Data
public class ImgVO  implements Serializable {
    private String mainImg;
    private String img01;
    private String img02;
    private String img03;
    private String img04;
}
