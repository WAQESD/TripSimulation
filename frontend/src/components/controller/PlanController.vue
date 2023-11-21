<script setup>
import { ref, watch } from "vue";
import { usePlayerStore } from "../../stores/player";
import { getStartIcon, getGoalIcon, getWayPointIcon } from "../../util/map";

import VPlace from "../VPlace.vue";

const playerStore = usePlayerStore();
const changeTarget = ref(null);
const polyLine = ref(null);

const setPath = ({ place, type, idx }) => {
  if (type === "start") playerStore.setStartPlace(place);
  else if (type === "goal") playerStore.setGoalPlace(place);
  else playerStore.wayPoints[idx] = place;
};

const changePath = (place, type, idx) => {
  if (!changeTarget.value) {
    changeTarget.value = { place, type, idx };
  } else if (changeTarget.value.place.placeId === place.placeId) {
    changeTarget.value = null;
  } else {
    setPath({ place: changeTarget.value.place, type, idx });
    setPath({ place, type: changeTarget.value.type, idx: changeTarget.value.idx });
    changeTarget.value = null;
  }
};

const drawPolyLine = () => {
  if (playerStore.tripStart) return;
  if (polyLine.value) polyLine.value.setMap(null);
  if (playerStore.startPlace.lat === 0 || playerStore.goalPlace.lat === 0) return;
  let polyLinePath = [
    new window.naver.maps.LatLng(playerStore.startPlace.lat, playerStore.startPlace.lng),
    ...playerStore.wayPoints.map((wayPoint) => new window.naver.maps.LatLng(wayPoint.lat, wayPoint.lng)),
    new window.naver.maps.LatLng(playerStore.goalPlace.lat, playerStore.goalPlace.lng),
  ];

  polyLine.value = new window.naver.maps.Polyline({
    map: playerStore.map,
    path: polyLinePath,
    fillColor: "#ff0000",
    fillOpacity: 0.3,
    strokeColor: "#ff0000",
    strokeOpacity: 0.6,
    strokeWeight: 3,
  });
};

const startMarker = ref(null);
const goalMarker = ref(null);
const wayPointMarkers = ref([]);

watch(
  () => playerStore.startPlace,
  () => {
    if (startMarker.value) startMarker.value.setMap(null);
    startMarker.value = null;
    startMarker.value = new window.naver.maps.Marker({
      position: new window.naver.maps.LatLng(playerStore.startPlace.lat, playerStore.startPlace.lng),
      map: playerStore.map,
      icon: {
        content: getStartIcon(),
        size: new window.naver.maps.Size(36, 36),
        anchor: new window.naver.maps.Point(6, 36),
      },
    });
    drawPolyLine();
  }
);

watch(
  () => playerStore.goalPlace,
  () => {
    if (goalMarker.value) goalMarker.value.setMap(null);
    goalMarker.value = null;
    goalMarker.value = new window.naver.maps.Marker({
      position: new window.naver.maps.LatLng(playerStore.goalPlace.lat, playerStore.goalPlace.lng),
      map: playerStore.map,
      icon: {
        content: getGoalIcon(),
        size: new window.naver.maps.Size(36, 36),
        anchor: new window.naver.maps.Point(6, 36),
      },
    });
    drawPolyLine();
  }
);

watch(playerStore.wayPoints, () => {
  wayPointMarkers.value.forEach((marker) => marker.setMap(null));
  wayPointMarkers.value = [];
  playerStore.wayPoints.forEach((wayPoint, idx) => {
    wayPointMarkers.value.push(
      new window.naver.maps.Marker({
        position: new window.naver.maps.LatLng(wayPoint.lat, wayPoint.lng),
        map: playerStore.map,
        icon: {
          content: getWayPointIcon(idx + 1),
          size: new window.naver.maps.Size(36, 36),
          anchor: new window.naver.maps.Point(18, 36),
        },
      })
    );
  });
  drawPolyLine();
});

watch(
  () => playerStore.tripStart,
  () => {
    if (playerStore.tripStart && polyLine.value) polyLine.value.setMap(null);
  }
);

watch(
  () => playerStore.wayPoints,
  () => {
    console.log("test2");
  }
);
</script>

<template>
  <div class="plan-controller-container">
    <h2 class="plan-controller-title">여행 경로 설정</h2>
    <fieldset v-if="playerStore.startPlace.lat > 0" class="plan-controller-start">
      <legend>출발</legend>
      <VPlace
        class="slide-left"
        :place="playerStore.startPlace"
        :class="{ selected: changeTarget && changeTarget.type === 'start' }"
        @click="
          () => {
            changePath(playerStore.startPlace, 'start', 0);
          }
        "
      ></VPlace>
    </fieldset>
    <div v-else><h2>출발지를 설정해주세요.</h2></div>
    <fieldset v-if="playerStore.wayPoints.length > 0" class="plan-controller-waypoint-list">
      <legend>경유</legend>
      <TransitionGroup name="fade">
        <VPlace
          v-for="(place, idx) in playerStore.wayPoints"
          :class="{ selected: changeTarget && changeTarget.type === 'waypoint' && changeTarget.idx === idx }"
          :key="place.placeId"
          :place="place"
          :isSelected="changeTarget && changeTarget.type === 'waypoint' && changeTarget.idx === idx"
          @click="
            () => {
              changePath(place, 'waypoint', idx);
            }
          "
        ></VPlace>
      </TransitionGroup>
    </fieldset>
    <fieldset v-if="playerStore.goalPlace.lat > 0" class="plan-controller-goal">
      <legend>도착</legend>
      <VPlace
        class="slide-left"
        :place="playerStore.goalPlace"
        :class="{ selected: changeTarget && changeTarget.type === 'goal' }"
        @click="
          () => {
            changePath(playerStore.goalPlace, 'goal', 0);
          }
        "
      ></VPlace>
    </fieldset>
    <div v-else><h2>목적지를 설정해주세요.</h2></div>
  </div>
</template>

<style scoped>
.plan-controller-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: white;
  padding: 20px 20px;
  border-radius: 0 16px 16px 0;
  border-right: 1px solid rgba(0, 0, 0, 0.3);
  box-sizing: border-box;
  width: 400px;
  height: 100vh;
  overflow: auto;
  overflow-x: hidden;
  z-index: 2;
}

.plan-controller-start,
.plan-controller-goal,
.plan-controller-waypoint-list {
  width: 100%;
  border: none;
}
.plan-controller-container::-webkit-scrollbar {
  display: none;
}

.plan-controller-title {
  width: 100%;
  text-align: center;
  padding: 0 0px 40px 0;
  border-bottom: 1px solid black;
}

legend {
  font-family: "Pretendard-Regular";
  font-size: 14px;
}

.selected {
  border: 2px dashed black !important;
  color: black !important;
}

.fade-move,
.fade-enter-active
/* .fade-leave-active  */ {
  transition: all 0.5s cubic-bezier(0.55, 0, 0.1, 1);
}

/* 2. declare enter from and leave to state */
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: scaleY(0.01) translate(30px, 0);
}
/* 3. ensure leaving items are taken out of layout flow so that moving
      animations can be calculated correctly. */
.fade-leave-active {
  position: absolute;
  width: 359px;
}
</style>
