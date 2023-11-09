<script setup>
import { ref, onMounted } from "vue";
import { URL, CENTER } from "../util/API";
import { addController } from "../util/map";
import { douglasPeucker } from "../util/DouglasPeucker";
import axios from "axios";

let map = null;
let start = null;
let goal = null;
let path = null;
let polylinePath = null;

const controllerEl = ref(null);
const car = ref("none");
const angle = ref(0);
const name = ref("경로");
const isStart = ref("출발");

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
  console.log(index);

  setTimeout(() => {
    move(index + 1);
  }, dist * 300000);

  map.setCenter(s);
  map.panTo(e, {
    duration: dist * 300000,
    easing: "linear",
  });
};

let startPath = () => {
  move(0);
  car.value = "block";
};

let getPath = async () => {
  if (!start || !goal) return;

  let { data } = await axios({
    method: "post",
    url: "http://localhost:8080/save",
    data: {
      name: name.value,
      start: { lng: start.position._lng, lat: start.position._lat },
      goal: { lng: goal.position._lng, lat: goal.position._lat },
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
    map: map,
  });

  map.setCenter(polylinePath[0]);
  map.setZoom(19);
  startPath();
};

onMounted(() => {
  const script = document.createElement("script");
  script.src = URL + import.meta.env.VITE_NAVER_MAP_API_KEY;
  script.type = "text/javascript";
  script.async = true;
  script.defer = true;
  document.head.appendChild(script);

  script.onload = () => {
    map = new window.naver.maps.Map("map", {
      center: new window.naver.maps.LatLng(CENTER.lat, CENTER.lng),
      zoom: 14,
    });

    addController(map, controllerEl.value, window.naver.maps.Position.TOP_RIGHT);

    window.naver.maps.Event.addListener(map, "click", function (e) {
      if (isStart.value == "출발") {
        if (!start) {
          start = new window.naver.maps.Marker({
            map: map,
          });
        }
        start.setPosition(e.coord);
      } else if (isStart.value == "도착") {
        if (!goal) {
          goal = new window.naver.maps.Marker({
            map: map,
          });
        }
        goal.setPosition(e.coord);
      }
    });
  };
});
</script>

<template>
  <div id="map-container">
    <img id="car" src="../assets/images/car.png" :style="{ display: car, transform: `rotate(${angle}deg)` }" />
    <div id="map"></div>
  </div>
  <div id="controller" ref="controllerEl">
    <label for="start">출발</label>
    <input type="radio" value="출발" id="start" v-model="isStart" />
    <label for="end">도착</label>
    <input type="radio" value="도착" id="goal" v-model="isStart" />
    <button type="button" @click="getPath">경로 찾기</button>
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
#controller {
  display: flex;
  align-items: center;
  margin: 20px;
  background-color: white;
  height: 40px;
  padding: 0 20px;
  border-radius: 12px;
  border: 1px solid rgba(0, 0, 0, 0.219);
}

#controller input {
  margin: 0 6px;
}

#controller button {
  background-color: white;
  border: 1px solid black;
  border-radius: 4px;
  cursor: pointer;
  padding: 2px 8px;
  font-family: "Pretendard-Regular";
}
</style>
