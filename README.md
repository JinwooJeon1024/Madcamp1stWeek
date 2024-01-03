# 오늘 뭐 먹지?
맛집 리스트, 리뷰, 맛집 추천 앱
<img src="https://github.com/JinwooJeon1024/Madcamp1stWeek/assets/104386015/36bf22fe-fdba-4ac4-aaa7-74b1af482f92" width="30%"/>



## 프로젝트 소개
탭 구조를 활용한 안드로이드 앱
<br>

## 개발 기간
* 23.12.28일 - 24.01.03일

### 맴버구성
 - 조수연(이화여대 컴퓨터공학부 20학번)
 - 전진우(카이스트 전산학부 21학번)

### 개발 환경
- **Front-end** : Kotlin
- **OS** : Android
- **IDE** : Android Studio
- **Target Device** : Galaxy S7

## 주요 기능
#### 맛집 리스트
<p align="center">
  <img src="https://github.com/JinwooJeon1024/Madcamp1stWeek/assets/104386015/ffcb4201-c628-4d14-be13-a9c38c0be94f" width="30%" style="margin-right:10px;"/>
  <img src="https://github.com/JinwooJeon1024/Madcamp1stWeek/assets/104386015/1982b3c6-ce3b-4f0a-9954-1654fb62140c" width="30%"/>
</p>

- **Major Features** : 이름순으로 정렬된 맛집 연락처가 나타납니다. 등록하기, 수정하기, 삭제하기 모두 가능합니다.
- **기술 설명** : RecyclerView를 이용해서 JSON에 저장된 데이터를 나열했고 새 액티비티를 열어서 식당 등록하기를 구현했습니다. CardView를 이용해서 UI를 구성했고 각 아이템을 누르면 Dialog를 통해서 수정, 삭제가 가능하게 만들었습니다.

#### 리뷰
<p align="center">
  <img src="https://github.com/JinwooJeon1024/Madcamp1stWeek/assets/104386015/f5f76342-d827-486d-a466-cc2686951d06" width="30%"/>
  <img src="https://github.com/JinwooJeon1024/Madcamp1stWeek/assets/104386015/c03442ae-3f3e-4529-a1b7-857545eb3b88" width="30%"/>

</p>

- **Major Features** : 이름순으로 정렬된 리뷰가 나타납니다. 갤러리와 연동해서 이미지가 포함된 리뷰를 업로드할 수 있습니다.
- **기술 설명** : 마찬가지로 RecyclerView를 이용해서 JSON에 저장된 리뷰 데이터를 나열했고, 새 액티비티 창에서 갤러리와 연동되어 업로드가 가능합니다. ViewPager2를 이용해서 스와이프로 탭을 이동할 수 있고, Spinner를 이용하여 이름순, 별점 높은 순, 별점 낮은 순으로 정렬이 가능합니다.

#### 맛집로또
<p align="center">
  <img src="https://github.com/JinwooJeon1024/Madcamp1stWeek/assets/104386015/82d0b487-db41-40b5-915e-30e63b9f13be" width="30%" style="margin-right:10px;"/>
  <img src="https://github.com/JinwooJeon1024/Madcamp1stWeek/assets/104386015/6f7817fa-7061-437b-9dd2-eca49c358bcd" width="30%"/>
  <img src="https://github.com/JinwooJeon1024/Madcamp1stWeek/assets/104386015/26a81d06-0d65-4186-82b3-78558ea6d450" width="30%"/>

</p>

- **Major Features** : 먹고 싶은 음식 카테고리를 원하는 만큼 고르면, 해당하는 카테고리 중에서 랜덤으로 식당을 골라줍니다.
- **기술 설명** : ImageButton을 이용해서 각 음식 카테고리를 선택할 수 있게 했고, 애니메이션을 이용해 로또 공이 움직이는 것을 구현했습니다. 마찬가지로 Dialog를 이용해서 팝업을 구현했고 선택한 카테고리에 해당하는 음식들만 리스트에 추가해서 리스트 중 랜덤으로 골라서 나타나게 만들었습니다.
