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
  () => !playerStore.tripStart,
  () => {
    if (!playerStore.tripStart) addController(playerStore.map, controllerEl.value, position);
    else removeController(playerStore.map, controllerEl.value, position);
  }
);
</script>

<template>
  <div v-show="playerStore.tripStart" ref="controllerEl"></div>
</template>

<style scoped></style>
