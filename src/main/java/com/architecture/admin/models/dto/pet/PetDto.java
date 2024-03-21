package com.architecture.admin.models.dto.pet;

import com.architecture.admin.libraries.excel.ExcelColumnName;
import com.architecture.admin.libraries.excel.ExcelFileName;
import com.architecture.admin.models.dto.excel.ExcelDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ExcelFileName(filename = "lang.pet.title.list")
public class PetDto implements ExcelDto {

    /**
     * pet
     */
    @ExcelColumnName(headerName = "lang.pet.idx")
    private Long idx;               // 고유번호
    private String uuid;            // 고유아이디
    private Long breedIdx;          // pet_breed.idx
    private Long memberIdx;         // 회원 idx
    @ExcelColumnName(headerName = "lang.pet.name")
    private String name;            // 이름
    @ExcelColumnName(headerName = "lang.pet.type")
    private String type;            // 반려동물 종류(강아지/고양이)
    @ExcelColumnName(headerName = "lang.pet.gender")
    private String genderText; // 성별 텍스트
    private String breed;           // 품종
    private Integer state;          // 상태값
    private String regDateTz;       // 등록일 타임존

    /**
     * pet_info
     */
    private Long petIdx;     // pet.idx
    private Long number;     // 등록번호
    private Integer gender;  // 성별 (1:male, 2:female, 3:neutering(수컷중성화), 4:spaying(암컷중성화)
    private Integer size;    // 크키 (1:small, 2:medium, 3:large)
    private Float weight;    // 몸무게
    private Integer age;     // 연령 (1:puppy, 2:adult, 3:senior)
    private String birth;    // 생년월일
    private Integer personalityCode;    // pet_personality_code.idx

    /**
     * pet_type
     */
    private String typeName;        // 반려동물 종류

    /**
     * pet_profile_img
     */
    private String imgUuid;         // 이미지 고유 아이디
    private String url;             // 이미지 url (도메인 제외)
    private String uploadName;      // 업로드 파일명
    private String uploadPath;      // 업로드 경로
    private Integer imgWidth;       // 이미지 가로 사이즈
    private Integer imgHeight;      // 이미지 세로 사이즈
    private Integer imgSort;        // 이미지 정렬 순서
    private String profileUrl;      // text

    /**
     * pet_breed
     */
    private Integer typeIdx;  // pet_type.idx
    @ExcelColumnName(headerName = "lang.pet.breed")
    private String breedName; // 반려동물 품종
    private Long sort;        // 정렬 순서
    private Long modiSort;    // 정렬 순서 변경 값

    /**
     * pet_personality_code
     */
    private String personality;  // 반려 동물 성격
    private Long personalityIdx; // personality.idx
    /**
     * pet_breed_etc
     */
    @ExcelColumnName(headerName = "lang.pet.breed.etc")
    private String breedNameEtc; // 기타 품종 이름

    /**
     * pet_personality_etc
     */
    private String personalityEtc; // 기타 반려 동물 성격

    /**
     * sns_member
     */
    @ExcelColumnName(headerName = "lang.pet.member.id")
    private String memberId;
    private String memberNick;
    @ExcelColumnName(headerName = "lang.pet.member.nick")
    private String memberUuid;

    /**
     * etc
     */
    private String sizeText;   // 사이즈 텍스트
    @ExcelColumnName(headerName = "lang.pet.state")
    private String ageText;    // 연령 텍스트
    private String stateBg;
    private String stateText;
    private List<String> allergyList;
    private List<String> healthList;
    private String allergyName;
    private Long totalCnt;
    private String originUrl; // 원본 이미지 url
    @ExcelColumnName(headerName = "lang.pet.regdate")
    private String regDate;         // 등록일

    // sql
    private Long insertedIdx;
    private Long affectedRow;

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, name, typeName, genderText, breedName, breedNameEtc, memberId, memberNick, stateText, regDate);
    }
}
