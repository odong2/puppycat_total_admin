package com.architecture.admin.libraries;

import com.squareup.pollexor.Thumbor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Data
@Slf4j
public class ThumborLibrary {

    private final S3Library s3Library;

    @Value("${cloud.aws.s3.img.url.member}")
    private String memberImgDomain;
    @Value("${cloud.aws.s3.img.url.sns}")
    private String snsImgDomain;
    @Value("${cloud.aws.s3.img.url.common}")
    private String commonImgDomain;
    @Value("${cloud.aws.s3.img.url.shop}")
    private String shopImgDomain;
    @Value("${thumbor.key}")
    private String thumborKey;
    @Value("${thumbor.key.shop}")
    private String thumborKeyShop;
    @Value("${cloud.aws.cf.tb.url}")
    private String cfUrl;
    @Value("${cloud.aws.cf.tb.url.shop}")
    private String cfUrlShop;

    /**
     * 리사이징 후 CFurl 생성
     * [가로 & 세로 사이즈 DB에서 조회한 사이즈 사용]
     *
     * @param imgWidth  이미지 원본 가로 사이즈
     * @param imgHeight 이미지 원본 세로 사이즈
     * @param url       이미지 url
     * @param width     리사이징할 가로 사이즈
     * @return url
     */
    public String setCFurl(Integer imgWidth, Integer imgHeight, String url, int width, String imgType) {

        // shop은 예외처리 추가
        if(Objects.equals(imgType, "shop")){
            thumborKey = thumborKeyShop;
            cfUrl = cfUrlShop;
        }

        try {
            int originWidth = imgWidth;
            int originHeight = imgHeight;

            if (originWidth == 0) {
                originWidth = 500;
            }
            if (originHeight == 0) {
                originHeight = 500;
            }

            // 이미지 full url
            String fullUrl = s3Library.getUploadedFullUrl(url, imgType);

            int height2 = width * originHeight / originWidth;

            Thumbor thumbor = switch (imgType) {
                case "sns" -> Thumbor.create(snsImgDomain, thumborKey);
                case "member" -> Thumbor.create(memberImgDomain, thumborKey);
                case "common" -> Thumbor.create(commonImgDomain, thumborKey);
                case "shop" -> Thumbor.create(shopImgDomain, thumborKey);
                default -> Thumbor.create(snsImgDomain, thumborKey);
            };

            String thumborUrl = thumbor.buildImage(fullUrl).resize(width, height2).toUrl();
            URL path = new URL(thumborUrl);

            return cfUrl + path.getPath();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cfUrl + url;
    }

    /**
     * 리사이징 후 CFurl 생성 (원본 URL 을 통해 가로 & 세로 사이즈 구함)
     * [이미지 태그 사용 시 사용]
     *
     * @param fullUrl
     * @param width
     */
    public String getCFurl(String fullUrl, int width, String imgType) {

        String returnUrl = "";

        // shop은 예외처리 추가
        if(Objects.equals(imgType, "shop")){
            thumborKey = thumborKeyShop;
            cfUrl = cfUrlShop;
        }

        try {
            // 이미지 full url
            URL url = new URL(fullUrl);

            BufferedImage image = ImageIO.read(url);

            int height2 = width * image.getHeight() / image.getWidth();

            Thumbor thumbor;

            if (imgType.equals("sns")) {
                thumbor = Thumbor.create(snsImgDomain, thumborKey);
            } else if (imgType.equals("member")) {
                thumbor = Thumbor.create(memberImgDomain, thumborKey);
            } else if (imgType.equals("common")) {
                thumbor = Thumbor.create(commonImgDomain, thumborKey);
            } else if (imgType.equals("shop")) {
                thumbor = Thumbor.create(shopImgDomain, thumborKey);
            } else {
                thumbor = Thumbor.create(snsImgDomain, thumborKey);
            }

            String thumborUrl = thumbor.buildImage(fullUrl).resize(width, height2).toUrl();
            URL path = new URL(thumborUrl);
            returnUrl = cfUrl + path.getPath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnUrl;
    }

    /*
     * domain 포함하여 full url 리턴
     * @param string fileUrl  기 db 저장된 url (예시: /store/26/55f24d86-3ef3-4dc1-a4d1-08afd03d4674.jpg)
     */
    public String getUploadedFullUrl(String fileUrl, String imgType) {
        try {
            String domain = switch (imgType) {
                case "sns" -> snsImgDomain;
                case "member" -> memberImgDomain;
                case "common" -> commonImgDomain;
                case "shop" -> shopImgDomain;
                default -> snsImgDomain;
            };
            return domain + fileUrl;
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TO get uploaded file(" + fileUrl + ") full url is error.");
        }
    }


    /**
     * 이미지 태그 CFurl 로 변환
     *
     * @param originalString
     * @param width
     * @return
     */
    public String replaceImgUrlToCFurl(String originalString, int width, String imgType) {

        /** 이미지 태그 패턴 **/
        Pattern imgTagPattern = Pattern.compile("\\<img src=\"([0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝|_|:|/|.|-]*)\"\\>");
        Matcher imgTagMatcher = imgTagPattern.matcher(originalString);

        StringBuilder stringBuilder = new StringBuilder();

        int lastIndex = 0;

        // 이미지 존재
        while (imgTagMatcher.find()) {

            stringBuilder.append(originalString, lastIndex, imgTagMatcher.start());
            // 패턴에 일치하는 문자열 이미지 태그 제거 후 반환
            String extractImgUrl = imgTagMatcher.group(1);
            // cfUrl 생성
            String cFurl = getCFurl(extractImgUrl, width, imgType);
            // 이미지 태그로 변환하여 cfUrl 추가
            stringBuilder.append(createImageTag(cFurl));
            lastIndex = imgTagMatcher.end(); // matcher 의 마지막 index로 초기화
        }
        stringBuilder.append(originalString.substring(lastIndex));

        return stringBuilder.toString();
    }


    /************************************************************
     * SUB
     ************************************************************/

    /**
     * 이미지 태그 생성
     *
     * @param imageUrl
     * @return
     */
    public String createImageTag(String imageUrl) {
        String imgTag = "<img src=\"" + imageUrl + "\">";

        return imgTag;
    }


}
