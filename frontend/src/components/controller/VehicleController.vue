<script setup>
import { ref, onMounted, nextTick, watch } from "vue";
import { initController, addController, removeController } from "../../util/map";
import { usePlayerStore } from "../../stores/player";

const isPaused = ref(false);
const position = window.naver.maps.Position.TOP_RIGHT;
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

const puaseOrRestart = () => {
  if (isPaused.value) playerStore.reStart();
  else playerStore.pause();
  isPaused.value = !isPaused.value;
};
</script>

<template>
  <div v-show="playerStore.tripStart" class="vehicle-controller-container" ref="controllerEl">
    <div class="vehicle-speed-btn" @click="playerStore.decreaseSpeed">⏪</div>
    <div class="vehicle-speed-btn" @click="puaseOrRestart">{{ isPaused ? "⏯" : "⏸" }}</div>
    <div class="vehicle-speed-btn" @click="playerStore.increaseSpeed">⏩</div>
  </div>
</template>

<style scoped>
.vehicle-controller-container {
  box-sizing: border-box;
  display: flex;
  padding: 5px 10px;
  margin: 16px 4px 0 0;
}
.vehicle-speed-btn {
  font-size: 30px;
  cursor: pointer;
}
</style>
