<script setup>
defineProps({
  addressList: Array,
});

const emit = defineEmits(["searchAddress"]);

const onClick = (item) => {
  emit("searchAddress", item);
};
</script>

<template>
  <ul class="address-list">
    <li
      type="none"
      class="address-item"
      v-for="item in addressList"
      :key="item.address"
      @click="
        () => {
          onClick(item);
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
  width: 330px;
  height: 690px;
  margin-top: 20px;
  border: 1px solid rgba(0, 0, 0, 0.5);
  border-radius: 8px;
  overflow: auto;
  padding: 0;
}

.address-item {
  height: 20px;
  padding: 10px 20px;
  line-height: 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.5);
  cursor: pointer;
}

.address-item:hover {
  font-weight: bold;
}
</style>
