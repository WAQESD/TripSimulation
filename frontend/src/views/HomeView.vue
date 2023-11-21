<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useModalStore } from "../stores/modal";
import { useTimeStore } from "../stores/time";

import TheHeader from "../commons/TheHeader.vue";
import TimePickerModal from "../components/modal/TimePickerModal.vue";

const router = useRouter();
const modalStore = useModalStore();
const timeStore = useTimeStore();

const startTrip = () => {
  modalStore.setModal(true, TimePickerModal, {
    callback: (hours, minute) => {
      router.push("/trip");
      modalStore.setModal(false);
      timeStore.setStartTime(hours, minute);
    },
  });
};

const mouseIn = ref(false);
</script>

<template>
  <TheHeader></TheHeader>
  <main>
    <img src="../assets/images/laptop.svg" alt="" />
    <div class="text-container">
      <div>
        <h2>Trippy</h2>
      </div>
      <div>
        <p>단순히 여행 경로를 계획하는 것이 아니라</p>
        <p>실시간으로 시뮬레이션하여 여행을 체험할 수 있습니다</p>
        <br />
        <p><strong>최적의 경로를 직접 찾아보세요</strong></p>
      </div>
      <div
        class="car-animation-container"
        @click="startTrip"
        @mouseenter="
          () => {
            mouseIn = true;
          }
        "
        @mouseleave="
          () => {
            mouseIn = false;
          }
        "
      >
        <div id="car-animation"></div>
        <button type="button" class="trip-btn" :class="{ hover: mouseIn }" @click.prevent="startTrip">
          눌러서 시작하기
        </button>
      </div>
    </div>
  </main>
</template>

<style scoped>
header {
  box-shadow: 0px 4px 8px 0px rgba(0, 0, 0, 0.15);
}

.car-animation-container {
  width: 100vw;
  height: 700px;
}

main {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
  flex-grow: 1;
  background-color: rgb(243, 243, 243);
}

p {
  text-align: center;
  font-size: 24px;
  margin: 12px 0 0 0;
}

h2 {
  font-family: "Baloo Chettan 2", sans-serif;
  /* color: #37479e; */
  font-size: 84px;
  text-align: center;
  margin: 0;
}

.text-container {
  height: 300px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: cen;
}

.trip-btn {
  position: absolute;
  left: calc(50% - 127px);
  bottom: 160px;
  margin: 50px 0 10px 0;
  align-self: center;
  width: 254px;
  height: 64px;
  border-radius: 32px;
  font-family: "Pretendard-Regular";
  font-size: 32px;
  cursor: pointer;
  /* box-shadow: 0px 2px 0px black; */
  border: none;
  position: relative;
  background-color: rgba(0, 0, 0, 0);
  font-weight: bold;
  color: #6f6f6f;
  transition: all 0.2s;
}

#car-animation {
  transition: font-size 0.3s;
}

#car-animation:hover {
  font-size: 22px;
  cursor: pointer;
}

.hover {
  color: black;
}
</style>
