package com.architecture.admin.services.login;

import com.architecture.admin.libraries.ServerLibrary;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dao.admin.AdminDao;
import com.architecture.admin.models.daosub.admin.AdminDaoSub;
import com.architecture.admin.models.dto.admin.AdminDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@Service
@Transactional
public class JoinService  extends BaseService {

    private final AdminDao adminDao;

    private final AdminDaoSub adminDaoSub;

    @Value("${env.server}")
    private String sServer;

    /*****************************************************
     *  Modules
     ****************************************************/
    /**
     * 어드민 회원 가입
     *
     * @param adminDto (id / password / passwordConfirm / name)
     * @return InsertedId
     * @throws Exception
     */
    public Integer regist(AdminDto adminDto) throws Exception {

        // 아이디/패스워드/이름 검증
        String id = adminDto.getId();
        String password = adminDto.getPassword();
        String name = adminDto.getName();

        if (id == null || id.equals("")){
            throw new CustomException(CustomError.JOIN_ID_ERROR);
        }
        if (password == null || password.equals("")){
            throw new CustomException(CustomError.JOIN_PW_ERROR);
        }

        if (name ==null || name.equals("")) {
            throw new CustomException(CustomError.JOIN_NAME_ERROR);
        }

        // 패스워드 확인
        String passwordConfirm = adminDto.getPasswordConfirm();
        if (!password.equals(passwordConfirm)) {
            throw new CustomException(CustomError.PASSWORD_CONFIRM);
        }

        // 패스워드 암호화
        adminDto.setPassword(super.encrypt(password));

        return insert(adminDto);
    }


    /**
     * 중복아이디 검색
     *
     * @param adminDto (id)
     * @return 카운트 값
     */
    public Boolean checkDupleId(AdminDto adminDto) {
        Integer iCount = adminDaoSub.getCountById(adminDto);

        // 0 이상이면 true , 아니면 false
        return iCount > 0;
    }

    /*****************************************************
     *  SubFunction - Insert
     ****************************************************/
    /**
     * 회원 등록
     *
     * @param adminDto ( id / password )
     * @return InsertedId
     */
    public Integer insert(AdminDto adminDto) {
        HttpServletRequest request = ServerLibrary.getCurrReq();
        // 등록일
        adminDto.setRegDate(dateLibrary.getDatetime());

        // 가입아이피
        adminDto.setJoinIp(super.getClientIP(request));
        // 로그인아이피
        adminDto.setLoginIp(super.getClientIP(request));

        // insert
        adminDao.insert(adminDto);

        pushAlarm(sServer+"관리자::\n[" +
                adminDto.getName() + "]님 회원가입 요청");

        return adminDto.getInsertedId();
    }
}
