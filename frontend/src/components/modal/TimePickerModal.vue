<script setup>
import { onMounted } from "vue";

const props = defineProps({
  callback: Function,
});

let instance = null;

onMounted(() => {
  const container = document.getElementById("tui-time-picker-container");
  instance = new window.tui.TimePicker(container, {
    inputType: "spinbox",
  });

  const timePicker = document.querySelector(".tui-timepicker");
  timePicker.style.border = "none";
});

const setStartTime = () => {
  props.callback(instance.getHour(), instance.getMinute());
};
</script>

<template>
  <div class="time-picker-modal-container">
    <div id="tui-time-picker-container"></div>
    <h3>출발 시각을 입력해주세요</h3>
    <button class="time-picker-btn" @click.prevent="setStartTime">시작</button>
  </div>
</template>

<style scoped>
.time-picker-modal-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 400px;
  height: 300px;
  border-radius: 8px;
  background-color: white;
}

#tui-time-picker-container {
  width: 300px;
}

.time-picker-btn {
  padding: 6px 12px;
  border-radius: 32px;
  background-color: black;
  color: white;
  border: none;
  cursor: pointer;
  font-weight: bold;
  font-size: 14px;
}
</style>
