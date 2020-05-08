package com.stylefeng.guns.rest.modular.film;

import com.stylefeng.guns.api.film.FilmAsynServiceAPI;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmRequestVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/film/")
public class FilmController {
    @Reference(interfaceClass = FilmServiceAPI.class)
    private FilmServiceAPI filmServiceAPI;

    @Reference(interfaceClass = FilmAsynServiceAPI.class,async = true)
    private FilmAsynServiceAPI filmAsynServiceAPI ;
    private static final String imgPre = "http://img.meetingshop.cn";
    /**
     * 首页接口
     * API网关
     *  优点：
     *      1.功能聚合，节省http请求
     *      2.多个业务只暴露一个接口，便于进行前后端分离开发
     *  缺点：
     *      1.一次获取的数据太多
     * @return
     */
    @GetMapping("getIndex")
    public ResponseVO getIndex(){
        //1.获取banner
        List<BannerVO> banners = filmServiceAPI.getBanners();
        //2.获取热门影片
        FilmVO hotFilms = filmServiceAPI.getHotFilms(true,8,1,1,99,99,99);
        //3.获取即将上映
        FilmVO soonFilms = filmServiceAPI.getSoonFilms(true, 8,1,1,99,99,99);
        //4.票房排行榜
        List<FilmInfo> boxRanking = filmServiceAPI.getBoxRanking();
        //5.获取受欢迎排行榜
        List<FilmInfo> expectRanking = filmServiceAPI.getExpectRanking();
        //6.获取前一百
        List<FilmInfo> top = filmServiceAPI.getTop();
        FilmIndexVO filmIndexVO = new FilmIndexVO();
        filmIndexVO.setBanners(banners);
        filmIndexVO.setHotFilms(hotFilms);
        filmIndexVO.setSoonFilms(soonFilms);
        filmIndexVO.setBoxRanking(boxRanking);
        filmIndexVO.setExpectRanking(expectRanking);
        filmIndexVO.setTop100(top);
        return ResponseVO.success(filmIndexVO,imgPre);
    }

    /**
     * 条件筛选影片
     * @param catId
     * @param sourceId
     * @param yearId
     * @return
     */
    @GetMapping("getConditionList")
    public ResponseVO getConditionList(@RequestParam(name = "catId",required = false,defaultValue = "99")String catId,
                                       @RequestParam(name = "sourceId",required = false,defaultValue = "99")String sourceId,
                                       @RequestParam(name = "yearId",required = false,defaultValue = "99")String yearId){
        FilmConditionVO filmConditionVO = new FilmConditionVO();
        boolean flag = false;
        //1.影片分类集合
            //判断是否指定了catId，如果存在则将对应的实体设置为启用
            //没有指定就返回全部
        List<CatVO> cats = filmServiceAPI.getCats();
        List<CatVO> catResult = new ArrayList<>();
        CatVO cat = new CatVO();
        for(CatVO catVO : cats){
            if(catVO.getCatId().equals("99")){
                cat = catVO;
                continue;
            }
            if(catVO.getCatId ().equals(catId)){
                flag = true;
                catVO.setActive(true);
            }else {
                catVO.setActive(false);
            }
            catResult.add(catVO);
        }
        if(!flag){
            cat.setActive(true);
            catResult.add(cat);
        }else {
            cat.setActive(false);
            catResult.add(cat);
        }
        //2.片源集合
            //同上，判断
        flag = false;
        List<SourceVO> sources = filmServiceAPI.getSources();
        List<SourceVO> sourceResult = new ArrayList<>();
        SourceVO source = new SourceVO();
        for(SourceVO sourceVO : sources){
            if(sourceVO.getSourceId().equals("99")){
                source = sourceVO;
                continue;
            }
            if(sourceVO.getSourceId ().equals(sourceId)){
                flag = true;
                sourceVO.setActive(true);
            }else {
                sourceVO.setActive(false);
            }
            sourceResult.add(sourceVO);
        }
        if(!flag){
            source.setActive(true);
            sourceResult.add(source);
        }else {
            source.setActive(false);
            sourceResult.add(source);
        }
        //3.年代集合
            //同上，判断
        flag = false;
        List<YearVO> years = filmServiceAPI.getYears();
        List<YearVO> yearResult = new ArrayList<>();
        YearVO year = new YearVO();
        for(YearVO yearVO : years){
            if(yearVO.getYearId().equals("99")){
                year = yearVO;
                continue;
            }
            if(yearVO.getYearId ().equals(catId)){
                flag = true;
                yearVO.setActive(true);
            }else {
                yearVO.setActive(false);
            }
            yearResult.add(yearVO);
        }
        if(!flag){
            year.setActive(true);
            yearResult.add(year);
        }else {
            year.setActive(false);
            yearResult.add(year);
        }
        filmConditionVO.setCatInfo(catResult);
        filmConditionVO.setSourceInfo(sourceResult);
        filmConditionVO.setYearInfo(yearResult);
        return ResponseVO.success(filmConditionVO);
    }

    @GetMapping("getFilms")
    public ResponseVO getFilms(FilmRequestVO filmRequestVO){
        FilmVO filmVO = null;
        //根据showType判断查询影片的类型
        switch (filmRequestVO.getShowType()){
            case 1:
                filmVO = filmServiceAPI.getHotFilms(false,filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getCatId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId());
                break;
            case 2:
                filmVO = filmServiceAPI.getSoonFilms(false,filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getCatId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId());
                break;
            case 3:
                filmVO = filmServiceAPI.getClassicFilms(filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getCatId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId());
                break;
            default:
                filmVO = filmServiceAPI.getHotFilms(false,filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getCatId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId());
                break;
        }
        return ResponseVO.success(filmVO.getFilmInfo(),imgPre,filmVO.getNowPage(),filmVO.getTotalPage());
    }

    @GetMapping("films/{searchParam}")
    public ResponseVO film(@PathVariable("searchParam") String searchParam,
                            int searchType)throws ExecutionException,InterruptedException{
        //根据searchType判断查询的类型并查询影片信息
        FilmDetailVO filmDetail = filmServiceAPI.getFilmDetail(searchType, searchParam);
        if(filmDetail == null){
            return ResponseVO.serviceFail("没有可查询的影片！！");
        }else if(filmDetail.getFilmId() == null || filmDetail.getFilmId().trim().length() == 0){
            return ResponseVO.serviceFail("没有可查询的影片！！");
        }
        String filmId = filmDetail.getFilmId();
        //==异步调用
        //1.获取影片描述信息
        filmAsynServiceAPI.getFilmDesc(filmId);
        Future<FilmDescVO> filmDescVOFuture = RpcContext.getContext().getFuture();
        //2.获取图片信息
        filmAsynServiceAPI.getImgs(filmId);
        Future<ImgVO> imgVOFuture = RpcContext.getContext().getFuture();
        //3.获取演员信息
        filmAsynServiceAPI.getActors(filmId);
        Future<List<ActorVO>> actorsFuture = RpcContext.getContext().getFuture();
        //4.获取导演信息
        filmAsynServiceAPI.getDectInfo(filmId);
        Future<ActorVO> dectInfoFuture = RpcContext.getContext().getFuture();

        //组织演员属性
        ActorRequestVO actorRequestVO = new ActorRequestVO();
        actorRequestVO.setActors(actorsFuture.get());
        actorRequestVO.setDirector(dectInfoFuture.get());
        //组织info属性
        InfoRequestVO infoRequestVO = new InfoRequestVO();
        infoRequestVO.setActors(actorRequestVO);
        infoRequestVO.setImgVO(imgVOFuture.get());
        infoRequestVO.setFilmId(filmId);
        infoRequestVO.setBiography(filmDescVOFuture.get().getBiography());
        //填入filmDetail
        filmDetail.setInfo04(infoRequestVO);
        return ResponseVO.success(filmDetail,imgPre);
    }

}
