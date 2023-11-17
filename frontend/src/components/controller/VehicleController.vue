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
    <img class="vehicle-speed-btn decrease" @click="playerStore.decreaseSpeed" src="@/assets/images/previous.png" />
    <img
      class="vehicle-speed-btn"
      @click="puaseOrRestart"
      :src="isPaused ? './src/assets/images/play.png' : './src/assets/images/pause.png'"
    />
    <img class="vehicle-speed-btn increase" @click="playerStore.increaseSpeed" src="@/assets/images/fast_forward.png" />
  </div>
</template>

<style scoped>
.vehicle-controller-container {
  box-sizing: border-box;
  display: flex;
  margin: 16px 12px 0 0;
  border-radius: 15px 0 0 15px;
}
.vehicle-speed-btn {
  width: 40px;
  height: 40px;
  cursor: pointer;
  padding: 5px 4px;
  z-index: 2;
  transition: all 0.3s;
  opacity: 0.4;
}

.vehicle-speed-btn:hover {
  opacity: 1;
}

.decrease,
.increase {
  width: 46px;
  height: 46px;
  padding: 2px 4px;
}
</style>
