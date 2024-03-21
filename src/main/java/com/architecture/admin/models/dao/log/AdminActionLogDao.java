package com.architecture.admin.models.dao.log;

import com.architecture.admin.models.dto.log.AdminActionLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/*****************************************************
 * admin action log
 ****************************************************/
@Repository
@Mapper
public interface AdminActionLogDao {

    /*****************************************************
     * Insert
     ****************************************************/
    Integer regist(AdminActionLogDto adminActionLogDto);// 입력
}
