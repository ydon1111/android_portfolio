# FavoritePlacesAPP

Google map API를 통해 지도에 방문한 장소를 남기고 기록하는 어플 

## 개발 환경

1. kotlin
2. targetSdkVersion 31, minSdkVersion 21
3. 유저에게 디바이스 자원,정보 사용요청(카메라,위치,폴더)
4. 외부라이브러리 사용 (CircleImageView,Dexter)
5. SQLite 사용 (CRUD구현)
6. constraintlayout 기반의 UI구성


## 기본구현

1. Recyclerview 통한 저장 장소 시각화 
    1. 밀어서 수정 및 삭제 구현 
    2. 아무런 저장 장소가 없을 시 추억 장소 추가 화면 

2. 추억장소 저장  
    1. 방문장소 및 상세정보, 방문날짜, 방문위치 , 이미지 추가 및 저장 
    2. 방문날짜는 달력형식으로 출력 
    3. 구글맵에서 방문위치 검색 후 마킹 및 위치좌표 출력 
    4. 방문위치는 GPS,Network정보를 바탕으로 현위치 공유 기능 구현 
    5. 이미지는 bitmap형식으로 저장 및 불러옴 
    6. 카메라로 바로 촬영 후 추가 또는 갤러리에서 사진 불러옴 

3. 수정 화면  
    1. 추억장소 저장과 기능은 동일 
    2. DB상에서 Updata항목을 구현

4. 상세 페이지 
    1. 저장한 이미지 확대하여 출력  
    2. 방문장소 및 상세정보 표시
    3. 방문위치 지도 마킹 출력 

## 구현진행중인 사항

## 앱 이미지 
<div>

![main](https://github.com/ydon1111/android_portfolio/assets/66169252/2879f808-77fa-4b28-8e29-59bce4f4dd05)
![placeAdd](https://github.com/ydon1111/android_portfolio/assets/66169252/e54f3ae8-891d-4d4a-afbd-c2386c68b6e2)
![accescontrol2](https://github.com/ydon1111/android_portfolio/assets/66169252/4a6debe2-7fba-43d3-a42a-2a78254d0df0)
![accesscontrol](https://github.com/ydon1111/android_portfolio/assets/66169252/9b0b9cf8-b3a7-42bf-8ba5-09d8367fca3d)
![diary](https://github.com/ydon1111/android_portfolio/assets/66169252/ebaaa8cf-77d6-495c-a2d7-5a18034d2589)
![list](https://github.com/ydon1111/android_portfolio/assets/66169252/4585c133-cb5d-4112-9511-57022a63f446)

</div>


## 버그 사항


