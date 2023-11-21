<script setup>
import { ref, onMounted } from "vue";
import { makeInfoWindowByCoord, searchAddressToCoordinate } from "../../util/map";
import { usePlayerStore } from "../../stores/player";

import AddressList from "../AddressList.vue";

const emit = defineEmits(["previousMenu"]);

const searchResults = ref([]);
const playerStore = usePlayerStore();

const timer = null;

let infoWindow = new window.naver.maps.InfoWindow({
  anchorSkew: true,
  borderColor: "#6981ff",
  borderWidth: "2",
});

onMounted(() => {
  window.naver.maps.Event.addListener(playerStore.map, "click", function ({ coord }) {
    if (!infoWindow.getMap()) makeInfoWindowByCoord(coord, infoWindow);
    else infoWindow.close();
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

const previousMenu = () => {
  emit("previousMenu", 1);
};
</script>

<template>
  <div id="controller">
    <div class="plan-controller-previous" @click="previousMenu"><span>←</span></div>
    <div class="controller-input-container">
      <div class="controller-input-wrapper">
        <img class="controller-input-icon" src="../../assets/images/search.png" />
        <div class="controller-input-text">Start</div>
        <input
          type="text"
          placeholder="출발지를 정해주세요"
          id="start"
          v-model="playerStore.startPlace.address"
          @keyup="
            (e) => {
              searchAddr(e);
            }
          "
        />
      </div>

      <div class="controller-input-wrapper">
        <img class="controller-input-icon" src="../../assets/images/search.png" />
        <div class="controller-input-text">End</div>
        <input
          type="text"
          placeholder="도착지를 정해주세요"
          id="goal"
          v-model="playerStore.goalPlace.address"
          @keyup="
            (e) => {
              searchAddr(e);
            }
          "
        />
      </div>
      <div class="path-controller-seperator"></div>
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
  padding: 10px 20px;
  border-right: 1px solid rgba(0, 0, 0, 0.3);
  box-sizing: border-box;
  width: 400px;
  height: 100vh;
  padding-top: 70px;
}

.plan-controller-previous {
  width: 100%;
  text-align: left;
  font-weight: bold;
  font-size: 20px;
  cursor: pointer;
  transition: all 0.2s;
  margin-bottom: 10px;
  color: rgba(0, 0, 0, 0.5);
}

.plan-controller-previous:hover {
  color: #7c91ff;
}

.controller-input-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-grow: 1;
}

.controller-input-wrapper {
  position: relative;
  height: 50px;
  margin-bottom: 20px;
}

.controller-input-text {
  position: absolute;
  top: -10px;
  left: 40px;
  background-color: white;
  width: 60px;
  height: 20px;
  text-align: center;
}
.controller-input-icon {
  position: absolute;
  width: 26px;
  height: 26px;
  top: 12px;
  left: 14px;
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

input {
  display: block;
  box-sizing: border-box;
  width: 300px;
  height: 50px;
  padding-left: 60px;
  border: 2px solid #7c91ff;
  line-height: 50px;
  font-size: 16px;
  border-radius: 10px;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
}

.path-controller-seperator {
  width: 399px;
  box-sizing: border-box;
  height: 13px;
  background-color: #f1f1f1;
  margin-top: 30px;
}
</style>
