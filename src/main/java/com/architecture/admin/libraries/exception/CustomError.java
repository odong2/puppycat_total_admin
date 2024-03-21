package com.architecture.admin.libraries.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumSet;

/**
 * ****** 오류코드 작성 규칙 ******
 * - 영문4자 와  숫자4자리로 구성 ex) ELGI-9999
 * - 앞4자리 영문은 기능이나 페이지를 알 수 있도록 작성
 * - 뒤4자리 숫자는 아래 규칙에 따라 분류
 * 오류번호   /   설명
 * 1000    =   정상
 * 2xxx    =   필수값 없음
 * 3xxx    =   유효성오류
 * 4xxx    =   sql구문오류
 * 5xxx    =   DB데이터오류
 * 6xxx    =   파일오류
 * 7xxx    =   권한오류
 * 9xxx    =   기타오류
 */
public enum CustomError {

    // ECOM : 공통 오류
    SWITCH_FALSE_ERROR("ECOM-9999", "lang.common.exception.switch.false")  // 이용 불가한 기능입니다.

    // ERTE : 토큰 관련 오류
    , TOKEN_EXPIRE_TIME_ERROR("ERTE-9999", "lang.token.expire.time.error.publish.fail") // 유효 기간이 만료 되어 토큰 검증에 실패 하였습니다.
    , TOKEN_ERROR("ERTE-9998", "lang.token.exception.token")                            // 토큰 값 에러 다시 로그인 해주세요.
    , TOKEN_EMPTY_ERROR("ERTE-9997", "lang.token.exception.empty")                      // 토큰값이 비어있습니다
    , TOKEN_ID_ERROR("ERTE-9996", "lang.token.exception.id")                            // 토큰이 만료되었습니다 재로그인후 다시 시도해주세요

    // ELGI : 로그인 관련 오류
    , LOGIN_FAIL("ELGI-9999", "lang.admin.exception.login_fail")        // 관리자 로그인이 실패하였습니다.
    , ADMIN_STATE_ERROR("ELGI-9997", "lang.admin.exception.state")      // 계정상태를 확인해주세요.
    , ADMIN_STATE_WAIT("ELGI-9997", "lang.admin.exception.state_wait")  // 계정상태를 확인해주세요.
    , LOGIN_ID_ERROR("ELGI-2999", "lang.admin.placeholder.id")          // 아이디를 입력해주세요
    , LOGIN_PW_ERROR("ELGI-2998", "lang.admin.placeholder.pw")          // 패스워드를 입력해주세요
    , IDX_MEMBER_ERROR("EMEM-9999", "IDX_ERROR")

    // EJOI : 회원가입 관련 오류
    , PASSWORD_CONFIRM("EJOI-9999", "lang.admin.placeholder.pw.confirm")// 패스워드를 동일하게 입력해주세요.
    , ID_DUPLE("EJOI-9998", "lang.admin.exception.id_duple")            // 이미 존재하는 아이디입니다.
    , JOIN_ID_ERROR("EJOI-2999", "lang.admin.placeholder.id")           // 아이디를 입력해주세요
    , JOIN_PW_ERROR("EJOI-2998", "lang.admin.placeholder.pw")           // 패스워드를 입력해주세요
    , JOIN_NAME_ERROR("EJOI-2997", "lang.admin.placeholder.name")       // 이름 입력해주세요

    // EAIP : 관리자 허용 IP 관련 오류
    , ADMIN_IP_IDX_EMPTY("EAIP-2999", "lang.ip.exception.idx.empty")        // 요청하신 IP 상세 정보를 찾을 수 없습니다.
    , ADMIN_IP_STATE_EMPTY("EAIP-2998", "lang.ip.exception.state.empty")    // IP 사용 상태를 선택해주세요.
    , ADMIN_IP_TYPE_EMPTY("EAIP-2997", "lang.ip.exception.type.empty")      // 관리자 구분값을 선택해주세요.
    , ADMIN_IP_EMPTY("EAIP-2996", "lang.ip.exception.ip.empty")             // IP 주소를 입력해주세요.
    , ADMIN_IP_MEMO_EMPTY("EAIP-2995", "lang.ip.exception.memo.empty")      // IP 주소 설명을 입력해주세요.
    , ADMIN_IP_IDX_ERROR("EAIP-3999", "lang.ip.exception.idx")              // 요청하신 IP 상세 정보를 찾을 수 없습니다.
    , ADMIN_IP_REGISTER_ERROR("EAIP-9999", "lang.ip.exception.register")    // IP 등록을 실패하였습니다.
    , ADMIN_IP_DELETE_ERROR("EAIP-9998", "lang.ip.exception.delete")        // IP 삭제를 실패하였습니다.
    , ADMIN_IP_MODIFY_ERROR("EAIP-9997", "lang.ip.exception.modify")        // IP 수정을 실패하였습니다.
    , ADMIN_IP_ACCESS_ERROR("EAIP-9996", "lang.ip.exception.access")        // 접근이 허용되지 않은 IP입니다.


    // ENIC : 닉네임 관련 오류
    , NICK_EMPTY("ENIC-2999", "lang.member.exception.nick_empty")           // 닉네임값이 비어있습니다.
    , NICK_LENGTH_ERROR("ENIC-3999", "lang.member.exception.nick_length")   //최소 2자 이상 최대 20자 이하만 입력할 수 있습니다
    , NICK_STRING_ERROR("ENIC-3998", "lang.member.exception.nick_string")   //사용할 수 없는 문자가 포함되어 있습니다.
    , NICK_DUPLE("ENIC-3997", "lang.member.exception.nick_duple")           //사용할 수 없는 문자가 포함되어 있습니다.
    , NICK_CHECK_FAIL("ENIC-3996", "lang.member.exception.nick_check")      //닉네임 검사 실패
    , NICK_HAVE("ENIC-3995", "lang.member.exception.nick_have")             // 이미 닉네임을 등록한 회원입니다

    // EADM : 관리자 연동 오류
    , IDX_ERROR("EADM-9998", "IDX_ERROR")
    , MEMBER_UUID_ERROR("EMEM-3999", "lang.member.exception.uuid.error")        // 회원 UUID가 유효하지 않습니다.
    , OLD_PASSWORD_EMPTY("EADM-2999", "lang.admin.exception.old.password.empty") // 이전 비밀번호를 입력해주세요.
    , NEW_PASSWORD_EMPTY("EADM-2998", "lang.admin.exception.new.password.empty") // 변경할 비밀번호를 입력해주세요.
    , PASSWORD_CONFIRM_EMPTY("EADM-2997", "lang.admin.exception.password.confirm.empty") // 비밀번호 확인란을 입력해주세요.
    , IDX_ADMIN_ERROR("EADM-3999", "lang.admin.exception.idx")            // 관리자 idx가 없습니다.
    , SEARCH_TYPE_ERROR("EADM-3998", "lang.common.exception.search.word") // 검색 조건을 설정해주세요.
    , LOGIN_ERROR("EADM-3997", "lang.common.exception.login") // 로그인 후 이용 가능합니다.
    , PASSWORD_CORRESPOND("EADM-3996", "lang.admin.exception.password.correspond") // 변경할 비밀번호가 현재 비밀번호와 같습니다.
    , PASSWORD_NOT_CORRESPOND("EADM-3995", "lang.admin.exception.password.confirm.error") // 변경할 비밀번호와 비밀번호 확인란이 일치하지 않습니다.
    , OLD_PASSWORD_ERROR("EADM-3994", "lang.admin.exception.old.password.error") // 현재 비밀번호가 올바르지 않습니다.
    , MY_MODIFY_ERROR("EADM-3992", "lang.admin.exception.my.modify.error") // 내 정보만 수정이 가능합니다

    // EAWS : AWS 관련 오류
    , AWS_SNS_FAIL("EAWS-9999", "lang.admin.exception.sqs.send_fail")  // SQS 전송 실패

    // EMEM : 회원 관련 오류
    , MEMBER_NULL("EMEM-9999", "lang.member.exception.member.null")            // 존재하지 않는 회원입니다.
    , LANG_MEMBER_ERROR("EMEM-2999", "lang.member.exception.lang")             // 사용언어를 선택해주세요.
    , MEMBER_UUID_EMPTY("EMEM-2998", "lang.member.exception.uuid.empty")       // 회원 UUID가 비었습니다.

    //ENMU : 메뉴 관련 오류
    , ACCESS_FAIL("ENMU-9999", "lang.common.accessfail")                // 접근 권한이 없습니다
    , MENU_SWAP_FAIL("ENMU-9998", "lang.menu.exception.swap_fail")      // 정렬 수정에 실패하였습니다.
    , MENU_NAME_FAIL("ENMU-9997", "lang.menu.placeholder.name")         // 이름을 입력해주세요.
    , MENU_LINK_FAIL("ENMU-9996", "lang.menu.placeholder.link")         // 링크를 입력해주세요.
    , MENU_ACCESS_FAIL("ENMU-9995", "lang.menu.exception.access_fail")  // 잘못된 접근입니다.
    , DATE_START_DATE_OVER_ERROR("EDAT-3999", "lang.common.exception.date.start.over") // 시작 날짜보다 이후 날짜로 입력해주세요.

    // MERE : 회원 제재 오류
    , MEMBER_IDX_RESTRAIN_ERROR("MERE-9999", "MEMBER_IDX_ERROR")
    , RESTRAIN_MEMBER_TYPE("MERE-2999", "lang.restrain.member.exception.regist.type")           // 제재 종류를 입력해주세요.
    , RESTRAIN_MEMBER_START("MERE-2998", "lang.restrain.member.exception.regist.start")         // 시작 날짜를 입력해주세요
    , RESTRAIN_MEMBER_END("MERE-2997", "lang.restrain.member.exception.regist.end")             // 종료 날짜를 입력해주세요
    , RESTRAIN_MEMBER_MEMO("MERE-2996", "lang.restrain.member.exception.regist.memo")           // 제재 사유를 입력해주세요
    , RESTRAIN_MEMBER_STARTTIME("MERE-2995", "lang.restrain.member.exception.regist.startTime") // 시작 시간을 입력해주세요
    , RESTRAIN_MEMBER_STATE("MERE-2994", "lang.restrain.member.exception.regist.state")         // 상태를 입력해주세요
    , RESTRAIN_MEMBER_DATE("MERE-2993", "lang.restrain.member.exception.regist.date")           // 제재 기간을 입력해주세요.
    , RESTRAIN_LEVEL_CHECK("MERE-3999", "lang.restrain.member.exception.level")                 // 제재 레벨 순서가 잘못되었습니다
    , RESTRAIN_DATE_CHECK("MERE-3998", "lang.restrain.member.exception.date")                   // 기간 중 제재 중인 제재가 있습니다.
    , RESTRAIN_IDX("MERE-9998", "IDX_ERROR")

    // EOUT : 회원 탈퇴 오류
    , MEMBER_OUT_FAIL("EOUT-9999", "lang.member.exception.out")  // 탈퇴 실패하였습니다.

    // ENOT : 공지사항 관련 오류
    , NOTICE_IDX_ERROR("ENOT-9999", "IDX_ERROR")
    , NOTICE_TITLE_NULL("ENOT-2999", "lang.notice.exception.title")         // 제목을 입력해주세요
    , NOTICE_MENU_NULL("ENOT-2998", "lang.notice.exception.menu")           // 공지 종류를 입력해주세요.
    , NOTICE_STATE_NULL("ENOT-2997", "lang.notice.exception.state")         // 상태값을 입력해주세요.
    , NOTICE_CONTENTS_NULL("ENOT-2996", "lang.notice.exception.contents")   // 내용을 입력해주세요.
    , NOTICE_NOTI_BODY_NULL("ENOT-2995", "lang.notice.exception.body")      // 알림 내용을 입력해주세요.

    // EFAQ : FAQ 관련 오류
    , FAQ_IDX_ERROR("EFAQ-9999", "IDX_ERROR")
    , FAQ_TITLE_NULL("EFAQ-2999", "lang.faq.exception.title")         // 제목을 입력해주세요
    , FAQ_MENU_NULL("EFAQ-2998", "lang.faq.exception.menu")           // 질문 종류를 입력해주세요.
    , FAQ_STATE_NULL("EFAQ-2997", "lang.faq.exception.state")         // 상태값을 입력해주세요.
    , FAQ_CONTENTS_NULL("EFAQ-2996", "lang.faq.exception.contents")   // 내용을 입력해주세요.

    // EPOL : 이용약관 관련 오류
    , POL_IDX_ERROR("EPOL-9999", "IDX_ERROR")
    , POL_CURRENT_DUPLE("EPOL-9998", "lang.policy.exception.current.duple") // 같은 카테고리 내 현행중인 약관이 존재합니다.
    , POL_TITLE_NULL("EPOL-2999", "lang.policy.exception.title")            // 제목을 입력해주세요
    , POL_REQUIRED_NULL("EPOL-2998", "lang.policy.exception.required")      // 필수여부를 입력해주세요.
    , POL_STATE_NULL("EPOL-2997", "lang.policy.exception.state")            // 상태값을 입력해주세요.
    , POL_CONTENTS_NULL("EPOL-2996", "lang.policy.exception.contents")      // 내용을 입력해주세요.
    , POL_CATEGORY_NULL("EPOL-2995", "lang.policy.exception.category")      // 카테고리를 선택해주세요.
    , POL_CURRENT_NULL("EPOL-2994", "lang.policy.exception.current")        // 현행여부를 선택해주세요.

    // EWOR : 금칙어 관련 오류
    , WORD_WORD_NULL("EWOR-2999", "lang.wordcheck.exception.word")               // 금칙어를 입력해주세요
    , WORD_CHANGEWORD_NULL("EWOR-2998", "lang.wordcheck.exception.changeWord")   // 변경단어를 입력해주세요.
    , WORD_TYPE_NULL("EWOR-2997", "lang.wordcheck.exception.type")               // 금칙어 타입을 선택해주세요.

    // EBLO : 회원 차단 관련 오류
    , BLOCK_DELETE_ERROR("EBLO-9999", "lang.block.exception.deleteFail")    // 삭제 실패하였습니다.

    // ECOM : 댓글 관련 오류
    , COMMENT_IDX_ERROR("ECOM-9999", "IDX_ERROR")

    // ECON : 콘텐츠 관련 오류
    , CONTENTS_IDX_NULL("ECON-9999", "lang.contents.exception.idx.null")                                           // 존재하지 않는 콘텐츠입니다.
    , CONTENTS_REGISTER_ERROR("ECON-9998", "lang.contents.exception.regist")                                       // 콘텐츠 등록에 실패하였습니다.
    , CONTENTS_REGISTER_MEMBER_TAG_ERROR("ECON-9997", "lang.contents.exception.membertag.regist")                  // 회원 태그 등록에 실패하였습니다.
    , CONTENTS_UPDATE_CONTENTS_ERROR("ECON-9996", "lang.contents.exception.contents.update")                       // 내용 업데이트 실패하였습니다.
    , CONTENTS_UPDATE_CONTENTS_IDX_ERROR("ECON-9995", "lang.contents.exception.contents.update.idx")               // 내용 업데이트 IDX 오류.
    , CONTENTS_REGISTER_IMG_TAG_ERROR("ECON-9994", "lang.contents.exception.imgTag.idx")                           // 이미지내 태그 idx 오류.
    , CONTENTS_REGISTER_LOCATION_ERROR("ECON-9993", "lang.contents.exception.location.regist")                     // 위치 등록에 실패하였습니다.
    , CONTENTS_REGISTER_LOCATION_MAPPING_ERROR("ECON-9992", "lang.contents.exception.location.mapping.regist")     // 위치 매핑 등록에 실패하였습니다.
    , CONTENTS_REGISTER_IMG_TAG_MEMBER_ERROR("ECON-9991", "lang.contents.exception.imgTag.memberidx")              // 존재하지 않는 회원이 이미지내 태그 되었습니다.
    , CONTENTS_IMAGE_RESTRAIN_ERROR("ECON-9990", "lang.contents.exception.image.restrain.regist")                  // 이미지 제재 등록에 실패하였습니다.
    , CONTENTS_IMAGE_SORT_UPDATE_ERROR("ECON-9989", "lang.contents.exception.image.sort.update")                   // 이미지 sort 업데이중 실패하였습니다.
    , CONTENTS_IMAGE_COUNT_UPDATE_ERROR("ECON-9988", "lang.contents.exception.image.count.update")                 // 이미지 총 개수 업데이트중 실패하였습니다.
    , CONTENTS_IMAGE_MEMBER_TAG_DELETE_ERROR("ECON-9987", "lang.contents.exception.image.membertag.delete")        // 이미지 내 멘션 업데이트중 실패하였습니다.
    , CONTENTS_UID_DUPLE("ECON-3999", "lang.contents.exception.uuid_duple")                                        // 이미 존재하는 고유아이디입니다.
    , CONTENTS_TEXT_LIMIT_ERROR("ECON-3998", "lang.contents.exception.text.limit.over")                            // 최대 입력 가능 글자수를 초과하였습니다.
    , CONTENTS_IMAGE_LIMIT_ERROR("ECON-3997", "lang.contents.exception.image.limit.over")                          // 사진은 최대 12장만 선택 가능합니다.
    , CONTENTS_IMAGE_MEMBER_TAG_LIMIT_ERROR("ECON-3996", "lang.contents.exception.image.membertag.limit.over")     // 최대 10명까지 태그 가능합니다.
    , CONTENTS_IMAGE_UID_DUPLE("ECON-3995", "lang.contents.exception.image.uuid.duple")                            // 이미 존재하는 이미지 고유아이디입니다.
    , CONTENTS_IDX_ERROR("ECON-3994", "lang.contents.exception.idx")                                               // 존재하지 않는 게시글입니다.
    , CONTENTS_REGISTER_IMAGE_EXTENSION_ERROR("ECON-3993", "lang.contents.exception.image.extension")               // 허용하지 않는 확장자를 가진 파일입니다.
    , CONTENTS_REGISTER_IMAGE_SIZE_ERROR("ECON-3992", "lang.contents.exception.image.size.over")                   // 이미지 용량이 너무 큽니다.
    , CONTENTS_TEXT_EMPTY("ECON-2999", "lang.contents.exception.text.empty")                                       // 내용을 입력해주세요.
    , CONTENTS_IMAGE_EMPTY("ECON-2998", "lang.contents.exception.image.empty")                                     // 사진을 등록해주세요.
    , CONTENTS_ISVIEW_EMPTY("ECON-2997", "lang.contents.exception.isView.empty")                                   // 공개범위를 선택해주세요.
    , CONTENTS_MENUIDX_EMPTY("ECON-2996", "lang.contents.exception.menu.idx.empty")                                // 카테고리를 선택해주세요.
    , CONTENTS_IMAGE_RESTRAIN_IDX_LIST_EMPTY("ECON-2995", "lang.contents.exception.image.restrain.idx.list.empty") // 제재할 이미지가 비었습니다.
    , CONTENTS_IMAGE_RESTRAIN_MEMO_EMPTY("ECON-2994", "lang.contents.exception.image.restrain.memo.empty")         // 제재 사유를 입력해주세요.

    // ETAG : 해시태그,멘션 관련 오류
    , TAG_HASH_DATA_ERROR("ETAG-9999", "lang.tag.exception.data")                                           // 잘못된 요청입니다.
    , TAG_MENTION_DATA_ERROR("ETAG-9998", "lang.tag.exception.data")                                        // 잘못된 요청입니다.
    , TAG_REGISTER_HASH_TAG_ERROR("ETAG-9997", "lang.tag.exception.hashtag.regist")                         // 해시태그 등록에 실패하였습니다.
    , TAG_REGISTER_HASH_TAG_MAPPING_ERROR("ETAG-9996", "lang.tag.exception.hashtag.mapping.regist")         // 해시태그 매핑 등록에 실패하였습니다.
    , TAG_UPDATE_HASH_TAG_ERROR("ETAG-9995", "lang.tag.exception.hashtag.update")                           // 해시태그 업데이트 실패하였습니다.
    , TAG_REGISTER_MENTION_ERROR("ETAG-9994", "lang.tag.exception.mention.regist")                          // 멘션 등록에 실패하였습니다.
    , TAG_REGISTER_MENTION_MAPPING_ERROR("ETAG-9993", "lang.tag.exception.mention.mapping.regist")          // 멘션 매핑 등록에 실패하였습니다.
    , TAG_UPDATE_MENTION_ERROR("ETAG-9992", "lang.tag.exception.mention.update")                            // 멘션 업데이트 실패하였습니다.

    // EPUS : 푸시 관련 오류
    , PUSH_TYPE_EMPTY("EPUS-2999", "lang.push.admin.exception.type.empty")                      // 타입을 입력해주세요.
    , PUSH_CONTENTS_TYPE_EMPTY("EPUS-2998", "lang.push.admin.exception.contentsType.empty")     // 컨텐츠 타입을 입력해주세요.
    , PUSH_CONTENTS_IDX_EMPTY("EPUS-2997", "lang.push.admin.exception.contentsIdx.empty")       // 컨텐츠 IDX를 입력해주세요.
    , PUSH_TITLE_EMPTY("EPUS-2996", "lang.push.admin.exception.title.empty")                    // 푸시 제목을 입력해주세요.
    , PUSH_BODY_EMPTY("EPUS-2995", "lang.push.admin.exception.body.empty")                      // 푸시 내용을 입력해주세요.
    , PUSH_IMAGE_EMPTY("EPUS-2994", "lang.push.admin.exception.img.empty")                      // 푸시 이미지를 입력해주세요
    , PUSH_NOTICE_IDX_ERROR("EPUS-3999", "lang.push.admin.exception.noticeIdx")                 // 공지 IDX를 확인해 주세요
    , PUSH_CONTENTS_IDX_ERROR("EPUS-3998", "lang.push.admin.exception.contentsIdx")             // 컨텐츠 IDX를 확인해 주세요
    , PUSH_CONTENTS_TYPE_ERROR("EPUS-3997", "lang.push.admin.exception.contentsType")           // 잘못된 컨텐츠 타입 입니다
    , PUSH_WORWER_ERROR("EPUS-9999", "lang.push.admin.exception.worker")                        // 기존에 등록 된 푸시가 진행 중입니다

    // ENTI : 알림 관련 오류
    , NOTI_SENDERUUID_ERROR("ENTI-2999", "lang.noti.exception.senderUuid.empty")     // 알림 보내는 회원 IDX가 비어있습니다.
    , NOTI_MEMBERUUID_ERROR("ENTI-2998", "lang.noti.exception.memberUuid.empty")     // 알림 받는 회원 IDX가 비어있습니다.
    , NOTI_CONTENTSIDX_ERROR("ENTI-2997", "lang.noti.exception.contentsIdx.null")    // 알림 보낼 컨텐츠 IDX가 비어있습니다.
    , NOTI_SUBTYPE_ERROR("ENTI-2996", "lang.noti.exception.subType.null")            // 서브타입을 입력해주세요.
    , NOTI_TITLE_ERROR("ENTI-2995", "lang.noti.exception.title.null")                // 알림 제목이 비어있습니다.
    , NOTI_BODY_ERROR("ENTI-2994", "lang.noti.exception.body.null")                  // 알림 내용이 비어있습니다.

    , PET_TYPEIDX_ERROR("EPET-3999", "lang.pet.exception.typeIdx")                   // 반려동물 종류를 입력해주세요
    , PET_BREED_ERROR("EPET-3998", "lang.pet.exception.breed")                       // 품종을 입력해주세요
    , PET_IDX_ERROR("EPET-3997", "lang.pet.exception.idx.null")                      // idx값이 비어있습니다.
    , PET_SORT_ERROR("EPET-3996", "lang.pet.exception.sort.null")                    // sort 값이 비어있습니다.
    , PET_MODISORT_ERROR("EPET-3995", "lang.pet.exception.modisort.null")            // 수정될 정렬값을 입력해주세요
    , PET_SORT_SAME_ERROR("EPET-3994", "lang.pet.exception.sort")                    // 같은 값으로는 변경 할 수 없습니다
    , PET_ACCESS_FAIL("EPET-9999", "lang.pet.exception.access_fail")                 // 잘못된 접근입니다.

    , APP_UPDATE_INFO_UPDATEERROR("EUIU-9999", "lang.inspect.update.fail")    // 프로필 이미지 수정에 실패하였습니다.
    , APP_UPDATE_CURRENT_BUILD_NUMBER_EMPTY("EUIU-9998", "lang.inspect.update.current.build.number.empty")    // 프로필 이미지 수정에 실패하였습니다.
    , APP_UPDATE_MINI_BUILD_NUMBER_EMPTY("EUIU-9997", "lang.inspect.update.mini.build.number.empty")    // 프로필 이미지 수정에 실패하였습니다.
    , APP_UPDATE_RECOMMEND_BUILD_NUMBER_EMPTY("EUIU-9990", "lang.inspect.update.recommed.build.number.empty")    // 프로필 이미지 수정에 실패하였습니다.
    , APP_UPDATE_FORCE_TITLE_EMPTY("EUIU-9996", "lang.inspect.update.force.title.empty")    // 프로필 이미지 수정에 실패하였습니다.
    , APP_UPDATE_FORCE_CONTEST_EMPTY("EUIU-9995", "lang.inspect.update.force.content.empty")    // 프로필 이미지 수정에 실패하였습니다.
    , APP_UPDATE_FORCE_PATCH_NOTE_EMPTY("EUIU-9994", "lang.inspect.update.force.patch.note.empty")    // 프로필 이미지 수정에 실패하였습니다.
    , APP_UPDATE_TITLE_EMPTY("EUIU-9993", "lang.inspect.update.title.empty")    // 프로필 이미지 수정에 실패하였습니다.
    , APP_UPDATE_CONTENTS_EMPTY("EUIU-9992", "lang.inspect.update.contents.empty")    // 프로필 이미지 수정에 실패하였습니다.
    , APP_UPDATE_PATCH_NOTE_EMPTY("EUIU-9991", "lang.inspect.update.patch.note.empty")    // 프로필 이미지 수정에 실패하였습니다.
    , APP_INSPECT_START_DATE_EMPTY("EUIU-9990", "lang.inspect.state.date.empty")    // 프로필 이미지 수정에 실패하였습니다.
    , APP_INSPECT_END_DATE_EMPTY("EUIU-9989", "lang.inspect.end.date.empty")    // 프로필 이미지 수정에 실패하였습니다.
    , APP_INSPECT_DATE_VALIDATION("EUIU-9988", "lang.inspect.date.validation")    // 프로필 이미지 수정에 실패하였습니다.

    // ECUP : 쿠폰 관련 오류
    , COUPON_IDX_ERROR("ECUP-9999", "lang.coupon.exception.idx")    // 존재하지 않는 쿠폰입니다.

    // EXCL : 엑셀 관련 오류
    , EXCEL_READ_ERROR("EXCL-9999", "lang.common.exception.excel.read")         // 엑셀 파일을 읽지 못하였습니다.
    , EXCEL_SAMPLE_ERROR("EXCL-9998", "lang.common.exception.excel.sample")     // 엑셀 양식이 올바르지 않습니다.
    , EXCEL_PARSING_ERROR("EXCL-9997", "lang.common.exception.excel.parsing")   // 엑셀을 읽던 중 에러가 발생했습니다.
    , EXCEL_DATA_EMPTY("EXCL-9996", "lang.common.exception.excel.no.data")      // 엑셀에 입력된 데이터가 없습니다.

    // EQNA : 1:1문의 관련 오류
    , QNA_IDX_ERROR("EQNA-9999", "lang.qna.exception.idx")              // 존재하지 않는 문의글 입니다.
    , QNA_DELETE_ERROR("EQNA-9998", "lang.qna.exception.delete")        // 삭제된 문의글 입니다.
    , QNA_ANSWER_ERROR("EQNA-9997", "lang.qna.exception.modify.answer") // 답변이 등록되어 수정이 불가능합니다.
    , QNA_TITLE_NULL("EQNA-2999", "lang.qna.exception.title")           // 제목을 입력해주세요
    , QNA_MENU_NULL("EQNA-2998", "lang.qna.exception.menu")             // 문의 종류를 입력해주세요.
    , QNA_CONTENTS_NULL("EQNA-2997", "lang.qna.exception.contents")     // 내용을 입력해주세요.

    // EPRD : 상품 관련 오류
    , PRODUCT_IDX_ERROR("EPRD-2999", "lang.product.exception.searchFail")   // MP 상품이 없습니다.

    // EPAR : 파트너 관련 오류
    , PARTNER_ID_DUPLE("EPAR-9999", "lang.partner.exception.id_duple")                                     // 이미 존재하는 아이디입니다.
    , PARTNER_HOLIDAY_BEFORE_NOW_DATE_ERROR("EPAR-9998", "lang.partner.exception.holiday.before.now.date") // 당일 이전 휴무일 등록은 불가능합니다.
    , PARTNER_HOLIDAY_NOW_DATE_ERROR("EPAR-9997", "lang.partner.exception.holiday.now.date")               // 당일 휴무일 등록은 불가능합니다.
    , PARTNER_JOIN_BUSINESS_NUMBER_ERROR("EPAR-3999", "lang.partner.exception.business.number")       // 사업자 번호를 다시 한번 확인해 주세요.
    , PARTNER_ID_STRING_ERROR("EPAR-3998", "lang.partner.exception.id.string")                        // 아이디는 6~10자 영문(소문자) 숫자 조합만 입력 가능합니다.
    , PARTNER_ID_LENGTH_MIN("EPAR-3997", "lang.partner.exception.id.length.min")                      // 아이디는 최소 6자리 이상 입력해야합니다.
    , PARTNER_ID_LENGTH_MAX("EPAR-3996", "lang.partner.exception.id.length.max")                      // 아이디는 최대 10자리까지 입력할 수 있습니다.
    , PARTNER_JOIN_ID_ERROR("EPAR-2999", "lang.partner.placeholder.id")                               // 아이디를 입력해주세요
    , PARTNER_HOLIDAY_DUPL_ERROR("EPAR-2998", "lang.partner.exception.holiday.duple")                 // 이미 해당 기간에 등록된 휴무일이 있습니다.
    , PARTNER_HOLIDAY_NOT_EXIST("EPAR-2997", "lang.partner.exception.holiday.not.exist")              // 존재하지 않는 공휴일입니다.

    // EPOI : 포인트 관련 오류
    , POINT_PRODUCT_ORDER_ID_ERROR("EPOI-3999", "lang.point.exception.product.order.id.empty")        // 존재하지 않는 주문번호입니다.
    ;


    @Autowired
    MessageSource messageSource;
    private String code;
    private String message;

    CustomError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
    }

    public CustomError setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
        return this;
    }

    @Component
    public static class EnumValuesInjectionService {

        @Autowired
        private MessageSource messageSource;

        // bean
        @PostConstruct
        public void postConstruct() {
            for (CustomError customError : EnumSet.allOf(CustomError.class)) {
                customError.setMessageSource(messageSource);
            }
        }
    }
}
