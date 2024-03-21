package com.architecture.admin.models.daosub.block;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.block.BlockMemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BlockMemberDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 차단 목록
     *
     * @param searchDto 검색조건
     * @return list
     */
    List<BlockMemberDto> getList(SearchDto searchDto);

    /**
     * 전체 카운트
     *
     * @param searchDto 검색조건
     * @return count
     */
    int getTotalCount(SearchDto searchDto);

    /**
     * 정상적인 차단 내역 체크
     *
     * @param blockMemberDto memberUuid, blockUuid
     * @return count
     */
    int getBlockByIdx(BlockMemberDto blockMemberDto);

    /**
     * 최근 차단 내역 20개 리스트 by memberUuid
     *
     * @param blockMemberDto memberUuid
     * @return list
     */
    List<BlockMemberDto> getBlockLastTwentyCasesListByMemberUuid(BlockMemberDto blockMemberDto);

}
