<script setup>
import { ref, onMounted } from "vue";
import { douglasPeucker } from "../util/DouglasPeucker";
import axios from "axios";

import PathController from "./controller/PathController.vue";

let path = null;
let polylinePath = null;

const car = ref("none");
const angle = ref(0);
const name = ref("경로");
const pathController = ref(null);
const map = ref(null);

onMounted(() => {
  map.value = new window.naver.maps.Map("map", {
    center: new window.naver.maps.LatLng(import.meta.env.VITE_CENTER_LAT, import.meta.env.VITE_CENTER_LNG),
    zoom: 14,
  });

  map.value.setCursor("pointer");
});

let getAngle = (s, e) => {
  let rad = Math.atan2(e.y - s.y, e.x - s.x);
  return 90 - (rad * 180) / Math.PI;
};

let endPath = () => {
  car.value = "none";
  alert("도착했습니다.");
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
  }, dist * 300000);

  map.value.setCenter(s);
  map.value.panTo(e, {
    duration: dist * 300000,
    easing: "linear",
  });
};

let startPath = () => {
  move(0);
  car.value = "block";
};

let getPath = async (start, goal) => {
  if (!start || !goal) return;

  let { data } = await axios({
    method: "post",
    url: "http://localhost:8080/save",
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
</script>

<template>
  <div id="map-container">
    <img id="car" src="../assets/images/car.png" :style="{ display: car, transform: `rotate(${angle}deg)` }" />
    <div id="map"></div>
    <PathController @get-path="getPath" :map="map" ref="pathController"></PathController>
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
