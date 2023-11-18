<script setup>
import { usePlayerStore } from "../../stores/player";

import VPlace from "../VPlace.vue";

const playerStore = usePlayerStore();
</script>

<template>
  <div class="plan-controller-container">
    <div class="plan-controller-start">
      <VPlace :place="playerStore.startPlace"></VPlace>
    </div>
    <div class="plan-controller-waypoint-list">
      <VPlace v-for="place in playerStore.wayPoints" :key="place.placeId" :place="place"></VPlace>
    </div>
    <div class="plan-controller-goal">
      <VPlace :place="playerStore.goalPlace"></VPlace>
    </div>
  </div>
</template>

<style scoped>
.plan-controller-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: white;
  padding: 40px 20px;
  border-radius: 0 16px 16px 0;
  border-right: 1px solid rgba(0, 0, 0, 0.3);
  box-sizing: border-box;
  width: 400px;
  height: 100vh;
  overflow: auto;
}

.plan-controller-start,
.plan-controller-goal,
.plan-controller-waypoint-list {
  width: 100%;
}
.plan-controller-container::-webkit-scrollbar {
  display: none;
}
</style>
