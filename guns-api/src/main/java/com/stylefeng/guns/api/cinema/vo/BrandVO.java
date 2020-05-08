package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 影院所属品牌VO
 */
@Data
public class BrandVO implements Serializable {
    private String brandId;
    private String brandName;
    private boolean isActive;
}
