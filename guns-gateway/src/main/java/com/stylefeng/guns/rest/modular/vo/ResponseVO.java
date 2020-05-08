package com.stylefeng.guns.rest.modular.vo;

import lombok.Data;

@Data
public class ResponseVO<M> {
    //0-成功;1-失败;999-系统异常
    private int statu;
    private String msg;
    private M data;
    //图片地址
    private String imgPre;
    //分页参数
    private int nowPage;
    private int totalPage;
    public static<M> ResponseVO success(M m){
        ResponseVO<M> responseVO = new ResponseVO<>();
        responseVO.setStatu(0);
        responseVO.setData(m);
        return responseVO;
    }
    public static<M> ResponseVO success(M m,String imgPre){
        ResponseVO<M> responseVO = new ResponseVO<>();
        responseVO.setStatu(0);
        responseVO.setData(m);
        responseVO.setImgPre(imgPre);
        return responseVO;
    }

    public static<M> ResponseVO success(M m,String imgPre,int nowPage,int totalPage){
        ResponseVO<M> responseVO = new ResponseVO<>();
        responseVO.setStatu(0);
        responseVO.setData(m);
        responseVO.setImgPre(imgPre);
        responseVO.setNowPage(nowPage);
        responseVO.setTotalPage(totalPage);
        return responseVO;
    }

    public static<M> ResponseVO success(String msg){
        ResponseVO<M> responseVO = new ResponseVO<>();
        responseVO.setStatu(0);
        responseVO.setMsg(msg);
        return responseVO;
    }

    public static<M> ResponseVO serviceFail(String msg){
        ResponseVO<M> responseVO = new ResponseVO<>();
        responseVO.setStatu(1);
        responseVO.setMsg(msg);
        return responseVO;
    }

    public static<M> ResponseVO appFail(String msg){
        ResponseVO<M> responseVO = new ResponseVO<>();
        responseVO.setStatu(999);
        responseVO.setMsg(msg);
        return responseVO;
    }

}
