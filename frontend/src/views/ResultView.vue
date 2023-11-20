<script setup>
import TheHeader from "../commons/TheHeader.vue";
import ResultCard from "../components/ResultCard.vue"

const placeList = [{
	placeId : 0,
	placeName : '종묘',
	category : '세계문화유산',
	description : '서울특별시 종로구 훈정동[3] 1-2번지에 자리한 조선시대 역대 왕과 왕비 및 추존된 왕과 왕비의 신주(神主)를 모신 조선 왕실, 대한제국 황실의 유교 사당. 면적은 186,786 ㎡. 태묘(太廟)라고도 한다. '종묘사직'이란 말에서 알 수 있듯 전제왕조 당시 왕실과 나라를 상징하는 대표적인 건물 중 하나였다. 1963년 1월 18일 사적으로 지정되었고 1995년 유네스코 세계문화유산으로 등재되었다.',
	thumbnail : ''
}]

for(let i = 1; i <= 5; i++) place.push({...place[0], placeId: i});
</script>

<template>
  <div>
    <TheHeader></TheHeader>
    <div class="path-result-container">
      <ResultCard v-for="(place, idx) in placeList" :key="place.placeId" :place="place" :idx="idx"></ResultCard>
    </div>
  </div>
</template>

<style scoped>
header {
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
}
</style>
