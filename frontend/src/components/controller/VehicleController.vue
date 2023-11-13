<script setup>
import { ref, onMounted, nextTick, watch } from "vue";
import { initController, addController, removeController } from "../../util/map";

const props = defineProps({
  map: Object,
  show: Boolean,
});

const isPaused = ref(false);

const position = window.naver.maps.Position.TOP_RIGHT;

const emit = defineEmits(["downSpeed", "upSpeed", "pause", "start"]);

const downSpeed = () => {
  emit("downSpeed");
};

const upSpeed = () => {
  emit("upSpeed");
};

const pause = () => {
  isPaused.value = true;
  emit("puase");
};

const start = () => {
  isPaused.value = false;
  emit("start");
};

const controllerEl = ref(null);

onMounted(() => {
  nextTick(() => {
    initController(props.map, controllerEl.value, position);
  });
});

watch(
  () => props.show,
  () => {
    if (props.show) addController(props.map, controllerEl.value, position);
    else removeController(props.map, controllerEl.value, position);
  }
);
</script>

<template>
  <div v-show="props.show" class="vehicle-controller-container" ref="controllerEl">
    <div class="vehicle-speed-btn" @click="downSpeed">⏪</div>
    <div v-show="!isPaused" class="vehicle-speed-btn" @click="pause">⏸</div>
    <div v-show="isPaused" class="vehicle-speed-btn" @click="start">⏯</div>
    <div class="vehicle-speed-btn" @click="upSpeed">⏩</div>
  </div>
</template>

<style scoped>
.vehicle-controller-container {
  box-sizing: border-box;
  display: flex;
  padding: 5px 10px;
  margin: 22px 30px 0 0;
}
.vehicle-speed-btn {
  font-size: 30px;
  cursor: pointer;
}
</style>
