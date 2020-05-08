package com.stylefeng.guns.rest.modular.film.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
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
@Service(interfaceClass = FilmServiceAPI.class)
public class DefaultFilmServiceImpl implements FilmServiceAPI{
    @Autowired
    private MoocBannerTMapper moocBannerTMapper;
    @Autowired
    private MoocFilmTMapper moocFilmTMapper;
    @Autowired
    private MoocCatDictTMapper moocCatDictTMapper;
    @Autowired
    private MoocSourceDictTMapper moocSourceDictTMapper;
    @Autowired
    private MoocYearDictTMapper moocYearDictTMapper;
    @Autowired
    private MoocFilmInfoTMapper moocFilmInfoTMapper;
    @Autowired
    private MoocActorTMapper moocActorTMapper;
    /**
     * 获取轮播图
     * @return
     */
    @Override
    public List<BannerVO> getBanners() {
        List<BannerVO> result = new ArrayList<>();
        List<MoocBannerT> banners = moocBannerTMapper.selectList(null);
        for(MoocBannerT moocBannerT : banners){
            BannerVO bannerVO = new BannerVO();
            bannerVO.setBannerId("" + moocBannerT.getUuid());
            bannerVO.setBannerUrl(moocBannerT.getBannerUrl());
            bannerVO.setBannerAddress(moocBannerT.getBannerAddress());
            result.add(bannerVO);
        }
        return result;
    }

    /**
     * 获取热映影片，首页和非首页都会请求该接口
     * @param isLimit
     * @param nums
     * @return
     */
    @Override
    public FilmVO getHotFilms(boolean isLimit, int nums,int nowPage,int sortId,int catId,int sourceId,int yearId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","1");
        //判断是否是首页请求
        if(isLimit){
            //是首页，则限制条数，限制内容为热映影片
            Page<MoocFilmT> page = new Page<>(1,nums);
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
            //拼装FilmInfos
            filmInfos = getFilmInfos(moocFilms);
            filmVO.setFilmNum(moocFilms.size());
            filmVO.setFilmInfo(filmInfos);
        }else {
            //非首页，不限制条数，但同样只能为热映影片
            Page<MoocFilmT> page = null;
            //排序，1-票房，2-上映时间，3-评分
            switch (sortId){
                case 1:
                    page = new Page<>(nowPage,nums,"film_box_office");
                    break;
                case 2:
                    page = new Page<>(nowPage,nums,"film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage,nums,"film_score");
                    break;
                default:
                    page = new Page<>(nowPage,nums,"film_box_office");
                    break;
            }
            if(catId != 99){
                //注意一个影片属于多个分类
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats",catStr);
            }
            if(sourceId != 99){
                entityWrapper.eq("film_source",sourceId);
            }
            if(yearId != 99){
                entityWrapper.eq("film_date",yearId);
            }
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
            //拼装FilmInfos
            filmInfos = getFilmInfos(moocFilms);
            filmVO.setFilmNum(moocFilms.size());
            //需要总页数
            int totalCount = moocFilmTMapper.selectCount(entityWrapper);
            int totalPage = (totalCount / nums) + 1;
            filmVO.setFilmInfo(filmInfos);
            filmVO.setNowPage(nowPage);
            filmVO.setTotalPage(totalPage);
        }
        return filmVO;
    }

    @Override
    public FilmVO getSoonFilms(boolean isLimit, int nums,int nowPage,int sortId,int catId,int sourceId,int yearId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","2");
        //判断是否是首页请求
        if(isLimit){
            //是首页，则限制条数，限制内容为即将上映影片
            Page<MoocFilmT> page = new Page<>(1,nums);
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
            //拼装FilmInfos
            filmInfos = getFilmInfos(moocFilms);
            filmVO.setFilmNum(moocFilms.size());
            filmVO.setFilmInfo(filmInfos);
        }else {
            //非首页，不限制条数，但同样只能为即将上映影片
            Page<MoocFilmT> page = null;
            //排序，1-票房，2-上映时间，3-评分
            switch (sortId){
                case 1:
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                    break;
                case 2:
                    page = new Page<>(nowPage,nums,"film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                    break;
                default:
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                    break;
            }
            if(catId != 99){
                //注意一个影片属于多个分类
                String catStr = "%#"+catId+"#%";
                entityWrapper.like("film_cats",catStr);
            }
            if(sourceId != 99){
                entityWrapper.eq("film_source",sourceId);
            }
            if(yearId != 99){
                entityWrapper.eq("film_date",yearId);
            }
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
            //拼装FilmInfos
            filmInfos = getFilmInfos(moocFilms);
            filmVO.setFilmNum(moocFilms.size());
            //需要总页数
            int totalCount = moocFilmTMapper.selectCount(entityWrapper);
            int totalPage = (totalCount / nums) + 1;
            filmVO.setFilmInfo(filmInfos);
            filmVO.setNowPage(nowPage);
            filmVO.setTotalPage(totalPage);
        }
        return filmVO;
    }

    /**
     * 获取经典影片
     * @param nums
     * @param nowPage
     * @param sortId
     * @param catId
     * @param sourceId
     * @param yearId
     * @return
     */
    @Override
    public FilmVO getClassicFilms(int nums, int nowPage, int sortId, int catId, int sourceId, int yearId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","3");
        Page<MoocFilmT> page = null;
        //排序，1-票房，2-上映时间，3-评分
        switch (sortId){
            case 1:
                page = new Page<>(nowPage,nums,"film_preSaleNum");
                break;
            case 2:
                page = new Page<>(nowPage,nums,"film_time");
                break;
            case 3:
                page = new Page<>(nowPage,nums,"film_preSaleNum");
                break;
            default:
                page = new Page<>(nowPage,nums,"film_preSaleNum");
                break;
        }
        if(catId != 99){
            //注意一个影片属于多个分类
            String catStr = "%#" + catId + "#%";
            entityWrapper.like("film_cats",catStr);
        }
        if(sourceId != 99){
            entityWrapper.eq("film_source",sourceId);
        }
        if(yearId != 99){
            entityWrapper.eq("film_date",yearId);
        }
        List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
        //拼装FilmInfos
        filmInfos = getFilmInfos(moocFilms);
        filmVO.setFilmNum(moocFilms.size());
        //需要总页数
        int totalCount = moocFilmTMapper.selectCount(entityWrapper);
        int totalPage = (totalCount / nums) + 1;
        filmVO.setFilmInfo(filmInfos);
        filmVO.setNowPage(nowPage);
        filmVO.setTotalPage(totalPage);
        return filmVO;
    }

    /**
     * 票房排行
     * @return
     */
    @Override
    public List<FilmInfo> getBoxRanking() {
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status",1);
        Page<MoocFilmT> page = new Page<>(1,10,"film_box_office");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page,entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    /**
     * 最受欢迎（预售）排行
     * @return
     */
    @Override
    public List<FilmInfo> getExpectRanking() {
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status",2);
        Page<MoocFilmT> page = new Page<>(1,10,"film_preSaleNum");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page,entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    /**
     * 在映影片前10
     * @return
     */
    @Override
    public List<FilmInfo> getTop() {
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status",1);
        Page<MoocFilmT> page = new Page<>(1,10,"film_score");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page,entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    /**
     * 查询分类
     * @return
     */
    @Override
    public List<CatVO> getCats() {
        List<CatVO> cats = new ArrayList<>();
        //1.查询实体对象 -> MoocCatDictT
        List<MoocCatDictT> moocCatDictTS = moocCatDictTMapper.selectList(null);
        //2.转换为业务对象 -> CatVO 返回
        for(MoocCatDictT moocCatDictT : moocCatDictTS){
            CatVO catVO = new CatVO();
            catVO.setCatId(""+moocCatDictT.getUuid());
            catVO.setCatName(moocCatDictT.getShowName());
            cats.add(catVO);
        }
        return cats;
    }


    /**
     * 查询片源
     * @return
     */
    @Override
    public List<SourceVO> getSources() {
        List<SourceVO> sources = new ArrayList<>();
        //1.查询实体对象
        List<MoocSourceDictT> moocSourceDictTS = moocSourceDictTMapper.selectList(null);
        //2.转换为业务对象
        for(MoocSourceDictT moocSourceDictT : moocSourceDictTS){
            SourceVO sourceVO = new SourceVO();
            sourceVO.setSourceId(""+moocSourceDictT.getUuid());
            sourceVO.setSourceName(moocSourceDictT.getShowName());
            sources.add(sourceVO);
        }
        return sources;
    }

    /**
     * 查询年代
     * @return
     */
    @Override
    public List<YearVO> getYears() {
        List<YearVO> years = new ArrayList<>();
        //1.查询实体对象
        List<MoocYearDictT> moocSourceDictTS = moocYearDictTMapper.selectList(null);
        //2.转换为业务对象
        for(MoocYearDictT moocYearDictT : moocSourceDictTS){
            YearVO yearVO = new YearVO();
            yearVO.setYearId(""+moocYearDictT.getUuid());
            yearVO.setYearName(moocYearDictT.getShowName());
            years.add(yearVO);
        }
        return years;
    }

    /**
     * 联合查询影片信息
     * @param searchType
     * @param searchParam
     * @return
     */
    @Override
    public FilmDetailVO getFilmDetail(int searchType, String searchParam) {
        //searchType-1:按名字查询
        //searchType-2:按ID查询
        FilmDetailVO filmDetailVO = null;
        if(searchType == 1){
            String searchStr = "%" + searchParam + "%";
            filmDetailVO = moocFilmTMapper.getFilmDetailByName(searchStr);
        }
        if(searchType == 2){
            filmDetailVO = moocFilmTMapper.getFilmDetailById(searchParam);
        }
        return filmDetailVO;
    }

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
