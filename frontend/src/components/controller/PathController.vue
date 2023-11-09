<script setup>
import { ref, onMounted, nextTick } from "vue";
import { addController } from "../../util/map";

const props = defineProps({
  map: Object,
});
const emit = defineEmits(["getPath"]);

const isStart = ref(false);

const start = ref(null);
const goal = ref(null);

const controllerEl = ref(null);

onMounted(() => {
  nextTick(() => {
    addController(props.map, controllerEl.value, window.naver.maps.Position.TOP_RIGHT);

    window.naver.maps.Event.addListener(props.map, "click", function (e) {
      if (isStart.value == "출발") {
        if (!start.value) {
          start.value = new window.naver.maps.Marker({
            map: props.map,
          });
        }
        start.value.setPosition(e.coord);
      } else if (isStart.value == "도착") {
        if (!goal.value) {
          goal.value = new window.naver.maps.Marker({
            map: props.map,
          });
        }
        goal.value.setPosition(e.coord);
      }
    });
  });
});

const getPath = () => {
  emit("getPath", start.value, goal.value);
};
</script>

<template>
  <div id="controller" ref="controllerEl">
    <label for="start">출발</label>
    <input type="radio" value="출발" id="start" v-model="isStart" />
    <label for="end">도착</label>
    <input type="radio" value="도착" id="goal" v-model="isStart" />
    <button type="button" @click="getPath">경로 찾기</button>
  </div>
</template>

<style scoped>
#controller {
  display: flex;
  align-items: center;
  margin: 20px;
  background-color: white;
  height: 40px;
  padding: 0 20px;
  border-radius: 12px;
  border: 1px solid rgba(0, 0, 0, 0.219);
}

#controller input {
  margin: 0 6px;
}

#controller button {
  background-color: white;
  border: 1px solid black;
  border-radius: 4px;
  cursor: pointer;
  padding: 2px 8px;
  font-family: "Pretendard-Regular";
}
</style>
