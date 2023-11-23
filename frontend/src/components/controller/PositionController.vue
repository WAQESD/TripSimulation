<script setup>
import { ref, onMounted, nextTick, watch } from "vue";
import { initController, addController, removeController } from "../../util/map";
import { usePlayerStore } from "../../stores/player";

const position = window.naver.maps.Position.BOTTOM_RIGHT;

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
  <div v-show="playerStore.tripStart" ref="controllerEl">
    <img
      class="current-position-icon"
      src="../../assets/images/current_position.png"
      @click="playerStore.toggleTraceMode"
    />
  </div>
</template>

<style scoped>
.current-position-icon {
  position: absolute;
  right: 36px;
  bottom: 36px;
  width: 30px;
  height: 30px;
  padding: 12px;
  border-radius: 40px;
  background-color: white;
  border: 1px solid #7c91ff;
  cursor: pointer;
  box-shadow: rgba(100, 100, 111, 0.5) 0px 7px 29px 0px;
}
</style>
