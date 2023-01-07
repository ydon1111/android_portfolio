# NaverMovieAPP

Naver 검색API를 통해 영화데이터를 불러와서 시각화 해주는 어플 

## 개발 환경

1. kotlin,retrofit,room 
2. targetSdkVersion 32, minSdkVersion 21
3. jetpack (dataBinding, hilt, lifecycle, paging )사용
4. glide 사용 
7. constraintlayout 기반의 UI구성
8. MVVM 패턴

## 기본구현

1. 메인화면 
    1. 검색, 최근검색 
2. 영화검색 
    1. 영화 타이틀 입력 후 검색 버튼 클릭
    2. 네이버 검색API를 사용하여 영화 이미지,제목,출시,평점 불러옴 
    3. Paging 을 사용하여 내용 출력 
3. 영화클릭 
    1. 영화 클릭시 네이버 검색API 제공 link로 webView 출력 
4. 최근검색 
    1. 영화 타이틀 입력값을 입력 순서에 따라 10개 저장 
    2. Room을 사용하여 로컬에 데이터 저장 
    3. 최근 검색 타이틀 클릭시 해당 검색어로 정보 출력 
   
## 구현진행중인 사항

## 버그 사항

