<script setup>
import { usePlayerStore } from "../stores/player";
const playerStore = usePlayerStore();

const props = defineProps({
  place: Object,
});

const onClick = () => {
  const placeCenter = new window.naver.maps.LatLng({ lat: props.place.lat, lng: props.place.lng });
  playerStore.map.setCenter(placeCenter);
};
</script>

<template>
  <div class="place-container" @click="onClick">
    <img class="place-thumbnail" :src="place.thumbnail" />
    <div class="place-info-container">
      <div class="place-info">
        <div class="place-name">{{ place.placeName }}</div>
        <div class="place-address">{{ place.placeAddress }}</div>
      </div>
      <div class="place-time-container">
        <div class="place-time">
          {{ place.departureTime ? `${place.departureTime.hour}:${place.departureTime.minute}` : "도착" }}
        </div>
        <div class="place-time">→</div>
        <div class="place-time">
          {{ place.arrivalTime ? `${place.arrivalTime.hour}:${place.arrivalTime.minute}` : "출발" }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.place-container {
  display: flex;
  flex-direction: row;
  font-family: "Pretendard-Regular";
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.4s;
  padding: 10px;
  box-sizing: border-box;
  border: 2px solid white;
}

.place-container:hover {
  color: rgba(255, 0, 0, 0.7);
  border: 2px solid rgba(255, 0, 0, 0.7);
  border-radius: 4px;
}

.place-info-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin-left: 40px;
}
.place-thumbnail {
  width: 160px;
  height: 90px;
}

.place-time-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-top: auto;
}

.place-name,
.place-address {
  text-align: right;
}
.place-name {
  font-size: 18px;
  font-weight: bold;
}
.place-address {
  font-size: 10px;
}

.place-time {
  width: 50px;
  text-align: center;
}
</style>
