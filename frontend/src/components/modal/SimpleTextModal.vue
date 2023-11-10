<script setup>
import { useModalStore } from "../../stores/modal";

const modalStore = useModalStore();

const props = defineProps({
  callback: Function,
  text: String,
});

const onClick = () => {
  props.callback();
  modalStore.setModal(false);
};
</script>

<template>
  <div class="simple-modal-container">
    <h2 class="simple-modal-text">{{ text }}</h2>
    <button class="simple-modal-btn" @click.prevent="onClick">확인</button>
  </div>
</template>

<style scoped>
.simple-modal-container {
  font-family: "Pretendard-Regular";
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 40px;
  width: 400px;
  height: 300px;
  background-color: white;
  border-radius: 12px;
}

.simple-modal-text {
  text-align: center;
}

.simple-modal-btn {
  margin: 0 auto;
  font-size: 14px;
  border: 1px solid rgba(0, 0, 0, 0.5);
  width: 100px;
  height: 40px;
  border-radius: 20px;
  cursor: pointer;
}
</style>
