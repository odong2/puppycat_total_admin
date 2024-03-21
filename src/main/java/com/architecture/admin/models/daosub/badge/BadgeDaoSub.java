package com.architecture.admin.models.daosub.badge;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.badge.BadgeDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BadgeDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 회원 목록
     *
     * @param searchDto
     * @return list
     */
    List<BadgeDto> getList(SearchDto searchDto);

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);

}
