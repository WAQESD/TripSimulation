<script setup>
import { computed } from "vue";
import { useRouter } from "vue-router";

import { usePlayerStore } from "../stores/player";
import { usePathStore } from "../stores/path";
import { useModalStore } from "../stores/modal";
import { useUserStore } from "../stores/user";
import { useTimeStore } from "../stores/time";

import TheHeader from "../commons/TheHeader.vue";
import ResultCard from "../components/ResultCard.vue";
import PathNameModal from "../components/modal/PathUploadModal.vue";

const router = useRouter();
const playerStore = usePlayerStore();
const pathStore = usePathStore();
const modalStore = useModalStore();
const userStore = useUserStore();
const timeStore = useTimeStore();

const uploadPath = () => {
  modalStore.setModal(true, PathNameModal, {
    callback: (pathName) => {
      console.log(playerStore.polylinePath);
      pathStore.uploadPath({
        waypoints: [
          extractData(playerStore.startPlace),
          ...playerStore.wayPoints,
          extractData(playerStore.goalPlace),
        ].map((place) => {
          return {
            placeName: place.placeName,
            lat: place.lat,
            lng: place.lng,
            arrivalTime: pathStore.timeToString(place.arrivalTime),
          };
        }),
        pathContent: {
          pathName,
          userEmail: userStore.userInfo.userEmail,
          path: playerStore.polylinePath.map(({ x, y }) => {
            return { lat: y, lng: x };
          }),
        },
      });
      router.push("/mypage");
    },
  });
};

const extractData = (place) => {
  return {
    lat: place.lat,
    lng: place.lng,
    placeName: place.placeName,
    arrivalTime: place.arrivalTime || timeStore.startTime,
  };
};

pathStore.uploadPath({});

const placeList = computed(() => [playerStore.startPlace, ...playerStore.wayPoints, playerStore.goalPlace]);
</script>

<template>
  <div>
    <TheHeader></TheHeader>
    <div class="path-result-container">
      <div class="path-result-center-line" :style="{ height: placeList.length * 350 + 210 + 'px' }">
        <div class="path-result-center-line-circle outer"></div>
        <div class="path-result-center-line-circle inner"></div>
      </div>
      <ResultCard v-for="(place, idx) in placeList" :key="place.placeId" :place="place" :idx="idx + 1"></ResultCard>
      <div class="path-result-btn-container">
        <button class="path-result-btn" @click="uploadPath">경로 저장하기</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
header {
  position: relative;
  z-index: 2;
  box-shadow: 0px 4px 8px 0px rgba(0, 0, 0, 0.15);
}

.path-result-container {
  padding-bottom: 200px;
}

.path-result-center-line {
  position: absolute;
  top: 0;
  left: 0;
  width: calc(50vw - 1px);
  height: 100%;
  border-right: 2px solid #7c91ff;
}

.path-result-center-line-circle {
  position: absolute;
  box-sizing: border-box;
  right: -8px;
  bottom: -8px;
  width: 16px;
  height: 16px;
  border: #7c91ff 2px solid;
  border-radius: 8px;
  background-color: white;
}

.outer {
  right: -12px;
  bottom: -12px;
  width: 24px;
  height: 24px;
  border-radius: 12px;
}

.path-result-btn {
  width: 450px;
  height: 100px;
  border-radius: 50px;
  line-height: 100px;
  text-align: center;
  font-size: 30px;
  border: none;
  background-color: #7c91ff;
  color: white;
  font-weight: bold;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  cursor: pointer;
}

.path-result-btn-container {
  width: 100%;
  margin-top: 350px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
