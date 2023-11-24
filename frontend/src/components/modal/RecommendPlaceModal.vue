<script setup>
import { ref } from "vue";

import { useModalStore } from "../../stores/modal";

import PlaceCard from "../PlaceCard.vue";
defineProps(["data", "callback"]);

const selectedIdx = ref(-1);

const modalStore = useModalStore();
</script>

<template>
  <div class="recommend-modal-container">
    <div class="place-card-wrapper">
      <PlaceCard
        v-for="(place, idx) in data"
        :key="place.placeId"
        :place="place"
        :selected="idx == selectedIdx"
        @select="
          () => {
            selectedIdx = idx;
          }
        "
      ></PlaceCard>
    </div>
    <button
      class="skip-btn"
      @click.prevent="
        () => {
          modalStore.setModal(false);
          callback();
        }
      "
    >
      건너뛰기
    </button>
  </div>
</template>

<style scoped>
.place-card-wrapper {
  width: 1000px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
}

.skip-btn {
  position: fixed;
  bottom: 10px;
  left: calc(50% - 60px);
  width: 120px;
  padding: 8px 16px;
  border: 1px solid #8f94ff;
  border-radius: 20px;
  font-size: 16px;
  margin-top: 20px;
  cursor: pointer;
}

.recommend-modal-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
