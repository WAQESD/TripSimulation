<script setup>
import { ref } from "vue";

defineProps({
  addressList: Array,
});

const emit = defineEmits(["searchAddress"]);
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
      v-for="(item, idx) in addressList"
      :key="item.address"
      @click="
        () => {
          onClick(item, idx);
        }
      "
    >
      {{ item.address }}
    </li>
  </ul>
</template>

<style scoped>
.address-list {
  box-sizing: border-box;
  width: 399px;
  overflow: auto;
  padding: 0;
  flex-grow: 1;
  margin: 0;
}

.address-item {
  width: 399px;
  box-sizing: border-box;
  height: 60px;
  padding: 10px 20px;
  line-height: 40px;
  border-bottom: 1px solid #f1f1f1;
  cursor: pointer;
}

.address-item:hover {
  font-weight: bold;
}

.selected {
  background: #e4e8ff;
}
</style>
