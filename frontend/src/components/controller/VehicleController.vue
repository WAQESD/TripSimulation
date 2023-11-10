<script setup>
import { ref, onMounted, nextTick } from "vue";
import { addController } from "../../util/Map";

const props = defineProps({
  map: Object,
});

const emit = defineEmits(["downSpeed", "upSpeed"]);

const controllerEl = ref(null);

onMounted(() => {
  nextTick(() => {
    addController(props.map, controllerEl.value, window.naver.maps.Position.TOP_RIGHT);
  });
});
</script>

<template>
  <div class="vehicle-controller-container" ref="controllerEl" display="none">
    <div class="vehicle-speed-btn">⏪</div>
    <div class="vehicle-speed-btn">⏸</div>
    <div class="vehicle-speed-btn">⏩</div>
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
