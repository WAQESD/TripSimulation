<script setup>
import { ref, computed, watch } from "vue";
import { useRouter } from "vue-router";

import { getBoundsByPathList } from "../util/map";
import { usePathStore } from "../stores/path";
import { useUserStore } from "../stores/user";
import { useTimeStore } from "../stores/time";
import { useModalStore } from "../stores/modal";

import TheHeader from "../commons/TheHeader.vue";
import TimePickerModal from "../components/modal/TimePickerModal.vue";

const router = useRouter();
const pathStore = usePathStore();
const userStore = useUserStore();
const modalStore = useModalStore();
const timeStore = useTimeStore();

const selectedIndex = ref(-1);
const map = ref(null);
const polylinePath = ref(null);

const selectedPath = computed(() => (selectedIndex.value < 0 ? null : pathStore.pathList[selectedIndex.value]));

watch(selectedIndex, () => {
  if (!map.value) {
    if (!window.naver) {
      const script = document.createElement("script");
      script.type = "text/javascript";
      script.src = import.meta.env.VITE_MAP_MODULE_SRC;
      document.head.appendChild(script);

      script.onload = loadMap;
    } else loadMap();
  } else drawPolyline();
});

const loadMap = () => {
  map.value = new window.naver.maps.Map("path-info-detail-map", {
    zoom: 14,
    draggable: false,
    pinchZoom: false,
    scrollWheel: false,
    keyboardShortcuts: false,
    disableDoubleTapZoom: true,
    disableDoubleClickZoom: true,
    disableTwoFingerTapZoom: true,
  });

  map.value.setCursor("default");
  window.naver.maps.Event.once(map.value, "init", drawPolyline);
};

const drawPolyline = () => {
  if (polylinePath.value) polylinePath.value.setMap(null);

  polylinePath.value = new window.naver.maps.Polyline({
    path: selectedPath.value.pathContent.path.map(({ lat, lng }) => new window.naver.maps.LatLng(lat, lng)),
    strokeColor: "#5347AA",
    strokeWeight: 3,
    map: map.value,
  });

  const bounds = getBoundsByPathList(selectedPath.value.pathContent.path);
  map.value.fitBounds(bounds);
};

const startTrip = () => {
  modalStore.setModal(true, TimePickerModal, {
    callback: (hours, minute) => {
      router.push("/trip");
      modalStore.setModal(false);
      timeStore.setStartTime(hours, minute);
    },
  });
};

const dateToString = (date) => {
  return new Intl.DateTimeFormat("kr").format(date);
};
</script>

<template>
  <TheHeader></TheHeader>
  <div class="my-page-container">
    <div class="user-info-container">
      <div class="user-info-image">
        <div class="user-info-image-indicator"></div>
      </div>
      <div class="user-info-text-container">
        <div class="user-info-text-nickname">{{ userStore.userInfo.userName }}님의 마이페이지</div>
        <div class="user-info-text-email">{{ userStore.userInfo.userEmail }}</div>
        <div class="user-info-text-sperator"></div>
        <div class="user-info-text-birth">{{ dateToString(new Date(userStore.userInfo.birth)) }}</div>
        <div class="user-info-text-mypage">MyPage</div>
      </div>
    </div>
    <div class="path-info-container">
      <div class="path-info-detail">
        <div id="path-info-detail-map"></div>
        <div class="path-info-detail-text">
          <div class="path-info-title-index">
            <span v-show="selectedIndex > -1" class="path-info-title">{{ selectedPath?.pathContent?.pathName }}</span>
          </div>
          <div class="path-info-waypoints">
            <div v-show="selectedPath" class="path-info-waypoints-list">
              <div
                v-for="(place, idx) in selectedPath?.waypoints"
                :key="place.arrivalTime"
                class="path-info-waypoints-wrapper"
              >
                <div class="path-info-waypoints-icon">{{ idx + 1 + "." }}</div>
                <div>
                  <div class="path-info-waypoints-title">
                    {{ place.placeName }}
                  </div>
                  <div class="path-info-waypoints-time">
                    {{ place.arrivalTime }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="path-info-list-container">
        <div class="path-info-list-mypath">My Path</div>
        <div class="path-info-list">
          <template v-for="(path, idx) in pathStore.pathList" :key="path.title">
            <div
              class="path-info-list-item"
              @click="
                () => {
                  selectedIndex = idx;
                }
              "
            >
              <div class="path-info-item-index">{{ idx + 1 }}</div>
              <div class="path-info-item-title">{{ path.pathContent.pathName }}</div>
              <div class="path-info-item-regdate">{{ dateToString(path.pathContent.regDate) }}</div>
            </div>
          </template>
        </div>
      </div>
    </div>
    <div class="path-create-btn-container" @click.prevent="startTrip">
      <button class="path-create-btn">
        새 여행경로 만들기
        <div class="path-create-btn-icon"></div>
      </button>
    </div>
  </div>
</template>

<style scoped>
.my-page-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  box-sizing: border-box;
}

.user-info-container {
  display: flex;
  align-items: center;
  width: 1000px;
  box-sizing: border-box;
  height: 165px;
  background-color: #7c91ff;
  border-radius: 20px;
  padding: 0 50px;
}

.user-info-image {
  position: relative;
  width: 117px;
  height: 117px;
  border-radius: 60px;
  background-color: white;
  margin-right: 30px;
}

.user-info-image-indicator {
  position: absolute;
  width: 27px;
  height: 27px;
  background-color: #9dff6e;
  border-radius: 14px;
}

.user-info-text-container {
  display: flex;
  align-items: center;
  margin-bottom: 40px;
  flex-grow: 1;
  padding: 0;
  text-align: center;
  color: white;
  font-size: 16px;
}

.user-info-text-nickname {
  width: 200px;
  height: 48px;
  border-radius: 20px;
  border: 1px solid white;
  line-height: 48px;
  font-size: 16px;
}

.user-info-text-email {
  margin-left: 60px;
}

.user-info-text-sperator {
  height: 34px;
  border-left: 1px solid white;
  margin: 0 30px;
}

.user-info-text-mypage {
  margin-left: auto;
  font-size: 25px;
}

.path-info-container {
  display: flex;
  position: relative;
  margin-top: 35px;
}
.path-info-detail {
  width: 280px;
  height: 520px;
  box-shadow: 0px 4px 10.4px 0px rgba(0, 0, 0, 0.18);
  background-color: #f4f4f4;
  border-radius: 20px;
}

#path-info-detail-map {
  width: 280px;
  height: 241px;
  background-color: #d9d9d9;
  border-radius: 20px 20px 0 0;
}

.path-info-detail-text {
  text-align: center;
  margin-top: 30px;
}

.path-info-index {
  position: absolute;
  display: inline-block;
  left: -32px;
  top: -3px;
  width: 24px;
  height: 24px;
  line-height: 24px;
  border-radius: 12px;
  background-color: #adbaff;
}

.path-info-title {
  position: relative;
  line-height: 20px;
  font-size: 16px;
}

.path-info-waypoints {
  margin-top: 10px;
  padding: 0 30px;
  padding-top: 20px;
  border-top: 3px solid rgba(128, 128, 128, 0.3);
  display: flex;
  flex-grow: 1;
  height: 150px;
  justify-content: center;
}

.path-info-waypoints-icon {
  width: 24px;
  height: 24px;
  line-height: 24px;
  border-radius: 12px;
  margin-right: 24px;
  color: #7c91ff;
}

.path-info-waypoints-title-list {
  box-sizing: border-box;
  border-right: 1px solid #d0d0d0;
}

.path-info-waypoints-time-list {
  box-sizing: border-box;
  padding: 20px;
  height: 200px;
}

.path-info-waypoints-title {
  font-size: 14px;
  margin-bottom: 4px;
  margin-right: 20px;
  width: 200px;
  text-align: left;
}

.path-info-waypoints-time {
  font-size: 14px;
  width: 200px;
  text-align: left;
}
.path-info-list-container {
  display: flex;
  flex-direction: column;
  position: relative;
  top: -86px;
  box-shadow: 0px 4px 10.4px 0px rgba(0, 0, 0, 0.18);
  width: 600px;
  height: 606px;
  margin-left: 30px;
  background-color: #f4f4f4;
  border-radius: 20px;
  padding: 0 34px;
  overflow-y: scroll;
  box-sizing: border-box;
}

.path-info-list-mypath {
  font-size: 25px;
  border-bottom: 1px solid #9a9a9a;
  padding: 12px 15px;
}

.path-info-list-item {
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 0 18px;
  width: 522px;
  height: 65px;
  box-sizing: border-box;
  border-radius: 20px;
  background-color: #e3e3e3;
  margin-top: 20px;
  margin-bottom: 20px;
  cursor: pointer;
}

.path-info-item-index {
  width: 38px;
  height: 38px;
  line-height: 38px;
  border-radius: 19px;
  text-align: center;
  background-color: #adbaff;
  font-size: 19px;
}

.path-info-item-title {
  margin-left: 25px;
}

.path-info-item-regdate {
  margin-left: auto;
}

.path-create-btn-container {
  width: 900px;
  display: flex;
  justify-content: flex-end;
}

.path-create-btn {
  width: 304px;
  height: 68px;
  box-sizing: border-box;
  background-color: #515151;
  border-radius: 25px;
  color: white;
  font-size: 18px;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  position: relative;
  line-height: 38px;
  margin-bottom: 40px;
  cursor: pointer;
  border: none;
}

.path-create-btn-icon {
  top: 15px;
  right: 15px;
  position: absolute;
  width: 38px;
  height: 38px;
  border-radius: 19px;
  background-color: #9dff6e;
}

.path-info-list-container::-webkit-scrollbar {
  width: 10px;
}

.path-info-list-container::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background-color: #b3ff8f;
}

.path-info-list-container::-webkit-scrollbar-button {
  width: 10px;
  height: 10px;
}

.path-info-waypoints-wrapper {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding-left: 40px;
}
</style>
