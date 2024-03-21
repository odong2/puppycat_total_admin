package com.architecture.admin.services.member;

import com.architecture.admin.models.daosub.member.MemberPetDaoSub;
import com.architecture.admin.models.dto.pet.PetDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberPetService extends BaseService {

    private final MemberPetDaoSub memberPetDaoSub;


    @Value("${pet.size.small}")
    private int sizeSmall;          // 작음
    @Value("${pet.size.medium}")
    private int sizeMedium;         // 중간
    @Value("${pet.size.large}")
    private int sizeLarge;          // 큼
    @Value("${pet.age.puppy}")
    private int agePuppy;          // 퍼피
    @Value("${pet.age.adult}")
    private int ageAdult;         // 어덜트
    @Value("${pet.age.senior}")
    private int ageSenior;          // 시니어
    @Value("${pet.gender.male}")
    private int genderMale;         // 수컷
    @Value("${pet.gender.female}")
    private int genderFemale;       // 암컷
    @Value("${pet.gender.neutering}")
    private int genderNeutering;    // 수컷 중성화
    @Value("${pet.gender.spaying}")
    private int genderSpaying;      // 암컷 중성화


    /**********************************************************
     * SubFunction - Module
     **********************************************************/
    /**
     * 펫 정보 조회
     *
     * @param petUuidList
     * @return
     */
    public List<PetDto> getPetInfoList(List<String> petUuidList) {

        List<PetDto> petList = memberPetDaoSub.getPetInfoList(petUuidList);
        stateText(petList);

        return petList;
    }

    /**********************************************************
     * SubFunction - Select
     **********************************************************/

    /**********************************************************
     * SubFunction - Insert
     **********************************************************/

    /**********************************************************
     * SubFunction - Update
     **********************************************************/

    /**********************************************************
     * SubFunction - Delete
     **********************************************************/

    /**********************************************************
     * SubFunction - ETC
     **********************************************************/

    /**
     * 텍스트 변환
     *
     * @param petList
     */
    private void stateText(List<PetDto> petList) {
        for (PetDto petDto : petList) {
            stateText(petDto);
        }
    }

    /**
     * 텍스트 변환
     *
     * @param petDto
     */
    private void stateText(PetDto petDto) {

        /** size **/
        if (petDto.getSize() != null) {
            if (petDto.getSize() == sizeSmall) {         // 작음
                petDto.setSizeText(super.langMessage("lang.pet.size.small"));
            } else if (petDto.getSize() == sizeMedium) { // 중간
                petDto.setSizeText(super.langMessage("lang.pet.size.medium"));
            } else if (petDto.getSize() == sizeLarge) {  // 큼
                petDto.setSizeText(super.langMessage("lang.pet.size.large"));
            }
        }

        /** age **/
        if (petDto.getAge() != null) {
            if (petDto.getAge() == agePuppy) {         // 퍼피
                petDto.setAgeText(super.langMessage("lang.pet.age.puppy"));
            } else if (petDto.getAge() == ageAdult) { // 어덜트
                petDto.setAgeText(super.langMessage("lang.pet.age.adult"));
            } else if (petDto.getAge() == ageSenior) {  // 시니어
                petDto.setAgeText(super.langMessage("lang.pet.age.senior"));
            }
        }

        /** gender **/
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

        // 앞단에 쓰지 않는 타입 값 null 처리
        petDto.setGender(null);
        petDto.setSize(null);
        petDto.setAge(null);
    }
}
