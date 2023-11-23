<script setup>
import { onMounted } from "vue";
import { useModalStore } from "../../stores/modal";

const modalStore = useModalStore();

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
    <h3>출발 시각을 입력해주세요</h3>
    <div id="tui-time-picker-container"></div>
    <div>
      <button class="modal-btn" @click.prevent="setStartTime">시작</button>
      <button class="modal-btn cancle" @click.prevent="modalStore.setModal(false)">취소</button>
    </div>
  </div>
</template>

<style scoped>
h3 {
  margin: 0;
  margin-left: 12px;
}
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
  margin-left: 53px;
}
</style>
