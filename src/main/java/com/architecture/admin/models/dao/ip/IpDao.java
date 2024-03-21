package com.architecture.admin.models.dao.ip;

import com.architecture.admin.models.dto.ip.IpDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IpDao {

    /**
     * IP 삭제
     * @param idx
     * @return
     */
    int deleteAdminIp(Long idx);

    /**
     * IP 등록
     * @param ipDto
     * @return
     */
    int insertAdminIp(IpDto ipDto);

    /**
     * IP 수정
     * @param ipDto
     * @return
     */
    int updateAdminIp(IpDto ipDto);
}
