package com.architecture.admin.models.daosub.shopping.certification;

import com.architecture.admin.models.dto.shopping.certification.CertificationDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CertificationDaoSub {
    List<CertificationDto> getCertificationList();
}
