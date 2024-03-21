package com.architecture.admin.models.daosub.ip;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.ip.IpDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IpDaoSub {

    /**
     * 해당 ip가 접근 가능 ip인지 조회
     * @return
     */
    int checkIsAccessibleIP(String ip);

    /**
     * 관리자 허용 ip 개수 조회
     * @return
     */
    int getAdminIpTotalCnt(SearchDto searchDto);
    
    /**
     * 관리자 허용 ip 리스트 조회
     * @return
     */
    List<IpDto> getAdminIpList(SearchDto searchDto);

    /**
     * 관리자 허용 ip 상세 조회
     * @return
     */
    IpDto getViewAdminIp(Long idx);
}
