package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 影片来源VO，用于分类检索
 */
@Data
public class SourceVO implements Serializable {
    private String sourceId;
    private String sourceName;
    private boolean isActive;
}
