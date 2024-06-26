echo Start

######## 스프링 부트 빌드 처리 ##################
./gradlew build
echo build Complete

######## jar 파일 복사 ##########################
cp /Users/ghainb/Sites/springboot_admin/build/libs/ROOT.jar /Users/ghainb/Sites/docker_springboot-main/conf/springboot/app/ROOT.jar
echo Copy Complete

####### 도커 경로로 이동 #######################
cd /Users/ghainb/Sites/docker_springboot-main
echo Move Complete

######## 스프링부트 컨테이너 재빌딩 #############
docker compose up -d --build springboot
echo Rebuild Complete
