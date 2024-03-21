package com.architecture.admin.services.shopping.certification;

import com.architecture.admin.models.daosub.shopping.certification.CertificationDaoSub;
import com.architecture.admin.models.dto.shopping.certification.CertificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CertificationService {

    private final CertificationDaoSub certificationDaoSub;

    public List<CertificationDto> getCertificationList() {
        List<CertificationDto> certificationList = certificationDaoSub.getCertificationList();

        return certificationList;
    }
}
