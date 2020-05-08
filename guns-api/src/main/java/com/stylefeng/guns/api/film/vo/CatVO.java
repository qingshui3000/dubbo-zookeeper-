package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 影片分类信息，用于分类检索
 */
@Data
public class CatVO implements Serializable {
    private String catId;
    private String catName;
    private boolean isActive;
}
