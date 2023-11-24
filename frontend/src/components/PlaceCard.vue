<script setup>
import { usePlayerStore } from "../stores/player";
import { useModalStore } from "../stores/modal";
const props = defineProps({
  place: Object,
  selected: Boolean,
});

const emit = defineEmits(["select"]);

const modalStore = useModalStore();
const playerStore = usePlayerStore();

const addWaypoint = () => {
  modalStore.setModal(false);
  playerStore.addWaypoint(props.place);
};

const changeGoalPlace = () => {
  modalStore.setModal(false);
  playerStore.changeGoalPlace(props.place);
};
</script>

<template>
  <div class="recommend-place-card" :style="`background-image : url(${place.thumbnail})`" @click="emit('select')">
    <h3 class="place-card-name">{{ place.placeName }}</h3>
    <h5>{{ place.category }}</h5>
    <h5>{{ place.address }}</h5>
    <div v-show="selected" class="place-card-selected">
      <div class="waypoint-card-btn" @click="addWaypoint">경유</div>
      <div class="goal-card-btn" @click="changeGoalPlace">도착</div>
    </div>
  </div>
</template>

<style scoped>
.place-card-selected {
  position: absolute;
  top: 0;
  left: 0;
  width: 300px;
  height: 300px;
  box-sizing: border-box;
  background-color: rgba(0, 0, 0, 0.363);
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
  padding: 100px 50px;
}

.waypoint-card-btn {
  background-color: #7c92ffc2;
  border: 2px solid #7c92ffc2;
  color: white;
}

.waypoint-card-btn:hover {
  border: 2px solid black;
}

.goal-card-btn {
  background-color: #e33535d2;
  border: 2px solid #e33535d2;
  color: white;
}

.goal-card-btn:hover {
  border: 2px solid black;
}

.place-card-selected div {
  padding: 10px 20px;
  border-radius: 30px;
}

.recommend-place-card {
  position: relative;
  width: 300px;
  height: 300px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  transition: all 0.5s;
  background-color: white;
  border-radius: 20px;
  padding: 16px;
  margin: 20px;
  background-size: 300px;
  background-repeat: no-repeat;
  cursor: pointer;
}

.place-info-container {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}
h3 {
  margin: 2px;
}

h5 {
  margin: 0;
}
</style>
