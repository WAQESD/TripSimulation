<script setup>
import { computed } from "vue";
import { useRouter } from "vue-router";

import { usePlayerStore } from "../stores/player";
import { usePathStore } from "../stores/path";
import { useModalStore } from "../stores/modal";
import { useUserStore } from "../stores/user";

import TheHeader from "../commons/TheHeader.vue";
import ResultCard from "../components/ResultCard.vue";
import PathNameModal from "../components/modal/PathUploadModal.vue";

const router = useRouter();
const playerStore = usePlayerStore();
const pathStore = usePathStore();
const modalStore = useModalStore();
const user = useUserStore();

// const placeList = [
//   {
//     placeId: 0,
//     placeName: "종묘",
//     category: "세계문화유산",
//     description:
//       "서울특별시 종로구 훈정동[3] 1-2번지에 자리한 조선시대 역대 왕과 왕비 및 추존된 왕과 왕비의 신주(神主)를 모신 조선 왕실, 대한제국 황실의 유교 사당.", //  면적은 186,786 ㎡. 태묘(太廟)라고도 한다. '종묘사직'이란 말에서 알 수 있듯 전제왕조 당시 왕실과 나라를 상징하는 대표적인 건물 중 하나였다. 1963년 1월 18일 사적으로 지정되었고 1995년 유네스코 세계문화유산으로 등재되었다.
//     thumbnail:
//       "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20200208_56%2F1581129640128GVrwD_JPEG%2FJJM3dYXS5wzDYp3RoxmWgJau.jpg",
//     arrivalTime: new Date(),
//   },
// ];

/*
		waypoints: [{placeName: String, lat : Number, lng : Number, arrivalTime : String}],
    pathContent: {
			path: [{lat : Number, lng : Number}],
			pathKey: String,
			regDate: String,
			pathName: String,
			userEmail: String
		}
		*/
const uploadPath = () => {
  modalStore.setModal(true, PathNameModal, {
    callback: (pathName) => {
      console.log(playerStore.polylinePath);
      pathStore.uploadPath({
        waypoints: playerStore.wayPoints.map((place) => {
          return {
            placeName: place.placeName,
            lat: place.lat,
            lng: place.lng,
            arrivalTime: pathStore.timeToString(place.arrivalTime),
          };
        }),
        pathContent: {
          pathName,
          userEmail: user.userInfo.userEmail,
          path: playerStore.polylinePath.map(({ x, y }) => {
            return { lat: y, lng: x };
          }),
        },
      });
      router.push("/mypage");
    },
  });
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
