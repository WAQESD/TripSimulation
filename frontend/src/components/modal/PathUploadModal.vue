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
  <div class="path-upload-container">
    <h3>경로 이름을 입력하세요</h3>
    <input type="text" v-model="pathName" />
    <div class="modal-btn-wrapper">
      <button
        class="modal-btn"
        @click="
          () => {
            if (pathName) callback(pathName);
          }
        "
      >
        등록
      </button>
      <button class="modal-btn cancle" @click="modalStore.setModal(false)">취소</button>
    </div>
  </div>
</template>

<style scoped>
.path-upload-container {
  display: flex;
  flex-direction: column;
  padding: 80px 40px 0 40px;
  width: 400px;
  height: 300px;
  background-color: white;
  border-radius: 12px;
  align-items: center;
}

h3 {
  margin: 20px 0;
}

input {
  padding: 10px 20px;
  border: 2px solid #7c91ff;
  border-radius: 20px;
  margin-bottom: 50px;
}
</style>
