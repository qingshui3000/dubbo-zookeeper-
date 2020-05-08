package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 场次详细信息
 */
@Data
public class HallInfoVO implements Serializable {
    private String hallFieldId;
    private String hallName;
    private String price;
    private String seatFile;
    private String soldSeats;
}
