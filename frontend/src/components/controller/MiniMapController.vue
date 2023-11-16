<script setup>
import { onMounted, watch, nextTick, ref } from "vue";
import { initController, addController, removeController } from "../../util/map";
import { usePlayerStore } from "../../stores/player";

const miniMap = ref(null);
const controllerEl = ref(null);
const position = window.naver.maps.Position.TOP_RIGHT;
const playerStore = usePlayerStore();
const marker = ref(null);
let path = null;

onMounted(() => {
  nextTick(() => {
    initController(playerStore.map, controllerEl.value, position);
    miniMap.value = new window.naver.maps.Map("minimap", {
      center: new window.naver.maps.LatLng(import.meta.env.VITE_DEFAULT_LAT, import.meta.env.VITE_DEFAULT_LNG),
      zoom: 14,
      draggable: false,
      pinchZoom: false,
      scrollWheel: false,
      keyboardShortcuts: false,
      disableDoubleTapZoom: true,
      disableDoubleClickZoom: true,
      disableTwoFingerTapZoom: true,
    });

    miniMap.value.setCursor("pointer");
  });
});

watch(
  () => playerStore.tripStart,
  () => {
    if (playerStore.tripStart) {
      addController(playerStore.map, controllerEl.value, position);

      if (playerStore.polylinePath) {
        if (path) path.setMap(null);

        path = new window.naver.maps.Polyline({
          path: playerStore.polylinePath,
          strokeColor: "#00008B",
          strokeWeight: 3,
          map: miniMap.value,
        });
      }

      if (playerStore.miniMapBounds) {
        miniMap.value.fitBounds(playerStore.miniMapBounds);
      }
    } else removeController(playerStore.map, controllerEl.value, position);
  }
);

watch(
  () => playerStore.currentStart,
  () => {
    if (marker.value) marker.value.setMap(null);
    marker.value = new window.naver.maps.Marker({
      position: playerStore.currentStart,
      map: miniMap.value,
      icon: {
        content: `<img id="minimap-car" src="/src/assets/images/car.png" width="22px" height="42px" style="transform: rotate(${playerStore.getAngle(
          playerStore.currentStart,
          playerStore.currentGoal
        )}deg)"></img>`,
        size: new window.naver.maps.Size(22, 42),
        anchor: new window.naver.maps.Point(11, 21),
      },
    });
  }
);
</script>

<template>
  <div v-show="playerStore.tripStart" class="minimap-controller-container" ref="controllerEl">
    <div id="minimap"></div>
  </div>
</template>

<style scoped>
.minimap-controller-container {
  background-color: white;
  margin: 15px 15px 0 0;
}

#minimap {
  width: 300px;
  height: 300px;
  border: 1px solid black;
}
</style>
