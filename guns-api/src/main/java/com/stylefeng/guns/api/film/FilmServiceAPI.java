package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

public interface FilmServiceAPI {
    //获取banners
    List<BannerVO> getBanners();
    //获取热映影片
    FilmVO getHotFilms(boolean isLimit,int nums,int nowPage,int sortId,int catId,int sourceId,int yearId);
    //获取即将上映影片[按受欢迎程度排序]
    FilmVO getSoonFilms(boolean isLimit,int nums,int nowPage,int sortId,int catId,int sourceId,int yearId);
    //获取经典影片
    FilmVO getClassicFilms(int nums,int nowPage,int sortId,int catId,int sourceId,int yearId);
    //获取票房排行
    List<FilmInfo> getBoxRanking();
    //获取人气排行
    List<FilmInfo> getExpectRanking();
    //获取top100
    List<FilmInfo> getTop();
    //====获取影片条件系列接口
    //1.获取影片分类
    List<CatVO> getCats();
    //2.获取影片来源
    List<SourceVO> getSources();
    //3.获取影片年代
    List<YearVO> getYears();

    //根据影片id或名称获取影片信息
    FilmDetailVO getFilmDetail(int searchType,String searchParam);
    //获取影片描述信息
    FilmDescVO getFilmDesc(String filmId);
    //获取图片信息
    ImgVO getImgs(String filmId);
    //获取演员信息
    List<ActorVO> getActors(String filmId);
    //获取导演信息
    ActorVO getDectInfo(String filmId);
}
