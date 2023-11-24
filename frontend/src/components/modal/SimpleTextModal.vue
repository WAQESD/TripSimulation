<script setup>
import { useModalStore } from "../../stores/modal";

const modalStore = useModalStore();

const props = defineProps({
  callback: Function,
  text: String,
});

const onClick = () => {
  modalStore.setModal(false);
  props.callback();
};
</script>

<template>
  <div class="simple-modal-container">
    <h2 class="simple-modal-text">{{ text }}</h2>
    <div class="modal-btrn-wrapper">
      <button class="modal-btn" @click.prevent="onClick">확인</button>
    </div>
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
  align-items: center;
}

.simple-modal-text {
  text-align: center;
}
</style>
