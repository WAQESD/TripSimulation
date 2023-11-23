<script setup>
import { useTimeStore } from "../stores/time";

defineProps({
  place: Object,
  idx: Number,
});

const timeStore = useTimeStore();

const timeToString = (date) => {
  return new Date(date).toTimeString().split(" ")[0].slice(0, 5);
};
</script>

<template>
  <div class="result-card-container">
    <div :class="idx % 2 === 0 ? 'right-box' : 'left-box'">
      <div :class="idx % 2 === 0 ? 'result-card-index-right' : 'result-card-index-left'">{{ idx }}</div>
      <div :class="idx % 2 === 0 ? 'result-card-circle-right' : 'result-card-circle-left'"></div>
    </div>
    <div class="result-card" :class="idx % 2 === 0 ? 'right' : 'left'">
      <img v-if="!!place.thumbnail" class="result-card-thumbnail" :src="place.thumbnail" width="177" height="177" />
      <div v-else class="result-card-thumbnail result-card-placeholder">
        <img src="../assets/images/logo.svg" class="logo" />
        <span>Trippy</span>
      </div>
      <div class="result-card-detail">
        <div class="result-card-title">{{ place.placeName }}</div>
        <div class="result-card-address">{{ place.address }}</div>
        <div class="result-card-category">{{ place.category }}</div>
        <div class="result-card-arrival-time">
          {{ idx == 1 ? `${timeToString(timeStore.startTime)}` : timeToString(place.arrivalTime) }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.result-card-container {
  position: relative;
  width: 100vw;
  height: 250px;
  margin-top: 100px;
}

.result-card {
  position: absolute;
  display: flex;
  flex-direction: row;
  top: 70px;
  width: 450px;
  height: 177px;
  background-color: #f8f8f8;
  border-radius: 20px;
  box-sizing: border-box;
  box-shadow: 0px 4px 14.4px 0px rgba(0, 0, 0, 0.16);
}

.result-card-thumbnail {
  border: 3px #7c92ff7a solid;
  border-right: none;
  box-sizing: border-box;
}
.result-card-placeholder {
  box-sizing: border-box;
  background-color: white;
  border: 3px #7c92ff7a solid;
  border-right: none;
  width: 177px;
  height: 177px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.result-card-placeholder span {
  margin-left: 4px;
  font-size: 24px;
  font-family: "Baloo Chettan 2", sans-serif;
  color: #7c91ff;
}

.left {
  right: calc(50% + 80px);
}

.right {
  left: calc(50% + 80px);
}

.left-box,
.right-box {
  position: absolute;
  border-top: 2px #7c91ff solid;
  width: 418px;
  height: 50px;
  top: 0;
}

.left-box {
  right: 50%;
  border-left: 2px #7c91ff solid;
}

.right-box {
  left: 50%;
  border-right: 2px #7c91ff solid;
}

.result-card-index-right,
.result-card-index-left {
  box-sizing: border-box;
  width: 36px;
  height: 36px;
  line-height: 32px;
  border-radius: 18px;
  color: white;
  top: -18px;
  background-color: #7c91ff;
  border: 2px solid #9fffc0;
  text-align: center;
  position: absolute;
  font-size: 24px;
}
.result-card-index-left {
  right: -18px;
}

.result-card-index-right {
  left: -18px;
}

.result-card-circle-left,
.result-card-circle-right {
  position: absolute;
  width: 8px;
  height: 8px;
  bottom: -4px;
  border-radius: 4px;
  background-color: #7c91ff;
  box-sizing: border-box;
}

.result-card-circle-left {
  left: -5px;
}
.result-card-circle-right {
  right: -5px;
}

.result-card-thumbnail {
  border-radius: 20px 0 0 20px;
}

.result-card-detail {
  padding: 12px 14px 10px 15px;
  display: flex;
  flex-grow: 1;
  flex-direction: column;
  border: #6e6e6e44 3px solid;
  border-radius: 0 20px 20px 0;
}

.result-card-title {
  font-size: 20px;
}

.result-card-category {
  font-size: 12px;
  color: #6e6e6e;
  margin-top: 4px;
}

.result-card-address {
  margin-top: 4px;
  font-size: 14px;
  max-width: 350px;
}

.result-card-arrival-time {
  margin-top: auto;
  font-size: 24px;
  color: #7c91ff;
  font-family: "Baloo Chettan 2", sans-serif;
}
</style>
