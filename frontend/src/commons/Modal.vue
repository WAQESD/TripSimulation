<script setup>
import { useModalStore } from "@/stores/modal.js";

const modalStore = useModalStore();
</script>

<template>
  <div :class="{ none: !modalStore.isActive }" class="modal-container">
    <component class="modal-content" :is="modalStore.content"></component>
  </div>
</template>

<style scoped>
.modal-container {
  z-index: 2;
  position: absolute;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  z-index: 3;
}

.none {
  display: none;
}
</style>
