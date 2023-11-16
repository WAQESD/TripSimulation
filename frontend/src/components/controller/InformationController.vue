<script setup>
import { ref, onMounted, nextTick, watch } from "vue";
import { initController, addController, removeController } from "../../util/map";
import { usePlayerStore } from "../../stores/player";

const props = defineProps({
  map: Object,
  show: Boolean,
});

const position = window.naver.maps.Position.TOP_LEFT;
const controllerEl = ref(null);
const playerStore = usePlayerStore();

onMounted(() => {
  nextTick(() => {
    initController(playerStore.map, controllerEl.value, position);
  });
});

watch(
  () => playerStore.tripStart,
  () => {
    if (playerStore.tripStart) addController(playerStore.map, controllerEl.value, position);
    else removeController(playerStore.map, controllerEl.value, position);
  }
);
</script>

<template>
  <div class="information-controller-container" v-show="playerStore.tripStart" ref="controllerEl">
    <div v-if="playerStore.tripStart" class="information-controller-time">
      {{ `${playerStore.departureTime.hour}:${playerStore.departureTime.minute}` }}
      <div class="information-contorller-departure">출발</div>
    </div>
    <div class="information-controller-icon">→</div>
    <div v-if="playerStore.tripStart" class="information-controller-time">
      {{ `${playerStore.arrivalTime.hour}:${playerStore.arrivalTime.minute}` }}
      <div class="information-contorller-arrival">도착</div>
    </div>
  </div>
</template>

<style scoped>
.information-controller-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding: 8px 24px;
  margin: 36px 12px;
  font-family: "Pretendard-Regular";
}

.information-controller-icon {
  font-size: 32px;
  padding: 4px 8px;
  line-height: 24px;
  font-weight: bold;
  margin-bottom: 8px;
}
.information-controller-time {
  position: relative;
  font-size: 20px;
  margin: 0 10px;
  padding: 8px 12px;
  border-radius: 24px;
  border: 1px solid black;
  background-color: white;
  width: 60px;
  text-align: center;
}
.information-contorller-departure,
.information-contorller-arrival {
  position: absolute;
  top: -34px;
  font-size: 20px;
  width: 60px;
  text-align: center;
}
</style>
