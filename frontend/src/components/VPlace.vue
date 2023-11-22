<script setup>
import { usePlayerStore } from "../stores/player";
const playerStore = usePlayerStore();

const props = defineProps({
  place: Object,
  isSelected: Boolean,
  isRemovable: Boolean,
});

const removeWayPoint = () => {
  playerStore.removeWayPoint(props.place);
};
</script>

<template>
  <div class="place-container" @click="onClick">
    <slot></slot>
    <!-- <img class="place-thumbnail" v-show="place.thumbnail" :src="place.thumbnail" /> -->
    <div class="place-info-container">
      <img
        class="place-remove-btn"
        v-if="isRemovable && !playerStore.tripStart"
        @click.stop="removeWayPoint"
        src="../assets/images/remove.svg"
      />
      <div class="place-info">
        <div class="place-name">{{ place.placeName }}</div>
        <div class="place-address">{{ place.address || place.placeAddress }}</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.place-container {
  position: relative;
  display: flex;
  flex-direction: row;
  align-items: center;
  font-family: "Pretendard-Regular";
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.4s;
  padding: 10px;
  box-sizing: border-box;
  border: 2px solid white;
  flex-grow: 1;
  border: 1px solid #b6b6b6;
  border-radius: 4px;
  height: 70px;
}

.place-container:hover {
  color: rgba(255, 0, 0, 0.7);
  border: 1px solid rgba(255, 0, 0, 0.7);
  border-radius: 4px;
}

.place-info-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin-left: auto;
  max-width: 100%;
}
.place-thumbnail {
  width: 160px;
  height: 90px;
}

.place-time-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.place-name,
.place-address {
  text-align: right;
}
.place-name {
  font-size: 18px;
  font-weight: bold;
}
.place-address {
  font-size: 10px;
}

.place-time {
  width: 40px;
  text-align: center;
}

.place-remove-btn {
  position: absolute;
  top: -15px;
  right: -15px;
  width: 30px;
  color: rgba(255, 0, 0, 0.7);
  background-color: white;
  border-radius: 15px;
}
.slide-left {
  animation: slide-left 0.3s both;
}

@keyframes slide-left {
  0% {
    opacity: 0;
    transform: translateX(200px);
  }
  100% {
    opacity: 1;
    transform: translateX(0);
  }
}
</style>
