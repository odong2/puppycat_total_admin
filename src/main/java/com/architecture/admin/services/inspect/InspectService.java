package com.architecture.admin.services.inspect;

import com.architecture.admin.libraries.S3Library;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dto.inspect.InspectDto;
import com.architecture.admin.models.dto.inspect.UpdateInspectDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/*****************************************************
 * 점검 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
@Transactional
public class InspectService extends BaseService {

    private final S3Library s3Library;

    @Value("${cloud.aws.s3.inspect.bucket}")
    private String inspectBucket;

    @Value("${inspect.s3.maintenance.path}")
    private String s3MaintenanceFilePath; // 파일 경로

    @Value("${inspect.s3.update.path}")
    private String s3UpdateFilePath; // 파일 경로

    @Value("${inspect.s3.update.file.full.path}")
    private String inspectUpdateFileFullFate;

    @Value("${inspect.s3.file.full.path}")
    private String inspectFileFullFate;

    /*
        get s3 update inspect info
     */
    public String getInspectInfo() {
        try {
            return getCurl(inspectFileFullFate);
        } catch (Exception e) {
            return null;
        }
    }

    /*
        inspect regist
     */
    public void registInspect(InspectDto inspectDto) throws IOException, ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (inspectDto.getTargetServiceList().isEmpty()) {
            throw new CustomException(CustomError.APP_UPDATE_INFO_UPDATEERROR);
        }

        if (inspectDto.getStartDate() == null || inspectDto.getStartDate().equals("")) {
            throw new CustomException(CustomError.APP_INSPECT_START_DATE_EMPTY);
        }

        if (inspectDto.getEndDate() == null || inspectDto.getEndDate().equals("")) {
            throw new CustomException(CustomError.APP_INSPECT_END_DATE_EMPTY);
        }

        long diffMin = (format.parse(inspectDto.getStartDate()).getTime() - format.parse(inspectDto.getEndDate()).getTime()) / 1000; //분 차이

        if (diffMin > 0) {
            throw new CustomException(CustomError.APP_INSPECT_DATE_VALIDATION);
        }

        File fileName = createFile(inspectDto);

        String s3UploadFilePath = s3MaintenanceFilePath;
        String s3UploadFileName = "/appInspectInfo.json";
        String s3UploadFileFullPath = s3UploadFilePath + s3UploadFileName;

        // 파일 S3 upload
        if (!s3Library.upload(inspectBucket, fileName, s3UploadFileFullPath)) {
            throw new CustomException(CustomError.APP_UPDATE_INFO_UPDATEERROR);
        };
    }

    /*
        create tmp inspect
    */
    public File createFile(InspectDto inspectDto) throws IOException {

        JSONObject jsonObj = new JSONObject(inspectDto);

        File file = new File("./tmp"); //디렉토리 가져오기

        if(!file.exists()){
            file.mkdirs();
        }

        //내컴퓨터에 json파일로 저장
        FileWriter jsonFile = new FileWriter(file + "/appInspectInfo.json");
        jsonFile.write(jsonObj.toString());//
        jsonFile.flush();
        jsonFile.close();

        return new File(file + "/appInspectInfo.json");
    }

    /*
        get s3 update inspect info
     */
    public String getUpdateInspectInfo() {
        try {
            return getCurl(inspectUpdateFileFullFate);
        } catch (Exception e) {
            return null;
        }
    }

    /*
        update s3 update inspect
     */
    public void registUpdateInspect(UpdateInspectDto updateInspectDto) throws IOException {

        if (updateInspectDto.getCurrentBuildNumber() == null) {
            throw new CustomException(CustomError.APP_UPDATE_CURRENT_BUILD_NUMBER_EMPTY);
        }

        if (updateInspectDto.getMinBuildNumber() == null) {
            throw new CustomException(CustomError.APP_UPDATE_MINI_BUILD_NUMBER_EMPTY);
        }

        if (updateInspectDto.getRecommendBuildNumber() == null) {
            throw new CustomException(CustomError.APP_UPDATE_RECOMMEND_BUILD_NUMBER_EMPTY);
        }

        if (updateInspectDto.getForceTitle() == null || updateInspectDto.getForceTitle().equals("")) {
            throw new CustomException(CustomError.APP_UPDATE_FORCE_TITLE_EMPTY);
        }

        if (updateInspectDto.getForceContents() == null || updateInspectDto.getForceContents().equals("")) {
            throw new CustomException(CustomError.APP_UPDATE_FORCE_CONTEST_EMPTY);
        }

        if (updateInspectDto.getForcePatchNote() == null || updateInspectDto.getForcePatchNote().equals("")) {
            throw new CustomException(CustomError.APP_UPDATE_FORCE_PATCH_NOTE_EMPTY);
        }

        if (updateInspectDto.getTitle() == null || updateInspectDto.getTitle().equals("")) {
            throw new CustomException(CustomError.APP_UPDATE_TITLE_EMPTY);
        }

        if (updateInspectDto.getContents() == null || updateInspectDto.getContents().equals("")) {
            throw new CustomException(CustomError.APP_UPDATE_CONTENTS_EMPTY);
        }

        if (updateInspectDto.getPatchNote() == null || updateInspectDto.getPatchNote().equals("")) {
            throw new CustomException(CustomError.APP_UPDATE_PATCH_NOTE_EMPTY);
        }

        // tmp 파일 생성 파일 업로이드
        File fileName = createUpdateInfoFile(updateInspectDto);

        String s3UploadFilePath = s3UpdateFilePath;
        String s3UploadFileName = "/appUpdateInfo.json";
        String s3UploadFileFullPath = s3UploadFilePath + s3UploadFileName;

        // 파일 S3 upload
        if (!s3Library.upload(inspectBucket, fileName, s3UploadFileFullPath)) {
            throw new CustomException(CustomError.APP_UPDATE_INFO_UPDATEERROR);
        };
    }

    /*
        update tmp 파일 생성 및 s3 업로드 사전 준비
     */
    public File createUpdateInfoFile(UpdateInspectDto updateInspectDto) throws IOException {

        JSONObject jsonObj = new JSONObject(updateInspectDto);

        //디렉토리가 존재하지 않는다면 생성
        File file = new File("./tmp"); //디렉토리 가져오기

        if(!file.exists()){
            file.mkdirs();
        }

        //내컴퓨터에 json파일로 저장
        FileWriter jsonFile = new FileWriter(file + "/appUpdateInfo.json");
        jsonFile.write(jsonObj.toString());//
        jsonFile.flush();
        jsonFile.close();

        return new File(file + "/appUpdateInfo.json");
    }
}
