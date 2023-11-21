<script setup>
import VPlace from "./VPlace.vue";

defineProps({
  startPlace: Object,
  goalPlace: Object,
  wayPoints: Array,
});
</script>

<template>
  <div class="place-list-title">현재 여행 경로</div>
  <div class="place-list-container">
    <VPlace :place="startPlace"></VPlace>
    <VPlace v-for="waypoint in wayPoints" :key="waypoint.placeId" :place="waypoint"></VPlace>
    <VPlace :place="goalPlace"></VPlace>
  </div>
</template>

<style scoped>
.place-list-title {
  text-align: center;
  font-family: "Pretendard-Regular";
  font-size: 24px;
  font-weight: bold;
  margin: 50px 0 20px 0;
}
.place-list-container {
  display: flex;
  flex-direction: column;
  border-top: 1px solid black;
  border-bottom: 1px solid black;
  flex-grow: 1;
  margin: 4px 0;
  padding: 20px 0;
  overflow: auto;
  width: 340px;
}

.place-list-container::-webkit-scrollbar {
  display: none;
}
</style>
