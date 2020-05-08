package com.stylefeng.guns.rest.modular.cinema.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.MoocBrandDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocBrandDictT;
import com.stylefeng.guns.rest.common.persistence.model.MoocCinemaT;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Service(interfaceClass = CinemaServiceAPI.class)
public class DefaultCinemaServiceImpl implements CinemaServiceAPI {
    @Autowired
    private MoocCinemaTMapper moocCinemaTMapper;
    @Autowired
    private MoocBrandDictTMapper moocBrandDictTMapper;
    /**
     * 根据CinemaQueryVO查询影院列表
     * @param cinemaQueryVO
     * @return
     */
    @Override
    public Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO) {
        Page<MoocCinemaT> page = new Page<>(cinemaQueryVO.getNowPage(),cinemaQueryVO.getPageSize());
        List<CinemaVO> cinemas = new ArrayList<>();
        //条件筛选
        EntityWrapper<MoocCinemaT> entityWrapper = new EntityWrapper<>();
        if(cinemaQueryVO.getBrandId() != 99){
            entityWrapper.eq("brand_id",cinemaQueryVO.getBrandId());
        }
        if(cinemaQueryVO.getDistrictId() != 99){
            entityWrapper.eq("area_id",cinemaQueryVO.getDistrictId());
        }
        if(cinemaQueryVO.getHallType() != 99){
            entityWrapper.like("hall_ids","%#" + cinemaQueryVO.getHallType() + "#%");
        }
        //数据实体转换为业务实体
        List<MoocCinemaT> moocCinemas = moocCinemaTMapper.selectPage(page, entityWrapper);
        for(MoocCinemaT moocCinemaT : moocCinemas){
            CinemaVO cinemaVO = new CinemaVO();
            cinemaVO.setUuid(""+moocCinemaT.getUuid());
            cinemaVO.setMinimumPrice(""+moocCinemaT.getMinimumPrice());
            cinemaVO.setCinemaName(moocCinemaT.getCinemaName());
            cinemaVO.setAddress(moocCinemaT.getCinemaAddress());
            cinemas.add(cinemaVO);
        }
        //根据条件，判断影院列表总数,用于分页
        Integer count = moocCinemaTMapper.selectCount(entityWrapper);
        Page<CinemaVO> result = new Page<>();
        result.setRecords(cinemas);
        result.setSize(cinemaQueryVO.getPageSize());
        result.setTotal(count);
        return result;
    }

    /**
     * 根据品牌id获取品牌列表
     * @param brandId
     * @return
     */
    @Override
    public List<BrandVO> getBrands(int brandId) {
        List<BrandVO> brandVOS = new ArrayList<>();
        boolean flag = false;
        //查询判断是否存在该品牌，不存在就默认99
        MoocBrandDictT moocBrandDictT = moocBrandDictTMapper.selectById(brandId);
        if(brandId == 99 || moocBrandDictT == null || moocBrandDictT.getUuid() == null){
            flag = true;
        }
        //查询所有列表
        List<MoocBrandDictT> moocBrandDictTS = moocBrandDictT.selectList(null);
        //判断flag，置active选中
        for(MoocBrandDictT brand : moocBrandDictTS){
            BrandVO brandVO = new BrandVO();
            brandVO.setBrandId(""+brand.getUuid());
            brandVO.setBrandName(brand.getShowName());
            if(flag){
                if(brand.getUuid() == 99){
                    brandVO.setActive(true);
                }
            }else{
                if(brand.getUuid() == brandId){
                    brandVO.setActive(true);
                }
            }
            brandVOS.add(brandVO);
        }
        return brandVOS;
    }

    /**
     * 根据区域id获取区域列表
     * @param areaId
     * @return
     */
    @Override
    public List<AreaVO> getAreas(int areaId) {
        return null;
    }

    /**
     * 获取影厅类型列表
     * @param hallTypes
     * @return
     */
    @Override
    public List<HallTypeVO> getHallTypes(int hallTypes) {
        return null;
    }

    /**
     * 根据影院id获取影院信息
     * @param cinemaId
     * @return
     */
    @Override
    public CinemaInfoVO getCinemaInfoById(int cinemaId) {
        return null;
    }

    /**
     * 根据影院id获取所有电影的信息和对应的放映场次的信息
     * @param cinemaId
     * @return
     */
    @Override
    public FilmInfoVO getFilmInfoByCinemaId(int cinemaId) {
        return null;
    }

    /**
     * 根据放映场次的id获取放映信息
     * @param fieldId
     * @return
     */
    @Override
    public FilmFieldVO getFilmFieldInfo(int fieldId) {
        return null;
    }

    /**
     * 根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
     * @param fieldId
     * @return
     */
    @Override
    public FilmInfoVO getFilmInfoByFieldId(int fieldId) {
        return null;
    }
}
