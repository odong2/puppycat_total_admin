package com.architecture.admin.services.pet;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.ThumborLibrary;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.models.daosub.pet.PetDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.models.dto.pet.PetDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PetService extends BaseService {

    private final PetDaoSub petDaoSub;
    private final ThumborLibrary thumborLibrary;
    private final ExcelData excelData;

    @Value("${pet.size.small}")
    private int sizeSmall;          // 작음
    @Value("${pet.size.medium}")
    private int sizeMedium;         // 중간
    @Value("${pet.size.large}")
    private int sizeLarge;          // 큼
    @Value("${pet.gender.male}")
    private int genderMale;         // 수컷
    @Value("${pet.gender.female}")
    private int genderFemale;       // 암컷
    @Value("${pet.gender.neutering}")
    private int genderNeutering;    // 수컷 중성화
    @Value("${pet.gender.spaying}")
    private int genderSpaying;      // 암컷 중성화
    @Value("${pet.age.puppy}")
    private int agePuppy;          // 퍼피
    @Value("${pet.age.adult}")
    private int ageAdult;         // 어덜트
    @Value("${pet.age.senior}")
    private int ageSenior;          // 시니어

    /**************************************************
     * SELECT
     **************************************************/

    /**
     * 반려동물 리스트 조회
     *
     * @param searchDto
     * @return
     */
    public List<PetDto> getPetList(SearchDto searchDto) {
        // 날짜 유효성
        dateLibrary.dateValidator(searchDto);

        // 회원 반려 동물 카운트
        int totalCnt = petDaoSub.getTotalPetCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCnt, searchDto);
        searchDto.setPagination(pagination);

        List<PetDto> petList = petDaoSub.getPetList(searchDto);
        stateText(petList);

        return petList;
    }

    /**
     * 반려동물 상세 리스트
     *
     * @param searchDto : idx[펫 idx], memberIdx[회원 idx]
     * @return
     */
    public List<PetDto> getDetailPetList(SearchDto searchDto) {

        Long idx = searchDto.getPetIdx();

        // 반려동물 상세(기본 정보)
        PetDto petDto = petDaoSub.getPetDetail(searchDto);
        // 반려동물 알러지 정보 리스트
        List<String> allergyList = petDaoSub.getPetAllergyListByIdx(idx);
        // 반려동물 건강 정보 리스트
        List<String> healthList = petDaoSub.getPetHealthListByIdx(idx);

        // 리스트 setting
        petDto.setAllergyList(allergyList);
        petDto.setHealthList(healthList);

        // 텍스트 변환
        stateText(petDto);

        if (!ObjectUtils.isEmpty(petDto.getUrl())) {
            // 이미지 사이즈 변환
            // TODO : imgType 인자값 추가 필요
//            String fullUrl = thumborLibrary.setCFurl(petDto.getImgWidth(), petDto.getImgHeight(), petDto.getUrl(), 150);
//            petDto.setOriginUrl(petDto.getUrl()); // 원본 url
//            petDto.setUrl(fullUrl); // cf url
        }
        List<PetDto> petList = new ArrayList<>();
        petList.add(petDto);

        return petList;
    }

    /**
     * 반려동물 정보 조회 by uuid
     *
     * @param petUuidList uuid
     * @return
     */
    public List<PetDto> getPetInfoByUuid(List<String> petUuidList) {
        return petDaoSub.getPetInfoListByUuid(petUuidList);
    }

    /**
     * 이미지 확대
     *
     * @param petDto
     * @return
     */
    public String getPetBigImg(PetDto petDto) {
        // 이미지 사이즈 변환
        // TODO : imgType 인자값 추가 필요
//        return thumborLibrary.setCFurl(petDto.getImgWidth(), petDto.getImgHeight(), petDto.getOriginUrl(), 1000);
        return null;
    }

    /**
     * 회원 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) {
        // get Lists
        List<PetDto> petList = petDaoSub.getPetList(searchDto);

        // 상태값 문자 변환
        stateText(petList);

        // 엑셀 데이터 생성
        return excelData.createExcelData(petList, PetDto.class);
    }

    /**********************************************
     * ETC
     **********************************************/

    /*****************************************************
     *  SubFunction - State
     ****************************************************/
    /**
     * 텍스트 변환 리스트
     *
     * @param petList
     */
    public void stateText(List<PetDto> petList) {
        for (PetDto petDto : petList) {
            stateText(petDto);
        }
    }

    /**
     * 텍스트 변환
     *
     * @param petDto
     */
    @SneakyThrows
    public void stateText(PetDto petDto) {
        // 상태값
        if (petDto.getState() != null) {
            if (petDto.getState() == 1) { // 정상
                petDto.setStateBg("badge-success");
                petDto.setStateText(super.langMessage("lang.pet.state.normal"));
            } else { // 삭제
                petDto.setStateBg("badge-danger");
                petDto.setStateText(super.langMessage("lang.pet.state.delete"));
            }
        }
        // 사이즈 텍스트 변환
        if (petDto.getSize() != null) {
            if (petDto.getSize() == sizeSmall) {         // 작음
                petDto.setSizeText(super.langMessage("lang.pet.size.small"));
            } else if (petDto.getSize() == sizeMedium) { // 중간
                petDto.setSizeText(super.langMessage("lang.pet.size.medium"));
            } else if (petDto.getSize() == sizeLarge) {  // 큼
                petDto.setSizeText(super.langMessage("lang.pet.size.large"));
            }
        }
        // 성별 텍스트 변환
        if (petDto.getGender() != null) {
            if (petDto.getGender() == genderMale) {    // 수컷
                petDto.setGenderText(super.langMessage("lang.pet.gender.male"));
            } else if (petDto.getGender() == genderFemale) { // 암컷
                petDto.setGenderText(super.langMessage("lang.pet.gender.female"));
            } else if (petDto.getGender() == genderNeutering) { //수컷 중성화
                petDto.setGenderText(super.langMessage("lang.pet.gender.neutering"));
            } else if (petDto.getGender() == genderSpaying) { // 암컷 중성화
                petDto.setGenderText(super.langMessage("lang.pet.gender.spaying"));
            }
        }
        // 생년월일 형식 변환
        if (petDto.getBirth() != null) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sf.parse(petDto.getBirth());
            String birth = sf.format(date);
            petDto.setBirth(birth);
        }
        // 나이 텍스트 변환
        if (petDto.getAge() != null) {
            if (petDto.getAge() == agePuppy) {         // 퍼피
                petDto.setAgeText(super.langMessage("lang.pet.age.puppy"));
            } else if (petDto.getAge() == ageAdult) { // 어덜트
                petDto.setAgeText(super.langMessage("lang.pet.age.adult"));
            } else if (petDto.getAge() == ageSenior) {  // 시니어
                petDto.setAgeText(super.langMessage("lang.pet.age.senior"));
            }
        }
    }
}
