<script setup>
import { ref, computed, onMounted, nextTick, watch } from "vue";
import {
  initController,
  addController,
  removeController,
  makeInfoWindowByCoord,
  searchAddressToCoordinate,
} from "../../util/map";
import { usePlayerStore } from "../../stores/player";

import AddressList from "../AddressList.vue";
const position = window.naver.maps.Position.TOP_LEFT;

const startPos = ref(null);
const goalPos = ref(null);

const startAddr = ref("");
const goalAddr = ref("");

const isClosed = ref(false);
const controllerEl = ref(null);
const searchResults = ref([]);
const playerStore = usePlayerStore();

const btnIcon = computed(() => (isClosed.value ? ">" : "<"));

const toggleController = () => {
  isClosed.value = !isClosed.value;
};

const timer = null;

let infoWindow = new window.naver.maps.InfoWindow({
  anchorSkew: true,
});

const makeInfoWindow = (item) => {
  let coord = new window.naver.maps.LatLng(item.point.y, item.point.x);
  if (infoWindow) infoWindow.close();
  makeInfoWindowByCoord(coord, infoWindow, playerStore.map, startPos, startAddr, goalPos, goalAddr);
  playerStore.map.setCenter(coord);
};

const searchAddr = (e) => {
  if (timer) clearTimeout(timer);

  setTimeout(() => {
    searchAddressToCoordinate(
      e.target.value,
      playerStore.map,
      infoWindow,
      startPos,
      goalPos,
      startAddr,
      goalAddr,
      searchResults
    );
  }, 150);
};

onMounted(() => {
  nextTick(() => {
    initController(playerStore.map, controllerEl.value, position);

    window.naver.maps.Event.addListener(playerStore.map, "click", function ({ coord }) {
      if (infoWindow) infoWindow.close();
      makeInfoWindowByCoord(coord, infoWindow, playerStore.map, startPos, startAddr, goalPos, goalAddr);
    });
  });
});

const getPath = () => {
  playerStore.getPath(startPos.value, goalPos.value);
};

watch(
  () => !playerStore.tripStart,
  () => {
    if (!playerStore.tripStart) addController(playerStore.map, controllerEl.value, position);
    else removeController(playerStore.map, controllerEl.value, position);
  }
);
</script>

<template>
  <div ref="controllerEl">
    <div :class="isClosed ? 'closed' : ''" id="controller">
      <div class="controller-input-container">
        <div class="input-wrapper">
          <label id="label-start" for="start">출 발</label>
          <input
            type="text"
            placeholder="출발지 입력"
            id="start"
            v-model="startAddr"
            @keyup="
              (e) => {
                searchAddr(e);
              }
            "
          />
        </div>
        <div class="input-wrapper">
          <label id="label-goal" for="goal">도 착</label>
          <input
            type="text"
            placeholder="도착지 입력"
            id="goal"
            v-model="goalAddr"
            @keyup="
              (e) => {
                searchAddr(e);
              }
            "
          />
        </div>
        <button type="button" @click="getPath">경로 찾기</button>
        <AddressList :addressList="searchResults" @search-address="makeInfoWindow"></AddressList>
      </div>
    </div>
    <div class="close-btn" :class="isClosed ? 'closed' : ''" @click="toggleController">{{ btnIcon }}</div>
  </div>
</template>

<style scoped>
#controller {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: white;
  height: 40px;
  padding: 40px 0;
  border-radius: 0 16px 16px 0;
  border-right: 1px solid rgba(0, 0, 0, 0.3);
  box-sizing: border-box;
  width: 400px;
  height: 100vh;
  transition: transform 0.5s;
}

.controller-input-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-grow: 1;
}

#controller button {
  background-color: white;
  border: 1px solid rgba(0, 0, 0, 0.5);
  cursor: pointer;
  padding: 2px 8px;
  margin-top: 20px;
  height: 40px;
  width: 330px;
  border-radius: 8px;
  box-sizing: border-box;
  font-family: "Pretendard-Regular";
  font-size: 16px;
}

#controller button:hover {
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
}

label,
input {
  font-family: "Pretendard-Regular";
  display: inline-block;
  box-sizing: border-box;
  height: 40px;
  padding: 0 12px;
  border: 1px solid rgba(0, 0, 0, 0.7);
  line-height: 40px;
  font-size: 16px;
}

label {
  width: 80px;
  text-align: center;
  background-color: #edede9;
}

input {
  width: 250px;
}

.input-wrapper {
  display: flex;
  height: 40px;
}

#label-start {
  border-radius: 8px 0 0 0;
  border-bottom: none;
}
#start {
  border-radius: 0 8px 0 0;
  border-bottom: none;
  border-left: none;
}
#goal {
  border-radius: 0 0 8px 0;
  border-left: none;
}
#label-goal {
  border-radius: 0 0 0 8px;
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
  transition: transform 0.5s;
}

.closed {
  transform: translateX(-400px);
}
</style>
