<script setup>
import { ref } from "vue";
import { usePlaceStore } from "../stores/place";

const emit = defineEmits(["searchAddress"]);

const placeStore = usePlaceStore();
const selectedIdx = ref(-1);

const onClick = (item, idx) => {
  emit("searchAddress", item);
  selectedIdx.value = idx;
};
</script>

<template>
  <ul class="address-list">
    <li
      type="none"
      class="address-item"
      :class="{ selected: selectedIdx == idx }"
      v-for="(item, idx) in placeStore.placeList"
      :key="item.address"
      @click="
        () => {
          onClick(item, idx);
        }
      "
    >
      <h2>{{ item.placeName }}</h2>
      <h5>{{ item.address }}</h5>
    </li>
  </ul>
</template>

<style scoped>
.address-list {
  box-sizing: border-box;
  width: 399px;
  padding: 0;
  flex-grow: 1;
  margin: 0;
  overflow-y: auto;
}

.address-item {
  width: 399px;
  height: 102px;
  box-sizing: border-box;
  padding: 10px 20px;
  line-height: 40px;
  border-bottom: 1px solid #f1f1f1;
  cursor: pointer;
}

.address-item > h2,
.address-item > h5 {
  margin: 0;
}

.address-item > h5 {
  font-weight: 300;
}

.address-item:hover {
  background-color: #7c91ff;
  color: white;
  border-bottom: 1px solid #7c91ff;
}

.selected {
  background-color: #7c91ff;
  color: white;
  border-bottom: 1px solid #7c91ff;
}

.address-list::-webkit-scrollbar {
  display: none;
}
</style>
