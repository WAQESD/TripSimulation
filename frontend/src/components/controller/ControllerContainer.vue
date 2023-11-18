<script setup>
import { ref, computed, onMounted, nextTick, watch } from "vue";
import { initController, addController, removeController } from "../../util/map";
import { usePlayerStore } from "../../stores/player";

import PathController from "./PathController.vue";
import PlanController from "./PlanController.vue";

const playerStore = usePlayerStore();

const position = window.naver.maps.Position.TOP_LEFT;
const controllerEl = ref(null);

const menuSelector = ref(0);
const isClosed = ref(false);
const btnIcon = computed(() => (isClosed.value ? ">" : "<"));

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

const toggleController = () => {
  isClosed.value = !isClosed.value;
};

const toggleMenu = () => {
  if (menuSelector.value === 0) menuSelector.value = 1;
  else menuSelector.value = 0;
};

document.addEventListener("keydown", (e) => {
  if (e.code === "Space") {
    toggleMenu();
  }
});
</script>

<template>
  <div class="controller-container" ref="controllerEl" :class="isClosed ? 'closed' : ''">
    <div class="search-btn"><img class="search-btn-icon" src="../../assets/images/search.png" /></div>
    <PathController v-show="menuSelector === 0" />
    <PlanController v-show="menuSelector === 1" />
    <div class="close-btn" @click="toggleController">{{ btnIcon }}</div>
  </div>
</template>

<style scoped>
.controller-container {
  transition: transform 0.5s;
  box-shadow: rgba(100, 100, 111, 0.4) 0px 7px 29px 0px;
}

.close-btn {
  position: absolute;
  top: calc(50vh - 40px);
  left: 400px;
  width: 24px;
  height: 80px;
  border: 1px solid rgba(0, 0, 0, 0.3);
  line-height: 80px;
  border-left: none;
  background-color: white;
  border-radius: 0 8px 8px 0;
  cursor: pointer;
  text-align: center;
}

.search-btn-icon {
  width: 20px;
  height: 20px;
}

.closed {
  transform: translateX(-400px);
}
</style>
