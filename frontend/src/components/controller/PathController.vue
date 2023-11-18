<script setup>
import { ref, onMounted } from "vue";
import { makeInfoWindowByCoord, searchAddressToCoordinate } from "../../util/map";
import { usePlayerStore } from "../../stores/player";

import AddressList from "../AddressList.vue";

const searchResults = ref([]);
const playerStore = usePlayerStore();

const timer = null;

let infoWindow = new window.naver.maps.InfoWindow({
  anchorSkew: true,
});

onMounted(() => {
  window.naver.maps.Event.addListener(playerStore.map, "click", function ({ coord }) {
    if (infoWindow) infoWindow.close();
    makeInfoWindowByCoord(coord, infoWindow);
  });
});

const makeInfoWindow = (item) => {
  let coord = new window.naver.maps.LatLng(item.point.y, item.point.x);
  if (infoWindow) infoWindow.close();
  makeInfoWindowByCoord(coord, infoWindow);
  playerStore.map.setCenter(coord);
};

const searchAddr = (e) => {
  if (timer) clearTimeout(timer);

  setTimeout(() => {
    searchAddressToCoordinate(e.target.value, infoWindow, searchResults);
  }, 150);
};

const getPath = () => {
  playerStore.getPath();
};
</script>

<template>
  <div id="controller">
    <div class="controller-input-container">
      <div class="input-wrapper">
        <label id="label-start" for="start">출 발</label>
        <input
          type="text"
          placeholder="출발지 입력"
          id="start"
          v-model="playerStore.startPlace.address"
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
          v-model="playerStore.goalPlace.address"
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
</style>
