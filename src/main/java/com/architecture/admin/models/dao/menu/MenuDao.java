package com.architecture.admin.models.dao.menu;

import com.architecture.admin.models.dto.menu.MenuDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/*****************************************************
 * 메뉴
 ****************************************************/
@Repository
@Mapper
public interface MenuDao {
    /*****************************************************
     * Insert
     ****************************************************/
    Integer insert(MenuDto menu);
    Integer insertName(MenuDto menuName);
    /*****************************************************
     * Update
     ****************************************************/
    Integer updateCate(MenuDto menu);
    Integer update(MenuDto menu);
    Integer updateName(MenuDto menu);
    Integer updateSort(MenuDto menuDto);
}
