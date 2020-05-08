package com.stylefeng.guns.rest.modular.film.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmAsynServiceAPI;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 影片业务实现类
 */
@Component
@Service(interfaceClass = FilmAsynServiceAPI.class)
public class DefaultAsynFilmServiceImpl implements FilmAsynServiceAPI{
    @Autowired
    private MoocFilmInfoTMapper moocFilmInfoTMapper;
    @Autowired
    private MoocActorTMapper moocActorTMapper;

    /**
     * 获取影片描述
     * @param filmId
     * @return
     */
    @Override
    public FilmDescVO getFilmDesc(String filmId) {
        //获取filminfo，填充所需字段返回
        FilmDescVO filmDescVO = new FilmDescVO();
        MoocFilmInfoT filmInfo = getFilmInfo(filmId);
        filmDescVO.setFilmId(filmId);
        filmDescVO.setBiography(filmInfo.getBiography());
        return filmDescVO;
    }

    /**
     * 获取影片图片地址
     * @param filmId
     * @return
     */
    @Override
    public ImgVO getImgs(String filmId) {
        //获取filminfo，填充所需字段返回
        ImgVO imgVO = new ImgVO();
        MoocFilmInfoT filmInfo = getFilmInfo(filmId);
        String[] filmImgs = filmInfo.getFilmImgs().split(",");
        imgVO.setMainImg(filmImgs[0]);
        imgVO.setImg01(filmImgs[1]);
        imgVO.setImg02(filmImgs[2]);
        imgVO.setImg03(filmImgs[3]);
        imgVO.setImg04(filmImgs[4]);
        return imgVO;
    }

    /**
     * 获取演员列表
     * @param filmId
     * @return
     */
    @Override
    public List<ActorVO> getActors(String filmId) {
        List<ActorVO> actors = moocActorTMapper.getActors(filmId);
        return actors;
    }

    /**
     * 获取导演信息
     * @param filmId
     * @return
     */
    @Override
    public ActorVO getDectInfo(String filmId) {
        //获取filminfo，填充所需字段返回
        ActorVO actorVO = new ActorVO();
        MoocFilmInfoT filmInfo = getFilmInfo(filmId);
        MoocActorT moocActorT = moocActorTMapper.selectById(filmInfo.getDirectorId());
        actorVO.setDirectorName(moocActorT.getActorName());
        actorVO.setImgAddress(moocActorT.getActorImg());
        return actorVO;
    }


    /**
     * 工具方法，根据moocFilm填充FilmInfo
     * @param moocFilms
     * @return
     */
    private List<FilmInfo> getFilmInfos(List<MoocFilmT> moocFilms){
        List<FilmInfo> filmInfos = new ArrayList<>();
        for (MoocFilmT moocFilm : moocFilms){
            FilmInfo filmInfo = new FilmInfo();
            filmInfo.setScore(moocFilm.getFilmScore());
            filmInfo.setImgAddress(moocFilm.getImgAddress());
            filmInfo.setFilmType(moocFilm.getFilmType());
            filmInfo.setFilmScore(moocFilm.getFilmScore());
            filmInfo.setFilmName(moocFilm.getFilmName());
            filmInfo.setFilmId("" + moocFilm.getUuid());
            filmInfo.setExpectNum(moocFilm.getFilmPresalenum());
            filmInfo.setBoxNum(moocFilm.getFilmBoxOffice());
            filmInfo.setShowTime(DateUtil.getDay(moocFilm.getFilmTime()));
            filmInfos.add(filmInfo);
        }
        return filmInfos;
    }

    /**
     * 工具方法，根据id获取FilmInfo
     * @param filmId
     * @return
     */
    private MoocFilmInfoT getFilmInfo(String filmId){
        MoocFilmInfoT moocFilmInfoT = new MoocFilmInfoT();
        moocFilmInfoT.setFilmId(filmId);
        moocFilmInfoT = moocFilmInfoTMapper.selectOne(moocFilmInfoT);
        return moocFilmInfoT;
    }
}
