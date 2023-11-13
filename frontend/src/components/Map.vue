<script setup>
import { douglasPeucker } from "../util/DouglasPeucker";
import { useModalStore } from "../stores/modal";
import { ref, onMounted } from "vue";
import axios from "axios";

import PathController from "./controller/PathController.vue";
import VehicleController from "./controller/VehicleController.vue";
import SimpleTextModal from "./modal/SimpleTextModal.vue";

let path = null;
let polylinePath = null;

const car = ref("none");
const angle = ref(0);
const name = ref("경로");
const map = ref(null);
const tripStart = ref(false);

const modalStore = useModalStore();

onMounted(() => {
  map.value = new window.naver.maps.Map("map", {
    center: new window.naver.maps.LatLng(import.meta.env.VITE_DEFAULT_LAT, import.meta.env.VITE_DEFAULT_LNG),
    zoom: 14,
  });

  map.value.setCursor("pointer");
});

let getAngle = (s, e) => {
  let rad = Math.atan2(e.y - s.y, e.x - s.x);
  return 90 - (rad * 180) / Math.PI;
};

let endPath = () => {
  modalStore.setModal(true, SimpleTextModal, {
    text: "목적지에 도착했습니다.",
    callback: () => {
      car.value = "none";
    },
  });
};

let move = (index) => {
  if (polylinePath.length <= index + 1) {
    endPath();
    return;
  }

  let s = polylinePath[index];
  let e = polylinePath[index + 1];

  let dist = Math.sqrt((s.x - e.x) ** 2 + (s.y - e.y) ** 2);
  angle.value = getAngle(s, e);

  setTimeout(() => {
    move(index + 1);
  }, dist * 700000);

  map.value.setCenter(s);
  map.value.panTo(e, {
    duration: dist * 700000,
    easing: "linear",
  });
};

let startPath = () => {
  modalStore.setModal(true, SimpleTextModal, {
    callback: () => {
      move(0);
      car.value = "block";
      tripStart.value = true;
    },
    text: "지정한 경로로 시뮬레이션을 시작합니다.",
  });
};

let getPath = async (start, goal) => {
  if (!start || !goal) return;

  let { data } = await axios({
    method: "post",
    url: "http://ec2-54-180-89-8.ap-northeast-2.compute.amazonaws.com:8080/save",
    data: {
      name: name.value,
      start: { lng: start.x, lat: start.y },
      goal: { lng: goal.x, lat: goal.y },
    },
  });

  let pathData = data.route.traoptimal[0].path;
  let zipped = douglasPeucker(pathData, 0.00002);

  console.log("before : ", pathData.length, "after : ", zipped.length);

  polylinePath = zipped.filter((p) => !!p).map(([lng, lat]) => new window.naver.maps.LatLng(lat, lng));
  // polylinePath = pathData.map(([lng, lat]) => new window.naver.maps.LatLng(lat, lng));

  if (path) path.setMap(null);

  path = new window.naver.maps.Polyline({
    path: polylinePath,
    strokeColor: "#5347AA",
    map: map.value,
  });

  map.value.setCenter(polylinePath[0]);
  map.value.setZoom(19);
  startPath();
};

document.addEventListener("keydown", (e) => {
  if (e.key == "a") {
    tripStart.value = !tripStart.value;
  }
});
</script>

<template>
  <div id="map-container">
    <img id="car" src="../assets/images/car.png" :style="{ display: car, transform: `rotate(${angle}deg)` }" />
    <div id="map"></div>
    <PathController :show="!tripStart" @get-path="getPath" :map="map"></PathController>
    <VehicleController :show="tripStart" :map="map"></VehicleController>
  </div>
</template>

<style scoped>
#map {
  width: 100vw;
  height: 100vh;
  z-index: 0;
}
#car {
  position: absolute;
  z-index: 1;
  left: calc(50vw - 22px);
  top: calc(50vh - 42.5px);
  width: 44px;
  height: 85px;
}
#map-container {
  position: relative;
}
</style>
