<script setup>
import { usePlayerStore } from "../../stores/player";

import VPlace from "../VPlace.vue";

const playerStore = usePlayerStore();
</script>

<template>
  <div class="plan-controller-container">
    <h2 class="plan-controller-title">여행 경로 설정</h2>
    <fieldset v-if="playerStore.startPlace.lat > 0" class="plan-controller-start">
      <legend>출발</legend>
      <VPlace :place="playerStore.startPlace"></VPlace>
    </fieldset>
    <div v-else><h2>출발지를 설정해주세요.</h2></div>
    <fieldset v-if="playerStore.wayPoints.length > 0" class="plan-controller-waypoint-list">
      <legend>경유</legend>
      <VPlace v-for="place in playerStore.wayPoints" :key="place.placeId" :place="place"></VPlace>
    </fieldset>
    <fieldset v-if="playerStore.goalPlace.lat > 0" class="plan-controller-goal">
      <legend>도착</legend>
      <VPlace :place="playerStore.goalPlace"></VPlace>
    </fieldset>
    <div v-else><h2>목적지를 설정해주세요.</h2></div>
  </div>
</template>

<style scoped>
.plan-controller-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: white;
  padding: 20px 20px;
  border-radius: 0 16px 16px 0;
  border-right: 1px solid rgba(0, 0, 0, 0.3);
  box-sizing: border-box;
  width: 400px;
  height: 100vh;
  overflow: auto;
  overflow-x: hidden;
  z-index: 2;
}

.plan-controller-start,
.plan-controller-goal,
.plan-controller-waypoint-list {
  width: 100%;
  border: none;
}
.plan-controller-container::-webkit-scrollbar {
  display: none;
}

.plan-controller-title {
  width: 100%;
  text-align: center;
  padding: 0 0px 40px 0;
  border-bottom: 1px solid black;
}

legend {
  font-family: "Pretendard-Regular";
  font-size: 14px;
}
</style>
