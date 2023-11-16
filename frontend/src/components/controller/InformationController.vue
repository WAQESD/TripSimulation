<script setup>
import { ref, onMounted, nextTick, watch, computed } from "vue";
import { initController, addController, removeController } from "../../util/map";
import { usePlayerStore } from "../../stores/player";

import PlaceList from "../PlaceList.vue";

const position = window.naver.maps.Position.TOP_LEFT;
const playerStore = usePlayerStore();
const controllerEl = ref(null);
const isClosed = ref(null);

const startPlace = ref({
  placeId: 1,
  departureTime: { hour: "08", minute: "22", second: "13" },
  arrivalTime: null,
  placeName: "NC 대전 유성점",
  placeAddress: "대전 유성구 계룡로 119",
  Lat: 123.123,
  Lng: 37.123,
  thumbnail:
    "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220111_295%2F1641891871024IliaN_JPEG%2F%25B4%25EB%25C0%25FCNC_%25C1%25B6%25B0%25A8%25B5%25B5.jpg",
});

const goalPlace = ref({
  placeId: 2,
  departureTime: null,
  arrivalTime: { hour: "08", minute: "52", second: "45" },
  placeName: "성심당 본점",
  placeAddress: "대전 중구 대종로480번길 15",
  Lat: 123.123,
  Lng: 37.123,
  thumbnail:
    "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20180413_221%2F1523606684451FP1va_JPEG%2FEI2p0GphPlS6XQaMl8hjn4Q1.jpg",
});

const wayPoints = ref([
  {
    ...startPlace.value,
    departureTime: { hour: "08", minute: "22", second: "13" },
    arrivalTime: { hour: "08", minute: "23", second: "13" },
  },
  {
    ...startPlace.value,
    departureTime: { hour: "08", minute: "23", second: "13" },
    arrivalTime: { hour: "08", minute: "24", second: "13" },
  },
  {
    ...startPlace.value,
    departureTime: { hour: "08", minute: "24", second: "13" },
    arrivalTime: { hour: "08", minute: "25", second: "13" },
  },
  {
    ...startPlace.value,
    departureTime: { hour: "08", minute: "25", second: "13" },
    arrivalTime: { hour: "08", minute: "26", second: "13" },
  },
  {
    ...startPlace.value,
    departureTime: { hour: "08", minute: "26", second: "13" },
    arrivalTime: { hour: "08", minute: "27", second: "13" },
  },
  {
    ...startPlace.value,
    departureTime: { hour: "08", minute: "27", second: "13" },
    arrivalTime: { hour: "08", minute: "28", second: "13" },
  },
  {
    ...startPlace.value,
    departureTime: { hour: "08", minute: "28", second: "13" },
    arrivalTime: { hour: "08", minute: "29", second: "13" },
  },
  {
    ...startPlace.value,
    departureTime: { hour: "08", minute: "29", second: "13" },
    arrivalTime: { hour: "08", minute: "30", second: "13" },
  },
  {
    ...startPlace.value,
    departureTime: { hour: "08", minute: "30", second: "13" },
    arrivalTime: { hour: "08", minute: "31", second: "13" },
  },
]);

const btnIcon = computed(() => (isClosed.value ? ">" : "<"));

const toggleController = () => {
  isClosed.value = !isClosed.value;
};

onMounted(() => {
  nextTick(() => {
    initController(playerStore.map, controllerEl.value, position);
  });
});

watch(
  () => playerStore.tripStart,
  () => {
    if (playerStore.tripStart) addController(playerStore.map, controllerEl.value, position);
    else removeController(playerStore.map, controllerEl.value, position);
  }
);
</script>
<template>
  <div
    class="information-controller-container"
    v-show="playerStore.tripStart"
    ref="controllerEl"
    :class="isClosed ? 'closed' : ''"
  >
    <!-- 
      place 구조
      {
        placeId : Number,
        departureTime : { hour : 'hh', minute : 'mm', second : 'ss'},
        arrivalTime : { hour : 'hh', minute : 'mm', second : 'ss'},
        placeName : String,
        placeAddress : String,
        Lat : Number,
        Lng : Number,
        thumbnail : String
      }
    -->
    <PlaceList :startPlace="startPlace" :goalPlace="goalPlace" :wayPoints="wayPoints"></PlaceList>
    <div class="close-btn" @click="toggleController">{{ btnIcon }}</div>
  </div>
</template>

<style scoped>
.information-controller-container {
  transition: transform 0.5s;
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
  box-shadow: rgba(100, 100, 111, 0.4) 0px 7px 29px 0px;
}
.information-time-container {
  display: flex;
  flex-direction: row;
  padding: 10px;
}

.information-controller-icon {
  font-size: 32px;
  padding: 4px 8px;
  line-height: 24px;
  font-weight: bold;
  margin-bottom: 8px;
}
.information-controller-time {
  position: relative;
  font-size: 20px;
  margin: 0 10px;
  padding: 8px 12px;
  border-radius: 24px;
  border: 1px solid black;
  background-color: white;
  width: 60px;
  text-align: center;
}
.information-contorller-departure,
.information-contorller-arrival {
  position: absolute;
  top: -34px;
  font-size: 20px;
  width: 60px;
  text-align: center;
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

.closed {
  transform: translateX(-400px);
}
</style>
