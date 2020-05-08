package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 演员VO类
 */
@Data
public class ActorVO implements Serializable {
    private String imgAddress;
    private String directorName;
    private String roleName;
}
