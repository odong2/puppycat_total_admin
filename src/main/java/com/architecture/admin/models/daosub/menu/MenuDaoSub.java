package com.architecture.admin.models.daosub.menu;

import com.architecture.admin.models.dto.menu.MenuDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/*****************************************************
 * 메뉴
 ****************************************************/
@Repository
@Mapper
public interface MenuDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    List<MenuDto> getList(MenuDto params);
    List<MenuDto> getLeftList(Integer idx);
    Integer getLastSort(MenuDto params);
    MenuDto getCateInfo(Integer idx);
    Integer getNextSort(MenuDto menuDto);
    Integer getPrevSort(MenuDto menuDto);
    Integer getMenuLevel(Integer idx);

}
