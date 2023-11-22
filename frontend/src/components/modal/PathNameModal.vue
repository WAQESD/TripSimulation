<script setup>
import { ref } from "vue";
import { useModalStore } from "../../stores/modal";

defineProps({
  callback: Function,
});

const pathName = ref("");

const modalStore = useModalStore();
</script>

<template>
  <div>
    <h5>경로 이름을 입력하세요</h5>
    <input type="text" v-model="pathName" />
    <button
      @click="
        () => {
          if (pathName) callback(pathName);
        }
      "
    >
      등록
    </button>
    <button @click="modalStore.setModal(false)">취소</button>
  </div>
</template>

<style scoped></style>
