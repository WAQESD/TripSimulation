<script setup>
import { ref, computed, onMounted, nextTick } from "vue";
import { initController } from "../../util/map";
import { usePlayerStore } from "../../stores/player";

import PathController from "./PathController.vue";
import PlanController from "./PlanController.vue";
import InformationController from "./InformationController.vue";

const playerStore = usePlayerStore();

const position = window.naver.maps.Position.TOP_LEFT;
const controllerEl = ref(null);

const menuSelector = ref(1);
const isClosed = ref(false);
const btnIcon = computed(() => (isClosed.value ? ">" : "<"));

onMounted(() => {
  nextTick(() => {
    initController(playerStore.map, controllerEl.value, position);
  });
});

const toggleController = () => {
  isClosed.value = !isClosed.value;
};

const toggleMenu = () => {
  if (menuSelector.value === 0) menuSelector.value = 1;
  else menuSelector.value = 0;
};

const setMenu = (value) => {
  menuSelector.value = value;
  isClosed.value = false;
};

document.addEventListener("keydown", (e) => {
  if (e.code === "Space") {
    toggleMenu();
  }
});
</script>

<template>
  <div class="controller-container" ref="controllerEl" :class="isClosed ? 'closed' : ''">
    <div class="controller-container-header">
      <img class="controller-container-header-logo" src="../../assets/images/logo.svg" />
      Trippy
    </div>
    <div class="btn-container">
      <div
        class="search-btn"
        @click="
          () => {
            setMenu(0);
          }
        "
        v-show="menuSelector !== 0"
      >
        <img class="search-btn-icon" src="../../assets/images/search.png" />
      </div>
      <div class="set-path-btn" v-show="!playerStore.tripStart" @click="playerStore.startTrip">
        <img class="set-path-icon" src="../../assets/images/path.png" />
      </div>
    </div>
    <PathController v-show="menuSelector === 0" @previous-menu="setMenu" />
    <PlanController v-show="menuSelector === 1 && !playerStore.tripStart" />
    <InformationController v-show="menuSelector === 1 && playerStore.tripStart" />
    <div class="close-btn" @click="toggleController">{{ btnIcon }}</div>
  </div>
</template>

<style scoped>
.controller-container {
  z-index: 3;
  transition: transform 0.5s;
  box-shadow: rgba(100, 100, 111, 0.4) 0px 7px 29px 0px;
}

.controller-container-header {
  position: absolute;
  display: flex;
  height: 50px;
  align-items: center;
  padding: 30px 40px;
  font-size: 26px;
  width: 400px;
  box-sizing: border-box;
  border-bottom: #37469e57 2px solid;
  font-family: "Baloo Chettan 2", sans-serif;
  box-shadow: rgba(100, 100, 111, 0.4) 0px 1px 29px 0px;
}

.controller-container-header-logo {
  width: 32px;
  height: 32px;
  margin-right: 12px;
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

.btn-container {
  display: flex;
  flex-direction: column;
  position: absolute;
  justify-content: center;
  align-items: center;
  left: 420px;
  top: 20px;
}
.search-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50px;
  height: 50px;
  background-color: white;
  border-radius: 32px;
  box-sizing: border-box;
  cursor: pointer;
  border: 1px solid #7c91ff;
  box-shadow: rgba(100, 100, 111, 0.4) 0px 7px 29px 0px;
  margin-bottom: 10px;
}
.search-btn-icon {
  width: 24px;
  height: 24px;
  box-sizing: border-box;
}

.set-path-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50px;
  height: 50px;
  background-color: white;
  border-radius: 32px;
  box-sizing: border-box;
  cursor: pointer;
  border: 1px solid #7c91ff;
  box-shadow: rgba(100, 100, 111, 0.4) 0px 7px 29px 0px;
}

.set-path-icon {
  width: 30px;
  height: 30px;
  box-sizing: border-box;
}

.closed {
  transform: translateX(-400px);
}
</style>
